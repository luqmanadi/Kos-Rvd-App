package com.kosrvd.app.core.domain.utils

import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import com.kosrvd.app.core.data.networking.toDataError
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.json.Json
import org.json.JSONObject


/**
 * Extension untuk mengambil single document sebagai Flow
 */
inline fun <reified DTO: Any, DOMAIN> DocumentReference.asFlow(
    crossinline mapper: (DTO) -> DOMAIN,
    crossinline onNotFound: () -> DataError,
    crossinline onMappingError: (Exception) -> DataError,
): Flow<Result<DOMAIN, DataError>> = callbackFlow {
    val listenerRegistration = addSnapshotListener { snapshot, error ->
        if (error != null) {
            trySend(Result.Error(error.toDataError()))
            return@addSnapshotListener
        }

        if (snapshot != null && snapshot.exists()){
            try {
                // Mengambil data sebagai DTO
                val dataDto = snapshot.toObject<DTO>()
                if (dataDto != null) {
                    // Map DTO ke Domain model
                    val domainData = mapper(dataDto)
                    trySend(Result.Success(domainData))
                } else {
                    trySend(Result.Error(onNotFound()))
                }
            } catch (e: Exception) {
                trySend(Result.Error(onMappingError(e)))
            }
        } else {
            trySend(Result.Error(onNotFound()))
        }
    }

    awaitClose {
        listenerRegistration.remove()
    }
}

/**
 * Extension untuk mengambil collection/query sebagai Flow untuk Output List
 */
inline fun <reified DTO : Any, DOMAIN> Query.asCollectionFlow(
    logTag: String = "FirestoreHelper",
    crossinline mapper: (DTO) -> DOMAIN
): Flow<Result<List<DOMAIN>, DataError>> = callbackFlow {

    val listenerRegistration = addSnapshotListener { snapshot, error ->
        if (error != null) {
            trySend(Result.Error(error.toDataError()))
            return@addSnapshotListener
        }

        if (snapshot != null) {
            val items = snapshot.documents.mapNotNull { document ->
                try {
                    // 1. Convert Document ke DTO
                    val dto = document.toObject<DTO>()
                    // 2. Convert DTO ke Domain Model
                    dto?.let { mapper(it) }
                } catch (e: Exception) {
                    Log.e(logTag, "Error mapping document ${document.id} to ${DTO::class.java.simpleName}", e)
                    null // Skip data yang korup/salah format
                }
            }

            Log.d(logTag, "Successfully fetched ${items.size} items")
            trySend(Result.Success(items))
        } else {
            trySend(Result.Success(emptyList()))
        }
    }

    awaitClose {
        listenerRegistration.remove()
    }
}



inline fun <reified T> Any?.toDto(): T {
    // 1. Ubah Any? (Map) menjadi JSON String menggunakan JSONObject (bawaan Android)
    val jsonString = JSONObject(this as Map<*, *>).toString()

    val json = Json {
        ignoreUnknownKeys = true // Agar tidak error jika ada field tambahan di Cloud Function
    }

    // 2. Decode menggunakan kotlinx-serialization (seperti cara Ktor)
    return json.decodeFromString(jsonString)
}


inline fun <reified DTO: Any, DOMAIN> Query.asFlowFirstItem(
    crossinline mapper: (DTO) -> DOMAIN,
    crossinline onNotFound: () -> DataError,
    crossinline onMappingError: (Exception) -> DataError,
): Flow<Result<DOMAIN, DataError>> = callbackFlow {
    val listenerRegistration = addSnapshotListener { snapshot, error ->
        if (error != null) {
            trySend(Result.Error(error.toDataError()))
            return@addSnapshotListener
        }

        if (snapshot != null && !snapshot.isEmpty){
            try {
                // Ambil dokumen pertama dari list (index 0)
                val document = snapshot.documents[0]

                // Convert document itu ke DTO
                val dataDto = document.toObject<DTO>()

                if (dataDto != null) {
                    // Map DTO ke Domain model
                    val domainData = mapper(dataDto)
                    trySend(Result.Success(domainData))
                } else {
                    trySend(Result.Error(onNotFound()))
                }
            } catch (e: Exception) {
                trySend(Result.Error(onMappingError(e)))
            }
        } else {
            trySend(Result.Error(onNotFound()))
        }
    }

    awaitClose {
        listenerRegistration.remove()
    }
}