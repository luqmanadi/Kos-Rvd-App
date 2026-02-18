package com.kosrvd.app.feature.management.presentation.ui.screen.akun.list_akun

import com.kosrvd.app.feature.management.presentation.ui.models.ListAkunUi

data class ListAkunUiState(
    val isLoading: Boolean = true,
    val loadError: String? = null,
    val listAkun: List<ListAkunUi> = emptyList()
)
