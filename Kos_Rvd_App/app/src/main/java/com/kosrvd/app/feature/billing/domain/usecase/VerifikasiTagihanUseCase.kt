package com.kosrvd.app.feature.billing.domain.usecase

import com.google.firebase.firestore.FieldValue
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.presentation.navigation.models.ResultTagihan
import com.kosrvd.app.feature.billing.domain.repository.TagihanRepository
import javax.inject.Inject

class VerifikasiTagihanUseCase @Inject constructor(
    private val tagihanRepository: TagihanRepository
) {
    suspend operator fun invoke(
        idTagihan: String,
        periodStart: String,
        periodEnd: String,
        jumlahDibayar: Long,
        nomorKamar: String
    ): Result<ResultTagihan, DataError> {
        val updatePaymentBill = mapOf(
            Constant.PAYMENT_STATUS_FIELD to Constant.LUNAS,
            Constant.VERIFICATION_DATE_FIELD to FieldValue.serverTimestamp(),
            Constant.DATE_PAID_OFF_FIELD to FieldValue.serverTimestamp()
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