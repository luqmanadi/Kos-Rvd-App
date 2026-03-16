const {onSchedule} = require("firebase-functions/v2/scheduler");
const {
  getFirestore,
  Timestamp,
  FieldValue,
} = require("firebase-admin/firestore");
const {info, error} = require("firebase-functions/logger");
const {createWibDate, BELUM_LUNAS, TAGIHAN} = require("./utils");
const admin = require("firebase-admin");

exports.generateTagihanOtomatis = onSchedule({
  // (Menit 0, Jam 7, Tanggal 10, Bulan apa saja, Hari apa saja)
  schedule: "0 7 10 * *",
  timeZone: "Asia/Jakarta",
}, async (event) => {
  try {
    const db = getFirestore();

    // 1. CEK PENGATURAN GATEKEEPER
    const configDoc =
        await db.collection("pengaturan").doc("otomatisasi").get();
    if (!configDoc.exists) return null;
    if (configDoc.data().useGenerateOtomatis !== true) return null;

    info("Mulai generate tagihan otomatis...");

    // 2. KALKULASI TANGGAL (Meniru DatePicker Android -> UTC Midnight)
    const nowWib = new Date(new Date().toLocaleString("en-US",
        {timeZone: "Asia/Jakarta"}));
    const currentYear = nowWib.getFullYear();
    const currentMonth = nowWib.getMonth();

    // Period Start: Tgl 16, Jam 00:00:00 UTC
    // Format Date.UTC: (year, monthIndex, day, hours, minutes, seconds, ms)
    const periodStartMs = Date.UTC(currentYear, currentMonth, 16, 0, 0, 0, 0);

    // Period End: Tgl 15 bulan depan,
    // Jam 00:00:00 UTC (Sama seperti DatePicker Android)
    const nextMonth =
        currentMonth + 1; // JS akan otomatis handle pergantian tahun jika > 11
    const periodEndMs = Date.UTC(currentYear, nextMonth, 15, 0, 0, 0, 0);

    // Due Date: Ini tetap kita set ke akhir
    // hari WIB agar penghuni punya waktu full s.d jam 23:59
    const dueDateDate =
        createWibDate(currentYear, currentMonth, 15, 23, 59, 59);

    // Hitung sumDayPeriodeBill meniru CalculateTotalBillUseCase Android
    // diffMillis / (1000 * 60 * 60 * 24) + 1 (inklusif)
    const sumDayPeriodeBill =
        Math.floor((periodEndMs - periodStartMs) / (1000 * 60 * 60 * 24)) + 1;

    // Convert ke Timestamp Firestore
    const periodStartTs = Timestamp.fromMillis(periodStartMs);
    const periodEndTs = Timestamp.fromMillis(periodEndMs);
    const dueDateTs = Timestamp.fromDate(dueDateDate);
    const dateCreatedTs = Timestamp.now();

    // 3. AMBIL DATA PENYEWA AKTIF
    const penyewaanSnapshot = await db.collection("penyewaan")
        .where("rentalStatus", "==", "Aktif").get();

    if (penyewaanSnapshot.empty) {
      info("Tidak ada penyewa yang aktif. Selesai.");
      return null;
    }

    const batch = db.batch();
    let billsCreated = 0;

    // 4. LOOPING PEMBUATAN TAGIHAN
    for (const doc of penyewaanSnapshot.docs) {
      const p = doc.data();
      const idPenyewa = p.idPenyewa || doc.id;

      // PENCEGAHAN DUPLIKAT
      const existCheck = await db.collection("tagihan")
          .where("idPenyewa", "==", idPenyewa)
          .where("periodEnd", "==", periodEndTs)
          .get();

      if (!existCheck.empty) {
        info(`Tagihan untuk penyewa ${idPenyewa} `+
            `periode ini sudah ada. Melewati...`);
        continue;
      }

      // Mapping Data
      const listResident = p.listResident || [];
      const residentAccountIdList = listResident.map((r) => r.idAkun);
      const residentNameList = listResident.map((r) => r.name);
      const isTwoPersons = listResident.length > 1;

      const roomRentalFee =
          (isTwoPersons && p.infoKamar.currentRoomRentalCost.twoPersons) ?
              p.infoKamar.currentRoomRentalCost.twoPersons :
              p.infoKamar.currentRoomRentalCost.onePerson;

      const carParkingRentalFeeMonthly =
          p.pemakaianParkirMobilBulanan?.zonaParkir?.monthlyFee || null;

      const alatElektronik = p.pemakaianAlatElektronikBulanan || [];
      const electronicCostTotal =
          alatElektronik.reduce((sum, item) => sum + (item.cost || 0), 0);

      const billAmount =
          roomRentalFee +
          (carParkingRentalFeeMonthly || 0) +
          electronicCostTotal;

      const newBillRef = db.collection("tagihan").doc();

      const tagihanData = {
        idPenyewa: idPenyewa,
        numberRoom: p.infoKamar.numberRoom,
        residentAccountIdList: residentAccountIdList,
        residentNameList: residentNameList,
        periodStart: periodStartTs,
        periodEnd: periodEndTs,
        dueDate: dueDateTs,
        dateCreated: dateCreatedTs,
        diskon: null,
        roomRentalFee: roomRentalFee,
        carParkingRentalFeeMonthly: carParkingRentalFeeMonthly,
        highPowerElectronicEquipmentUsageCostsMonthly: alatElektronik,
        adminFees: false,
        billAmount: billAmount,
        sumDayPeriodeBill: sumDayPeriodeBill,
        prorataDetail: null,
        paymentStatus: BELUM_LUNAS, // Menggunakan konstan dari utils
        proofOfPayment: null,
        rejectionStatement: null,
        dateUploadProof: null,
        verificationDate: null,
        datePaidOff: null,
      };

      batch.set(newBillRef, tagihanData);
      billsCreated++;
    }

    // 5. COMMIT BATCH
    if (billsCreated > 0) {
      await batch.commit();
      info(`Berhasil membuat ${billsCreated} tagihan otomatis.`);
    } else {
      info("Tidak ada tagihan baru yang perlu dibuat " +
          "(semua sudah di-generate sebelumnya).");
    }

    return null;
  } catch (e) {
    // Penanganan Error Tingkat Lanjut
    error("Gagal menjalankan scheduler generateTagihanOtomatis:", e);
    return null;
  }
});


exports.pengingatTagihanHarian = onSchedule({
  schedule: "0 9 * * *", // Berjalan setiap hari jam 09:00 pagi
  timeZone: "Asia/Jakarta",
}, async (event) => {
  try {
    const db = getFirestore();

    info("Mulai menjalankan pengingatTagihanHarian...");

    // CEK PENGATURAN GATEKEEPER
    const configDoc =
        await db.collection("pengaturan").doc("otomatisasi").get();
    if (!configDoc.exists) return null;
    if (configDoc.data().useAutoReminder !== true) return null;

    // 1. Ambil waktu saat ini dalam WIB
    const nowWib = new Date(
        new Date().toLocaleString("en-US", {timeZone: "Asia/Jakarta"}));
    // Normalize ke jam 00:00:00 agar kalkulasi selisih hari akurat
    const todayMidnight = new Date(
        nowWib.getFullYear(), nowWib.getMonth(), nowWib.getDate());

    // 2. Cari semua tagihan yang BELUM LUNAS
    const tagihanSnapshot = await db.collection("tagihan")
        .where("paymentStatus", "==", BELUM_LUNAS)
        .get();

    if (tagihanSnapshot.empty) {
      info("Semua tagihan lunas! Tidak ada notifikasi yang dikirim.");
      return null;
    }

    const allPromises = [];

    // 3. Looping setiap tagihan yang belum lunas
    for (const doc of tagihanSnapshot.docs) {
      const data = doc.data();
      const residentIds = data.residentAccountIdList || [];
      const dueDateTs = data.dueDate; // Timestamp dari Firestore
      const tagihanId = doc.id;
      const roomNo = data.numberRoom;

      if (!dueDateTs || residentIds.length === 0) continue;

      // Kalkulasi selisih hari
      const dueDateWib =
          new Date(dueDateTs.toDate()
              .toLocaleString("en-US", {timeZone: "Asia/Jakarta"}));
      const dueMidnight =
          new Date(dueDateWib.getFullYear(),
              dueDateWib.getMonth(), dueDateWib.getDate());

      const diffTime = dueMidnight - todayMidnight;
      const diffDays =
          Math.ceil(diffTime / (1000 * 60 * 60 * 24)); // Hasil dalam hari

      // Format string tanggal untuk isi pesan
      const deadlineStr = dueDateWib.toLocaleDateString("id-ID", {
        day: "numeric", month: "long", year: "numeric",
      });

      let title = "";
      let content = "";
      let isMilestone =
          false; // Jika true -> Masuk DB. Jika false -> Ghost FCM.
      let shouldSend =
          true; // Kontrol apakah hari ini perlu kirim notif atau skip

      // 4. Logika Penentuan Pesan & Milestone
      if (diffDays === 2) {
        title = "Pengingat: H-2 Jatuh Tempo";
        content = `Tagihan kamar no ${roomNo} jatuh tempo `+
            `pada ${deadlineStr}. Jangan lupa bayar ya!`;
        isMilestone = true;
      } else if (diffDays === 1) {
        title = "Peringatan: H-1 Jatuh Tempo!";
        content = `Besok adalah hari terakhir pembayaran `+
            `tagihan kamar no ${roomNo}. Segera lunasi ya!`;
        isMilestone = true;
      } else if (diffDays === 0) {
        title = "🚨 HARI INI JATUH TEMPO 🚨";
        content = `Tagihan kamar no ${roomNo} harus dilunasi hari ini `+
            `(${deadlineStr}) sebelum jam 23:59.`;
        isMilestone = true;
      } else if (diffDays === -1) {
        // H+1 Telat (Peringatan pertama menunggak, simpan ke DB sebagai bukti)
        title = "⚠️ TAGIHAN MULAI MENUNGGAK ⚠️";
        content = `Tagihan kamar no ${roomNo} telah melewati `+
            `batas waktu pembayaran. Mohon segera lunasi.`;
        isMilestone = true;
      } else if (diffDays < -1) {
        // H+2 dan seterusnya (Murni Ghost FCM "Teror" harian)
        const hariTelat = Math.abs(diffDays);
        title = "⚠️ TAGIHAN MENUNGGAK ⚠️";
        content = `Tagihan kamar no ${roomNo} telah menunggak `+
            `${hariTelat} hari! Segera lakukan pembayaran.`;
        isMilestone = false; // Tidak masuk riwayat DB, tapi HP berbunyi
      } else {
        // diffDays > 2 (Misal H-5, H-4, H-3)
        // Opsional: Matikan shouldSend
        // jika tidak ingin spam Ghost FCM terlalu dini.
        // Saat ini diatur tetap mengirim
        // Ghost FCM "Info Tagihan Aktif" setiap pagi.
        title = "Info Tagihan Aktif";
        content = `Anda memiliki tagihan aktif untuk kamar no `+
            `${roomNo}. Batas akhir pembayaran: ${deadlineStr}.`;
        isMilestone = false;
        shouldSend = true; // Ubah ke 'false' jika H-5 s.d
        // H-3 tidak mau ada notif sama sekali
      }

      if (!shouldSend) continue;
      // Skip jika hari ini tidak dijadwalkan kirim notif

      // 5. Eksekusi Notifikasi per Penghuni
      for (const idAkun of residentIds) {
        if (isMilestone) {
          // OPSI A: Simpan ke DB (otomatis trigger onNotifikasiCreate milikmu)
          const dbPromise = db.collection("notifikasi").add({
            idAkun: idAkun,
            idDetailReferensi: tagihanId,
            title: title,
            content: content,
            date: FieldValue.serverTimestamp(),
            alreadyRead: false,
            notificationType: TAGIHAN,
          });
          allPromises.push(dbPromise);
        } else {
          // OPSI B: Ghost FCM (Kirim langsung tanpa masuk DB)
          const ghostFcmPromise =
              kirimGhostFCM(db, idAkun, title, content, tagihanId);
          allPromises.push(ghostFcmPromise);
        }
      }
    }

    if (allPromises.length > 0) {
      await Promise.all(allPromises);
      info(`Berhasil memproses ${allPromises.length} `+
          `aksi pengingat (Campuran DB & Ghost FCM).`);
    } else {
      info(
          "Tidak ada target notifikasi yang memenuhi kriteria hari ini.");
    }

    return null;
  } catch (e) {
    error("Gagal menjalankan pengingatTagihanHarian:", e);
    return null;
  }
});

/**
 * Helper terpisah untuk mengirim Ghost FCM beserta fitur pembersihan Token Basi
 * @param {FirebaseFirestore.Firestore} db
 * @param {string} idAkun
 * @param {string} title
 * @param {string} content
 * @param {string} tagihanId
 */
async function kirimGhostFCM(db, idAkun, title, content, tagihanId) {
  try {
    const userDoc = await db.collection("akun").doc(idAkun).get();
    if (!userDoc.exists) return;

    const fcmTokens = userDoc.data().fcmTokens || [];
    if (fcmTokens.length === 0) return;

    const tokens = fcmTokens.map((t) => t.token);

    const message = {
      notification: {title: title, body: content},
      data: {
        idNotifikasi: "", // Kosongkan karena tidak ada di DB Notifikasi
        typeNotification: TAGIHAN,
        // Supaya DeepLink Android tetap jalan ke halaman Detail Tagihan
        idDetailReferensi: tagihanId,
      },
      android: {
        priority: "high",
      },
      tokens: tokens,
    };

    const response = await admin.messaging().sendEachForMulticast(message);

    // Cleanup Token Basi (Sama persis seperti di fungsi utamamu)
    if (response.failureCount > 0) {
      const failedTokens = [];
      response.responses.forEach((resp, idx) => {
        if (!resp.success) {
          const errCode = resp.error?.code;
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
        info(`[Ghost FCM] Menghapus ${failedTokens.length} `+
            `token basi untuk akun ${idAkun}.`);
      }
    }
  } catch (e) {
    error(`Gagal kirim Ghost FCM ke ${idAkun}`, e);
  }
}

exports.hapusNotifikasiLama = onSchedule({
  schedule: "0 2 * * *",
  timeZone: "Asia/Jakarta",
}, async (event) => {
  try {
    const db = getFirestore();

    info("Mulai menjalankan pembersihan notifikasi lama...");

    // 1. Hitung tanggal batas (Cut-off Date) -> 30 Hari yang lalu
    const now = new Date();
    // 30 hari * 24 jam * 60 menit * 60 detik * 1000 milidetik
    const batasWaktuDate = new Date(now.getTime() - (30 * 24 * 60 * 60 * 1000));
    const batasWaktuTs = Timestamp.fromDate(batasWaktuDate);

    info(`Mencari notifikasi yang dibuat sebelum: `+
        `${batasWaktuDate.toISOString()}`);

    // 2. Query ke Firestore
    // (Ambil max 500 dokumen karena batas batch Firestore)
    // Kita filter berdasarkan field
    // "date" yang lebih kecil (lebih lama) dari batas waktu
    const snapshot = await db.collection("notifikasi")
        .where("date", "<", batasWaktuTs)
        .limit(300)
        .get();

    if (snapshot.empty) {
      info("Database bersih. Tidak ada notifikasi berumur lebih dari 30 hari.");
      return null;
    }

    // 3. Proses Penghapusan menggunakan
    // Batch (Biar performa cepat dan hemat operasi)
    const batch = db.batch();
    let totalDihapus = 0;

    snapshot.docs.forEach((doc) => {
      batch.delete(doc.ref);
      totalDihapus++;
    });

    // Eksekusi penghapusan
    await batch.commit();

    info(`Pembersihan Selesai! Berhasil menghapus `+
        `${totalDihapus} notifikasi lama.`);

    // Catatan: Jika kebetulan ada > 300 notif usang,
    // sisanya akan otomatis terhapus pada jadwal pembersihan besok paginya.
    return null;
  } catch (e) {
    error("Gagal menjalankan hapusNotifikasiLama:", e);
    return null;
  }
});
