package com.kosrvd.app.feature.management.data.repository.dto

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

@Keep
data class ParkirHarianMobilDto(
    @DocumentId
    val idParkirHarianMobil: String = "",
    val idZonaParkir: String = "",
    val userName: String = "",
    val zoneName: String = "",
    val numberPlate: String = "",
    val carBrand: String = "",
    val carName: String = "",
    val notes: String? = null,
    val startDate: Timestamp = Timestamp.now(),
    val completionDate: Timestamp = Timestamp.now(),
    val proofOfPayment: String? = null,
    val paymentStatus: String = "",
    val totalCost: Long = 0,
    val isCancelled: Boolean = false
)
