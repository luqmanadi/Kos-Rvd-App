package com.kosrvd.app.feature.management.presentation.ui.screen.zona_parkiran_mobil.tambah_zona_parkiran_mobil

import com.kosrvd.app.core.presentation.utils.UiText

data class TambahZonaParkiranMobilUiState(
    val isButtonLoading: Boolean = false,
    val zoneName: String = "",
    val zoneNameError: UiText? = null,
    val isZoneNameError: Boolean = false,
    val shakeTriggerZoneNameError: Int = 0,
    val biayaBulanan: String = "",
    val biayaBulananError: UiText? = null,
    val isBiayaBulananError: Boolean = false,
    val shakeTriggerBiayaBulananError: Int = 0,
    val biayaHarian: String = "",
    val biayaHarianError: UiText? = null,
    val isBiayaHarianError: Boolean = false,
    val shakeTriggerBiayaHarianError: Int = 0,
)
