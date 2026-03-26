package com.kosrvd.app.feature.room.data.repository.dto

import androidx.annotation.Keep
import com.google.firebase.firestore.DocumentId
import com.kosrvd.app.core.data.repository.dto.AlatElektronikDto
import com.kosrvd.app.core.data.repository.dto.HargaDto

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