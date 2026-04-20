package com.kosrvd.app.feature.rental.domain.model

import androidx.annotation.Keep
import com.kosrvd.app.core.domain.models.Harga

@Keep
data class InfoKamarSewa(
    val idKamar: String,
    val currentRoomRentalCost: Harga,
    val numberRoom: String
)