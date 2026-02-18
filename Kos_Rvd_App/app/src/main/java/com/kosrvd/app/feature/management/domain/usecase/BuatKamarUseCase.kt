package com.kosrvd.app.feature.management.domain.usecase

import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.core.domain.utils.getOrElse
import com.kosrvd.app.feature.management.domain.model.BuatKamar
import com.kosrvd.app.feature.management.domain.repository.KamarRepository
import javax.inject.Inject

class BuatKamarUseCase @Inject constructor(
    private val kamarRepository: KamarRepository
) {
    suspend operator fun invoke(buatKamarFormat: BuatKamar): Result<Unit, DataError> {
        val isNomorKamarExist = kamarRepository.getKamarByNomorKamar(buatKamarFormat.numberRoom)
            .getOrElse { return Result.Error(it) }

        return if (isNomorKamarExist.isEmpty()) {
            kamarRepository.addKamar(buatKamarFormat)
        } else {
            Result.Error(DataError.NOMOR_KAMAR_TERPAKAI)
        }
    }
}