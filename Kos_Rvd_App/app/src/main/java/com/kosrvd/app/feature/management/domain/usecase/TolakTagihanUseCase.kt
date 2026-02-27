package com.kosrvd.app.feature.management.domain.usecase

import com.google.firebase.firestore.FieldValue
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.core.navigation.models.ResultTagihan
import com.kosrvd.app.feature.management.domain.repository.TagihanRepository
import javax.inject.Inject

class TolakTagihanUseCase @Inject constructor(
    private val tagihanRepository: TagihanRepository
) {
    suspend operator fun invoke(
        idTagihan: String,
        periodStart: String,
        periodEnd: String,
        jumlahDibayar: Long,
        nomorKamar: Int,
        alasanPenolakan: String
    ): Result<ResultTagihan, DataError>{
        val updatePaymentBill = mapOf(
            Constant.PAYMENT_STATUS_FIELD to Constant.BELUM_LUNAS,
            Constant.VERIFICATION_DATE_FIELD to FieldValue.serverTimestamp(),
            Constant.REJECTION_STATEMENT_FIELD to alasanPenolakan,
        )
        tagihanRepository.updatePaymentBill(
            idTagihan = idTagihan,
            updatePaymentBill = updatePaymentBill
        ).onError { return Result.Error(it)  }

        val resultTagihan = ResultTagihan(
            idTagihan = idTagihan,
            statusTagihan = Constant.BELUM_LUNAS,
            periodStart = periodStart,
            periodEnd = periodEnd,
            jumlahDibayar = jumlahDibayar,
            nomorKamar = nomorKamar,
            alasanPenolakan = alasanPenolakan
        )

        return Result.Success(resultTagihan)
    }
}