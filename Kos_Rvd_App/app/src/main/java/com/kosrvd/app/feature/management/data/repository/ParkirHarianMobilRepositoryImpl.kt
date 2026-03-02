package com.kosrvd.app.feature.management.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.data.constant.ErrorMessages
import com.kosrvd.app.core.data.networking.safeCall
import com.kosrvd.app.core.data.networking.toObjectListOrThrow
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.core.domain.utils.map
import com.kosrvd.app.feature.management.data.mappers.toListOfParkirHarianMobil
import com.kosrvd.app.feature.management.data.mappers.toParkirHarianMobil
import com.kosrvd.app.feature.management.data.repository.dto.ParkirHarianMobilDto
import com.kosrvd.app.feature.management.domain.model.ParkirHarianMobil
import com.kosrvd.app.feature.management.domain.model.TambahParkirHarianMobil
import com.kosrvd.app.feature.management.domain.repository.ParkirHarianMobilRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ParkirHarianMobilRepositoryImpl @Inject constructor (
    private val db: FirebaseFirestore
): ParkirHarianMobilRepository {
    override suspend fun getAllParkirHarianMobilIsCancelledFalse(): Result<List<ParkirHarianMobil>, DataError> {
        return safeCall {
            db.collection(Constant.PARKIR_HARIAN_MOBIL_COLLECTION)
                .whereEqualTo(Constant.IS_CANCELLED_FIELD, false)
                .get()
                .await()
                .toObjectListOrThrow<ParkirHarianMobilDto>(
                    mappingErrorMessage = ErrorMessages.PARKIR_HARIAN_MOBIL_MAPPING_ERROR
                )
        }.map { it.toListOfParkirHarianMobil() }
    }
    override suspend fun getAllParkirHarianMobil(): Result<List<ParkirHarianMobil>, DataError> {
        return safeCall {
            db.collection(Constant.PARKIR_HARIAN_MOBIL_COLLECTION)
                .get()
                .await()
                .toObjectListOrThrow<ParkirHarianMobilDto>(
                    mappingErrorMessage = ErrorMessages.PARKIR_HARIAN_MOBIL_MAPPING_ERROR
                )
        }.map { it.toListOfParkirHarianMobil() }
    }

    override suspend fun getParkirHarianMobilById(idParkirHarianMobil: String): Result<ParkirHarianMobil, DataError> {
        return safeCall {
            db.collection(Constant.PARKIR_HARIAN_MOBIL_COLLECTION)
                .document(idParkirHarianMobil)
                .get()
                .await()
                .toObject<ParkirHarianMobilDto>() ?: throw Exception(ErrorMessages.PARKIR_HARIAN_MOBIL_NOT_FOUND)
        }.map { it.toParkirHarianMobil() }
    }

    override suspend fun updateDataParkirHarianMobil(
        idParkirHarianMobil: String,
        updateDataParkirHarianMobil: Map<String, Any?>
    ): Result<Unit, DataError> {
        return safeCall {
            db.collection(Constant.PARKIR_HARIAN_MOBIL_COLLECTION)
                .document(idParkirHarianMobil)
                .update(updateDataParkirHarianMobil)
                .await()
        }
    }

    override suspend fun deleteParkirHarianMobil(idParkirHarianMobil: String): Result<Unit, DataError> {
        return safeCall {
            db.collection(Constant.PARKIR_HARIAN_MOBIL_COLLECTION)
                .document(idParkirHarianMobil)
                .delete()
                .await()
        }
    }

    override suspend fun addParkirHarianMobil(tambahParkirHarianMobil: TambahParkirHarianMobil): Result<Unit, DataError> {
        return safeCall {
            db.collection(Constant.PARKIR_HARIAN_MOBIL_COLLECTION)
                .add(tambahParkirHarianMobil)
                .await()
        }
    }

}