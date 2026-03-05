package com.kosrvd.app.core.data.constant

object ErrorMessages {
    // Auth errors
    const val USER_NOT_LOGGED_IN = "Anda harus login terlebih dahulu"
    const val INVALID_CREDENTIALS = "Email atau password salah"
    const val ADMIN_NO_ACCESS = "Admin tidak memiliki akses di aplikasi penghuni"

    // Parkir Harian Mobil Errors
    const val PARKIR_HARIAN_MOBIL_MAPPING_ERROR = "Gagal memproses data parkir harian mobil"
    const val PARKIR_HARIAN_MOBIL_NOT_FOUND = "Data parkir harian mobil tidak ditemukan"
    const val PROOF_OF_PAYMENT_NOT_FOUND = "Bukti pembayaran tidak ditemukan"

    // Data Dashboard Admin Errors
    const val DATA_DASHBOARD_ADMIN_NOT_FOUND = "Data dashboard admin tidak ditemukan"
    const val DATA_DASHBOARD_ADMIN_MAPPING_ERROR = "Gagal memproses data dashboard admin"

    // Keluhan Errors
    const val KELUHAN_NOT_FOUND = "Data keluhan tidak ditemukan"
    const val KELUHAN_MAPPING_ERROR = "Gagal memproses data keluhan"

    // Akun errors
    const val ACCOUNT_NOT_FOUND = "Akun tidak ditemukan"
    const val ROLE_NOT_FOUND = "Role tidak ditemukan"
    const val ACCOUNT_MAPPING_ERROR = "Gagal memproses data akun"
    const val ACCOUNT_ALREADY_EXISTS = "Akun sudah ada"

    // Tagihan Errors
    const val TAGIHAN_NOT_FOUND = "Data tagihan tidak ditemukan"
    const val TAGIHAN_SUDAH_TERDAFTAR = "Tagihan untuk penyewa ini pada periode yang sama sudah terdaftar. Silakan periksa daftar tagihan."


    // Resident errors
    const val RESIDENT_NOT_FOUND = "Data penghuni tidak ditemukan"
    const val RESIDENT_MAPPING_ERROR = "Gagal memproses data penghuni"
    const val RESIDENT_ALREADY_EXISTS = "Data penghuni sudah ada"
    const val RESIDENT_CREATE_FAILED = "Gagal menambah data penghuni"
    const val RESIDENT_UPDATE_FAILED = "Gagal mengupdate data penghuni"
    const val RESIDENT_DELETE_FAILED = "Gagal menghapus data penghuni"
    const val RESIDENT_ID_IS_BLANK = "ID penghuni di local storage kosong"

    // Pengumuman errors
    const val PENGUMUMAN_NOT_FOUND = "Data pengumuman tidak ditemukan"
    const val PENGUMUMAN_MAPPING_ERROR = "Gagal memproses data pengumuman"

    // General errors
    const val DATA_NOT_FOUND = "Data tidak ditemukan"
    const val DATA_INVALID = "Data tidak valid"
    const val DATA_MAPPING_ERROR = "Gagal memproses data"
    const val OPERATION_NOT_ALLOWED = "Operasi tidak diizinkan"
    const val VALIDATION_FAILED = "Data gagal divalidasi"


    // Penyewaan Errors
    const val PENYEWAAN_NOT_FOUND = "Data penyewaan tidak ditemukan"
    const val PENYEWAAN_MAPPING_ERROR = "Gagal memproses data penyewaan"

    // Pemakaian Alat Elektronik Errors
    const val PEMAKAIAN_ALAT_ELEKTRONIK_MAPPING_ERROR = "Gagal memproses data pemakaian alat elektronik"
    const val PEMAKAIAN_ALAT_ELEKTRONIK_NOT_FOUND = "Data pemakaian alat elektronik tidak ditemukan"

    // Pengaturan Error
    const val OTOMATISASI_NOT_FOUND = "Data otomatisasi tidak ditemukan"
    const val OTOMATISASI_MAPPING_ERROR = "Gagal memproses data automatisasi"

    // Kamar Error
    const val KAMAR_MAPPING_ERROR = "Gagal memproses data kamar"
    const val KAMAR_NOT_FOUND = "Data kamar tidak ditemukan"

    // Firebase Functions Errors Message
    const val FUNCTION_INTERNAL = "Terjadi kesalahan pada server"
    const val FUNCTION_UNAVAILABLE = "Layanan tidak tersedia"
    const val FUNCTION_UNAUTHENTICATED = "Anda tidak memiliki akses"
    const val FUNCTION_INVALID_ARGUMENT = "Format data tidak valid"
    const val FUNCTION_RESOURCE = "Kuota terlampaui"
    const val FUNCTION_DATA_LOSS = "Terjadi kerusakan data"
    const val FUNCTION_ABORTED = "Operasi dibatalkan"
    const val FUNCTION_DEADLINE = "Waktu operasi habis"
    const val FUNCTION_FAILED_PRECONDITION = "Operasi tidak dapat dilakukan dalam kondisi saat ini"
    const val FUNCTION_NOT_FOUND = "Fungsi tidak ditemukan"
    const val FUNCTION_PERMISSION = "Tidak memiliki izin akses"
    const val FUNCTION_CANCELLED = "Operasi dibatalkan"
    const val FUNCTION_UNKNOWN = "Terjadi kesalahan pada server"
    const val FUNCTION_ALREADY_EXISTS = "Fungsi sudah ada"
    const val FUNCTION_OUT_OF_RANGE = "Data tidak valid"
    const val FUNCTION_UNIMPLEMENT = "Fungsi belum tersedia"

    // Kamar Errors
    const val NOMOR_KAMAR_TERPAKAI = "Nomor kamar sudah terpakai"

    // Zona Parkiran Mobil Errors
    const val ZONA_PARKIRAN_MOBIL_MAPPING_ERROR = "Gagal memproses data zona parkir mobil"
    const val ZONA_PARKIRAN_MOBIL_NOT_FOUND = "Data zona parkir mobil tidak ditemukan"

}