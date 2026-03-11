package com.kosrvd.app.feature.management.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.data.constant.ErrorMessages
import com.kosrvd.app.core.data.networking.safeCall
import com.kosrvd.app.core.data.networking.toObjectListOrThrow
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.core.domain.utils.map
import com.kosrvd.app.feature.management.data.mappers.toKamar
import com.kosrvd.app.feature.management.data.mappers.toListKamar
import com.kosrvd.app.feature.management.data.repository.dto.KamarDto
import com.kosrvd.app.feature.management.domain.model.AlatElektronik
import com.kosrvd.app.feature.management.domain.model.BuatKamar
import com.kosrvd.app.feature.management.domain.model.Harga
import com.kosrvd.app.feature.management.domain.model.Kamar
import com.kosrvd.app.feature.management.domain.repository.KamarRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class KamarRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
): KamarRepository {
    override suspend fun getAllKamarStatusKosong(): Result<List<Kamar>, DataError> {
        return safeCall {
            db.collection(Constant.KAMAR_COLLECTION)
                .whereEqualTo(Constant.STATUS_FIELD, Constant.KOSONG)
                .get()
                .await()
                .toObjectListOrThrow<KamarDto>(
                    mappingErrorMessage = ErrorMessages.KAMAR_MAPPING_ERROR
                )
        }.map { it.toListKamar() }
    }
    override suspend fun getAllKamar(): Result<List<Kamar>, DataError> {
        return safeCall {
            db.collection(Constant.KAMAR_COLLECTION)
                .orderBy(Constant.STATUS_FIELD, Query.Direction.DESCENDING)
                .orderBy(Constant.NUMBER_ROOM_FIELD, Query.Direction.ASCENDING)
                .get()
                .await()
                .toObjectListOrThrow<KamarDto>(
                    mappingErrorMessage = ErrorMessages.KAMAR_MAPPING_ERROR
                )
        }.map { it.toListKamar() }
    }

    override suspend fun deleteKamar(idKamar: String): Result<Unit, DataError> {
        return safeCall {
            db.collection(Constant.KAMAR_COLLECTION)
                .document(idKamar)
                .delete()
                .await()
        }
    }

    override suspend fun getKamarById(idKamar: String): Result<Kamar, DataError> {
        return safeCall {
            db.collection(Constant.KAMAR_COLLECTION)
                .document(idKamar)
                .get()
                .await()
                .toObject<KamarDto>()?: throw Exception(ErrorMessages.KAMAR_NOT_FOUND)
        }.map { it.toKamar() }
    }

    override suspend fun addKamar(buatKamar: BuatKamar): Result<Unit, DataError> {
        return safeCall {
            db.collection(Constant.KAMAR_COLLECTION)
                .add(buatKamar)
                .await()
        }
    }

    override suspend fun getKamarByNomorKamar(nomorKamar: Int): Result<List<Kamar>, DataError> {
        return safeCall {
            db.collection(Constant.KAMAR_COLLECTION)
                .whereEqualTo(Constant.NUMBER_ROOM_FIELD, nomorKamar)
                .get()
                .await()
                .toObjectListOrThrow<KamarDto>(
                    mappingErrorMessage = ErrorMessages.KAMAR_MAPPING_ERROR
                )
        }.map { it.toListKamar() }
    }

    override suspend fun updateNomorKamar(
        idKamar: String,
        nomorKamarBaru: Int
    ): Result<Unit, DataError> {
        return safeCall {
            db.collection(Constant.KAMAR_COLLECTION)
                .document(idKamar)
                .update(Constant.NUMBER_ROOM_FIELD, nomorKamarBaru)
                .await()
        }
    }

    override suspend fun updateUkuranKamar(
        idKamar: String,
        ukuranKamarBaru: String
    ): Result<Unit, DataError> {
        return safeCall {
            db.collection(Constant.KAMAR_COLLECTION)
                .document(idKamar)
                .update(Constant.SIZE_FIELD, ukuranKamarBaru)
                .await()
        }
    }

    override suspend fun updateTarifKamar(
        idKamar: String,
        tarifKamarBaru: Harga
    ): Result<Unit, DataError> {
        return safeCall {
            db.collection(Constant.KAMAR_COLLECTION)
                .document(idKamar)
                .update(Constant.PRICE_FIELD, tarifKamarBaru)
                .await()
        }
    }

    override suspend fun updateLayananAlatElektronikGratis(
        idKamar: String,
        layananAlatElektronikGratisBaru: List<AlatElektronik>
    ): Result<Unit, DataError> {
        return safeCall {
            db.collection(Constant.KAMAR_COLLECTION)
                .document(idKamar)
                .update(Constant.FREE_SERVICE_FIELD, layananAlatElektronikGratisBaru)
                .await()
        }
    }

    override suspend fun updateFasilitasKamar(
        idKamar: String,
        fasilitasKamarBaru: List<String>
    ): Result<Unit, DataError> {
        return safeCall {
            db.collection(Constant.KAMAR_COLLECTION)
                .document(idKamar)
                .update(Constant.FACILITY_FIELD, fasilitasKamarBaru)
                .await()
        }
    }

}