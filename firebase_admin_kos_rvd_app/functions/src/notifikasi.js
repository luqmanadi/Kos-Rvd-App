const {
  onDocumentCreated,
  onDocumentUpdated,
  onDocumentDeleted,
} = require("firebase-functions/v2/firestore");
const {info, error} = require("firebase-functions/logger");
const admin = require("firebase-admin");
const {getFirestore, FieldValue} = require("firebase-admin/firestore");

// --- 1. CREATE NOTIFIKASI (Kirim FCM & Update Counter +1) ---
exports.onNotifikasiCreate = onDocumentCreated("notifikasi/{notifikasiId}",
    async (event) => {
      try {
        // Cek apakah data ada (defense mechanism)
        if (!event.data) {
          return null;
        }

        const data = event.data.data();
        const notifikasiId = event.params["notifikasiId"];
        const db = admin.firestore();

        const idAkun = data.idAkun;
        const title = data.title || "Notifikasi Baru";
        const content = data.content || "";

        // Data penting untuk Deep Link di Android
        // Pastikan key ini SAMA PERSIS dengan yang ada di MainActivity.kt
        const typeNotification = data.notificationType || "";
        const idDetailReferensi = data.idDetailReferensi || "";

        if (!idAkun) {
          info(`Notifikasi ${notifikasiId} tidak memiliki idAkun. Skip.`);
          return null;
        }

        const allPromises = [];

        // A. Update Counter Unread Notification (+1) di Akun
        const incrementCounter = db.collection("akun").doc(idAkun).update({
          numberOfUnreadNotification: FieldValue.increment(1),
        }).catch((err) => {
          error(`Gagal update counter +1 akun ${idAkun}`, err);
        });
        allPromises.push(incrementCounter);

        // B. Kirim FCM
        // Kita bungkus logika FCM dalam
        // async function biar bisa masuk Promise.all
        const sendFCMProcess = async () => {
          const userDoc = await db.collection("akun").doc(idAkun).get();
          if (!userDoc.exists) return;

          const userData = userDoc.data();
          const fcmTokens = userData.fcmTokens || [];

          if (fcmTokens.length === 0) {
            info(`Akun ${idAkun} tidak memiliki FCM Token.`);
            return;
          }

          const tokens = fcmTokens.map((t) => t.token);

          const message = {
            notification: {title: title, body: content},
            data: {
              idNotifikasi: notifikasiId,
              typeNotification: typeNotification,
              idDetailReferensi: idDetailReferensi,
            },
            android: {
              priority: "high",
            },
            tokens: tokens,
          };

          const response = await admin.messaging()
              .sendEachForMulticast(message);

          // Cleanup Token Basi
          if (response.failureCount > 0) {
            const failedTokens = [];
            response.responses.forEach((resp, idx) => {
              if (!resp.success) {
                const errCode = resp.error.code;
                if (errCode === "messaging/registration-token-not-registered" ||
                    errCode === "messaging/invalid-argument") {
                  failedTokens.push(tokens[idx]);
                }
              }
            });

            if (failedTokens.length > 0) {
              const updatedTokens = fcmTokens.filter(
                  (t) => !failedTokens.includes(t.token),
              );
              await db.collection("akun").doc(idAkun).update({
                fcmTokens: updatedTokens,
              });
              info(`Menghapus ${failedTokens.length} token basi.`);
            }
          }
          info(`FCM terkirim ke ${idAkun}: Success ${response.successCount}`);
        };

        allPromises.push(sendFCMProcess());

        // Eksekusi Update Counter & Kirim FCM bersamaan
        await Promise.all(allPromises);
      } catch (e) {
        error(`GAGAL Kirim FCM untuk Notif ID: ` +
            `${event.params["notifikasiId"]}`, e);
      }
      return null;
    });


// --- 2. UPDATE NOTIFIKASI (Cek Read Status -> Counter -1) ---
exports.onNotifikasiUpdate = onDocumentUpdated("notifikasi/{notifikasiId}",
    async (event) => {
      try {
        const dataBefore = event.data.before.data();
        const dataAfter = event.data.after.data();
        if (!dataAfter) return null;

        const db = getFirestore();
        const idAkun = dataAfter.idAkun;

        // Cek: Apakah status berubah dari Belum Dibaca (false) -> Dibaca (true)
        const isJustRead =
            dataBefore.alreadyRead === false &&
            dataAfter.alreadyRead === true;

        if (isJustRead && idAkun) {
          // Kurangi counter unread (-1)
          await db.collection("akun").doc(idAkun).update({
            numberOfUnreadNotification: FieldValue.increment(-1),
          });
          info(`Notifikasi dibaca. Counter akun ${idAkun} dikurangi 1.`);
        }
      } catch (e) {
        error(`GAGAL onNotifikasiUpdate ID: `+
            `${event.params["notifikasiId"]}`, e);
      }
      return null;
    });


// --- 3. DELETE NOTIFIKASI (Cleanup Counter) ---
exports.onNotifikasiDelete = onDocumentDeleted("notifikasi/{notifikasiId}",
    async (event) => {
      try {
        if (!event.data) return null;

        const data = event.data.data();
        const db = getFirestore();
        const idAkun = data.idAkun;
        const alreadyRead = data.alreadyRead;

        // Logika: Jika notifikasi yang
        // dihapus statusnya 'Belum Dibaca' (false),
        // maka counter di akun user masih menghitung notif ini.
        // Jadi kita harus kurangi 1 agar angkanya valid.
        if (idAkun && alreadyRead === false) {
          await db.collection("akun").doc(idAkun).update({
            numberOfUnreadNotification: FieldValue.increment(-1),
          });
          info(`Notifikasi unread dihapus. `+
              `Counter akun ${idAkun} dikurangi 1.`);
        }
      } catch (e) {
        error(`GAGAL onNotifikasiDelete ID: `+
            `${event.params["notifikasiId"]}`, e);
      }
      return null;
    });
