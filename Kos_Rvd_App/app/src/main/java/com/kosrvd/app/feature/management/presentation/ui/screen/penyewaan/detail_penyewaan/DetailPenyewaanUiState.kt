package com.kosrvd.app.feature.management.presentation.ui.screen.penyewaan.detail_penyewaan

import com.kosrvd.app.feature.management.presentation.ui.models.PenyewaUi

data class DetailPenyewaanUiState(
    val idPenyewa: String = "",
    val isLoading: Boolean = true,
    val penyewaUi: PenyewaUi? = null,
    val loadError: String? = null,
    val showDialogEndRental: Boolean = false,
    val isButtonLoadingEndRental: Boolean = false
)
