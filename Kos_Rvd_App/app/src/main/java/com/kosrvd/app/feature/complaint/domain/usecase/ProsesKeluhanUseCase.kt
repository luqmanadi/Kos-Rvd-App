package com.kosrvd.app.feature.complaint.domain.usecase

import com.google.firebase.firestore.FieldValue
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.presentation.navigation.models.ResultLaporanKeluhan
import com.kosrvd.app.feature.complaint.domain.repository.KeluhanRepository
import javax.inject.Inject

class ProsesKeluhanUseCase @Inject constructor(
    private val keluhanRepository: KeluhanRepository
) {
    suspend operator fun invoke(
        idKeluhan : String,
        nomorKamar : String,
        namaPelapor : String,
        judulLaporan : String,
    ): Result<ResultLaporanKeluhan, DataError> {
        val updateProgresLaporanKeluhan = mapOf(
            Constant.PROCESS_DATE_FIELD to FieldValue.serverTimestamp(),
            Constant.COMPLAINT_STATUS_FIELD to Constant.SEDANG_DIPROSES
        )
        keluhanRepository.updateProgresLaporanKeluhan(idKeluhan, updateProgresLaporanKeluhan)
            .onError { return Result.Error(it) }

        val resultLaporanKeluhan = ResultLaporanKeluhan(
            idLaporan = idKeluhan,
            statusLaporan = Constant.SEDANG_DIPROSES,
            nomorKamar = nomorKamar,
            namaPelapor = namaPelapor,
            judulLaporan = judulLaporan,
        )

        return Result.Success(resultLaporanKeluhan)
    }
}