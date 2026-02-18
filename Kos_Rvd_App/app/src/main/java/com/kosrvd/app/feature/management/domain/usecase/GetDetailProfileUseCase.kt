package com.kosrvd.app.feature.management.domain.usecase

import com.kosrvd.app.core.domain.repository.AccountRepository
import com.kosrvd.app.core.domain.repository.AuthRepository
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.feature.management.presentation.ui.models.DetailProfileUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetDetailProfileUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val accountRepository: AccountRepository
) {
    operator fun invoke(): Flow<Result<DetailProfileUi, DataError>> = flow {
        val user = authRepository.currentUser
        val email = user?.email
        if (email != null) {
            accountRepository.getAccountByIdWithFlow(user.uid).collect { accountResult ->
                when (accountResult) {
                    is Result.Success -> {
                        val account = accountResult.data
                        val detailProfileUi = DetailProfileUi(
                            idAkun = account.idAkun,
                            idPenyewa = account.dataPenghuni?.finalBill?.idPenyewa,
                            photoProfile = account.photo,
                            name = account.name,
                            email = email,
                            phoneNumber = account.dataPenghuni?.phoneNumber,
                            address = account.dataPenghuni?.address,
                            role = account.role,
                            status = account.status,
                            numberRoom = account.dataPenghuni?.numberRoom,
                            photoKtp = account.dataPenghuni?.photoKtp,
                        )
                        emit(Result.Success(detailProfileUi))
                    }
                    is Result.Error -> {
                        emit(Result.Error(accountResult.error))
                    }
                }
            }
        } else {
            emit(Result.Error(DataError.AUTH_USER_NOT_FOUND))
        }
    }
}