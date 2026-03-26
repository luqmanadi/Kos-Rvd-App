package com.kosrvd.app.core.domain.usecase

import com.kosrvd.app.core.data.source.local.SessionStorage
import com.kosrvd.app.core.domain.repository.AuthRepository
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.core.domain.utils.onSuccess
import javax.inject.Inject

class CheckSessionUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionStorage: SessionStorage,
    private val removeFcmTokenUseCase: RemoveFcmTokenUseCase
){
    suspend operator fun invoke(): Result<Boolean, DataError> {
        val currentUser = authRepository.currentUser
        if (currentUser == null){
            val authInfoSession = sessionStorage.getAuthInfo().idAkun
            if (authInfoSession.isNotEmpty()){
                removeFcmTokenUseCase.invoke(authInfoSession)
                    .onSuccess {
                        sessionStorage.clearAuthInfo()
                        return Result.Success(false)
                    }
                    .onError {
                        sessionStorage.clearAuthInfo()
                        return Result.Error(it)
                    }
            } else {
                sessionStorage.clearAuthInfo()
                return Result.Success(false)
            }
        }
        return Result.Success(true)

    }
}