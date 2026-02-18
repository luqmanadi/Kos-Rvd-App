package com.kosrvd.app.feature.management.domain.usecase

import com.kosrvd.app.core.data.source.local.SessionStorage
import com.kosrvd.app.core.domain.models.Account
import com.kosrvd.app.core.domain.repository.AccountRepository
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.core.domain.utils.Role
import com.kosrvd.app.feature.management.domain.model.DataDashboardAdmin
import com.kosrvd.app.feature.management.domain.repository.RingkasanDashboardAdminRepository
import com.kosrvd.app.feature.management.presentation.ui.models.CombineDataDashboardUi
import com.kosrvd.app.feature.management.presentation.ui.models.toAdminDashboardUi
import com.kosrvd.app.feature.management.presentation.ui.models.toPenghuniDashboardUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetDataDashboardUseCase @Inject constructor(
    private val sessionStorage: SessionStorage,
    private val accountRepository: AccountRepository,
    private val ringkasanDashboardAdminRepository: RingkasanDashboardAdminRepository
) {
    operator fun invoke(): Flow<Result<CombineDataDashboardUi, DataError>> = flow {
        val getDataSession = sessionStorage.getAuthInfo()

        when (getDataSession.role) {
            Role.ADMIN -> {
                emitAll(
                    ringkasanDashboardAdminRepository.getRingkasanDashboardAdmin()
                        .map { result ->
                            mapToCombineUi(result, isAdmin = true)
                        }
                )
            }
            Role.PENGHUNI -> {
                emitAll(
                    accountRepository.getAccountByIdWithFlow(getDataSession.idAkun)
                        .map { result ->
                            mapToCombineUi(result, isAdmin = false)
                        }
                )
            }
            Role.EMPTY -> {
                emit(Result.Error(DataError.LOCAL_DATA_ROLE_EMPTY))
            }
        }
    }

    // Helper function untuk mapping agar kode lebih bersih
    // T (Generic) bisa berupa AdminData atau AccountData tergantung inputnya
    private fun <T> mapToCombineUi(
        result: Result<T, DataError>,
        isAdmin: Boolean
    ): Result<CombineDataDashboardUi, DataError> {
        return when (result) {
            is Result.Success -> {
                val data = if (isAdmin) {
                    CombineDataDashboardUi(
                        adminDashboardUi = (result.data as? DataDashboardAdmin)?.toAdminDashboardUi(),
                        penghuniDashboardUi = null
                    )
                } else {
                    CombineDataDashboardUi(
                        adminDashboardUi = null,
                        penghuniDashboardUi = (result.data as? Account)?.toPenghuniDashboardUi()
                    )
                }
                Result.Success(data)
            }
            is Result.Error -> {
                Result.Error(result.error)
            }
        }
    }
}