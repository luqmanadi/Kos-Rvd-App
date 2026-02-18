package com.kosrvd.app.feature.management.presentation.designsystem.component.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult

suspend fun SnackbarHostState.showCustomSnackbar(
    message: String,
    type: SnackbarType,
    actionLabel: String? = null,
    duration: SnackbarDuration = SnackbarDuration.Short,
    withDismissAction: Boolean = false,
): SnackbarResult {
    return showSnackbar(
        CustomSnackbarVisuals(
            message = message,
            type = type,
            actionLabel = actionLabel,
            duration = duration,
            withDismissAction = withDismissAction
        )
    )
}