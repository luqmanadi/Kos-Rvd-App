package com.kosrvd.app.core.data.networking

import android.util.Log
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

suspend inline fun <reified T> responseToResult(
    response: HttpResponse
): Result<T, DataError> {
    return when(response.status.value) {
        in 200..299 -> {
            try {
                Result.Success(response.body<T>())
            } catch (e: Exception) {
                Log.e("responseToResult", "responseToResult: $e")
                Result.Error(DataError.NETWORK_SERIALIZATION)
            }
        }
        400 -> {
            Log.e("responseToResult", "responseToResult: ${response.status.value}")
            Result.Error(DataError.FIRESTORE_INVALID_ARGUMENT)
        }
        401 -> {
            Log.e("responseToResult", "responseToResult: ${response.status.value}")
            Result.Error(DataError.NETWORK_UNAUTHORIZED)
        }
        403 -> {
            Log.e("responseToResult", "responseToResult: ${response.status.value}")
            Result.Error(DataError.FIRESTORE_PERMISSION_DENIED)
        }
        404 -> {
            Log.e("responseToResult", "responseToResult: ${response.status.value}")
            Result.Error(DataError.FIRESTORE_NOT_FOUND)
        }
        408 -> {
            Log.e("responseToResult", "responseToResult: ${response.status.value}")
            Result.Error(DataError.NETWORK_TIMEOUT)
        }
        409 -> {
            Log.e("responseToResult", "responseToResult: ${response.status.value}")
            Result.Error(DataError.FIRESTORE_ALREADY_EXISTS)
        }
        429 -> {
            Log.e("responseToResult", "responseToResult: ${response.status.value}")
            Result.Error(DataError.AUTH_TOO_MANY_REQUESTS)
        }
        in 500..599 -> {
            Log.e("responseToResult", "responseToResult: ${response.status.value}")
            Result.Error(DataError.NETWORK_SERVER_ERROR)
        }
        else -> {
            Log.e("responseToResult", "responseToResult: ${response.status.value}")
            Result.Error(DataError.NETWORK_UNKNOWN_ERROR)
        }
    }
}