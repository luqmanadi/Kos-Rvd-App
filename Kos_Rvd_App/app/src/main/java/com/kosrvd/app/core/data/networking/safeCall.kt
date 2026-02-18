package com.kosrvd.app.core.data.networking


import android.util.Log
import androidx.datastore.core.IOException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.FirebaseFirestoreException.Code
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.firebase.storage.StorageException
import com.kosrvd.app.core.data.constant.ErrorMessages
import com.kosrvd.app.core.data.constant.FirebaseAuthErrors
import com.kosrvd.app.core.data.constant.LocalErrorMessages
import com.kosrvd.app.core.domain.utils.DataError
import java.util.concurrent.CancellationException
import com.kosrvd.app.core.domain.utils.Result

suspend fun <T> safeCall(
    call: suspend () -> T
): Result<T, DataError> {
    return try {
        Result.Success(call())
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        Result.Error(e.toDataError())
    }
}

private fun Exception.toDataError(): DataError {
    return when (this) {
        // Firebase Auth Errors
        is FirebaseAuthException -> {
            when (errorCode) {
                FirebaseAuthErrors.USER_NOT_FOUND -> DataError.AUTH_USER_NOT_FOUND
                FirebaseAuthErrors.USER_DISABLED -> DataError.AUTH_USER_DISABLED
                FirebaseAuthErrors.EMAIL_ALREADY_IN_USE -> DataError.AUTH_EMAIL_ALREADY_IN_USE
                FirebaseAuthErrors.INVALID_CREDENTIAL -> DataError.AUTH_INVALID_CREDENTIAL
                FirebaseAuthErrors.TOKEN_EXPIRED -> DataError.AUTH_EXPIRED_ACTION_CODE
                FirebaseAuthErrors.INVALID_TOKEN -> DataError.AUTH_INVALID_ACTION_CODE
                FirebaseAuthErrors.ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL -> DataError.AUTH_ACCOUNT_EXISTS_DIFFERENT_CREDENTIAL
                FirebaseAuthErrors.CREDENTIAL_ALREADY_IN_USE -> DataError.AUTH_CREDENTIAL_ALREADY_IN_USE
                FirebaseAuthErrors.ERROR_REQUIRES_RECENT_LOGIN -> DataError.AUTH_ERROR_REQUIRES_RECENT_LOGIN
                FirebaseAuthErrors.WRONG_PASSWORD -> DataError.AUTH_WRONG_PASSWORD
                else -> DataError.AUTH_UNKNOWN_ERROR.also {
                    Log.e("safeCall Auth", "Unknown error: $message, Error Code: $errorCode")
                }
            }
        }

        is FirebaseTooManyRequestsException -> DataError.AUTH_TOO_MANY_REQUESTS

        // Firestore Errors
        is FirebaseFirestoreException -> {
            when (code) {
                Code.PERMISSION_DENIED -> DataError.FIRESTORE_PERMISSION_DENIED
                Code.NOT_FOUND -> DataError.FIRESTORE_NOT_FOUND
                Code.ALREADY_EXISTS -> DataError.FIRESTORE_ALREADY_EXISTS
                Code.INVALID_ARGUMENT -> DataError.FIRESTORE_INVALID_ARGUMENT
                Code.UNAVAILABLE -> DataError.FIRESTORE_UNAVAILABLE
                Code.UNAUTHENTICATED -> DataError.FIRESTORE_UNAUTHENTICATED
                Code.DATA_LOSS -> DataError.FIRESTORE_DATA_LOSS
                Code.RESOURCE_EXHAUSTED -> DataError.FIRESTORE_RESOURCE_EXHAUSTED
                Code.FAILED_PRECONDITION -> DataError.FIRESTORE_FAILED_PRECONDITION
                Code.ABORTED -> DataError.FIRESTORE_ABORTED
                Code.DEADLINE_EXCEEDED -> DataError.FIRESTORE_DEADLINE_EXCEEDED
                Code.INTERNAL -> DataError.FIRESTORE_INTERNAL
                Code.UNIMPLEMENTED -> DataError.FIRESTORE_UNIMPLEMENTED
                Code.CANCELLED -> DataError.FIRESTORE_CANCELLED
                Code.UNKNOWN -> DataError.FIRESTORE_UNKNOWN_ERROR
                else -> DataError.FIRESTORE_UNKNOWN_ERROR.also {
                    Log.e("safeCall Firestore", "Unknown error: $message, Error Code: $code")
                }
            }
        }

        // Firebase Function Errors
        is FirebaseFunctionsException -> {
            when(code){
                FirebaseFunctionsException.Code.INTERNAL -> DataError.FUNCTION_INTERNAL.also {
                    Log.e("safeCall Function",
                        "Internal error: $message, " +
                                "Error Code: $code, " +
                                "Cause: $cause, " +
                                "Details: $details, " +
                                "Stack Trace: ${stackTrace.forEach { it.toString() }}," +
                                " Stack Trace String: ${stackTraceToString()}," +
                                " Stack Trace Elements: ${stackTrace.contentToString()}")
                }
                FirebaseFunctionsException.Code.UNAVAILABLE -> DataError.FUNCTION_UNAVAILABLE
                FirebaseFunctionsException.Code.UNAUTHENTICATED -> DataError.FUNCTION_UNAUTHENTICATED
                FirebaseFunctionsException.Code.INVALID_ARGUMENT -> DataError.FUNCTION_INVALID_ARGUMENT
                FirebaseFunctionsException.Code.RESOURCE_EXHAUSTED -> DataError.FUNCTION_RESOURCE_EXHAUSTED
                FirebaseFunctionsException.Code.DATA_LOSS -> DataError.FUNCTION_DATA_LOSS
                FirebaseFunctionsException.Code.ABORTED -> DataError.FUNCTION_ABORTED
                FirebaseFunctionsException.Code.DEADLINE_EXCEEDED -> DataError.FUNCTION_DEADLINE_EXCEEDED
                FirebaseFunctionsException.Code.FAILED_PRECONDITION -> DataError.FUNCTION_FAILED_PRECONDITION
                FirebaseFunctionsException.Code.NOT_FOUND -> DataError.FUNCTION_NOT_FOUND
                FirebaseFunctionsException.Code.PERMISSION_DENIED -> DataError.FUNCTION_PERMISSION_DENIED
                FirebaseFunctionsException.Code.CANCELLED -> DataError.FUNCTION_CANCELLED
                FirebaseFunctionsException.Code.UNKNOWN -> DataError.FUNCTION_UNKNOWN
                FirebaseFunctionsException.Code.ALREADY_EXISTS -> {
                    if (this.message?.contains("Email", ignoreCase = true) == true) {
                        DataError.AUTH_EMAIL_ALREADY_IN_USE
                    } else {
                        DataError.FUNCTION_ALREADY_EXISTS
                    }
                }
                FirebaseFunctionsException.Code.OUT_OF_RANGE -> DataError.FUNCTION_OUT_OF_RANGE
                FirebaseFunctionsException.Code.UNIMPLEMENTED -> DataError.FUNCTION_UNIMPLEMENTED
                else -> DataError.FUNCTION_UNKNOWN.also {
                    Log.e("safeCall Function", "Unknown error: $message, Error Code: $code")
                }
            }
        }

        // Network Errors
        is FirebaseNetworkException -> DataError.NETWORK_NO_INTERNET
        is IOException -> {
            when {
                message?.contains("Unable to resolve host") == true -> DataError.NETWORK_NO_INTERNET
                message?.contains("Canceled") == true -> DataError.NETWORK_CANCELLED
                message?.contains("timeout", ignoreCase = true) == true -> DataError.NETWORK_TIMEOUT
                else -> DataError.NETWORK_SERVER_ERROR
            }
        }

        is StorageException -> {
            when(errorCode){
                StorageException.ERROR_NOT_AUTHORIZED -> DataError.STORAGE_NOT_AUTHORIZED
                StorageException.ERROR_OBJECT_NOT_FOUND -> DataError.STORAGE_OBJECT_NOT_FOUND
                StorageException.ERROR_BUCKET_NOT_FOUND -> DataError.STORAGE_BUCKET_NOT_FOUND
                StorageException.ERROR_PROJECT_NOT_FOUND -> DataError.STORAGE_PROJECT_NOT_FOUND
                StorageException.ERROR_QUOTA_EXCEEDED -> DataError.STORAGE_QUOTA_EXCEEDED
                StorageException.ERROR_NOT_AUTHENTICATED -> DataError.STORAGE_NOT_AUTHENTICATED
                StorageException.ERROR_RETRY_LIMIT_EXCEEDED -> DataError.STORAGE_RETRY_LIMIT_EXCEEDED
                StorageException.ERROR_INVALID_CHECKSUM -> DataError.STORAGE_INVALID_CHECKSUM
                StorageException.ERROR_CANCELED -> DataError.STORAGE_CANCELED
                else -> DataError.STORAGE_UNKNOWN_ERROR.also {
                    Log.e("safeCall Storage", "Unknown error: $message, Error Code: $errorCode")
                }
            }
        }

        // Local Storage Errors
        is SecurityException -> DataError.LOCAL_PERMISSION_DENIED

        // Custom Business Logic Errors
        else -> {
            // Handle custom exceptions yang kita throw
            when (message) {
                LocalErrorMessages.SAVE_AUTH_ERROR -> DataError.LOCAL_SAVE_AUTH_ERROR
                LocalErrorMessages.LOAD_AUTH_ERROR -> DataError.LOCAL_LOAD_AUTH_ERROR
                LocalErrorMessages.CLEAR_AUTH_ERROR -> DataError.LOCAL_CLEAR_AUTH_ERROR
                ErrorMessages.USER_NOT_LOGGED_IN -> DataError.BUSINESS_USER_NOT_LOGGED_IN
                ErrorMessages.INVALID_CREDENTIALS  -> DataError.BUSINESS_INVALID_CREDENTIALS
                ErrorMessages.ACCOUNT_NOT_FOUND -> DataError.BUSINESS_ACCOUNT_NOT_FOUND
                ErrorMessages.ACCOUNT_MAPPING_ERROR -> DataError.BUSINESS_ACCOUNT_MAPPING_ERROR
                ErrorMessages.ACCOUNT_ALREADY_EXISTS -> DataError.BUSINESS_ACCOUNT_ALREADY_EXISTS
                ErrorMessages.ROLE_NOT_FOUND -> DataError.BUSINESS_ROLE_NOT_FOUND
                ErrorMessages.PENGUMUMAN_MAPPING_ERROR -> DataError.PENGUMUMAN_MAPPING_ERROR
                ErrorMessages.PENGUMUMAN_NOT_FOUND -> DataError.PENGUMUMAN_NOT_FOUND
                ErrorMessages.RESIDENT_NOT_FOUND -> DataError.BUSINESS_RESIDENT_NOT_FOUND
                ErrorMessages.RESIDENT_MAPPING_ERROR -> DataError.BUSINESS_RESIDENT_MAPPING_ERROR
                ErrorMessages.RESIDENT_ALREADY_EXISTS -> DataError.BUSINESS_RESIDENT_ALREADY_EXISTS
                ErrorMessages.TAGIHAN_NOT_FOUND -> DataError.TAGIHAN_NOT_FOUND
                ErrorMessages.ADMIN_NO_ACCESS -> DataError.BUSINESS_ADMIN_NO_ACCESS
                ErrorMessages.PENYEWAAN_NOT_FOUND -> DataError.PENYEWAAN_NOT_FOUND
                ErrorMessages.PENYEWAAN_MAPPING_ERROR -> DataError.PENYEWAAN_MAPPING_ERROR
                ErrorMessages.KELUHAN_NOT_FOUND -> DataError.KELUHAN_NOT_FOUND
                ErrorMessages.KELUHAN_MAPPING_ERROR -> DataError.KELUHAN_MAPPING_ERROR
                ErrorMessages.PEMAKAIAN_ALAT_ELEKTRONIK_MAPPING_ERROR -> DataError.PEMAKAIAN_ALAT_ELEKTRONIK_MAPPING_ERROR
                ErrorMessages.PEMAKAIAN_ALAT_ELEKTRONIK_NOT_FOUND -> DataError.PEMAKAIAN_ALAT_ELEKTRONIK_NOT_FOUND
                ErrorMessages.KAMAR_MAPPING_ERROR -> DataError.KAMAR_MAPPING_ERROR
                ErrorMessages.KAMAR_NOT_FOUND -> DataError.KAMAR_NOT_FOUND
                ErrorMessages.ZONA_PARKIRAN_MOBIL_MAPPING_ERROR -> DataError.ZONA_PARKIRAN_MOBIL_MAPPING_ERROR
                ErrorMessages.ZONA_PARKIRAN_MOBIL_NOT_FOUND -> DataError.ZONA_PARKIRAN_MOBIL_NOT_FOUND
                ErrorMessages.PARKIR_HARIAN_MOBIL_MAPPING_ERROR -> DataError.PARKIR_HARIAN_MOBIL_MAPPING_ERROR
                ErrorMessages.PARKIR_HARIAN_MOBIL_NOT_FOUND -> DataError.PARKIR_HARIAN_MOBIL_NOT_FOUND
                else -> DataError.UNKNOWN_ERROR.also {
                    Log.e("safeCall", "Unknown error: $message")
                }
            }
        }
    }
}