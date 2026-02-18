package com.kosrvd.app.feature.management.presentation.ui.screen.pengumuman.buat_pengumuman

import com.kosrvd.app.core.presentation.utils.UiText

data class BuatPengumumanUiState(
    val isButtonLoading: Boolean = false,
    val loadError: String? = null,
    val title: String = "",
    val titleError: UiText? = null,
    val isTitleError: Boolean = false,
    val shakeTargetTitle: Int = 0,
    val description: String = "",
    val descriptionError: UiText? = null,
    val isDescriptionError: Boolean = false,
    val shakeTargetDescription: Int = 0
)
