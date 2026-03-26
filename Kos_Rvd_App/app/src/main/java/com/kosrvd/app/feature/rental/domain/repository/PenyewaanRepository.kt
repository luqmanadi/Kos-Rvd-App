package com.kosrvd.app.feature.rental.domain.repository

import com.kosrvd.app.core.domain.models.AlatElektronik
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.feature.rental.domain.model.BuatPenyewaan
import com.kosrvd.app.feature.rental.domain.model.InfoPakaiParkirMobilBulanan
import com.kosrvd.app.feature.rental.domain.model.Penyewaan
import kotlinx.coroutines.flow.Flow

interface PenyewaanRepository {
    suspend fun getAllPenyewaanActive(): Result<List<Penyewaan>, DataError>
    fun getAllPenyewaanFlow(): Flow<Result<List<Penyewaan>, DataError>>
    suspend fun getPenyewaanById(idPenyewaan: String): Result<Penyewaan, DataError>
    suspend fun buatPenyewaan(item: BuatPenyewaan): Result<Penyewaan, DataError>
    suspend fun getJumlahPenghuniByIdKamarAndStatusPenyewaanAktif(idKamar: String): Result<Int, DataError>
    suspend fun endPenyewaan(idPenyewaan: String): Result<Unit, DataError>
    suspend fun editPemakaianElektronikBulanan(idPenyewaan: String, listAlatElektronik: List<AlatElektronik>): Result<Unit, DataError>
    suspend fun editPemakaianParkirMobilBulanan(idPenyewaan: String, pakaiParkirMobilBulanan: InfoPakaiParkirMobilBulanan): Result<Unit, DataError>
    suspend fun endRentalPemakaianParkirMobilBulanan(idPenyewaan: String): Result<Unit, DataError>
}