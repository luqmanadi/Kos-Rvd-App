package com.kosrvd.app.feature.room.domain.repository

import com.kosrvd.app.core.domain.models.AlatElektronik
import com.kosrvd.app.core.domain.models.Harga
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.feature.room.domain.model.BuatKamar
import com.kosrvd.app.feature.room.domain.model.Kamar

interface KamarRepository {
    suspend fun getAllKamar(): Result<List<Kamar>, DataError>
    suspend fun deleteKamar(idKamar: String): Result<Unit, DataError>
    suspend fun getKamarById(idKamar: String): Result<Kamar, DataError>
    suspend fun addKamar(buatKamar: BuatKamar): Result<Unit, DataError>
    suspend fun getKamarByNomorKamar(nomorKamar: Int): Result<List<Kamar>, DataError>
    suspend fun updateNomorKamar(idKamar: String, nomorKamarBaru: Int): Result<Unit, DataError>
    suspend fun updateUkuranKamar(idKamar: String, ukuranKamarBaru: String): Result<Unit, DataError>
    suspend fun updateTarifKamar(idKamar: String, tarifKamarBaru: Harga): Result<Unit, DataError>
    suspend fun updateLayananAlatElektronikGratis(idKamar: String, layananAlatElektronikGratisBaru: List<AlatElektronik>): Result<Unit, DataError>
    suspend fun updateFasilitasKamar(idKamar: String, fasilitasKamarBaru: List<String>): Result<Unit, DataError>
    suspend fun getAllKamarStatusKosong(): Result<List<Kamar>, DataError>
}