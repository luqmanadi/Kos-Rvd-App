package com.kosrvd.app.feature.management.domain.model

data class InfoPakaiParkirMobilBulanan(
    val carName: String,
    val numberPlate: String,
    val carBrand: String,
    val notes: String?,
    val zonaParkir: InfoZonaParkir
)
