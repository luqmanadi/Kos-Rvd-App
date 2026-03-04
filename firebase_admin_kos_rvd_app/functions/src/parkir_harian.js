const {
  onDocumentUpdated,
  onDocumentDeleted,
} = require("firebase-functions/v2/firestore");
const {info, error} = require("firebase-functions/logger");
const admin = require("firebase-admin");
const utils = require("./utils");

exports.onParkirHarianMobilUpdate =
    onDocumentUpdated("parkirHarianMobil/{parkirHarianMobilId}",
        async (event) => {
          try {
            const parkirHarianMobilId = event.params["parkirHarianMobilId"];
            const dataBefore = event.data.before.data();
            const dataAfter = event.data.after.data();

            if (!dataAfter) return null;

            // Deteksi perubahan cancelledStatus (false -> true)
            const cancelledStatusChanged =
                dataBefore.cancelledStatus === false &&
                    dataAfter.cancelledStatus === true;
            const proofOfPayment = dataAfter.proofOfPayment;

            if (!cancelledStatusChanged) {
              return null; // Silent skip
            }

            if (!proofOfPayment) { // Cek null atau string kosong
              info(`Parkir Harian ${parkirHarianMobilId}: `+
                  `Tidak ada file. Skip.`);
              return null;
            }

            // 1. Hapus bukti pembayaran dari storage (Langsung di-await)
            const getPathUrl = utils.getFilePathFromUrl(proofOfPayment);
            await admin.storage().bucket().file(getPathUrl).delete()
                .catch((err) => {
                  if (err.code !== 404) {
                    error("Gagal hapus bukti pembayaran di storage", err);
                  }
                });

            // 2. UPDATE FIRESTORE: Kosongkan field agar
            // tidak jadi link mati di UI Android
            await event.data.after.ref.update({
              proofOfPayment: null,
            });

            info(`Parkir Harian ${parkirHarianMobilId}: `+
                `Bukti pembayaran dihapus dari Storage & DB.`);
            return null;
          } catch (e) {
            error(`GAGAL onParkirHarianMobilUpdate ID: `+
                `${event.params["parkirHarianMobilId"]}`, e);
            return null;
          }
        });


exports.onDucumentDeletedParkirHarianMobil = onDocumentDeleted(
    "parkirHarianMobil/{parkirHarianMobilId}", async (event) => {
      try {
        const parkirHarianMobilId = event.params["parkirHarianMobilId"];
        const data = event.data.data();
        const proofOfPayment = data.proofOfPayment;

        if (!proofOfPayment) {
          info(`Parkir Harian ${parkirHarianMobilId} tidak memiliki `+
            `bukti pembayaran. Skip.`);
          return null;
        }

        // Langsung hapus dari storage tanpa perlu array promise
        const getProofOfPaymentPathUrl =
            utils.getFilePathFromUrl(proofOfPayment);
        await admin.storage().bucket().file(getProofOfPaymentPathUrl).delete()
            .catch((err) => {
              if (err.code !== 404) {
                error("Gagal hapus bukti pembayaran", err);
              }
            });

        info(`Dokumen dihapus. Storage Parkir Harian `+
            `${parkirHarianMobilId} ikut dibersihkan.`);
        return null;
      } catch (e) {
        error(`GAGAL onDucumentDeletedParkirHarianMobil ID:`+
            `${event.params["parkirHarianMobilId"]}`, e);
        return null;
      }
    });
