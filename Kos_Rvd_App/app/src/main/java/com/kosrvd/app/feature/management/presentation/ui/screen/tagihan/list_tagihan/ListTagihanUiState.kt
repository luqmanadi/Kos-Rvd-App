package com.kosrvd.app.feature.management.presentation.ui.screen.tagihan.list_tagihan

import androidx.compose.runtime.Immutable
import com.kosrvd.app.core.domain.utils.Role
import com.kosrvd.app.feature.management.presentation.ui.models.TagihanGroupUi

@Immutable
data class ListTagihanUiState(
    val listTagihanBelumLunasUi: TabTagihanUi = TabTagihanUi(),
    val listTagihanMenungguVerifikasiUi: TabTagihanUi = TabTagihanUi(),
    val listTagihanLunasUi: TabTagihanUi = TabTagihanUi(),
    val selectedTab: Int = 0
)

data class TabTagihanUi(
    val isLoading: Boolean = true,
    val listTagihan: List<TagihanGroupUi> = emptyList(),
    val loadError: String? = null,
)
