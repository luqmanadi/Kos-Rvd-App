package com.kosrvd.app.feature.rental.presentation.list_penyewaan

import com.kosrvd.app.feature.rental.presentation.models.ListPenyewaanUi

data class ListPenyewaanUiState(
    val isLoading: Boolean = true,
    val listPenyewaanUi: List<ListPenyewaanUi> = emptyList(),
    val loadError: String? = null
)
