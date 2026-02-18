package com.kosrvd.app.feature.management.presentation.ui.screen.profile.main_page

import com.kosrvd.app.feature.management.presentation.ui.models.ProfileUi

data class ProfileUiState(
    val loading: Boolean = true,
    val buttonLoading: Boolean = false,
    val profileUi: ProfileUi? = null,
    val loadError: String? = null,
    val isLogoutDialogVisible: Boolean = false
)
