package com.kosrvd.app.feature.management.domain.repository


import com.kosrvd.app.core.domain.utils.DataError
import kotlinx.coroutines.flow.Flow
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.feature.management.domain.model.BuatLaporanKeluhan
import com.kosrvd.app.feature.management.domain.model.Keluhan
import com.kosrvd.app.feature.management.domain.utils.TypeKeluhan

interface KeluhanRepository {
    fun getAllKeluhanForPenghuni(typeKeluhan: TypeKeluhan, idAkun: String): Flow<Result<List<Keluhan>, DataError>>
    fun getAllKeluhanForAdmin(typeKeluhan: TypeKeluhan): Flow<Result<List<Keluhan>, DataError>>
    suspend fun createLaporanKeluhan(laporanKeluhan: BuatLaporanKeluhan): Result<Keluhan, DataError>
    suspend fun getDetailKeluhanById(idKeluhan: String): Result<Keluhan, DataError>
    suspend fun updateProgresLaporanKeluhan(idKeluhan: String, updateProgresLaporanKeluhan: Map<String, Any?>): Result<Unit, DataError>
    suspend fun delleteKeluhan(idKeluhan: String): Result<Unit, DataError>

}