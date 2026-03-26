package com.kosrvd.app.core.presentation.designsystem.component.snackbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class SnackbarType(
    val containerColor: Color,
    val contentColor: Color = Color.White,
    val icon: ImageVector = Icons.Filled.Info,
) {
    SUCCESS(Color(0xFF006877), Color(0xFFFFFFFF)),
    ERROR(Color(0xFFBA1A1A), Color(0xFFFFFFFF))
}