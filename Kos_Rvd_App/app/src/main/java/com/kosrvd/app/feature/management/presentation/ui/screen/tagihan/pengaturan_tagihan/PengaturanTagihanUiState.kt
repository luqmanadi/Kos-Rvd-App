package com.kosrvd.app.feature.management.presentation.ui.screen.tagihan.pengaturan_tagihan

data class PengaturanTagihanUiState(
    val isLoading : Boolean = true,
    val errorMessage : String? = null,
    val useAutoReminder: Boolean = false,
    val useGenerateOtomatis: Boolean = false,
    val isSwitchUseAutoReminderEnabled: Boolean = true,
    val isSwitchUseGenerateOtomatisEnabled: Boolean = true
)
