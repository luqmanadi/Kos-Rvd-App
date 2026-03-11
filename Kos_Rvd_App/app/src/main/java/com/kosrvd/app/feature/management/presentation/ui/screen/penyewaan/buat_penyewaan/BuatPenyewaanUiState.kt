package com.kosrvd.app.feature.management.presentation.ui.screen.penyewaan.buat_penyewaan

import com.kosrvd.app.core.domain.models.Account
import com.kosrvd.app.core.presentation.utils.UiText
import com.kosrvd.app.feature.management.domain.model.AlatElektronik
import com.kosrvd.app.feature.management.domain.model.Kamar
import com.kosrvd.app.feature.management.domain.model.ZonaParkiran


data class BuatPenyewaanUiState(
    val isListKamarLoading: Boolean = true,
    val loadKamarError: String? = null,
    val listKamar: List<Kamar> = emptyList(),
    val selectedKamar: Kamar? = null,
    val listPenghuni: List<Account?> = emptyList(),
    val selectedPenghuniPertama: Account? = null,
    val selectedPenghuniKedua: Account? = null,
    val listZoneParking: List<ZonaParkiran?> = emptyList(),
    val selectedZoneParking: ZonaParkiran? = null,
    val namaAlatElektronik: String = "",
    val priceAlatElektronik: String = "",
    val listAlatElektronik: List<AlatElektronik> = emptyList(),
    val numberPlate: String = "",
    val numberPlateError: UiText? = null,
    val isNumberPlateError: Boolean = false,
    val carBrand: String = "",
    val carBrandError: UiText? = null,
    val isCarBrandError: Boolean = false,
    val carName: String = "",
    val carNameError: UiText? = null,
    val isCarNameError: Boolean = false,
    val listNamePenghuni: List<String> = emptyList(),
    val notes: String = "",
    val roomPrice: Long = 0,
    val totalBiaya: Long = 0,
    val currentPage: Int = 1,
    val isButtonNextEnabled: Boolean = false,
    val isButtonSubmitLoading: Boolean = false,
    val listNamePage: List<String> = listOf("Kamar", "Penghuni", "Elektronik", "Parkir", "Konfirmasi")
)