package com.kosrvd.app.feature.management.presentation.ui.screen.kamar.detail_kamar

import com.kosrvd.app.feature.management.domain.model.Kamar

data class DetailKamarUiState(
    val isLoading: Boolean = true,
    val loadError: String? = null,
    val kamarUi: Kamar? = null,
    val isButtonDeleteLoading: Boolean = false,
    val showDialogDeleteKamar: Boolean = false,
)
