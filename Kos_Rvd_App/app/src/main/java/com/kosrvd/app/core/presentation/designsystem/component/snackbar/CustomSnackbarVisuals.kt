package com.kosrvd.app.core.presentation.designsystem.component.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals

data class CustomSnackbarVisuals(
    override val actionLabel: String?,
    override val duration: SnackbarDuration = SnackbarDuration.Short,
    override val message: String,
    override val withDismissAction: Boolean = false,
    val type: SnackbarType
): SnackbarVisuals
