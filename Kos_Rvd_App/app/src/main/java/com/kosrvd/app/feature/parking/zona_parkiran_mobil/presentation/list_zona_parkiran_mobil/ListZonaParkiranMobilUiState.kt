package com.kosrvd.app.feature.parking.zona_parkiran_mobil.presentation.list_zona_parkiran_mobil

import com.kosrvd.app.feature.parking.zona_parkiran_mobil.presentation.models.ListZonaParkiranMobilUi

data class ListZonaParkiranMobilUiState(
    val isLoading : Boolean = true,
    val loadError : String? = null,
    val listZonaParkiranMobil : List<ListZonaParkiranMobilUi> = emptyList()
)
