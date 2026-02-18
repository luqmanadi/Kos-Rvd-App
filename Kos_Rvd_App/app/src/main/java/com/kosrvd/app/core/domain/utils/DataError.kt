package com.kosrvd.app.core.domain.utils

import com.kosrvd.app.core.data.constant.ErrorMessages
import com.kosrvd.app.core.data.constant.ErrorMessages.PARKIR_HARIAN_MOBIL_MAPPING_ERROR
import com.kosrvd.app.core.data.constant.LocalErrorMessages
import com.kosrvd.app.core.data.constant.NetworkErrorsMessages
import com.kosrvd.app.core.data.constant.SystemErrorsMessages

enum class DataError : Error {
    // Network Errors
    NETWORK_NO_INTERNET,
    NETWORK_UNAUTHORIZED,
    NETWORK_NOT_FOUND,
    NETWORK_SERVER_ERROR,
    NETWORK_TIMEOUT,
    NETWORK_CANCELLED,
    NETWORK_UNKNOWN_ERROR,

    //Zona Parkiran Errors
    ZONA_PARKIRAN_MOBIL_MAPPING_ERROR,
    ZONA_PARKIRAN_MOBIL_NOT_FOUND,

    // safecallApi Errors
    NETWORK_SERIALIZATION,

    // Kamar Errors
    NOMOR_KAMAR_TERPAKAI,

    // Firebase Functions Errors
    FUNCTION_INTERNAL,
    FUNCTION_UNAVAILABLE,
    FUNCTION_UNAUTHENTICATED,
    FUNCTION_INVALID_ARGUMENT,
    FUNCTION_RESOURCE_EXHAUSTED,
    FUNCTION_DATA_LOSS,
    FUNCTION_ABORTED,
    FUNCTION_DEADLINE_EXCEEDED,
    FUNCTION_FAILED_PRECONDITION,
    FUNCTION_NOT_FOUND,
    FUNCTION_PERMISSION_DENIED,
    FUNCTION_CANCELLED,
    FUNCTION_UNKNOWN,
    FUNCTION_ALREADY_EXISTS,
    FUNCTION_OUT_OF_RANGE,
    FUNCTION_UNIMPLEMENTED,


    // Data Admin Dashboard Errors
    DATA_DASHBOARD_ADMIN_NOT_FOUND,
    DATA_DASHBOARD_ADMIN_MAPPING_ERROR,

    // Kamar Errors
    KAMAR_MAPPING_ERROR,
    KAMAR_NOT_FOUND,

    // Local Errors
    LOCAL_STORAGE_FULL,
    LOCAL_PERMISSION_DENIED,
    LOCAL_SAVE_AUTH_ERROR,
    LOCAL_LOAD_AUTH_ERROR,
    LOCAL_CLEAR_AUTH_ERROR,
    LOCAL_DATA_ROLE_EMPTY,


    // Firebase Auth Errors
    AUTH_INVALID_EMAIL,
    AUTH_WRONG_PASSWORD,
    AUTH_USER_NOT_FOUND,
    AUTH_USER_DISABLED,
    AUTH_TOO_MANY_REQUESTS,
    AUTH_OPERATION_NOT_ALLOWED,
    AUTH_EMAIL_ALREADY_IN_USE,
    AUTH_WEAK_PASSWORD,
    AUTH_REQUIRES_RECENT_LOGIN,
    AUTH_NETWORK_REQUEST_FAILED,
    AUTH_INVALID_CREDENTIAL,
    AUTH_INVALID_VERIFICATION_CODE,
    AUTH_INVALID_VERIFICATION_ID,
    AUTH_EXPIRED_ACTION_CODE,
    AUTH_INVALID_ACTION_CODE,
    AUTH_ACCOUNT_EXISTS_DIFFERENT_CREDENTIAL,
    AUTH_CREDENTIAL_ALREADY_IN_USE,
    AUTH_PROVIDER_ALREADY_LINKED,
    AUTH_UNKNOWN_ERROR,
    AUTH_ERROR_REQUIRES_RECENT_LOGIN,

    // Firestore Errors
    FIRESTORE_PERMISSION_DENIED,
    FIRESTORE_UNAUTHENTICATED,
    FIRESTORE_NOT_FOUND,
    FIRESTORE_ALREADY_EXISTS,
    FIRESTORE_INVALID_ARGUMENT,
    FIRESTORE_DATA_LOSS,
    FIRESTORE_UNAVAILABLE,
    FIRESTORE_RESOURCE_EXHAUSTED,
    FIRESTORE_INTERNAL,
    FIRESTORE_UNIMPLEMENTED,
    FIRESTORE_FAILED_PRECONDITION,
    FIRESTORE_ABORTED,
    FIRESTORE_DEADLINE_EXCEEDED,
    FIRESTORE_CANCELLED,
    FIRESTORE_UNKNOWN_ERROR,

    // Business Logic Errors
    BUSINESS_USER_NOT_LOGGED_IN,
    BUSINESS_INVALID_CREDENTIALS,
    BUSINESS_ACCOUNT_NOT_FOUND,
    BUSINESS_ROLE_NOT_FOUND,
    BUSINESS_RESIDENT_NOT_FOUND,
    BUSINESS_RESIDENT_MAPPING_ERROR,
    BUSINESS_RESIDENT_ALREADY_EXISTS,
    BUSINESS_RESIDENT_CREATE_FAILED,
    BUSINESS_RESIDENT_UPDATE_FAILED,
    BUSINESS_RESIDENT_DELETE_FAILED,
    BUSINESS_DATA_NOT_FOUND,
    BUSINESS_DATA_INVALID,
    BUSINESS_DATA_MAPPING_ERROR,
    BUSINESS_OPERATION_NOT_ALLOWED,
    BUSINESS_VALIDATION_FAILED,
    BUSINESS_ADMIN_NO_ACCESS,
    BUSINESS_ACCOUNT_MAPPING_ERROR,
    BUSINESS_ACCOUNT_ALREADY_EXISTS,

    // System Errors
    SYSTEM_ERROR,
    UNKNOWN_ERROR,

    // Pengumuman errors
    PENGUMUMAN_MAPPING_ERROR,
    PENGUMUMAN_NOT_FOUND,
    BUSINESS_RESIDENT_ID_IS_BLANK,

    // Firebase Storage Errors
    STORAGE_BUCKET_NOT_FOUND,
    STORAGE_NOT_AUTHORIZED,
    STORAGE_OBJECT_NOT_FOUND,
    STORAGE_PROJECT_NOT_FOUND,
    STORAGE_QUOTA_EXCEEDED,
    STORAGE_NOT_AUTHENTICATED,
    STORAGE_RETRY_LIMIT_EXCEEDED,
    STORAGE_INVALID_CHECKSUM,
    STORAGE_CANCELED,
    STORAGE_UNKNOWN_ERROR,

    // Tagihan Error
    TAGIHAN_NOT_FOUND,
    TAGIHAN_SUDAH_TERDAFTAR,


    // Penyewaan Error
    PENYEWAAN_NOT_FOUND,
    PENYEWAAN_MAPPING_ERROR,

    // Keluhan Error
    KELUHAN_NOT_FOUND,
    KELUHAN_MAPPING_ERROR,

    // Pemakaian Alat Elektronik Error
    PEMAKAIAN_ALAT_ELEKTRONIK_MAPPING_ERROR,
    PEMAKAIAN_ALAT_ELEKTRONIK_NOT_FOUND,

    // Parkir Harian Mobil Errors
    PARKIR_HARIAN_MOBIL_NOT_FOUND,
    PARKIR_HARIAN_MOBIL_MAPPING_ERROR,
    PROOF_OF_PAYMENT_NOT_FOUND;

    val message: String
        get() = when (this) {

            // Parkir Harian Mobil Errors
            PARKIR_HARIAN_MOBIL_MAPPING_ERROR -> ErrorMessages.PARKIR_HARIAN_MOBIL_MAPPING_ERROR
            PARKIR_HARIAN_MOBIL_NOT_FOUND -> ErrorMessages.PARKIR_HARIAN_MOBIL_NOT_FOUND
            PROOF_OF_PAYMENT_NOT_FOUND -> ErrorMessages.PROOF_OF_PAYMENT_NOT_FOUND

            // Zona Parkiran Errors
            ZONA_PARKIRAN_MOBIL_MAPPING_ERROR -> ErrorMessages.ZONA_PARKIRAN_MOBIL_MAPPING_ERROR
            ZONA_PARKIRAN_MOBIL_NOT_FOUND -> ErrorMessages.ZONA_PARKIRAN_MOBIL_NOT_FOUND

            // Kamar Errors
            NOMOR_KAMAR_TERPAKAI -> ErrorMessages.NOMOR_KAMAR_TERPAKAI

            // Firebase Functions Errors
            FUNCTION_INTERNAL -> ErrorMessages.FUNCTION_INTERNAL
            FUNCTION_UNAVAILABLE -> ErrorMessages.FUNCTION_UNAVAILABLE
            FUNCTION_UNAUTHENTICATED -> ErrorMessages.FUNCTION_UNAUTHENTICATED
            FUNCTION_INVALID_ARGUMENT -> ErrorMessages.FUNCTION_INVALID_ARGUMENT
            FUNCTION_RESOURCE_EXHAUSTED -> ErrorMessages.FUNCTION_RESOURCE
            FUNCTION_DATA_LOSS -> ErrorMessages.FUNCTION_DATA_LOSS
            FUNCTION_ABORTED -> ErrorMessages.FUNCTION_ABORTED
            FUNCTION_DEADLINE_EXCEEDED -> ErrorMessages.FUNCTION_DEADLINE
            FUNCTION_FAILED_PRECONDITION -> ErrorMessages.FUNCTION_FAILED_PRECONDITION
            FUNCTION_NOT_FOUND -> ErrorMessages.FUNCTION_NOT_FOUND
            FUNCTION_PERMISSION_DENIED -> ErrorMessages.FUNCTION_PERMISSION
            FUNCTION_CANCELLED -> ErrorMessages.FUNCTION_CANCELLED
            FUNCTION_UNKNOWN -> ErrorMessages.FUNCTION_UNKNOWN
            FUNCTION_ALREADY_EXISTS -> ErrorMessages.FUNCTION_ALREADY_EXISTS
            FUNCTION_OUT_OF_RANGE -> ErrorMessages.FUNCTION_OUT_OF_RANGE
            FUNCTION_UNIMPLEMENTED -> ErrorMessages.FUNCTION_UNIMPLEMENT


            // Data Dashboard Admin Errors
            DATA_DASHBOARD_ADMIN_NOT_FOUND -> ErrorMessages.DATA_DASHBOARD_ADMIN_NOT_FOUND
            DATA_DASHBOARD_ADMIN_MAPPING_ERROR -> ErrorMessages.DATA_DASHBOARD_ADMIN_MAPPING_ERROR

            // Kamar Errors
            KAMAR_MAPPING_ERROR -> ErrorMessages.KAMAR_MAPPING_ERROR
            KAMAR_NOT_FOUND -> ErrorMessages.KAMAR_NOT_FOUND

            // Pemakaian Alat Elektronik Errors
            PEMAKAIAN_ALAT_ELEKTRONIK_MAPPING_ERROR -> ErrorMessages.PEMAKAIAN_ALAT_ELEKTRONIK_MAPPING_ERROR
            PEMAKAIAN_ALAT_ELEKTRONIK_NOT_FOUND -> ErrorMessages.PEMAKAIAN_ALAT_ELEKTRONIK_NOT_FOUND

            // Firebase Storage Errors
            STORAGE_BUCKET_NOT_FOUND -> "Bucket tidak ditemukan"
            STORAGE_NOT_AUTHORIZED -> "Tidak memiliki izin akses"
            STORAGE_OBJECT_NOT_FOUND -> "Object tidak ditemukan"
            STORAGE_PROJECT_NOT_FOUND -> "Project tidak ditemukan"
            STORAGE_QUOTA_EXCEEDED -> "Kuota storage terlampaui"
            STORAGE_NOT_AUTHENTICATED -> "Sesi login telah berakhir, silakan login ulang"
            STORAGE_RETRY_LIMIT_EXCEEDED -> "Terlalu banyak percobaan. Coba lagi nanti"
            STORAGE_INVALID_CHECKSUM -> "Checksum object tidak valid"
            STORAGE_CANCELED -> "Operasi dibatalkan"
            STORAGE_UNKNOWN_ERROR -> "Terjadi kesalahan storage"

            // Keluhan Errors
            KELUHAN_NOT_FOUND -> ErrorMessages.KELUHAN_NOT_FOUND
            KELUHAN_MAPPING_ERROR -> ErrorMessages.KELUHAN_MAPPING_ERROR

            // Penyewaan Errors
            PENYEWAAN_NOT_FOUND -> ErrorMessages.PENYEWAAN_NOT_FOUND
            PENYEWAAN_MAPPING_ERROR -> ErrorMessages.PENYEWAAN_MAPPING_ERROR

            // Network Errors
            NETWORK_NO_INTERNET -> NetworkErrorsMessages.NETWORK_ERROR
            NETWORK_UNAUTHORIZED -> NetworkErrorsMessages.NETWORK_UNAUTHORIZED
            NETWORK_NOT_FOUND -> NetworkErrorsMessages.NETWORK_NOT_FOUND
            NETWORK_SERVER_ERROR -> NetworkErrorsMessages.NETWORK_SERVER_ERROR
            NETWORK_TIMEOUT -> NetworkErrorsMessages.NETWORK_TIMEOUT
            NETWORK_CANCELLED -> NetworkErrorsMessages.NETWORK_CANCELLED
            NETWORK_UNKNOWN_ERROR -> NetworkErrorsMessages.NETWORK_UNKNOWN_ERROR

            // safecallApi Errors
            NETWORK_SERIALIZATION -> NetworkErrorsMessages.NETWORK_SERIALIZATION

            // Tagihan Error
            TAGIHAN_NOT_FOUND -> ErrorMessages.TAGIHAN_NOT_FOUND
            TAGIHAN_SUDAH_TERDAFTAR -> ErrorMessages.TAGIHAN_SUDAH_TERDAFTAR

            // Pengumuman Errors
            PENGUMUMAN_MAPPING_ERROR -> ErrorMessages.PENGUMUMAN_MAPPING_ERROR
            PENGUMUMAN_NOT_FOUND -> ErrorMessages.PENGUMUMAN_NOT_FOUND

            // Local Errors
            LOCAL_STORAGE_FULL -> LocalErrorMessages.STORAGE_FULL
            LOCAL_PERMISSION_DENIED -> LocalErrorMessages.PERMISSION_DENIED
            LOCAL_SAVE_AUTH_ERROR -> LocalErrorMessages.SAVE_AUTH_ERROR
            LOCAL_LOAD_AUTH_ERROR -> LocalErrorMessages.LOAD_AUTH_ERROR
            LOCAL_CLEAR_AUTH_ERROR -> LocalErrorMessages.CLEAR_AUTH_ERROR
            LOCAL_DATA_ROLE_EMPTY -> LocalErrorMessages.DATA_ROLE_EMPTY


            // Firebase Auth Errors
            AUTH_INVALID_EMAIL -> "Format email tidak valid"
            AUTH_WRONG_PASSWORD -> "Password salah"
            AUTH_USER_NOT_FOUND -> "Akun tidak ditemukan"
            AUTH_USER_DISABLED -> "Akun dinonaktifkan oleh administrator"
            AUTH_TOO_MANY_REQUESTS -> "Terlalu banyak percobaan. Coba lagi nanti"
            AUTH_OPERATION_NOT_ALLOWED -> "Login dengan email/password tidak diizinkan"
            AUTH_EMAIL_ALREADY_IN_USE -> "Email sudah digunakan oleh akun lain"
            AUTH_WEAK_PASSWORD -> "Password terlalu lemah, minimal 6 karakter"
            AUTH_REQUIRES_RECENT_LOGIN -> "Sesi login telah berakhir, silakan login ulang"
            AUTH_ERROR_REQUIRES_RECENT_LOGIN -> "Operasi sensitif, perlu autentikasi ulang. Silakan login kembali."
            AUTH_NETWORK_REQUEST_FAILED -> "Gagal terhubung ke jaringan"
            AUTH_INVALID_CREDENTIAL -> "Email atau password salah"
            AUTH_INVALID_VERIFICATION_CODE -> "Kode verifikasi tidak valid"
            AUTH_INVALID_VERIFICATION_ID -> "ID verifikasi tidak valid"
            AUTH_EXPIRED_ACTION_CODE -> "Kode verifikasi telah kadaluarsa"
            AUTH_INVALID_ACTION_CODE -> "Kode verifikasi tidak valid"
            AUTH_ACCOUNT_EXISTS_DIFFERENT_CREDENTIAL -> "Akun sudah ada dengan kredensial berbeda"
            AUTH_CREDENTIAL_ALREADY_IN_USE -> "Kredensial sudah digunakan oleh akun lain"
            AUTH_PROVIDER_ALREADY_LINKED -> "Provider sudah terhubung dengan akun ini"
            AUTH_UNKNOWN_ERROR -> "Terjadi kesalahan autentikasi"

            // Firestore Errors
            FIRESTORE_PERMISSION_DENIED -> "Tidak memiliki izin akses"
            FIRESTORE_UNAUTHENTICATED -> "Sesi login telah berakhir, silakan login ulang"
            FIRESTORE_NOT_FOUND -> "Data tidak ditemukan"
            FIRESTORE_ALREADY_EXISTS -> "Data sudah ada"
            FIRESTORE_INVALID_ARGUMENT -> "Format data tidak valid"
            FIRESTORE_DATA_LOSS -> "Terjadi kerusakan data"
            FIRESTORE_UNAVAILABLE -> "Layanan database tidak tersedia"
            FIRESTORE_RESOURCE_EXHAUSTED -> "Kuota database terlampaui. Coba lagi nanti"
            FIRESTORE_INTERNAL -> "Terjadi kesalahan internal server"
            FIRESTORE_UNIMPLEMENTED -> "Fitur ini belum tersedia"
            FIRESTORE_FAILED_PRECONDITION -> "Operasi tidak dapat dilakukan dalam kondisi saat ini"
            FIRESTORE_ABORTED -> "Operasi dibatalkan, coba lagi"
            FIRESTORE_DEADLINE_EXCEEDED -> "Waktu operasi habis, coba lagi"
            FIRESTORE_CANCELLED -> "Operasi dibatalkan"
            FIRESTORE_UNKNOWN_ERROR -> "Terjadi kesalahan database"

            // Business Logic Errors
            BUSINESS_USER_NOT_LOGGED_IN -> ErrorMessages.USER_NOT_LOGGED_IN
            BUSINESS_INVALID_CREDENTIALS -> ErrorMessages.INVALID_CREDENTIALS
            BUSINESS_ACCOUNT_NOT_FOUND -> ErrorMessages.ACCOUNT_NOT_FOUND
            BUSINESS_ACCOUNT_MAPPING_ERROR -> ErrorMessages.ACCOUNT_MAPPING_ERROR
            BUSINESS_ACCOUNT_ALREADY_EXISTS -> ErrorMessages.ACCOUNT_ALREADY_EXISTS
            BUSINESS_ROLE_NOT_FOUND -> ErrorMessages.ROLE_NOT_FOUND
            BUSINESS_RESIDENT_NOT_FOUND -> ErrorMessages.RESIDENT_NOT_FOUND
            BUSINESS_RESIDENT_MAPPING_ERROR -> ErrorMessages.RESIDENT_MAPPING_ERROR
            BUSINESS_RESIDENT_ALREADY_EXISTS -> ErrorMessages.RESIDENT_ALREADY_EXISTS
            BUSINESS_RESIDENT_CREATE_FAILED -> ErrorMessages.RESIDENT_CREATE_FAILED
            BUSINESS_RESIDENT_UPDATE_FAILED -> ErrorMessages.RESIDENT_UPDATE_FAILED
            BUSINESS_RESIDENT_DELETE_FAILED -> ErrorMessages.RESIDENT_DELETE_FAILED
            BUSINESS_DATA_NOT_FOUND -> ErrorMessages.DATA_NOT_FOUND
            BUSINESS_DATA_INVALID -> ErrorMessages.DATA_INVALID
            BUSINESS_DATA_MAPPING_ERROR -> ErrorMessages.DATA_MAPPING_ERROR
            BUSINESS_OPERATION_NOT_ALLOWED -> ErrorMessages.OPERATION_NOT_ALLOWED
            BUSINESS_VALIDATION_FAILED -> ErrorMessages.VALIDATION_FAILED
            BUSINESS_ADMIN_NO_ACCESS -> ErrorMessages.ADMIN_NO_ACCESS
            BUSINESS_RESIDENT_ID_IS_BLANK -> ErrorMessages.RESIDENT_ID_IS_BLANK

            // System Errors
            SYSTEM_ERROR -> SystemErrorsMessages.SYSTEM_ERROR
            UNKNOWN_ERROR -> SystemErrorsMessages.UNKNOWN_ERROR
        }
}