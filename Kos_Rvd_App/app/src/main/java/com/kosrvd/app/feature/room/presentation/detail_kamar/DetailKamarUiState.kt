package com.kosrvd.app.feature.room.presentation.detail_kamar

import com.kosrvd.app.feature.room.domain.model.Kamar

data class DetailKamarUiState(
    val isLoading: Boolean = true,
    val loadError: String? = null,
    val kamarUi: Kamar? = null,
    val isButtonDeleteLoading: Boolean = false,
    val showDialogDeleteKamar: Boolean = false,
)
