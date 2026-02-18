package com.kosrvd.app.feature.management.domain.usecase

import com.google.firebase.Timestamp
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.data.source.local.SessionStorage
import com.kosrvd.app.core.domain.repository.AccountRepository
import com.kosrvd.app.core.domain.repository.StorageRepository
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.core.domain.utils.getOrNull
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.feature.management.domain.model.BuatLaporanKeluhan
import com.kosrvd.app.feature.management.domain.model.CompressedResult
import com.kosrvd.app.feature.management.domain.model.Keluhan
import com.kosrvd.app.feature.management.domain.repository.KeluhanRepository
import javax.inject.Inject

class BuatLaporanKeluhanUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val keluhanRepository: KeluhanRepository,
    private val sessionStorage: SessionStorage,
    private val firestoreStorage: StorageRepository
) {
    suspend operator fun invoke(
        title: String,
        description: String,
        compressedResult: CompressedResult? = null
    ): Result<Keluhan, DataError> {
        val authInfo = sessionStorage.getAuthInfo()

        val dataPenghuni = accountRepository.getAccountById(authInfo.idAkun)
            .onError { return Result.Error(it) }
            .getOrNull()?: return Result.Error(DataError.UNKNOWN_ERROR)

        val photoUrlString = if (compressedResult != null) {
            when(val result = firestoreStorage.addImageLaporanKeluhan(compressedResult)){
                is Result.Success -> {
                    result.data.toString()
                }
                is Result.Error -> {
                    return Result.Error(result.error)
                }
            }
        } else {
            null
        }

        val formatLaporanKeluhan = BuatLaporanKeluhan(
            complaintStatus = Constant.MENUNGGU_KONFIRMASI,
            completionDate = null,
            description = description,
            idAkun = authInfo.idAkun,
            numberRoom = dataPenghuni.dataPenghuni?.numberRoom,
            photoComplaint = photoUrlString,
            photoResponse = null,
            processDate = null,
            reportDate = Timestamp.now(),
            reporterName = dataPenghuni.name,
            response = null,
            title = title
        )

        val resultCreateLaporanKeluhan = keluhanRepository.createLaporanKeluhan(formatLaporanKeluhan)

        return resultCreateLaporanKeluhan
    }
}