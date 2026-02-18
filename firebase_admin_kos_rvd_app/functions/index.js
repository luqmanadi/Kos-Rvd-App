
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
