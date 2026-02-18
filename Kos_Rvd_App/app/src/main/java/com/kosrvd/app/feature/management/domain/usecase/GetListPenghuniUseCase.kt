package com.kosrvd.app.feature.management.domain.usecase

import com.kosrvd.app.core.data.source.local.SessionStorage
import com.kosrvd.app.core.domain.models.Account
import com.kosrvd.app.core.domain.repository.AccountRepository
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.core.domain.utils.Role
import javax.inject.Inject

class GetListPenghuniUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val sessionStorage: SessionStorage
) {
    suspend operator fun invoke(): Result<List<Account>, DataError>{
        val authSession = sessionStorage.getAuthInfo()
        return when(authSession.role){
            Role.ADMIN -> {
                accountRepository.getAllAccountWithRolePenghuniStatusIsAktifForAdmin()
            }
            Role.PENGHUNI -> {
                accountRepository.getAllAccountWithRolePenghuniStatusIsAktifForPenghuni(authSession.idAkun)
            }
            Role.EMPTY -> {
                Result.Error(DataError.LOCAL_DATA_ROLE_EMPTY)
            }
        }
    }
}