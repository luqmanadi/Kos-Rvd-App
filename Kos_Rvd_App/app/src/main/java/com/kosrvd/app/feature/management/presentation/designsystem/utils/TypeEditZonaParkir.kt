package com.kosrvd.app.feature.management.presentation.designsystem.utils

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Serializable
@Keep
enum class TypeEditZonaParkir {
    EDIT_BIAYA_BULANAN,
    EDIT_BIAYA_HARIAN,
    EDIT_NAMA_ZONA
}