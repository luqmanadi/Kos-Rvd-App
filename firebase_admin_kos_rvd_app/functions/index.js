
const {initializeApp} = require("firebase-admin/app");
const {setGlobalOptions} = require("firebase-functions/v2");

initializeApp({
  // Masukkan nama bucket storage kamu di sini
  storageBucket: "kos-rvd-app-12dac.firebasestorage.app",
});

setGlobalOptions({
  // eslint-disable-next-line max-len
  region: "asia-southeast2", // Kode fungsi jalan di Jakarta (dekat dengan Firestore)
  maxInstances: 5,
});


// --- IMPORT FUNGSI LAIN ---
const akun = require("./src/akun");
const kamar = require("./src/kamar");
const tagihan = require("./src/tagihan");
const keluhan = require("./src/keluhan");
const pengumuman = require("./src/pengumuman");
const notifikasi = require("./src/notifikasi");
const zonaParkiran = require("./src/zona_parkiran");
const parkirHarian = require("./src/parkir_harian");
const penyewaan = require("./src/penyewaan");
const pengaturan = require("./src/pengaturan");

// --- EXPORT FUNCTIONS ---

// Akun
exports.getDetailAkunPengguna = akun.getDetailAkunPengguna;
exports.createAkunPengguna = akun.createAkunPengguna;
exports.deactivateUserAccount = akun.deactivateUserAccount;
exports.reactivateUserAccount = akun.reactivateUserAccount;

// Kamar
exports.onKamarUpdate = kamar.onKamarUpdate;

// Tagihan
exports.onTagihanUpdate = tagihan.onTagihanUpdate;
exports.onTagihanCreate = tagihan.onTagihanCreate;
exports.onTagihanDelete = tagihan.onTagihanDelete;

// Keluhan
exports.onKeluhanUpdate = keluhan.onKeluhanUpdate;
exports.onKeluhanCreate = keluhan.onKeluhanCreate;
exports.onKeluhanDelete = keluhan.onKeluhanDelete;

// Pengumuman
exports.onDocumentCreatedPengumuman = pengumuman.onDocumentCreatedPengumuman;
exports.onDucumentDeletedPengumuman = pengumuman.onDucumentDeletedPengumuman;

// Notifikasi
exports.onNotifikasiCreate = notifikasi.onNotifikasiCreate;
exports.onNotifikasiUpdate = notifikasi.onNotifikasiUpdate;
exports.onNotifikasiDelete = notifikasi.onNotifikasiDelete;

// Zona Parkiran
exports.onZonaParkirUpdate = zonaParkiran.onZonaParkirUpdate;

// Parkir Harian
exports.onParkirHarianMobilUpdate = parkirHarian.onParkirHarianMobilUpdate;
exports.onDucumentDeletedParkirHarianMobil =
    parkirHarian.onDucumentDeletedParkirHarianMobil;

// Penyewaan
exports.onPenyewaanCreate = penyewaan.onPenyewaanCreate;
exports.onPenyewaanUpdate = penyewaan.onPenyewaanUpdate;

// Pengaturan
exports.generateTagihanOtomatis = pengaturan.generateTagihanOtomatis;
exports.pengingatTagihanHarian = pengaturan.pengingatTagihanHarian;
exports.hapusNotifikasiLama = pengaturan.hapusNotifikasiLama;
