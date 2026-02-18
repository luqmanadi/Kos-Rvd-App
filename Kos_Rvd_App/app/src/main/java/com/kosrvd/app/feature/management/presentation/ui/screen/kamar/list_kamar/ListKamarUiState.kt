package com.kosrvd.app.feature.management.presentation.ui.screen.kamar.list_kamar

import com.kosrvd.app.feature.management.presentation.ui.models.ListKamarUi

data class ListKamarUiState(
    val isLoading: Boolean = true,
    val loadError: String? = null,
    val listKamarUi: List<ListKamarUi> = emptyList()
)
