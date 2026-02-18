package com.kosrvd.app.core.data.networking

import android.util.Log
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObject
import com.kosrvd.app.core.data.constant.ErrorMessages

// Generic extension function untuk single object
inline fun <reified T> QuerySnapshot.getFirstObjectOrThrow(
    notFoundMessage: String = ErrorMessages.DATA_NOT_FOUND,
    mappingErrorMessage: String = ErrorMessages.DATA_MAPPING_ERROR
): T {
    return when {
        isEmpty -> throw Exception(notFoundMessage)
        size() > 1 -> {
            Log.w("Firestore", "Multiple documents found, taking first")
            documents.first().toObject<T>() ?: throw Exception(mappingErrorMessage)
        }
        else -> documents.first().toObject<T>() ?: throw Exception(mappingErrorMessage)
    }
}

// Generic extension function untuk list
inline fun <reified T> QuerySnapshot.toObjectListOrThrow(
    mappingErrorMessage: String = ErrorMessages.DATA_MAPPING_ERROR
): List<T> {
    return if (isEmpty) {
        emptyList()
    } else {
        try {
            documents.mapNotNull { it.toObject<T>() }
        } catch (e: Exception) {
            throw Exception(mappingErrorMessage)
        }
    }
}