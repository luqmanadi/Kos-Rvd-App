package com.kosrvd.app.feature.room.presentation.list_kamar

import com.kosrvd.app.feature.room.presentation.models.ListKamarUi


data class ListKamarUiState(
    val isLoading: Boolean = true,
    val loadError: String? = null,
    val listKamarUi: List<ListKamarUi> = emptyList()
)
