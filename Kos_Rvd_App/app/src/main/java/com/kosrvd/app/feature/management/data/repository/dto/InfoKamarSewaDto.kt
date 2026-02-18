package com.kosrvd.app.feature.management.data.repository.dto

import androidx.annotation.Keep

@Keep
data class InfoKamarSewaDto(
    val idKamar: String = "",
    val currentRoomRentalCost: HargaDto = HargaDto(0, null),
    val numberRoom: Int = 0
)
