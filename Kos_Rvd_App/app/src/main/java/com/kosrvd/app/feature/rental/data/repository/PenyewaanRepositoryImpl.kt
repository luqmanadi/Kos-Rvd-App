package com.kosrvd.app.feature.rental.data.repository

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.data.constant.ErrorMessages
import com.kosrvd.app.core.data.networking.getFirstObjectOrThrow
import com.kosrvd.app.core.data.networking.safeCall
import com.kosrvd.app.core.data.networking.toObjectListOrThrow
import com.kosrvd.app.core.domain.models.AlatElektronik
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.core.domain.utils.asCollectionFlow
import com.kosrvd.app.core.domain.utils.map
import com.kosrvd.app.feature.rental.data.mappers.toListPenyewaan
import com.kosrvd.app.feature.rental.data.mappers.toPenyewaan
import com.kosrvd.app.feature.rental.data.repository.dto.PenyewaanDto
import com.kosrvd.app.feature.rental.domain.model.BuatPenyewaan
import com.kosrvd.app.feature.rental.domain.model.InfoPakaiParkirMobilBulanan
import com.kosrvd.app.feature.rental.domain.model.Penyewaan
import com.kosrvd.app.feature.rental.domain.repository.PenyewaanRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PenyewaanRepositoryImpl @Inject constructor(
    val db: FirebaseFirestore
): PenyewaanRepository {
    override suspend fun getAllPenyewaanActive(): Result<List<Penyewaan>, DataError> {
        return safeCall {
            db.collection(Constant.PENYEWAAN_COLLECTION)
                .whereEqualTo(Constant.RENTAL_STATUS_FIELD, Constant.ACTIVE)
                .orderBy(Constant.INFO_KAMAR_NUMBER_ROOM_FIELD, Query.Direction.ASCENDING)
                .get()
                .await()
                .toObjectListOrThrow<PenyewaanDto>(
                    mappingErrorMessage = ErrorMessages.PENYEWAAN_MAPPING_ERROR
                )
        }.map { it.toListPenyewaan() }
    }

    override fun getAllPenyewaanFlow(): Flow<Result<List<Penyewaan>, DataError>> {
        return db.collection(Constant.PENYEWAAN_COLLECTION)
            .orderBy(Constant.RENTAL_STATUS_FIELD, Query.Direction.ASCENDING)
            .orderBy(Constant.INFO_KAMAR_NUMBER_ROOM_FIELD, Query.Direction.ASCENDING)
            .asCollectionFlow<PenyewaanDto, Penyewaan>(
                logTag = "Penyewaan Repo",
                mapper = { it.toPenyewaan() }
            )
    }

    override suspend fun getPenyewaanById(idPenyewaan: String): Result<Penyewaan, DataError> {
        return safeCall {
            db.collection(Constant.PENYEWAAN_COLLECTION)
                .document(idPenyewaan)
                .get()
                .await()
                .toObject<PenyewaanDto>() ?: throw Exception(ErrorMessages.PENYEWAAN_NOT_FOUND)
        }.map { it.toPenyewaan() }
    }

    override suspend fun buatPenyewaan(item: BuatPenyewaan): Result<Penyewaan, DataError> {
        return safeCall {
            val docRef = db.collection(Constant.PENYEWAAN_COLLECTION).document()
            val generateId = docRef.id

            docRef.set(item).await()

            item.toPenyewaan(generateId)
        }
    }

    override suspend fun getJumlahPenghuniByIdKamarAndStatusPenyewaanAktif(idKamar: String): Result<Int, DataError> {
        return safeCall {
            db.collection(Constant.PENYEWAAN_COLLECTION)
                .whereEqualTo(Constant.INFO_KAMAR_GET_ID_KAMAR, idKamar)
                .whereEqualTo(Constant.RENTAL_STATUS_FIELD, Constant.ACTIVE)
                .get()
                .await()
                .getFirstObjectOrThrow<PenyewaanDto>(
                    notFoundMessage = ErrorMessages.PENYEWAAN_NOT_FOUND,
                    mappingErrorMessage = ErrorMessages.PENYEWAAN_MAPPING_ERROR
                )
        }.map { it.listResident.size }
    }

    override suspend fun endPenyewaan(idPenyewaan: String): Result<Unit, DataError> {
        val dataUpdate = hashMapOf(
            Constant.RENTAL_STATUS_FIELD to Constant.NON_ACTIVE,
            Constant.RENTAL_COMPLETION_DATE_FIELD to FieldValue.serverTimestamp()
        )
        return safeCall {
            db.collection(Constant.PENYEWAAN_COLLECTION)
                .document(idPenyewaan)
                .update(dataUpdate)
                .await()
        }
    }

    override suspend fun editPemakaianElektronikBulanan(
        idPenyewaan: String,
        listAlatElektronik: List<AlatElektronik>
    ): Result<Unit, DataError> {
        return safeCall {
            db.collection(Constant.PENYEWAAN_COLLECTION)
                .document(idPenyewaan)
                .update(Constant.PEMAKAIAN_ALAT_ELEKTRONIK_BULANAN_FIELD, listAlatElektronik)
                .await()
        }
    }

    override suspend fun editPemakaianParkirMobilBulanan(
        idPenyewaan: String,
        pakaiParkirMobilBulanan: InfoPakaiParkirMobilBulanan
    ): Result<Unit, DataError> {
        return safeCall {
            db.collection(Constant.PENYEWAAN_COLLECTION)
                .document(idPenyewaan)
                .update(Constant.PEMAKAIAN_PARKIR_MOBIL_BULANAN_FIELD, pakaiParkirMobilBulanan)
                .await()
        }
    }

    override suspend fun endRentalPemakaianParkirMobilBulanan(idPenyewaan: String): Result<Unit, DataError> {
        return safeCall {
            db.collection(Constant.PENYEWAAN_COLLECTION)
                .document(idPenyewaan)
                .update(Constant.PEMAKAIAN_PARKIR_MOBIL_BULANAN_FIELD, null)
                .await()
        }
    }

    override suspend fun deletePenyewaan(idPenyewaan: String): Result<Unit, DataError> {
        return safeCall {
            db.collection(Constant.PENYEWAAN_COLLECTION)
                .document(idPenyewaan)
                .delete()
                .await()
        }
    }
}