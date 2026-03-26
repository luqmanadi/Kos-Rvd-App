package com.kosrvd.app.feature.complaint.domain.repository

import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.feature.complaint.domain.model.Keluhan
import com.kosrvd.app.feature.complaint.domain.model.BuatLaporanKeluhan
import com.kosrvd.app.feature.complaint.domain.utils.TypeKeluhan
import kotlinx.coroutines.flow.Flow

interface KeluhanRepository {
    fun getAllKeluhanForPenghuni(typeKeluhan: TypeKeluhan, idAkun: String): Flow<Result<List<Keluhan>, DataError>>
    fun getAllKeluhanForAdmin(typeKeluhan: TypeKeluhan): Flow<Result<List<Keluhan>, DataError>>
    suspend fun createLaporanKeluhan(laporanKeluhan: BuatLaporanKeluhan): Result<Keluhan, DataError>
    suspend fun getDetailKeluhanById(idKeluhan: String): Result<Keluhan, DataError>
    suspend fun updateProgresLaporanKeluhan(idKeluhan: String, updateProgresLaporanKeluhan: Map<String, Any?>): Result<Unit, DataError>
    suspend fun delleteKeluhan(idKeluhan: String): Result<Unit, DataError>

}