package com.kosrvd.app.feature.management.presentation.ui.screen.penyewaan.list_penyewaan

import com.kosrvd.app.feature.management.presentation.ui.models.ListPenyewaanUi

data class ListPenyewaanUiState(
    val isLoading: Boolean = true,
    val listPenyewaanUi: List<ListPenyewaanUi> = emptyList(),
    val loadError: String? = null
)
