const {onDocumentUpdated} = require("firebase-functions/v2/firestore");
const {getFirestore, Timestamp} = require("firebase-admin/firestore");
const {info, error} = require("firebase-functions/logger");

exports.onZonaParkirUpdate = onDocumentUpdated("zonaParkiran/{zonaParkirId}",
    async (event) => {
      try {
        const zonaParkirId = event.params["zonaParkirId"];
        const dataBefore = event.data.before.data();
        const dataAfter = event.data.after.data();

        if (!dataAfter) return null;

        // --- DETEKSI PERUBAHAN ---
        const isZoneNameChanged = dataBefore.zoneName !== dataAfter.zoneName;
        const monthlyFeeChanged = dataBefore.monthlyFee !==
            dataAfter.monthlyFee;

        // Early Return: Jika tidak ada perubahan pada
        // Nama atau Harga, hentikan (hemat kuota)
        if (!isZoneNameChanged && !monthlyFeeChanged) {
          info(`Tidak ada perubahan nama atau harga pada zona `+
                  `${zonaParkirId}. Skipping...`);
          return null;
        }

        const db = getFirestore();
        const allPromises = [];
        const now = Timestamp.now();

        // =================================================================
        // LOGIC 1: UPDATE HARIAN (Hanya jika Nama Zona berubah)
        // Collection: parkirHarianMobil (Kini murni khusus Harian)
        // =================================================================
        if (isZoneNameChanged) {
          const updateHarian = async () => {
            const harianQuery = db.collection("parkirHarianMobil")
                .where("idZonaParkir", "==", zonaParkirId)
                .where("isCancelled", "==", false)
                .where("completionDate", ">", now);

            const snapshot = await harianQuery.get();
            if (snapshot.empty) return;

            const updates = [];
            snapshot.forEach((doc) => {
              updates.push(doc.ref.update({
                zoneName: dataAfter.zoneName,
              }));
            });
            await Promise.all(updates);
            info(`Berhasil update ${updates.length} data Parkir Harian.`);
          };
          allPromises.push(updateHarian());
        }

        // =================================================================
        // LOGIC 2: UPDATE BULANAN (Jika Nama ATAU Harga berubah)
        // Collection: penyewaan
        // =================================================================
        if (isZoneNameChanged || monthlyFeeChanged) {
          const updatePenyewaan = async () => {
            const penyewaanQuery = db.collection("penyewaan")
                .where("rentalStatus", "==", "Aktif")
                .where("pemakaianParkirMobil.zonaParkir.idZonaParkir",
                    "==", zonaParkirId);

            const snapshot = await penyewaanQuery.get();
            if (snapshot.empty) return;

            const updates = [];
            snapshot.forEach((doc) => {
              const dataToUpdate = {};

              if (isZoneNameChanged) {
                dataToUpdate["pemakaianParkirMobil.zonaParkir.zoneName"] =
                    dataAfter.zoneName;
              }
              if (monthlyFeeChanged) {
                dataToUpdate["pemakaianParkirMobil.zonaParkir.monthlyFee"] =
                    dataAfter.monthlyFee;
              }

              updates.push(doc.ref.update(dataToUpdate));
            });
            await Promise.all(updates);
            info(`Berhasil update ${updates.length} data Penyewaan (Bulanan).`);
          };
          allPromises.push(updatePenyewaan());
        }

        // Eksekusi semua promises secara paralel
        await Promise.all(allPromises);
        info(`Selesai mengeksekusi trigger update untuk zona ${zonaParkirId}`);

        return null;
      } catch (e) {
        error(`Terjadi kesalahan pada onZonaParkirUpdate: ${e.message}`, e);
        return null;
      }
    });
