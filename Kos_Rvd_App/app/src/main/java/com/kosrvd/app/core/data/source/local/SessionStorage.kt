package com.kosrvd.app.core.data.source.local

import com.kosrvd.app.core.domain.models.AuthInfo
import com.kosrvd.app.core.domain.utils.DataError
import kotlinx.coroutines.flow.Flow
import com.kosrvd.app.core.domain.utils.Result

interface SessionStorage {
    val authInfoFlow: Flow<AuthInfo>
    suspend fun getAuthInfo(): AuthInfo
    suspend fun setAuthInfo(authInfo: AuthInfo): Result<Unit, DataError>
    suspend fun clearAuthInfo(): Result<Unit, DataError>
}