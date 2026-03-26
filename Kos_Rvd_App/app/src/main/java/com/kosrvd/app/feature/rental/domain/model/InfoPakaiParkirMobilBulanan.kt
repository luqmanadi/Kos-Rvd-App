package com.kosrvd.app.feature.rental.domain.model

import androidx.annotation.Keep

@Keep
data class InfoPakaiParkirMobilBulanan(
    val carName: String,
    val numberPlate: String,
    val carBrand: String,
    val notes: String?,
    val zonaParkir: InfoZonaParkir
)