package com.kosrvd.app.feature.room.domain.usecase

import com.kosrvd.app.core.domain.usecase.CheckNetworkUseCase
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.core.domain.utils.getOrElse
import com.kosrvd.app.feature.room.domain.model.BuatKamar
import com.kosrvd.app.feature.room.domain.repository.KamarRepository
import javax.inject.Inject

class BuatKamarUseCase @Inject constructor(
    private val kamarRepository: KamarRepository,
    private val checkNetworkUseCase: CheckNetworkUseCase
) {
    suspend operator fun invoke(buatKamarFormat: BuatKamar): Result<Unit, DataError> {
        if (!checkNetworkUseCase()){
            return Result.Error(DataError.NETWORK_NO_INTERNET)
        }
        val isNomorKamarExist = kamarRepository.getKamarByNomorKamar(buatKamarFormat.numberRoom)
            .getOrElse { return Result.Error(it) }

        return if (isNomorKamarExist.isEmpty()) {
            kamarRepository.addKamar(buatKamarFormat)
        } else {
            Result.Error(DataError.NOMOR_KAMAR_TERPAKAI)
        }
    }
}