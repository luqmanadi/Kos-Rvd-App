package com.kosrvd.app.feature.rental.domain.model

import androidx.annotation.Keep

@Keep
data class InfoZonaParkir(
    val idZonaParkir: String,
    val zoneName: String,
    val monthlyFee: Long
)