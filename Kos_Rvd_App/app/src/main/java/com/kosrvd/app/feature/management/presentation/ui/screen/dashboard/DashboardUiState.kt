package com.kosrvd.app.feature.management.presentation.ui.screen.dashboard

import androidx.compose.runtime.Immutable
import com.kosrvd.app.feature.management.presentation.ui.models.AdminDashboardUi
import com.kosrvd.app.feature.management.presentation.ui.models.PenghuniDashboardUi

@Immutable
data class DashboardUiState(
    val isLoading: Boolean = true,
    val penghuniDashboardUi: PenghuniDashboardUi? = null,
    val adminDashboardUi: AdminDashboardUi? = null,
    val loadError: String? = null,
    val showRationaleDialog: Boolean = false
)
