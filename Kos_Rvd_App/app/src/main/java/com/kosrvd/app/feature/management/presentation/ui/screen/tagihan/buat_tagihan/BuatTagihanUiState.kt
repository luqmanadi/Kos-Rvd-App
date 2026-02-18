package com.kosrvd.app.feature.management.presentation.ui.screen.tagihan.buat_tagihan

import com.kosrvd.app.feature.management.domain.model.Penyewaan

data class BuatTagihanUiState(
    val isButtonErrorLoading: Boolean = false,
    val isButtonLoading: Boolean = false,
    val adminFees: Boolean = false,
    val itemSelected: Penyewaan? = null,
    val loadError: String? = null,
    val listPenyewaan: List<Penyewaan> = emptyList(),
)
