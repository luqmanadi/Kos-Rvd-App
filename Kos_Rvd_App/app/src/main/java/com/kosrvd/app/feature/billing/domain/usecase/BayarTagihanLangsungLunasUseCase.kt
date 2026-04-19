package com.kosrvd.app.feature.billing.domain.usecase

import com.google.firebase.firestore.FieldValue
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.domain.repository.StorageRepository
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.presentation.navigation.models.ResultTagihan
import com.kosrvd.app.core.domain.models.CompressedResult
import com.kosrvd.app.feature.billing.domain.repository.TagihanRepository
import javax.inject.Inject

class BayarTagihanLangsungLunasUseCase @Inject constructor(
    private val tagihanRepository: TagihanRepository,
    private val storageRepository: StorageRepository
) {
    suspend operator fun invoke(
        idTagihan: String,
        compressedResult: CompressedResult?,
        periodStart: String,
        periodEnd: String,
        jumlahDibayar: Long,
        nomorKamar: Int,
        buktiPembayaranSebelumnya: String? = null
    ): Result<ResultTagihan, DataError> {
        if (buktiPembayaranSebelumnya != null){
            storageRepository.deleteImageReferenceUrlImage(buktiPembayaranSebelumnya).onError { return Result.Error(it) }
        }
        val photoUrl = if (compressedResult != null) {
            when (val storageResult =
                storageRepository.addImageBuktiPembayaranTagihan(compressedResult)) {
                is Result.Success -> {
                    storageResult.data.toString()
                }

                is Result.Error -> {
                    return Result.Error(storageResult.error)
                }
            }
        } else {
            ""
        }

        val updatePaymentBill = mapOf(
            Constant.PAYMENT_STATUS_FIELD to Constant.LUNAS,
            Constant.PROOF_OF_PAYMENT_FIELD to photoUrl,
            Constant.DATE_UPLOAD_PROOF_FIELD to FieldValue.serverTimestamp(),
            Constant.VERIFICATION_DATE_FIELD to FieldValue.serverTimestamp(),
            Constant.DATE_PAID_OFF_FIELD to FieldValue.serverTimestamp(),
            Constant.REJECTION_STATEMENT_FIELD to null
        )

        tagihanRepository.updatePaymentBill(
            idTagihan = idTagihan,
            updatePaymentBill = updatePaymentBill
        ).onError { return Result.Error(it)  }

        val resultTagihan = ResultTagihan(
            idTagihan = idTagihan,
            periodStart = periodStart,
            periodEnd = periodEnd,
            jumlahDibayar = jumlahDibayar,
            nomorKamar = nomorKamar,
            statusTagihan = Constant.LUNAS
        )

        return Result.Success(resultTagihan)
    }
}