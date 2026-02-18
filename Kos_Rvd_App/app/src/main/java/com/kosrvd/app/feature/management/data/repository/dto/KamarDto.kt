package com.kosrvd.app.feature.management.data.repository.dto

import androidx.annotation.Keep
import com.google.firebase.firestore.DocumentId


@Keep
data class KamarDto(
    @DocumentId
    val idKamar: String = "",
    val numberRoom: Int = 0,
    val price: HargaDto = HargaDto(0, null),
    val facility: List<String> = emptyList(),
    val size: String = "",
    val freeService: List<AlatElektronikDto> = emptyList(),
    val status: String = ""
)