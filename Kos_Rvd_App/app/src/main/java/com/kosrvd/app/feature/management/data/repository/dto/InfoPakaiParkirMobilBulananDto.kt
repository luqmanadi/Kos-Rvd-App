package com.kosrvd.app.feature.management.data.repository.dto

import androidx.annotation.Keep

@Keep
data class InfoPakaiParkirMobilBulananDto(
    val carName: String = "",
    val numberPlate: String = "",
    val carBrand: String = "",
    val notes: String? = null,
    val zonaParkir: InfoZonaParkirDto = InfoZonaParkirDto()
)
