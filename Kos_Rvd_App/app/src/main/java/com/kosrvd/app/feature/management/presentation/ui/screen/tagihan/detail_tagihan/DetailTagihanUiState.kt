package com.kosrvd.app.feature.management.presentation.ui.screen.tagihan.detail_tagihan

import android.net.Uri
import com.kosrvd.app.core.domain.utils.Role
import com.kosrvd.app.core.presentation.utils.UiText
import com.kosrvd.app.feature.management.presentation.ui.models.DetailTagihanUi

data class DetailTagihanUiState(
    val isLoading: Boolean = true,
    val detailTagihanUi: DetailTagihanUi? = null,
    val loadError: String? = null,
    val isButtonKirimOrBayarLoading: Boolean = false,
    val isButtonHapusLoading: Boolean = false,
    val isButtonVerifikasiLoading: Boolean = false,
    val isButtonKirimTolakLoading: Boolean = false,
    val isHapusDialogVisible: Boolean = false,
    val isTolakBottomSheetVisible: Boolean = false,
    val alasanPenolakan: String = "",
    val isAlasanPenolakanError: Boolean = false,
    val alasanPenolakanError: UiText? = null,
    val alasanPenolakanShakeTrigger: Int = 0,
    val proofOfPayment: Uri = Uri.EMPTY,
    val role: Role = Role.EMPTY
)
