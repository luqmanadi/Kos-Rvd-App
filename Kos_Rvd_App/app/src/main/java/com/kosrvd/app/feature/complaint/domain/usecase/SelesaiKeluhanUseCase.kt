package com.kosrvd.app.feature.complaint.domain.usecase

import com.google.firebase.firestore.FieldValue
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.domain.repository.StorageRepository
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.presentation.navigation.models.ResultLaporanKeluhan
import com.kosrvd.app.feature.complaint.domain.repository.KeluhanRepository
import com.kosrvd.app.core.domain.models.CompressedResult
import javax.inject.Inject

class SelesaiKeluhanUseCase @Inject constructor(
    private val keluhanRepository: KeluhanRepository,
    private val firestoreStorage: StorageRepository
) {
    suspend operator fun invoke(
        idKeluhan: String,
        response: String,
        compressedResult: CompressedResult?,
        nomorKamar : Int,
        namaPelapor : String,
        judulLaporan : String,
    ): Result<ResultLaporanKeluhan, DataError> {
        val photoUrlString = if (compressedResult != null) {
            when(val result = firestoreStorage.addImageResponseLaporanKeluhan(compressedResult)){
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

        val updateProgresLaporanKeluhan = mapOf(
            Constant.COMPLETION_DATE_FIELD to FieldValue.serverTimestamp(),
            Constant.COMPLAINT_STATUS_FIELD to Constant.SELESAI,
            Constant.RESPONSE_FIELD to response,
            Constant.PHOTO_RESPONSE_FIELD to photoUrlString
        )
        keluhanRepository.updateProgresLaporanKeluhan(idKeluhan, updateProgresLaporanKeluhan)
            .onError { return Result.Error(it) }

        val resultLaporanKeluhan = ResultLaporanKeluhan(
            idLaporan = idKeluhan,
            statusLaporan = Constant.SELESAI,
            nomorKamar = nomorKamar,
            namaPelapor = namaPelapor,
            judulLaporan = judulLaporan,
        )

        return Result.Success(resultLaporanKeluhan)
    }
}