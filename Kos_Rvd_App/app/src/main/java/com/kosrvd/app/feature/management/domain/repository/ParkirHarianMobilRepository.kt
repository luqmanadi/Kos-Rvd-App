package com.kosrvd.app.feature.management.domain.repository

import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.feature.management.domain.model.ParkirHarianMobil
import com.kosrvd.app.feature.management.domain.model.TambahParkirHarianMobil

interface ParkirHarianMobilRepository {
    suspend fun getAllParkirHarianMobilCancelledFalseAndCompletionDateGreaterThan(): Result<List<ParkirHarianMobil>, DataError>
    suspend fun getAllParkirHarianMobil(): Result<List<ParkirHarianMobil>, DataError>
    suspend fun getParkirHarianMobilById(idParkirHarianMobil: String): Result<ParkirHarianMobil, DataError>
    suspend fun updateDataParkirHarianMobil(idParkirHarianMobil: String, updateDataParkirHarianMobil: Map<String, Any?>): Result<Unit, DataError>
    suspend fun deleteParkirHarianMobil(idParkirHarianMobil: String): Result<Unit, DataError>
    suspend fun addParkirHarianMobil(tambahParkirHarianMobil: TambahParkirHarianMobil): Result<Unit, DataError>
    suspend fun getAllParkirHarianMobilCancelledStatusFalse(): Result<List<ParkirHarianMobil>, DataError>
}