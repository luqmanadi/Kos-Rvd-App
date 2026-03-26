package com.kosrvd.app.feature.complaint.presentation.list_keluhan

import androidx.compose.runtime.Immutable
import com.kosrvd.app.feature.complaint.presentation.models.KeluhanGroupUi

@Immutable
data class ListKeluhanUiState(
    val listKeluhanMenungguKonfirmasi: TabKeluhanUi = TabKeluhanUi(),
    val listKeluhanSedangDiproses: TabKeluhanUi = TabKeluhanUi(),
    val listKeluhanSelesai: TabKeluhanUi = TabKeluhanUi(),
    val selectedTab: Int = 0
)

data class TabKeluhanUi(
    val isLoading: Boolean = true,
    val loadError: String? = null,
    val listKeluhan: List<KeluhanGroupUi> = emptyList(),
)
