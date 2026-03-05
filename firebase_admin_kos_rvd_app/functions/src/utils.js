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

/**
 * Helper untuk membuat objek Date dengan zona waktu WIB (+07:00).
 *
 * @param {number} year - Tahun (contoh: 2024).
 * @param {number} month - Bulan dalam angka 0-11 (0 = Januari, 11 = Desember).
 * @param {number} day - Tanggal dalam sebulan (1-31).
 * @param {number} hour - Jam (0-23).
 * @param {number} minute - Menit (0-59).
 * @param {number} second - Detik (0-59).
 * @return {Date} Objek Date yang sudah disesuaikan dengan offset WIB.
 */
function createWibDate(year, month, day, hour, minute, second) {
  const pad = (n) => n.toString().padStart(2, "0");
  // Format ISO string dengan offset WIB: YYYY-MM-DDTHH:mm:ss.000+07:00
  const isoString = `${year}-${pad(month + 1)}-${pad(day)}T` +
      `${pad(hour)}:${pad(minute)}:${pad(second)}.000+07:00`;
  return new Date(isoString);
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
  createWibDate,
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
