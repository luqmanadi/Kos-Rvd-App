package com.kosrvd.app.feature.resident.presentation.detail_penghuni

import com.kosrvd.app.feature.resident.presentation.models.DetailPenghuniUi

data class DetailPenghuniUiState(
    val isLoading: Boolean = true,
    val loadError: String? = null,
    val penghuni: DetailPenghuniUi? = null
)
