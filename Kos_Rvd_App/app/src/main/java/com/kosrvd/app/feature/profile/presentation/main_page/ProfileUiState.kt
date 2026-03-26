package com.kosrvd.app.feature.profile.presentation.main_page

import com.kosrvd.app.feature.profile.presentation.models.ProfileUi

data class ProfileUiState(
    val loading: Boolean = true,
    val buttonLoading: Boolean = false,
    val profileUi: ProfileUi? = null,
    val loadError: String? = null,
    val isLogoutDialogVisible: Boolean = false
)
