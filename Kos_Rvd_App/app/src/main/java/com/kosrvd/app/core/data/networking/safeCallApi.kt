package com.kosrvd.app.core.data.networking

import android.util.Log
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException

suspend inline fun <reified T> safeCallApi(
    execute: () -> HttpResponse
): Result<T, DataError> {
    val response = try {
        execute()
    } catch (e: UnresolvedAddressException){
        Log.e("safeCallApi", "safeCallApi: $e")
        return Result.Error(DataError.NETWORK_UNKNOWN_ERROR)
    } catch (e: SerializationException){
        Log.e("safeCallApi", "safeCallApi: $e")
        return Result.Error(DataError.NETWORK_SERIALIZATION)
    } catch (e: Exception){
        currentCoroutineContext().ensureActive()
        Log.e("safeCallApi", "safeCallApi: $e")
        return Result.Error(DataError.NETWORK_NO_INTERNET)
    }

    return responseToResult(response)
}