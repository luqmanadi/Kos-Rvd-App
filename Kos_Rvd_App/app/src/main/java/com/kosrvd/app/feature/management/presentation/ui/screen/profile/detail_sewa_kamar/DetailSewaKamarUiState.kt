package com.kosrvd.app.feature.management.presentation.ui.screen.profile.detail_sewa_kamar

import com.kosrvd.app.feature.management.presentation.ui.models.DetailSewaKamarUi

data class DetailSewaKamarUiState(
    val isLoading: Boolean = false,
    val detailSewaKamarUi: DetailSewaKamarUi? = null,
    val error: String? = null
)
