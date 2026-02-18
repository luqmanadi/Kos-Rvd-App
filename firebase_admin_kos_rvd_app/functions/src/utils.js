const {info, error} = require("firebase-functions/logger");

/**
 * Mengambil nama file dari URL.
 * @param {string} url - Link dari Firebase.
 * @return {string|null} - Nama file atau null jika error.
 */
function getFilePathFromUrl(url) {
  if (!url) return null;
  try {
    // 1. Buang bagian query params (?alt=...)
    const baseUrl = url.split("?")[0];

    // 2. Ambil string setelah /o/
    const parts = baseUrl.split("/o/");
    if (parts.length < 2) return null; // Validasi jika format URL beda

    const encodedPath = parts[1];

    // 3. Decode (%2F jadi /) agar dapat path aslinya
    return decodeURIComponent(encodedPath);
  } catch (e) {
    console.error("Error parsing URL:", e);
    return null;
  }
}

/**
 * Helper untuk update statistik dashboard admin.
 * Mencari dokumen pertama (limit 1) di collection ringkasanDashboardAdmin.
 * @param {Firestore} db Instance Firestore
 * @param {Object} updateData Object berisi field ->
 * yang mau diupdate (bisa pakai FieldValue)
 * @return {Promise<void>}
 */
async function updateAdminDashboardStat(db, updateData) {
  try {
    const snapshot = await db.collection("ringkasanDashboardAdmin")
        .limit(1).get();

    if (!snapshot.empty) {
      const docRef = snapshot.docs[0].ref;
      await docRef.update(updateData);
      info(`Update statistik dashboard berhasil: `+
          `${JSON.stringify(updateData)}`);
    } else {
      info("Dokumen ringkasanDashboardAdmin tidak ditemukan. Skip update.");
    }
  } catch (e) {
    error("Gagal update statistik dashboard admin: ", e);
  }
}


// Variabel untuk status tagihan
const BELUM_LUNAS = "Belum Lunas";
const LUNAS = "Lunas";
const MENUNGGU_VERIFIKASI = "Menunggu Verifikasi";
const MENUNGGU_KONFIRMASI = "Menunggu Konfirmasi";
const SEDANG_DIPROSES = "Sedang Diproses";
const SELESAI = "Selesai";
const TAGIHAN = "Tagihan";
const LAPORAN_KELUHAN = "Laporan Keluhan";
const PENGUMUMAN = "Pengumuman";

// EXPORT DI SINI
module.exports = {
  getFilePathFromUrl,
  updateAdminDashboardStat,
  BELUM_LUNAS,
  LUNAS,
  MENUNGGU_VERIFIKASI,
  MENUNGGU_KONFIRMASI,
  SEDANG_DIPROSES,
  SELESAI,
  TAGIHAN,
  LAPORAN_KELUHAN,
  PENGUMUMAN,
  // nanti kalau ada fungsi lain, tinggal tambah di bawahnya:
  // , formatRupiah
};
