package com.kosrvd.app.feature.auth.domain

import com.kosrvd.app.core.data.source.local.SessionStorage
import com.kosrvd.app.core.domain.repository.AuthRepository
import com.kosrvd.app.core.domain.usecase.CheckNetworkUseCase
import com.kosrvd.app.core.domain.usecase.RemoveFcmTokenUseCase
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.core.domain.utils.onError
import javax.inject.Inject

class LogOutUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionStorage: SessionStorage,
    private val removeFcmTokenUseCase: RemoveFcmTokenUseCase,
    private val checkNetworkUseCase: CheckNetworkUseCase
) {
    suspend operator fun invoke(): Result<Boolean, DataError> {
        if (!checkNetworkUseCase()){
            return Result.Error(DataError.NETWORK_NO_INTERNET)
        }

        val uid = authRepository.currentUser?.uid

        removeFcmTokenUseCase(uid!!)
            .onError { return Result.Error(it) }

        sessionStorage.clearAuthInfo()
            .onError {
                return Result.Error(it)
            }

        authRepository.signOut()
            .onError { return Result.Error(it) }

        return Result.Success(true)

    }
}