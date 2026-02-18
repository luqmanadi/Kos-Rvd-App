package com.kosrvd.app.feature.management.presentation.ui

import com.kosrvd.app.core.domain.utils.Role
import com.kosrvd.app.feature.management.presentation.ui.models.MainScreenUi

data class MainScreenUiState(
    val mainScreenUi: MainScreenUi = MainScreenUi(),
    val isLoadingTopBarDashboard: Boolean = true,
    val userRole: Role = Role.EMPTY
)