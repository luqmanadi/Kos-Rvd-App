package com.kosrvd.app.feature.announcement.presentation.list_pengumuman

import com.kosrvd.app.core.domain.utils.Role
import com.kosrvd.app.feature.announcement.presentation.models.PengumumanUi

data class PengumumanUiState(
    val isLoading: Boolean = true,
    val loadError: String? = null,
    val listPengumuman: List<PengumumanUi> = emptyList(),
    val role: Role = Role.EMPTY,
    val isDeleteDialogVisible: Boolean = false,
    val buttonLoading: Boolean = false,
    val buttonCancelEnabled: Boolean = true,
    val idPengumumanForDelete: String = ""
)
