package com.kosrvd.app.feature.complaint.presentation.buat_laporan_keluhan

import android.net.Uri
import com.kosrvd.app.core.presentation.utils.UiText

data class BuatLaporanKeluhanUiState(
    val isButtonLoading: Boolean = false,
    val title: String = "",
    val description: String = "",
    val photoReport: Uri = Uri.EMPTY,
    val titleError: UiText? = null,
    val descriptionError: UiText? = null,
    val isTitleError: Boolean = false,
    val isDescriptionError: Boolean = false,
)