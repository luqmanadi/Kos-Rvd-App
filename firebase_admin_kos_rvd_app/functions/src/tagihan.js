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

// --- 1. UPDATE TAGIHAN ---
exports.onTagihanUpdate = onDocumentUpdated("tagihan/{tagihanId}",
    async (event) => {
      try {
        const tagihanId = event.params["tagihanId"];
        const dataBefore = event.data.before.data();
        const dataAfter = event.data.after.data();

        // Jika dokumen dihapus, stop.
        if (!dataAfter) return null;

        const db = getFirestore();
        const allPromises = [];

        // Cek Logika Perubahan Status
        // 1. Logika: Belum Lunas -> Menunggu Verifikasi (Notif ke semua Admin)
        const isToMenungguVerifikasi =
            dataBefore.paymentStatus === utils.BELUM_LUNAS &&
            dataAfter.paymentStatus === utils.MENUNGGU_VERIFIKASI;

        // 2. Logika: Menunggu Verifikasi -> Lunas (Notif ke semua Penghuni)
        const isToLunas =
            dataBefore.paymentStatus === utils.MENUNGGU_VERIFIKASI &&
            dataAfter.paymentStatus === utils.LUNAS;

        // 3. Logika: Penolakan (Notif ke semua Penghuni)
        const isRejected =
            dataBefore.paymentStatus === utils.MENUNGGU_VERIFIKASI &&
            dataAfter.paymentStatus === utils.BELUM_LUNAS &&
            dataAfter.rejectionStatement !== null;

        if (!isToMenungguVerifikasi &&
            !isToLunas && !isRejected) {
          info(`Tagihan ${tagihanId}: Tidak ada perubahan kritikal. Skip.`);
          return null;
        }

        info(`Tagihan ${tagihanId} berubah. Memulai create notifikasi...`);

        const roomNo = dataAfter.numberRoom;

        // Skenario 1: Admin (Menunggu Verifikasi)
        // Notif Admin + Update Statistik
        if (isToMenungguVerifikasi) {
          // A. Update Statistik: Perlu Verif (+1), Belum Lunas (-1)
          const statUpdate = {
            billAmountNeedsVerification: FieldValue.increment(1),
            amountOfUnpaidBills: FieldValue.increment(-1),
          };

          allPromises.push(utils.updateAdminDashboardStat(db, statUpdate));

          // B. Notif ke Admin
          const adminSnapshot = await db.collection("akun")
              .where("role", "==", "admin")
              .where("status", "==", "Aktif")
              .get();

          adminSnapshot.forEach((doc) => {
            allPromises.push(
                db.collection("notifikasi").add({
                  idAkun: doc.id,
                  idDetailReferensi: tagihanId,
                  title: "Tagihan Perlu Verifikasi",
                  content: `Segera lakukan verifikasi` +
                      ` pembayaran tagihan kamar no ${roomNo}.`,
                  date: Timestamp.now(),
                  alreadyRead: false,
                  notificationType: utils.TAGIHAN,
                }),
            );
          });
        }

        // Skenario 2: Penghuni (Lunas)
        // Notif Penghuni + Update Statistik
        if (isToLunas) {
          // A. Update Statistik: Perlu Verif (-1)
          const statsUpdate = {
            billAmountNeedsVerification: FieldValue.increment(-1),
          };
          allPromises.push(utils.updateAdminDashboardStat(db, statsUpdate));

          // B. Notif ke Penghuni
          const residentIds = dataAfter.residentAccountIdList || [];
          residentIds.forEach((idAkun) => {
            allPromises.push(
                db.collection("notifikasi").add({
                  idAkun: idAkun,
                  idDetailReferensi: tagihanId,
                  title: "Tagihan Lunas",
                  content: `Pembayaran Tagihan Kamar no ` +
                      `${roomNo} sudah lunas. Terima kasih.`,
                  date: Timestamp.now(),
                  alreadyRead: false,
                  notificationType: utils.TAGIHAN,
                }),
            );
          });
        }

        // Skenario 3: Penghuni (Ditolak)
        // Notif Penghuni + Update Statistik
        if (isRejected) {
          // A. Update Statistik: Perlu Verif (-1), Belum Lunas (+1)
          const statsUpdate = {
            billAmountNeedsVerification: FieldValue.increment(-1),
            amountOfUnpaidBills: FieldValue.increment(1),
          };
          allPromises.push(utils.updateAdminDashboardStat(db, statsUpdate));

          // B. Notif ke Penghuni (Deadline tgl 15)
          const residentIds = dataAfter.residentAccountIdList || [];

          // Hitung tanggal 15 bulan ini, jam 23:59
          const deadline = new Date();
          deadline.setDate(15);
          deadline.setHours(23, 59, 59, 999);
          const deadlineStr = deadline.toLocaleDateString("id-ID", {
            day: "numeric",
            month: "long",
            year: "numeric",
          });

          residentIds.forEach((idAkun) => {
            allPromises.push(
                db.collection("notifikasi").add({
                  idAkun: idAkun,
                  idDetailReferensi: tagihanId,
                  title: "Pembayaran Tagihan ditolak",
                  content: `Pembayaran ditolak.` +
                      ` Segera lakukan pembayaran tagihan kamar no` +
                      ` ${roomNo}. paling lambat ${deadlineStr}, 23:59`,
                  date: Timestamp.now(),
                  alreadyRead: false,
                  notificationType: utils.TAGIHAN,
                }),
            );
          });
        }

        if (allPromises.length > 0) {
          await Promise.all(allPromises);
          info(`Berhasil memproses ${allPromises.length} updates/notifikasi.`);
        }
      } catch (e) {
        error(`GAGAL Create Notifikasi untuk idTagihan` +
            ` ${event.params["tagihanId"]}`, e);
      }
      return null;
    });

// --- 2. CREATE TAGIHAN ---
exports.onTagihanCreate = onDocumentCreated("tagihan/{tagihanId}",
    async (event) => {
      try {
        // Cek apakah data ada (defense mechanism)
        if (!event.data) {
          return null;
        }

        const db = getFirestore();
        const dataTagihanBaru = event.data.data();
        const tagihanId = event.params["tagihanId"];
        const allPromises = [];

        // 1. Update Statistik: Tagihan Baru -> Unpaid (+1)
        const statsUpdate = {
          amountOfUnpaidBills: FieldValue.increment(1),
        };
        allPromises.push(utils.updateAdminDashboardStat(db, statsUpdate));

        // 2. Buat Notifikasi Penghuni
        const roomNo = dataTagihanBaru.numberRoom;

        // Deadline tanggal 15 bulan pembuatan
        const deadline = new Date();
        deadline.setDate(15);
        deadline.setHours(23, 59, 59, 999);
        const deadlineStr = deadline.toLocaleDateString("id-ID", {
          day: "numeric",
          month: "long",
          year: "numeric",
        });

        const residentIds = dataTagihanBaru.residentAccountIdList || [];

        // Loop ke setiap penghuni di list
        residentIds.forEach((idAkun) => {
          allPromises.push(
              db.collection("notifikasi").add({
                idAkun: idAkun,
                idDetailReferensi: tagihanId,
                title: "Tagihan Belum Lunas",
                content: `Segera lakukan pembayaran tagihan kamar no ` +
                    `${roomNo}. paling lambat ${deadlineStr}, 23:59`,
                date: Timestamp.now(),
                alreadyRead: false,
                notificationType: utils.TAGIHAN,
              }),
          );
        });

        if (allPromises.length > 0) {
          await Promise.all(allPromises);
          info(`Berhasil Create Tagihan: Dashboard updated & Notif sent.`);
        }
      } catch (e) {
        error(`GAGAL Create Notif Tagihan Baru id:` +
          ` ${event.params["tagihanId"]}`, e);
      }
      return null;
    });

// --- 3. DELETE TAGIHAN ---
exports.onTagihanDelete = onDocumentDeleted("tagihan/{tagihanId}",
    async (event) => {
      try {
        if (!event.data) return null;

        const data = event.data.data();
        const proofOfPayment = data.proofOfPayment;
        const paymentStatus = data.paymentStatus;
        const tagihanId = event.params["tagihanId"];
        const db = getFirestore();
        const allPromises = [];

        // A. Hapus Storage (Bukti Bayar)
        if (proofOfPayment) {
          const filePath = utils.getFilePathFromUrl(proofOfPayment);
          if (filePath) {
            allPromises.push(
                admin.storage().bucket().file(filePath).delete()
                    .catch((err) => {
                      // Ignore 404, log other errors
                      if (err.code !== 404) error("Gagal hapus file", err);
                    }),
            );
          }
        }

        // B. Hapus Notifikasi Terkait (Cascade Delete)
        const notifSnapshot = await db.collection("notifikasi")
            .where("idDetailReferensi", "==", tagihanId)
            .get();

        if (!notifSnapshot.empty) {
          const batch = db.batch();
          notifSnapshot.docs.forEach((doc) => {
            batch.delete(doc.ref);
          });

          allPromises.push(batch.commit());
        }

        // C. Update Statistik Dashboard (Pengurangan agar konsisten)
        let statsUpdate = null;
        if (paymentStatus === utils.BELUM_LUNAS) {
          // Kalau yang dihapus tagihan belum lunas -> Kurangi Unpaid
          statsUpdate = {
            amountOfUnpaidBills: FieldValue.increment(-1),
          };
        } else if (paymentStatus === utils.MENUNGGU_VERIFIKASI) {
          // Kalau yang dihapus tagihan waiting -> Kurangi NeedsVerif
          statsUpdate = {
            billAmountNeedsVerification: FieldValue.increment(-1),
          };
        }

        if (statsUpdate) {
          allPromises.push(utils.updateAdminDashboardStat(db, statsUpdate));
        }

        await Promise.all(allPromises);
        info(`Tagihan ${tagihanId} Deleted. Clean up complete.`);
      } catch (e) {
        error(`GAGAL proses onTagihanDelete untuk ID: ` +
              `${event.params["tagihanId"]}`, e);
      }
      return null;
    });
