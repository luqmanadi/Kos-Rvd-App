package com.kosrvd.app.feature.resident.presentation.list_penghuni

import com.kosrvd.app.core.domain.utils.Role
import com.kosrvd.app.feature.resident.presentation.models.ListResidentUi

data class ListPenghuniUiState(
    val isLoading: Boolean = true,
    val loadError: String? = null,
    val listPenghuni: List<ListResidentUi> = emptyList(),
    val role: Role = Role.EMPTY
)
