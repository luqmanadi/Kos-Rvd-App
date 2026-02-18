package com.kosrvd.app.feature.management.presentation.ui.screen.zona_parkiran_mobil.edit_zona_parkiran_mobil

import com.kosrvd.app.core.presentation.utils.UiText
import com.kosrvd.app.feature.management.presentation.designsystem.utils.TypeEditZonaParkir

data class EditZonaParkiranMobilUiState(
    val idZonaParkir: String = "",
    val isButtonLoading: Boolean = false,
    val typeEditZonaParkir: TypeEditZonaParkir? = null,
    val oldZoneName: String = "",
    val zoneName: String = "",
    val zoneNameError: UiText? = null,
    val isZoneNameError: Boolean = false,
    val shakeTriggerZoneNameError: Int = 0,
    val oldBiayaBulanan: String = "",
    val biayaBulanan: String = "",
    val biayaBulananError: UiText? = null,
    val isBiayaBulananError: Boolean = false,
    val shakeTriggerBiayaBulananError: Int = 0,
    val oldBiayaHarian: String = "",
    val biayaHarian: String = "",
    val biayaHarianError: UiText? = null,
    val isBiayaHarianError: Boolean = false,
    val shakeTriggerBiayaHarianError: Int = 0,
)
