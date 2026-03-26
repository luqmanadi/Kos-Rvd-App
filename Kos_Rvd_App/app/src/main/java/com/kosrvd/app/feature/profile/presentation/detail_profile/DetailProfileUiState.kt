package com.kosrvd.app.feature.profile.presentation.detail_profile

import com.kosrvd.app.feature.profile.presentation.models.DetailProfileUi

data class DetailProfileUiState(
    val isLoading: Boolean = true,
    val loadError: String? = null,
    val profileUi: DetailProfileUi? = null
)
