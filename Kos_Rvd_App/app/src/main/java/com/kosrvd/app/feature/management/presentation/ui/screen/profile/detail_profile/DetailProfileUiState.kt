package com.kosrvd.app.feature.management.presentation.ui.screen.profile.detail_profile

import com.kosrvd.app.feature.management.presentation.ui.models.DetailProfileUi

data class DetailProfileUiState(
    val isLoading: Boolean = true,
    val loadError: String? = null,
    val profileUi: DetailProfileUi? = null
)
