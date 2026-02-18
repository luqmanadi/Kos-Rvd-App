const {
  onDocumentUpdated,
  onDocumentCreated,
  onDocumentDeleted,
} = require("firebase-functions/v2/firestore");
const admin = require("firebase-admin");
const {
  getFirestore,
  Timestamp,
  FieldValue,
} = require("firebase-admin/firestore");
const {info, error} = require("firebase-functions/logger");
const utils = require("./utils");


exports.onKeluhanUpdate = onDocumentUpdated("keluhan/{keluhanId}",
    async (event) => {
      try {
        const keluhanId = event.params["keluhanId"];
        const dataBefore = event.data.before.data();
        const dataAfter = event.data.after.data();

        // Jika dokumen dihapus, stop.
        if (!dataAfter) return null;

        const db = getFirestore();
        const allPromises = [];
        const idAkun = dataAfter.idAkun;

        const isToSedangDiproses =
            dataBefore.complaintStatus === utils.MENUNGGU_KONFIRMASI &&
            dataAfter.complaintStatus === utils.SEDANG_DIPROSES;
        const isToSelesai =
            dataBefore.complaintStatus === utils.SEDANG_DIPROSES &&
            dataAfter.complaintStatus === utils.SELESAI;

        if (!isToSedangDiproses && !isToSelesai) {
          info(`Keluhan ${keluhanId}: Tidak ada perubahan kritikal. Skip.`);
          return null;
        }

        // Skenario 1: Menunggu Konfirmasi -> Sedang Diproses (Ke Penghuni)
        if (isToSedangDiproses) {
          // A. Update Statistik: Kurangi jumlah keluhan baru (-1)
          const statsUpdate = {
            numberOfNewComplaints: FieldValue.increment(-1),
          };
          allPromises.push(utils.updateAdminDashboardStat(db, statsUpdate));

          // B. Notif ke Penghuni
          allPromises.push(
              db.collection("notifikasi").add({
                idAkun: idAkun,
                idDetailReferensi: keluhanId,
                title: "Laporan diproses",
                content: "Laporan anda sedang diproses oleh pengelola kos. " +
                    "Tunggu perkembangan selanjutnya.",
                date: Timestamp.now(),
                alreadyRead: false,
                notificationType: utils.LAPORAN_KELUHAN,
              }),
          );
        }

        // Skenario 2: Sedang Diproses -> Selesai (Ke Penghuni)
        if (isToSelesai) {
          // Tidak ada update statistik di sini
          // (karena sudah dikurangi saat jadi 'Sedang Diproses')

          // Notif ke Penghuni
          allPromises.push(
              db.collection("notifikasi").add({
                idAkun: idAkun,
                idDetailReferensi: keluhanId,
                title: "Laporan Selesai",
                content: "Laporan anda telah selesai diproses " +
                    "oleh pengelola kos. Terima kasih.",
                date: Timestamp.now(),
                alreadyRead: false,
                notificationType: utils.LAPORAN_KELUHAN,
              }),
          );
        }

        if (allPromises.length > 0) {
          await Promise.all(allPromises);
          info(`Berhasil memproses ${allPromises.length} updates/notifikasi.`);
        }
      } catch (e) {
        error(`GAGAL Create Notifikasi untuk idKeluhan:` +
              ` ${event.params["keluhanId"]}`, e);
      }
      return null;
    });


exports.onKeluhanCreate = onDocumentCreated("keluhan/{keluhanId}",
    async (event) => {
      try {
        if (!event.data) return null;
        const db = getFirestore();
        const data = event.data.data();
        const keluhanId = event.params["keluhanId"];
        const allPromises = [];

        // Ambil nama pelapor (default "-" jika kosong)
        const reporterName = data.reporterName || "-";

        info(`Keluhan baru ${keluhanId} dari ` +
            `${reporterName}. Notif ke Admin...`);

        // A. Update Statistik: Tambah jumlah keluhan baru (+1)
        const statsUpdate = {
          numberOfNewComplaints: FieldValue.increment(1),
        };
        allPromises.push(utils.updateAdminDashboardStat(db, statsUpdate));

        // B. Notif ke SEMUA Admin Aktif
        const adminSnapshot = await db.collection("akun")
            .where("role", "==", "admin")
            .where("status", "==", "Aktif")
            .get();

        adminSnapshot.forEach((doc) => {
          allPromises.push(
              db.collection("notifikasi").add({
                idAkun: doc.id,
                idDetailReferensi: keluhanId,
                title: "Laporan Perlu Konfirmasi",
                content: `Segera lakukan konfirmasi laporan keluhan ` +
                        `dari ${reporterName}.`,
                date: Timestamp.now(),
                alreadyRead: false,
                notificationType: utils.LAPORAN_KELUHAN,
              }),
          );
        });

        if (allPromises.length > 0) {
          await Promise.all(allPromises);
          info(`Berhasil Create Keluhan: Dashboard updated & Notif sent.`);
        }
      } catch (e) {
        error(`GAGAL Create Notif Keluhan Baru: ` +
            `${event.params["keluhanId"]}`, e);
      }
      return null;
    });


exports.onKeluhanDelete = onDocumentDeleted("keluhan/{keluhanId}",
    async (event) => {
      try {
        if (!event.data) return null;

        const data = event.data.data();
        const keluhanId = event.params["keluhanId"];
        const complaintStatus = data.complaintStatus;
        const db = getFirestore();
        const allPromises = [];

        // A. Hapus Foto Keluhan
        if (data.photoComplaint) {
          const path1 = utils.getFilePathFromUrl(data.photoComplaint);
          if (path1) {
            allPromises.push(
                admin.storage().bucket().file(path1).delete()
                    .catch((err) => {
                      if (err.code !== 404) error("Gagal hapus foto 1", err);
                    }),
            );
          }
        }

        // B. Hapus Foto Respon
        if (data.photoResponse) {
          const path2 = utils.getFilePathFromUrl(data.photoResponse);
          if (path2) {
            allPromises.push(
                admin.storage().bucket().file(path2).delete()
                    .catch((err) => {
                      if (err.code !== 404) error("Gagal hapus foto 2", err);
                    }),
            );
          }
        }

        // C. Hapus Notifikasi Terkait
        const notifSnapshot = await db.collection("notifikasi")
            .where("idDetailReferensi", "==", keluhanId)
            .get();

        if (!notifSnapshot.empty) {
          const batch = db.batch();
          notifSnapshot.docs.forEach((doc) => {
            batch.delete(doc.ref);
          });
          allPromises.push(batch.commit());
        }

        // D. Update Statistik Dashboard
        // (JIKA status masih "Menunggu Konfirmasi")
        // Kalau dihapus saat masih "Menunggu",
        // berarti counter harus dikurangi agar sinkron.
        // Kalau dihapus saat sudah "Selesai" atau "Proses",
        // counter tidak perlu diubah.
        if (complaintStatus === utils.MENUNGGU_KONFIRMASI) {
          const statsUpdate = {
            numberOfNewComplaints: FieldValue.increment(-1),
          };
          allPromises.push(utils.updateAdminDashboardStat(db, statsUpdate));
        }

        // Eksekusi semua secara paralel
        await Promise.all(allPromises);
        info(`Keluhan ${keluhanId} deleted. Cleanup complete.`);
      } catch (e) {
        error(`GAGAL Proses Delete Keluhan: ${event.params["keluhanId"]}`, e);
      }
      return null;
    });
