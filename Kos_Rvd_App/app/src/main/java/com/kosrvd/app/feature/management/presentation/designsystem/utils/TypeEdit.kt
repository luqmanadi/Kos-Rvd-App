package com.kosrvd.app.feature.management.presentation.designsystem.utils

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
enum class TypeEdit {
    NAMA,
    EMAIL,
    GANTI_PASSWORD,
    NO_HP,
    ALAMAT
}