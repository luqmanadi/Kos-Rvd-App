package com.kosrvd.app.feature.announcement.domain.repository

import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.feature.announcement.domain.model.BuatPengumuman
import com.kosrvd.app.feature.announcement.domain.model.Pengumuman
import kotlinx.coroutines.flow.Flow

interface PengumumanRepository {
    suspend fun getAllPengumuman(): Flow<Result<List<Pengumuman>, DataError>>
    suspend fun deletePengumuman(idPengumuman: String): Result<Unit, DataError>
    suspend fun createPengumuman(createPengumuman: BuatPengumuman): Result<Unit, DataError>
}