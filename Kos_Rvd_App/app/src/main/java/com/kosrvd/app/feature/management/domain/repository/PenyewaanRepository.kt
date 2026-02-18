package com.kosrvd.app.feature.management.domain.repository

import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.feature.management.domain.model.BuatPenyewaan
import com.kosrvd.app.feature.management.domain.model.Penyewaan
import kotlinx.coroutines.flow.Flow

interface PenyewaanRepository {
    suspend fun getAllPenyewaanActive(): Result<List<Penyewaan>, DataError>
    fun getAllPenyewaanFlow(): Flow<Result<List<Penyewaan>, DataError>>
    suspend fun getPenyewaanById(idPenyewaan: String): Result<Penyewaan, DataError>
    suspend fun buatPenyewaan(item: BuatPenyewaan): Result<Penyewaan, DataError>
    suspend fun getJumlahPenghuniByIdKamarAndStatusPenyewaanAktif(idKamar: String): Result<Int, DataError>
}