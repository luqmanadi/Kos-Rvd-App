const {
  onDocumentCreated,
  onDocumentDeleted,
} = require("firebase-functions/v2/firestore");
const {getFirestore, Timestamp} = require("firebase-admin/firestore");
const {info, error} = require("firebase-functions/logger");
const utils = require("./utils");

exports.onDocumentCreatedPengumuman =
    onDocumentCreated("pengumuman/{pengumumanId}", async (event) => {
      try {
        if (!event.data) return null;

        const pengumumanId = event.params["pengumumanId"];
        const data = event.data.data();
        const db = getFirestore();
        const notificationPromises = [];

        // Ambil judul pengumuman
        const contentTitle = data.title || "Ada pengumuman baru";

        const penghuniSnapshot = await db.collection("akun")
            .where("role", "==", "penghuni")
            .where("status", "==", "Aktif")
            .get();

        penghuniSnapshot.forEach((doc) => {
          notificationPromises.push(
              db.collection("notifikasi").add({
                idAkun: doc.id,
                idDetailReferensi: pengumumanId,
                title: utils.PENGUMUMAN,
                content: contentTitle,
                date: Timestamp.now(),
                alreadyRead: false,
                notificationType: utils.PENGUMUMAN,
              }),
          );
        });

        if (notificationPromises.length > 0) {
          await Promise.all(notificationPromises);
          info(`Berhasil kirim notif pengumuman baru `+
                `ke ${notificationPromises.length} penghuni.`);
        }
      } catch (e) {
        error(`GAGAL Create Notif Pengumuman Baru: ` +
              `${event.params["pengumumanId"]}`, e);
      }
      return null;
    });


exports.onDucumentDeletedPengumuman =
    onDocumentDeleted("pengumuman/{pengumumanId}", async (event) => {
      try {
        if (!event.data) return null;
        const pengumumanId = event.params["pengumumanId"];
        const db = getFirestore();

        const snapshot = await db.collection("notifikasi")
            .where("idDetailReferensi", "==", pengumumanId)
            .get();

        if (!snapshot.empty) {
          const batch = db.batch();
          snapshot.docs.forEach((doc) => {
            batch.delete(doc.ref);
          });
          await batch.commit();
          info(`Berhasil hapus ${snapshot.size} `+
                  `notifikasi terkait pengumuman ini.`);
        } else {
          info(`Tidak ada notifikasi terkait untuk `+
              `pengumuman: ${pengumumanId}`);
        }
      } catch (e) {
        error(`GAGAL Proses Delete Pengumuman: `+
            `${event.params["pengumumanId"]}`, e);
      }
      return null;
    });
