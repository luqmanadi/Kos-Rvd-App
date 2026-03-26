package com.kosrvd.app.feature.rental.presentation.detail_penyewaan

import com.kosrvd.app.feature.rental.presentation.models.PenyewaUi

data class DetailPenyewaanUiState(
    val idPenyewa: String = "",
    val isLoading: Boolean = true,
    val penyewaUi: PenyewaUi? = null,
    val loadError: String? = null,
    val showDialogEndRental: Boolean = false,
    val isButtonLoadingEndRental: Boolean = false
)
