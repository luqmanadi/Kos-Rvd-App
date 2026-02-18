package com.kosrvd.app.feature.management.domain.usecase

import com.kosrvd.app.core.data.source.local.SessionStorage
import com.kosrvd.app.core.domain.repository.AccountRepository
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.feature.management.presentation.ui.models.ProfileUi
import com.kosrvd.app.feature.management.presentation.ui.models.toProfileUi
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val sessionStorage: SessionStorage,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(): Result<ProfileUi, DataError> {
        val uid = sessionStorage.getAuthInfo().idAkun
        return when(val dataPenghuni = accountRepository.getAccountById(uid)){
            is Result.Error -> {
                Result.Error(dataPenghuni.error)
            }
            is Result.Success ->{
                Result.Success(dataPenghuni.data.toProfileUi())
            }
        }
    }
}