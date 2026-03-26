package com.kosrvd.app.feature.rental.data.repository.dto

import androidx.annotation.Keep
import com.kosrvd.app.core.data.repository.dto.HargaDto

@Keep
data class InfoKamarSewaDto(
    val idKamar: String = "",
    val currentRoomRentalCost: HargaDto = HargaDto(0, null),
    val numberRoom: Int = 0
)
