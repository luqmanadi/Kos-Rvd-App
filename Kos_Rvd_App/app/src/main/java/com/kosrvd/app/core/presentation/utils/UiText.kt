package com.kosrvd.app.core.presentation.utils

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class UiText {
    // 1. Untuk String biasa (hardcoded)
    data class DynamicString(val value: String) : UiText()

    // 2. Untuk String dari resource (R.string.xxx)
    class StringResource(
        @get:StringRes val id: Int,
        vararg val args: Any
    ) : UiText()

    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> context.getString(id, *args)
        }
    }

    // Fungsi helper agar gampang dipakai di Composable
    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> stringResource(id, *args)
        }
    }
}