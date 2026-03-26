package com.kosrvd.app.feature.parking.parkir_harian_mobil.presentation.list_parkir_harian_mobil

import com.kosrvd.app.feature.parking.parkir_harian_mobil.presentation.models.ListParkirHarianMobilUi

data class ListParkirHarianMobilUiState(
    val isLoading: Boolean = false,
    val listParkirHarianMobil: List<ListParkirHarianMobilUi> = emptyList(),
    val loadError: String? = null
)
