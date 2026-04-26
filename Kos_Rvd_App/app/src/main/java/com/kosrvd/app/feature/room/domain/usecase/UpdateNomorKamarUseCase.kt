package com.kosrvd.app.feature.room.domain.usecase

import com.kosrvd.app.core.domain.usecase.CheckNetworkUseCase
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.core.domain.utils.getOrElse
import com.kosrvd.app.feature.room.domain.repository.KamarRepository
import javax.inject.Inject

class UpdateNomorKamarUseCase @Inject constructor(
    private val kamarRepository: KamarRepository,
    private val checkNetworkUseCase: CheckNetworkUseCase
) {
    suspend operator fun invoke(idKamar: String, nomorKamarBaru: String): Result<Unit, DataError> {
        if (!checkNetworkUseCase()){
            return Result.Error(DataError.NETWORK_NO_INTERNET)
        }
        val isNomorKamarEmptyAvailable = kamarRepository.getKamarByNomorKamar(nomorKamarBaru)
            .getOrElse { return Result.Error(it) }

        return if (isNomorKamarEmptyAvailable.isEmpty()) {
            kamarRepository.updateNomorKamar(idKamar, nomorKamarBaru)
        } else {
            Result.Error(DataError.NOMOR_KAMAR_TERPAKAI)
        }
    }
}