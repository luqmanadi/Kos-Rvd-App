package com.kosrvd.app.feature.management.presentation.ui.screen.parkir_harian_mobil.detail_parkir_harian_mobil

import android.net.Uri
import com.kosrvd.app.feature.management.presentation.ui.models.DetailParkirHarianMobilUi

data class DetailParkirHarianMobilUiState(
    val idParkirHarianMobil: String = "",
    val isLoading: Boolean = true,
    val dataDetailParkirHarianMobil: DetailParkirHarianMobilUi? = null,
    val loadError: String? = null,
    val isButtonUploadLoading: Boolean = false,
    val isButtonHapusLoading: Boolean = false,
    val isButtonCancelledLoading: Boolean = false,
    val isCancelledDialogVisible: Boolean = false,
    val isHapusDialogVisible: Boolean = false,
    val proofOfPayment: Uri = Uri.EMPTY
)
