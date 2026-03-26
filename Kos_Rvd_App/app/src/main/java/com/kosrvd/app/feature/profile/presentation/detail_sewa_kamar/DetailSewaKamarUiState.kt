package com.kosrvd.app.feature.profile.presentation.detail_sewa_kamar

import com.kosrvd.app.feature.profile.presentation.models.DetailSewaKamarUi

data class DetailSewaKamarUiState(
    val isLoading: Boolean = false,
    val detailSewaKamarUi: DetailSewaKamarUi? = null,
    val error: String? = null
)
