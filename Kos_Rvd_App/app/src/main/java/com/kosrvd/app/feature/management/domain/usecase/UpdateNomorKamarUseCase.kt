package com.kosrvd.app.feature.management.domain.usecase

import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.core.domain.utils.getOrElse
import com.kosrvd.app.feature.management.domain.repository.KamarRepository
import javax.inject.Inject

class UpdateNomorKamarUseCase @Inject constructor(
    private val kamarRepository: KamarRepository
) {
    suspend operator fun invoke(idKamar: String, nomorKamarBaru: Int): Result<Unit, DataError> {
        val isNomorKamarEmptyAvailable = kamarRepository.getKamarByNomorKamar(nomorKamarBaru)
            .getOrElse { return Result.Error(it) }

        return if (isNomorKamarEmptyAvailable.isEmpty()) {
            kamarRepository.updateNomorKamar(idKamar, nomorKamarBaru)
        } else {
            Result.Error(DataError.NOMOR_KAMAR_TERPAKAI)
        }
    }
}