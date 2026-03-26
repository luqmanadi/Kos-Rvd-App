package com.kosrvd.app.feature.account.presentation.list_akun

import com.kosrvd.app.feature.account.presentation.models.ListAkunUi

data class ListAkunUiState(
    val isLoading: Boolean = true,
    val loadError: String? = null,
    val listAkun: List<ListAkunUi> = emptyList()
)
