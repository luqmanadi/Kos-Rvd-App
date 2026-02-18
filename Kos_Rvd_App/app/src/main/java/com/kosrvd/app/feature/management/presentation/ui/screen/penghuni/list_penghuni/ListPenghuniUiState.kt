package com.kosrvd.app.feature.management.presentation.ui.screen.penghuni.list_penghuni

import com.kosrvd.app.core.domain.utils.Role
import com.kosrvd.app.feature.management.presentation.ui.models.ListPenghuniUi

data class ListPenghuniUiState(
    val isLoading: Boolean = true,
    val loadError: String? = null,
    val listPenghuni: List<ListPenghuniUi> = emptyList(),
    val role: Role = Role.EMPTY
)
