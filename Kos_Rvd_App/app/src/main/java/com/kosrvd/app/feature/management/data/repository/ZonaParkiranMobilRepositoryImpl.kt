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
import com.kosrvd.app.feature.management.data.mappers.toZonaParkiran
import com.kosrvd.app.feature.management.data.mappers.toZonaParkiranList
import com.kosrvd.app.feature.management.data.repository.dto.ZonaParkiranDto
import com.kosrvd.app.feature.management.domain.model.ZonaParkiran
import com.kosrvd.app.feature.management.domain.repository.ZonaParkiranMobilRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ZonaParkiranMobilRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
): ZonaParkiranMobilRepository {
    override suspend fun getAllZonaParkiranMobil(): Result<List<ZonaParkiran>, DataError> {
        return safeCall {
            db.collection(Constant.ZONA_PARKIRAN_COLLECTION)
                .orderBy(Constant.STATUS_FIELD, Query.Direction.DESCENDING)
                .get()
                .await()
                .toObjectListOrThrow<ZonaParkiranDto>(
                    mappingErrorMessage = ErrorMessages.ZONA_PARKIRAN_MOBIL_MAPPING_ERROR
                )
        }.map { it.toZonaParkiranList() }
    }

    override suspend fun getZonaParkiranMobilById(idZonaParkir: String): Result<ZonaParkiran, DataError> {
        return safeCall {
            db.collection(Constant.ZONA_PARKIRAN_COLLECTION)
                .document(idZonaParkir)
                .get()
                .await()
                .toObject<ZonaParkiranDto>() ?: throw Exception(ErrorMessages.ZONA_PARKIRAN_MOBIL_NOT_FOUND)
        }.map { it.toZonaParkiran() }
    }

    override suspend fun addZonaParkiranMobil(
        zoneName: String,
        monthlyFee: Long,
        dailyCosts: Long
    ): Result<Unit, DataError> {
        return safeCall {
            val zonaParkiranBaruModel = mapOf(
                Constant.ZONE_NAME_FIELD to zoneName,
                Constant.MONTHLY_FEE_FIELD to monthlyFee,
                Constant.DAILY_COSTS_FIELD to dailyCosts,
                Constant.STATUS_FIELD to Constant.KOSONG
            )
            db.collection(Constant.ZONA_PARKIRAN_COLLECTION)
                .add(zonaParkiranBaruModel)
                .await()
        }
    }

    override suspend fun deleteZonaParkiranMobil(idZonaParkir: String): Result<Unit, DataError> {
        return safeCall {
            db.collection(Constant.ZONA_PARKIRAN_COLLECTION)
                .document(idZonaParkir)
                .delete()
                .await()
        }
    }

    override suspend fun updateZoneName(
        idZonaParkir: String,
        zoneNameBaru: String
    ): Result<Unit, DataError> {
        return safeCall {
            db.collection(Constant.ZONA_PARKIRAN_COLLECTION)
                .document(idZonaParkir)
                .update(Constant.ZONE_NAME_FIELD, zoneNameBaru)
                .await()
        }
    }

    override suspend fun updateMonthlyFee(
        idZonaParkir: String,
        monthlyFeeBaru: Long
    ): Result<Unit, DataError> {
        return safeCall {
            db.collection(Constant.ZONA_PARKIRAN_COLLECTION)
                .document(idZonaParkir)
                .update(Constant.MONTHLY_FEE_FIELD, monthlyFeeBaru)
                .await()
        }
    }

    override suspend fun updateDailyCosts(
        idZonaParkir: String,
        dailyCostsBaru: Long
    ): Result<Unit, DataError> {
        return safeCall {
            db.collection(Constant.ZONA_PARKIRAN_COLLECTION)
                .document(idZonaParkir)
                .update(Constant.DAILY_COSTS_FIELD, dailyCostsBaru)
                .await()
        }
    }
}