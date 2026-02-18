package com.kosrvd.app.feature.management.domain.usecase

import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.domain.repository.StorageRepository
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.feature.management.domain.model.CompressedResult
import com.kosrvd.app.feature.management.domain.repository.ParkirHarianMobilRepository
import javax.inject.Inject

class UploadProofOfPaymentParkirHarianMobilUseCase @Inject constructor(
    private val storageRepository: StorageRepository,
    private val parkirHarianMobilRepository: ParkirHarianMobilRepository
) {
    suspend operator fun invoke(
        idParkirHarianMobil: String,
        compressedResult: CompressedResult?
    ): Result<Unit, DataError> {
        val photoUrl = if (compressedResult != null) {
            when (val storageResult =
                storageRepository.addImageBuktiPembayaranParkirHarianMobil(
                    idParkirHarianMobil = idParkirHarianMobil,
                    compressedResult =compressedResult)
            ) {
                is Result.Success -> {
                    storageResult.data.toString()
                }

                is Result.Error -> {
                    return Result.Error(storageResult.error)
                }
            }
        } else {
            return Result.Error(DataError.PROOF_OF_PAYMENT_NOT_FOUND)
        }

        val updateParkirHarianMobil = mapOf(
            Constant.PROOF_OF_PAYMENT_FIELD to photoUrl,
            Constant.PAYMENT_STATUS_FIELD to Constant.LUNAS
        )

        return parkirHarianMobilRepository.updateDataParkirHarianMobil(
            idParkirHarianMobil = idParkirHarianMobil,
            updateDataParkirHarianMobil = updateParkirHarianMobil
        )
    }
}