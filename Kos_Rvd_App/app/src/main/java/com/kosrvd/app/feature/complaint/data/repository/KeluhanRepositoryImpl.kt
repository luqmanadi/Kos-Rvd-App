package com.kosrvd.app.feature.complaint.data.repository

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
import com.kosrvd.app.feature.complaint.data.mappers.toKeluhan
import com.kosrvd.app.feature.complaint.data.repository.dto.KeluhanDto
import com.kosrvd.app.feature.complaint.domain.model.BuatLaporanKeluhan
import com.kosrvd.app.feature.complaint.domain.model.Keluhan
import com.kosrvd.app.feature.complaint.domain.repository.KeluhanRepository
import com.kosrvd.app.feature.complaint.domain.utils.TypeKeluhan
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class KeluhanRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
): KeluhanRepository {

    override fun getAllKeluhanForAdmin(typeKeluhan: TypeKeluhan): Flow<Result<List<Keluhan>, DataError>> {
        val typeKeluhanString = parseTypeKeluhan(typeKeluhan)
        val orderBy = orderByTypeKeluhan(typeKeluhan)

        return db.collection(Constant.KELUHAN_COLLECTION)
            .whereEqualTo(Constant.COMPLAINT_STATUS_FIELD, typeKeluhanString)
            .orderBy(orderBy, Query.Direction.DESCENDING)
            .asCollectionFlow<KeluhanDto, Keluhan>(
                logTag = "Keluhan Repo",
                mapper = { it.toKeluhan() }
            )
    }

    override fun getAllKeluhanForPenghuni(typeKeluhan: TypeKeluhan, idAkun: String): Flow<Result<List<Keluhan>, DataError>> {
        val typeKeluhanString = parseTypeKeluhan(typeKeluhan)
        val orderBy = orderByTypeKeluhan(typeKeluhan)

        return db.collection(Constant.KELUHAN_COLLECTION)
            .whereEqualTo(Constant.ID_ACCOUNT_FIELD, idAkun)
            .whereEqualTo(Constant.COMPLAINT_STATUS_FIELD, typeKeluhanString)
            .orderBy(orderBy, Query.Direction.DESCENDING)
            .asCollectionFlow<KeluhanDto, Keluhan>(
                logTag = "Keluhan Repo",
                mapper = { it.toKeluhan() }
            )
    }

    override suspend fun createLaporanKeluhan(laporanKeluhan: BuatLaporanKeluhan): Result<Keluhan, DataError> {
        return safeCall {
            val docRef = db.collection(Constant.KELUHAN_COLLECTION).document()
            val generateId = docRef.id

            // Add data to Firestore with new generateId
            docRef.set(laporanKeluhan).await()

            // return Laporan Keluhan in data type Keluhan with new generatedId
            laporanKeluhan.toKeluhan(generateId)
        }
    }

    override suspend fun delleteKeluhan(idKeluhan: String): Result<Unit, DataError> {
        return safeCall {
            db.collection(Constant.KELUHAN_COLLECTION)
                .document(idKeluhan)
                .delete()
                .await()
        }
    }

    override suspend fun getDetailKeluhanById(idKeluhan: String): Result<Keluhan, DataError> {
        return safeCall {
            db.collection(Constant.KELUHAN_COLLECTION)
                .document(idKeluhan)
                .get()
                .await()
                .toObject<KeluhanDto>() ?: throw Exception(ErrorMessages.KELUHAN_NOT_FOUND)
        }.map { it.toKeluhan() }
    }

    override suspend fun updateProgresLaporanKeluhan(
        idKeluhan: String,
        updateProgresLaporanKeluhan: Map<String, Any?>
    ): Result<Unit, DataError> {
        return safeCall {
            db.collection(Constant.KELUHAN_COLLECTION)
                .document(idKeluhan)
                .update(updateProgresLaporanKeluhan)
                .await()
        }
    }

    private fun orderByTypeKeluhan(typeKeluhan: TypeKeluhan): String {
        return when(typeKeluhan){
            TypeKeluhan.WAITING_CONFIRMATION -> Constant.REPORT_DATE_FIELD
            TypeKeluhan.PROCESS -> Constant.PROCESS_DATE_FIELD
            TypeKeluhan.COMPLETION -> Constant.COMPLETION_DATE_FIELD
        }
    }

    private fun parseTypeKeluhan(typeKeluhan: TypeKeluhan): String {
        return when(typeKeluhan){
            TypeKeluhan.WAITING_CONFIRMATION -> Constant.MENUNGGU_KONFIRMASI
            TypeKeluhan.PROCESS -> Constant.SEDANG_DIPROSES
            TypeKeluhan.COMPLETION -> Constant.SELESAI
        }
    }

}