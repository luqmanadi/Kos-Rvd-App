package com.kosrvd.app.feature.account.presentation.detail_akun

import com.kosrvd.app.feature.account.presentation.models.DetailAkunUi

data class DetailAkunUiState(
    val isLoading: Boolean = true,
    val buttonNonAktifIsLoading: Boolean = false,
    val buttonAktifIsLoading: Boolean = false,
    val loadError: String? = null,
    val detailAkun: DetailAkunUi? = null,
    val showDialogNonAktifAkun: Boolean = false,
    val showDialogActivateAccount: Boolean = false
)
