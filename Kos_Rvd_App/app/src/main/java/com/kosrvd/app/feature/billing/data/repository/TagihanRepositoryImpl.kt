package com.kosrvd.app.feature.billing.data.repository

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.data.constant.ErrorMessages
import com.kosrvd.app.core.data.networking.safeCall
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.core.domain.utils.asCollectionFlow
import com.kosrvd.app.core.domain.utils.map
import com.kosrvd.app.feature.billing.data.mappers.toTagihan
import com.kosrvd.app.feature.billing.domain.model.BuatTagihan
import com.kosrvd.app.feature.billing.domain.model.Tagihan
import com.kosrvd.app.feature.billing.domain.repository.TagihanRepository
import com.kosrvd.app.feature.billing.domain.utils.TypeTagihan
import com.kosrvd.app.feature.billing.data.repository.dto.TagihanDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TagihanRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
): TagihanRepository {
    override fun getAllTagihanForAdmin(typeTagihan: TypeTagihan): Flow<Result<List<Tagihan>, DataError>> {
        val paymentStatusFilter = parseTypeTagihan(typeTagihan)
        val orderBy = orderByTypeTagihan(typeTagihan)
        return db.collection(Constant.TAGIHAN_COLLECTION)
            .whereEqualTo(Constant.PAYMENT_STATUS_FIELD, paymentStatusFilter)
            .orderBy(orderBy, Query.Direction.DESCENDING)
            .asCollectionFlow<TagihanDto, Tagihan>(
                logTag = "Tagihan Repo",
                mapper = { it.toTagihan() }
            )
    }

    override fun getAllTagihanForPenghuni(typeTagihan: TypeTagihan, idAkun: String): Flow<Result<List<Tagihan>, DataError>> {
        val paymentStatusFilter = parseTypeTagihan(typeTagihan)
        val orderBy = orderByTypeTagihan(typeTagihan)
        return db.collection(Constant.TAGIHAN_COLLECTION)
            .whereArrayContains(Constant.RESIDENT_ACCOUNT_ID_LIST_FIELD, idAkun)
            .whereEqualTo(Constant.PAYMENT_STATUS_FIELD, paymentStatusFilter)
            .orderBy(orderBy, Query.Direction.DESCENDING)
            .asCollectionFlow<TagihanDto, Tagihan>(
                logTag = "Tagihan Repo",
                mapper = { it.toTagihan() }
            )
    }

    override suspend fun getDetailTagihan(idTagihan: String): Result<Tagihan, DataError> {
        return safeCall {
            db.collection(Constant.TAGIHAN_COLLECTION)
                .document(idTagihan)
                .get()
                .await()
                .toObject<TagihanDto>() ?: throw Exception(ErrorMessages.TAGIHAN_NOT_FOUND)
        }.map { it.toTagihan() }
    }

    override suspend fun updatePaymentBill(
        idTagihan: String,
        updatePaymentBill: Map<String, Any?>
    ): Result<Unit, DataError> {
        return safeCall {
            db.collection(Constant.TAGIHAN_COLLECTION)
                .document(idTagihan)
                .update(updatePaymentBill)
                .await()
        }
    }

    override suspend fun deteleTagihan(idTagihan: String): Result<Unit, DataError> {
        return safeCall {
            db.collection(Constant.TAGIHAN_COLLECTION)
                .document(idTagihan)
                .delete()
                .await()
        }
    }

    override suspend fun buatTagihan(item: BuatTagihan): Result<Tagihan, DataError> {
        return safeCall {
            val docRef = db.collection(Constant.TAGIHAN_COLLECTION).document()
            val generateId = docRef.id

            docRef.set(item).await()

            item.toTagihan(generateId)
        }
    }

    override suspend fun checkTagihanByIdPenyewa(idPenyewa: String, periodEnd: Timestamp): Result<Boolean, DataError> {
        return safeCall {
            val snapshoot = db.collection(Constant.TAGIHAN_COLLECTION)
                .whereEqualTo(Constant.ID_PENYEWA_FIELD, idPenyewa)
                .whereEqualTo(Constant.PERIOD_END_FIELD, periodEnd)
                .limit(1)
                .get()
                .await()
            !snapshoot.isEmpty
        }
    }

    private fun orderByTypeTagihan(typeTagihan: TypeTagihan): String {
        return when(typeTagihan){
            TypeTagihan.UNPAID -> Constant.DATE_CREATED_FIELD
            TypeTagihan.PAID_OFF -> Constant.LUNAS_DATE_FIELD
            TypeTagihan.WAITING_VERIFICATION -> Constant.WAIT_VERIFICATION_DATE_FIELD
        }
    }

    private fun parseTypeTagihan(typeTagihan: TypeTagihan): String {
        return when(typeTagihan){
            TypeTagihan.UNPAID -> Constant.BELUM_LUNAS
            TypeTagihan.PAID_OFF -> Constant.LUNAS
            TypeTagihan.WAITING_VERIFICATION -> Constant.MENUNGGU_VERIFIKASI
        }
    }
}