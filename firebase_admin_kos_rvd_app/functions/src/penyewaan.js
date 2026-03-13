const {
  onDocumentCreated,
  onDocumentUpdated,
} = require("firebase-functions/v2/firestore");
const {getFirestore} = require("firebase-admin/firestore");
const {info, error} = require("firebase-functions/logger");
const utils = require("./utils");


// 1. ON CREATE PENYEWAAN
exports.onPenyewaanCreate = onDocumentCreated("penyewaan/{penyewaanId}",
    async (event) => {
      try {
        if (!event.data) return null;
        const db = getFirestore();
        const data = event.data.data();
        const allPromises = [];

        // Skenario 1: Update status kamar menjadi "Dipakai"
        if (data.infoKamar?.idKamar) {
          const kamarRef = db.collection("kamar").doc(data.infoKamar.idKamar);
          allPromises.push(kamarRef.update({status: "Dipakai"}));
        }

        // Skenario 2: Update status zona parkir menjadi "Dipakai" jika ada
        if (data.pemakaianParkirMobilBulanan?.zonaParkir?.idZonaParkir) {
          const idZona = data.pemakaianParkirMobilBulanan
              .zonaParkir.idZonaParkir;
          const zonaParkirRef = db.collection("zonaParkiran").doc(idZona);
          allPromises.push(zonaParkirRef.update({status: "Dipakai"}));
        }

        // Skenario 3: Update dataPenghuni pada akun setiap resident
        const penyewaanId = event.params["penyewaanId"];
        if (Array.isArray(data.listResident) && data.listResident.length > 0) {
          const numberRoom = data.infoKamar?.numberRoom ?? null;

          for (const resident of data.listResident) {
            if (resident.idAkun) {
              const akunRef = db.collection("akun").doc(resident.idAkun);
              allPromises.push(
                  akunRef.update({
                    "dataPenghuni.numberRoom": numberRoom,
                    "dataPenghuni.idPenyewa": penyewaanId,
                  }),
              );
            }
          }
        }

        if (allPromises.length > 0) {
          await Promise.all(allPromises);
          info(`Berhasil inisialisasi status Kamar & `+
              `Zona Parkir untuk penyewaan: ${event.params["penyewaanId"]}`);
        }
      } catch (e) {
        error(`Gagal memproses onPenyewaanCreate id: `+
            `${event.params["penyewaanId"]}`, e);
      }
      return null;
    });

exports.onPenyewaanUpdate = onDocumentUpdated("penyewaan/{penyewaanId}",
    async (event) => {
      try {
        const dataBefore = event.data.before.data();
        const dataAfter = event.data.after.data();
        if (!dataAfter || !dataBefore) return null;

        const db = getFirestore();
        const allPromises = [];
        const idPenyewaan = event.params["penyewaanId"];

        // ==========================================
        // Skenario 1: Checkout (Gunakan dataBefore untuk keamanan)
        // ==========================================
        const isCheckout = dataBefore.rentalStatus === "Aktif" &&
              dataAfter.rentalStatus === "Tidak Aktif";

        if (isCheckout) {
          // 1a. Kosongkan kamar yang SEBELUMNYA dipakai
          if (dataBefore.infoKamar?.idKamar) {
            allPromises.push(
                db.collection("kamar")
                    .doc(dataBefore.infoKamar.idKamar)
                    .update({status: "Kosong"}),
            );
          }

          // 1b. Kosongkan parkir yang SEBELUMNYA dipakai
          if (dataBefore.pemakaianParkirMobilBulanan?.
              zonaParkir?.idZonaParkir) {
            const idZona = dataBefore.pemakaianParkirMobilBulanan
                .zonaParkir.idZonaParkir;
            allPromises.push(
                db.collection("zonaParkiran")
                    .doc(idZona).update({status: "Kosong"}),
            );
          }

          // 1c. Hapus data tagihan yang "Belum Lunas" berdasarkan idPenyewa
          if (dataBefore.idPenyewa) {
            const idPenyewa = dataBefore.idPenyewa;
            const tagihanBelumLunasSnapshot = await db.collection("tagihan")
                .where("idPenyewa", "==", idPenyewa)
                .where("paymentStatus", "==", utils.BELUM_LUNAS)
                .get();

            if (!tagihanBelumLunasSnapshot.empty) {
              let countDeleted = 0;
              tagihanBelumLunasSnapshot.forEach((doc) => {
                allPromises.push(doc.ref.delete());
                countDeleted++;
              });

              info(`Menghapus ${countDeleted} tagihan Belum Lunas ` +
                  `untuk penyewa: ${idPenyewa}`);
            }
          }

          // Reset dataPenghuni di akun setiap resident saat checkout
          const listResident = dataBefore.listResident;
          if (Array.isArray(listResident) && listResident.length > 0) {
            for (const resident of listResident) {
              if (resident.idAkun) {
                const akunRef = db.collection("akun").doc(resident.idAkun);
                allPromises.push(
                    akunRef.update({
                      "dataPenghuni.numberRoom": null,
                      "dataPenghuni.idPenyewa": "",
                    }),
                );
              }
            }
          }
        }

        // ==========================================
        // Skenario 2 & 3: Perubahan Parkir (Berhenti / Mulai / Pindah)
        // ==========================================
        // Catatan: Jika isCheckout true,
        // kita tidak perlu menjalankan skenario ini
        // agar tidak terjadi double-update pada zonaParkiran menjadi "Kosong".
        if (!isCheckout) {
          const parkirBefore = dataBefore.pemakaianParkirMobilBulanan;
          const parkirAfter = dataAfter.pemakaianParkirMobilBulanan;

          if (parkirBefore !== null && parkirAfter === null) {
            // Berhenti langganan parkir
            const idZonaBefore = parkirBefore.zonaParkir.idZonaParkir;
            allPromises.push(
                db.collection("zonaParkiran")
                    .doc(idZonaBefore).update({status: "Kosong"}),
            );
          } else if (parkirBefore === null && parkirAfter !== null) {
            // Baru mulai langganan parkir
            const idZonaAfter = parkirAfter.zonaParkir.idZonaParkir;
            allPromises.push(
                db.collection("zonaParkiran")
                    .doc(idZonaAfter).update({status: "Dipakai"}),
            );
          } else if (parkirBefore !== null && parkirAfter !== null) {
            // Pindah lokasi parkir
            const idBefore = parkirBefore.zonaParkir.idZonaParkir;
            const idAfter = parkirAfter.zonaParkir.idZonaParkir;

            if (idBefore !== idAfter) {
              allPromises.push(
                  db.collection("zonaParkiran")
                      .doc(idBefore).update({status: "Kosong"}),
              );
              allPromises.push(
                  db.collection("zonaParkiran")
                      .doc(idAfter).update({status: "Dipakai"}),
              );
            }
          }
        }

        // Eksekusi semua promise
        if (allPromises.length > 0) {
          await Promise.all(allPromises);
          info(`Berhasil update dependensi (Kamar/Parkir/Tagihan) `+
                  `untuk penyewaan: ${idPenyewaan}`);
        }
      } catch (e) {
        error(`Gagal memproses onPenyewaanUpdate id: `+
            `${event.params["penyewaanId"]}`, e);
      }
      return null;
    });
