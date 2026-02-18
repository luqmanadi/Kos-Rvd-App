package com.kosrvd.app.feature.management.presentation.ui.screen.keluhan.detail_keluhan

import android.net.Uri
import com.kosrvd.app.core.domain.utils.Role
import com.kosrvd.app.core.presentation.utils.UiText
import com.kosrvd.app.feature.management.presentation.ui.models.DetailKeluhanUi

data class DetailKeluhanUiState(
    val isLoading: Boolean = true,
    val detailKeluhanUi: DetailKeluhanUi? = null,
    val loadError: String? = null,
    val role: Role = Role.EMPTY,
    val response: String = "",
    val responseError: UiText? = null,
    val isResponseError: Boolean = false,
    val responseImage: Uri = Uri.EMPTY,
    val shakeTriggerResponseError: Int = 0,
    val isButtonProsesLoading: Boolean = false,
    val isButtonSelesaiLoading: Boolean = false,
    val isButtonDeleteLoading: Boolean = false,
    val isShowDialogDeleteVisible: Boolean = false
)