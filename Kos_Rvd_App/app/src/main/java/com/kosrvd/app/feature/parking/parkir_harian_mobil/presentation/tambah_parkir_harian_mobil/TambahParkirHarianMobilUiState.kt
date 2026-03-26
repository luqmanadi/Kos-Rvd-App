package com.kosrvd.app.feature.parking.parkir_harian_mobil.presentation.tambah_parkir_harian_mobil

import com.kosrvd.app.core.presentation.utils.UiText
import com.kosrvd.app.feature.parking.zona_parkiran_mobil.domain.model.ZonaParkiran

data class TambahParkirHarianMobilUiState(
    val isButtonSubmitLoading: Boolean = false,
    val isListZoneParkingLoading: Boolean = false,
    val startDate: Long? = null,
    val startDateFormat: String? = null,
    val completionDate: Long? = null,
    val completionDateFormat: String? = null,
    val userName: String = "",
    val userNameError: UiText? = null,
    val isUserNameError: Boolean = false,
    val numberPlate: String = "",
    val numberPlateError: UiText? = null,
    val isNumberPlateError: Boolean = false,
    val carBrand: String = "",
    val carBrandError: UiText? = null,
    val isCarBrandError: Boolean = false,
    val carName: String = "",
    val carNameError: UiText? = null,
    val isCarNameError: Boolean = false,
    val notes: String = "",
    val totalCost: Long = 0,
    val sumDayBooking: Int = 0,
    val listZoneParking: List<ZonaParkiran> = emptyList(),
    val loadError: String? = null,
    val selectedZoneParking: ZonaParkiran? = null,
    val currentPage: Int = 1,
    val isButtonNextEnabled: Boolean = false,
    val listNamePage: List<String> = listOf("Tanggal", "Zona", "Kendaraan", "Konfirmasi")
)
