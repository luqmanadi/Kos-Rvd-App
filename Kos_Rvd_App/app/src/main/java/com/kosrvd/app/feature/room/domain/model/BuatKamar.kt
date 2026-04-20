package com.kosrvd.app.feature.room.domain.model

import androidx.annotation.Keep
import com.kosrvd.app.core.domain.models.AlatElektronik
import com.kosrvd.app.core.domain.models.Harga

@Keep
data class BuatKamar(
    val numberRoom: String,
    val price: Harga,
    val facility: List<String>,
    val size: String,
    val freeService: List<AlatElektronik>,
    val status: String
)
