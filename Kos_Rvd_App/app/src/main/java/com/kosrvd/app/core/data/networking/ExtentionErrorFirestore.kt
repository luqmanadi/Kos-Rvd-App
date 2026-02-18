package com.kosrvd.app.core.data.networking

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.FirebaseFirestoreException.Code
import com.kosrvd.app.core.domain.utils.DataError

fun FirebaseFirestoreException.toDataError(): DataError {
    return when(this.code){
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
            Log.e("FirestoreException", "Unknown error: $message")
        }
    }
}