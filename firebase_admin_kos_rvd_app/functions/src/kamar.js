const {onDocumentUpdated} = require("firebase-functions/v2/firestore");
const {getFirestore, FieldValue} = require("firebase-admin/firestore");
const {info, error} = require("firebase-functions/logger");
const utils = require("./utils");

exports.onKamarUpdate = onDocumentUpdated("kamar/{kamarId}", async (event) => {
  try {
    const kamarId = event.params["kamarId"];
    const dataBefore = event.data.before.data();
    const dataAfter = event.data.after.data();

    if (!dataAfter) return null;

    const db = getFirestore();
    const allPromises = [];

    // --- DETEKSI PERUBAHAN ---
    const isNumberChanged = dataBefore.numberRoom !== dataAfter.numberRoom;

    // Gunakan JSON.stringify untuk membandingkan object/array deep equality
    const isPriceChanged =
        JSON.stringify(dataBefore.price) !== JSON.stringify(dataAfter.price);
    const isFreeServiceChanged =
        JSON.stringify(dataBefore.freeService) !==
        JSON.stringify(dataAfter.freeService);

    const statusBefore = dataBefore.status;
    const statusAfter = dataAfter.status;
    const isStatusChanged = statusBefore !== statusAfter;

    // Jika tidak ada perubahan di field-field penting, stop.
    if (!isNumberChanged && !isPriceChanged &&
        !isFreeServiceChanged && !isStatusChanged) {
      info(`Kamar ${kamarId}: Tidak ada perubahan kritikal. Skip.`);
      return null;
    }

    info(`Kamar ${kamarId} berubah. Memproses update...`);

    // ======================================================
    // BAGIAN 1: Update Data Penyewaan (Jika Detail Kamar Berubah)
    // ======================================================
    if (isNumberChanged || isPriceChanged || isFreeServiceChanged) {
      // Kita bungkus dalam async function agar bisa masuk Promise.all
      const updateRentalsProcess = async () => {
        const rentalsSnapshot = await db.collection("penyewaan")
            .where("infoKamar.idKamar", "==", kamarId)
            .where("rentalStatus", "==", "Aktif")
            .get();

        if (rentalsSnapshot.empty) {
          info(`Skip update penyewaan: Tidak ada penyewaan aktif.`);
          return;
        }

        const batch = db.batch();
        let updateCount = 0;

        rentalsSnapshot.docs.forEach((doc) => {
          const rentalData = doc.data();
          const updates = {};

          if (isNumberChanged) {
            updates["infoKamar.numberRoom"] = dataAfter.numberRoom;
          }
          if (isPriceChanged) {
            updates["infoKamar.currentRoomRentalCost"] = dataAfter.price;
          }
          if (isFreeServiceChanged) {
            // Logic: Gabungkan Add-On lama user +
            // Service Gratis baru dari kamar
            const currentList = rentalData.pemakaianAlatElektronik || [];

            // Filter: Ambil item yang origin-nya "ADD_ON" (barang bawaan user)
            const saveAddOns = currentList.filter(
                (tool) => tool.origin === "ADD_ON",
            );

            const newFreeServices = dataAfter.freeService || [];

            updates["pemakaianAlatElektronik"] =
                [...saveAddOns, ...newFreeServices];
          }

          if (Object.keys(updates).length > 0) {
            batch.update(doc.ref, updates);
            updateCount++;
          }
        });

        if (updateCount > 0) {
          await batch.commit();
          info(`Sukses update ${updateCount} doc penyewaan.`);
        }
      };

      // Masukkan proses ini ke antrian promise
      allPromises.push(updateRentalsProcess());
    }

    // ======================================================
    // BAGIAN 2: Update Statistik Dashboard (Jika Status Berubah)
    // ======================================================
    if (isStatusChanged) {
      let statsUpdate = null;

      // Skenario A: Kamar terisi (Kosong -> Dipakai)
      // Jumlah kamar kosong BERKURANG 1
      if (statusBefore === "Kosong" && statusAfter === "Dipakai") {
        statsUpdate = {
          numberOfEmptyRooms: FieldValue.increment(-1),
        };
      } else if (statusBefore === "Dipakai" && statusAfter === "Kosong") {
        // Skenario B: Kamar kosong kembali (Dipakai -> Kosong)
        // Jumlah kamar kosong BERTAMBAH 1
        statsUpdate = {
          numberOfEmptyRooms: FieldValue.increment(1),
        };
      }

      // Jika ada update statistik yang valid, eksekusi pakai utils
      if (statsUpdate) {
        allPromises.push(utils.updateAdminDashboardStat(db, statsUpdate));
      }
    }

    // Eksekusi semua secara paralel
    if (allPromises.length > 0) {
      await Promise.all(allPromises);
      info(`Kamar ${kamarId}: Semua proses update selesai.`);
    }

    return null;
  } catch (e) {
    error(`GAGAL Update Kamar ${event.params["kamarId"]}`, e);
    return null;
  }
});
