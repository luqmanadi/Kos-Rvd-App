package com.kosrvd.app.feature.room.domain.model

import com.kosrvd.app.core.domain.models.AlatElektronik
import com.kosrvd.app.core.domain.models.Harga

data class Kamar(
    val idKamar: String,
    val numberRoom: Int,
    val price: Harga,
    val facility: List<String>,
    val size: String,
    val freeService: List<AlatElektronik>,
    val status: String
)
