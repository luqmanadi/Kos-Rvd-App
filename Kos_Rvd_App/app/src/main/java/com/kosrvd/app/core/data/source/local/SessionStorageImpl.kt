package com.kosrvd.app.core.data.source.local

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import com.kosrvd.app.core.data.constant.LocalErrorMessages
import com.kosrvd.app.core.data.mappers.toAuthInfo
import com.kosrvd.app.core.data.mappers.toAuthInfoDto
import com.kosrvd.app.core.data.networking.safeCall
import com.kosrvd.app.core.data.repository.dto.AuthInfoDto
import com.kosrvd.app.core.domain.models.AuthInfo
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SessionStorageImpl @Inject constructor(
    private val authDataStore: DataStore<AuthInfoDto>
): SessionStorage {

    override val authInfoFlow : Flow<AuthInfo> = authDataStore.data
        .catch { exception ->
            if (exception is IOException){
                emit(AuthInfoDto.Empty )
            }else{
                throw exception
            }
        }.map { it.toAuthInfo() }

    override suspend fun getAuthInfo(): AuthInfo {
        return authInfoFlow.firstOrNull()?: AuthInfo.empty
    }

    override suspend fun setAuthInfo(authInfo: AuthInfo): Result<Unit, DataError> {
        return safeCall {
            try {
                authDataStore.updateData { authInfo.toAuthInfoDto() }
                Log.d("SessionStorageImpl", "Berhasil menyimpan data auth: $authInfo")
            } catch (e: IOException) {
                throw Exception(LocalErrorMessages.SAVE_AUTH_ERROR)
            }
        }
    }

    override suspend fun clearAuthInfo(): Result<Unit, DataError> {
        return safeCall {
            try {
                authDataStore.updateData { AuthInfo.empty.toAuthInfoDto() }
                Log.d("SessionStorageImpl", "Berhasil menghapus data auth")
            } catch (e: IOException) {
                throw Exception(LocalErrorMessages.CLEAR_AUTH_ERROR)
            }
        }
    }
}