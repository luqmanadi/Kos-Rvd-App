package com.kosrvd.app.feature.rental.data.repository.dto

import androidx.annotation.Keep

@Keep
data class InfoZonaParkirDto(
    val idZonaParkir: String = "",
    val zoneName: String = "",
    val monthlyFee: Long = 0
)
