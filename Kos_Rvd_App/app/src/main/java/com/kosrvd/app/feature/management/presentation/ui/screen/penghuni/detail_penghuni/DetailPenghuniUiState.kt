package com.kosrvd.app.feature.management.presentation.ui.screen.penghuni.detail_penghuni

import com.kosrvd.app.feature.management.presentation.ui.models.DetailPenghuniUi

data class DetailPenghuniUiState(
    val isLoading: Boolean = true,
    val loadError: String? = null,
    val penghuni: DetailPenghuniUi? = null
)
