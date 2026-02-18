package com.kosrvd.app.feature.management.presentation.ui.screen.zona_parkiran_mobil.list_zona_parkiran_mobil

import com.kosrvd.app.feature.management.presentation.ui.models.ListZonaParkiranMobilUi

data class ListZonaParkiranMobilUiState(
    val isLoading : Boolean = true,
    val loadError : String? = null,
    val listZonaParkiranMobil : List<ListZonaParkiranMobilUi> = emptyList()
)
