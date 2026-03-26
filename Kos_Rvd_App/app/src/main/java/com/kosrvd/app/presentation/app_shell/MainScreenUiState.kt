package com.kosrvd.app.presentation.app_shell

import com.kosrvd.app.core.domain.utils.Role
import com.kosrvd.app.presentation.app_shell.models.MainScreenUi

data class MainScreenUiState(
    val mainScreenUi: MainScreenUi = MainScreenUi(),
    val isLoadingTopBarDashboard: Boolean = true,
    val userRole: Role = Role.EMPTY
)