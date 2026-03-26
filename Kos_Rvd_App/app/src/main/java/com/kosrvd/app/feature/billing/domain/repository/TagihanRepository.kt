package com.kosrvd.app.feature.billing.domain.repository

import com.google.firebase.Timestamp
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.feature.billing.domain.utils.TypeTagihan
import com.kosrvd.app.feature.billing.domain.model.BuatTagihan
import com.kosrvd.app.feature.billing.domain.model.Tagihan
import kotlinx.coroutines.flow.Flow

interface TagihanRepository {
    fun getAllTagihanForPenghuni(typeTagihan: TypeTagihan, idAkun: String): Flow<Result<List<Tagihan>, DataError>>

    fun getAllTagihanForAdmin(typeTagihan: TypeTagihan): Flow<Result<List<Tagihan>, DataError>>

    suspend fun getDetailTagihan(idTagihan: String): Result<Tagihan, DataError>

    suspend fun updatePaymentBill(idTagihan: String, updatePaymentBill: Map<String, Any?>): Result<Unit, DataError>

    suspend fun deteleTagihan(idTagihan: String): Result<Unit, DataError>

    suspend fun buatTagihan(item: BuatTagihan): Result<Tagihan, DataError>

    suspend fun checkTagihanByIdPenyewa(idPenyewa: String, periodEnd: Timestamp): Result<Boolean, DataError>

}