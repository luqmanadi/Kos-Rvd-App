package com.kosrvd.app.feature.parking.parkir_harian_mobil.domain.usecase

import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.domain.repository.StorageRepository
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.core.domain.models.CompressedResult
import com.kosrvd.app.core.domain.usecase.CheckNetworkUseCase
import com.kosrvd.app.feature.parking.parkir_harian_mobil.domain.repository.ParkirHarianMobilRepository
import javax.inject.Inject

class UploadProofOfPaymentParkirHarianMobilUseCase @Inject constructor(
    private val storageRepository: StorageRepository,
    private val parkirHarianMobilRepository: ParkirHarianMobilRepository,
    private val checkNetworkUseCase: CheckNetworkUseCase
) {
    suspend operator fun invoke(
        idParkirHarianMobil: String,
        compressedResult: CompressedResult?
    ): Result<Unit, DataError> {
        if (!checkNetworkUseCase()){
            return Result.Error(DataError.NETWORK_NO_INTERNET)
        }
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