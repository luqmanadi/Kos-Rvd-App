package com.kosrvd.app.feature.parking.zona_parkiran_mobil.domain.utils

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Serializable
@Keep
enum class TypeEditZonaParkir {
    EDIT_BIAYA_BULANAN,
    EDIT_BIAYA_HARIAN,
    EDIT_NAMA_ZONA
}