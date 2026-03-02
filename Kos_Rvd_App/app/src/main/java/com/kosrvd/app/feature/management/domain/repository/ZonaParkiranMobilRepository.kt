package com.kosrvd.app.feature.management.domain.repository

import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.feature.management.domain.model.ZonaParkiran

interface ZonaParkiranMobilRepository {
    suspend fun getAllZonaParkiranMobil(): Result<List<ZonaParkiran>, DataError>
    suspend fun getZonaParkiranMobilById(idZonaParkir: String): Result<ZonaParkiran, DataError>
    suspend fun addZonaParkiranMobil(zoneName: String, monthlyFee: Long, dailyCosts: Long): Result<Unit, DataError>
    suspend fun deleteZonaParkiranMobil(idZonaParkir: String): Result<Unit, DataError>
    suspend fun updateZoneName(idZonaParkir: String, zoneNameBaru: String): Result<Unit, DataError>
    suspend fun updateMonthlyFee(idZonaParkir: String, monthlyFeeBaru: Long): Result<Unit, DataError>
    suspend fun updateDailyCosts(idZonaParkir: String, dailyCostsBaru: Long): Result<Unit, DataError>
    suspend fun getAllZonaParkiranMobilStatusKosong(): Result<List<ZonaParkiran>, DataError>
}