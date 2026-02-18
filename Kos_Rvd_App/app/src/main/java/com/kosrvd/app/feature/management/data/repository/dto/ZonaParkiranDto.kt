package com.kosrvd.app.feature.management.data.repository.dto

import androidx.annotation.Keep
import com.google.firebase.firestore.DocumentId

@Keep
data class ZonaParkiranDto(
    @DocumentId
    val idZonaParkir: String = "",
    val zoneName: String = "",
    val monthlyFee: Long = 0,
    val dailyCosts: Long = 0,
    val status: String = ""
)
