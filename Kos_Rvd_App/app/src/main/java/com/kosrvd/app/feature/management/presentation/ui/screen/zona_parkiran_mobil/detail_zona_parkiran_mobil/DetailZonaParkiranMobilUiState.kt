package com.kosrvd.app.feature.management.presentation.ui.screen.zona_parkiran_mobil.detail_zona_parkiran_mobil

import com.kosrvd.app.feature.management.domain.model.ZonaParkiran

data class DetailZonaParkiranMobilUiState(
    val isLoading: Boolean = false,
    val detailZonaParkiranMobilUi: ZonaParkiran? = null,
    val loadError: String? = null,
    val showDialogDeleteZonaParkir: Boolean = false,
    val buttonDeleteIsLoading: Boolean = false
)
