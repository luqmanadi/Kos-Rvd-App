package com.kosrvd.app.feature.rental.presentation.edit_pemakaian_parkir_mobil

import com.kosrvd.app.core.presentation.utils.UiText
import com.kosrvd.app.feature.rental.domain.model.InfoZonaParkir
import com.kosrvd.app.feature.parking.zona_parkiran_mobil.domain.model.ZonaParkiran

data class EditPemakaianParkirMobilUiState(
    val idPenyewaan: String = "",
    val isButtonSubmitLoading: Boolean = false,
    val isButtonEndRentalLoading: Boolean = false,
    val isButtonSubmitEnabled: Boolean = false,
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
    val listZonaParkir: List<ZonaParkiran> = emptyList(),
    val newSelectedZoneParking: ZonaParkiran? = null,
    val oldSelectedZoneParking: InfoZonaParkir? = null,
    val oldCarBrand: String = "",
    val oldCarName: String = "",
    val oldNumberPlate: String = "",
    val oldNotes: String = ""
)
