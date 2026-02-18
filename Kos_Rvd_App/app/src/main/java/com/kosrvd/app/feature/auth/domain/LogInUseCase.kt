package com.kosrvd.app.feature.auth.domain

import com.kosrvd.app.core.data.source.local.SessionStorage
import com.kosrvd.app.core.domain.models.AuthInfo
import com.kosrvd.app.core.domain.repository.AccountRepository
import com.kosrvd.app.core.domain.repository.AuthRepository
import com.kosrvd.app.core.domain.usecase.AddFcmTokenUseCase
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.core.domain.utils.Role
import com.kosrvd.app.core.domain.utils.getOrNull
import com.kosrvd.app.core.domain.utils.onError
import javax.inject.Inject

class LogInUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val accountRepository: AccountRepository,
    private val sessionStorage: SessionStorage,
    private val addFcmTokenUseCase: AddFcmTokenUseCase
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): Result<Boolean, DataError> {
        // get uid from auth
        val uid = authRepository.loginWithEmailAndPassword(email, password)
            .onError { return Result.Error(it) }
            .getOrNull()?.uid ?: return Result.Error(DataError.UNKNOWN_ERROR)

        // get dataAccount from akun collection
        val dataAccount = accountRepository.getAccountById(uid)
            .onError { return cleanupAndError(it) }
            .getOrNull() ?: return Result.Error(DataError.UNKNOWN_ERROR)

        // create auth info
        val authInfo = AuthInfo(idAkun = uid, role = parseRole(dataAccount.role))

        // save auth info to session storage
        sessionStorage.setAuthInfo(authInfo)
            .onError { return cleanupAndError(it) }

        // add fcm token to fcm token field in account collection
        addFcmTokenUseCase(uid).onError { return cleanupAndError(it) }

        // return success
        return Result.Success(true)
    }

    private suspend fun cleanupAndError(error: DataError): Result.Error<DataError> {
        sessionStorage.clearAuthInfo()
        authRepository.signOut()
        return Result.Error(error)
    }

    private fun parseRole(roleString: String?): Role {
        return when (roleString?.lowercase()) {
            "penghuni" -> Role.PENGHUNI
            "admin" -> Role.ADMIN
            else -> Role.EMPTY
        }
    }
}