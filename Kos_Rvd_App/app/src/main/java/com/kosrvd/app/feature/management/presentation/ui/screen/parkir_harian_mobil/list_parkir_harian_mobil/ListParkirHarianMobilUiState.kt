package com.kosrvd.app.feature.management.presentation.ui.screen.parkir_harian_mobil.list_parkir_harian_mobil

import com.kosrvd.app.feature.management.presentation.ui.models.ListParkirHarianMobilUi

data class ListParkirHarianMobilUiState(
    val isLoading: Boolean = false,
    val listParkirHarianMobil: List<ListParkirHarianMobilUi> = emptyList(),
    val loadError: String? = null
)
