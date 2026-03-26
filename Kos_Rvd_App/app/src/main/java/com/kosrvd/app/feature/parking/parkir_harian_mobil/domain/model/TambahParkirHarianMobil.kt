package com.kosrvd.app.feature.parking.parkir_harian_mobil.domain.model

import androidx.annotation.Keep
import com.google.firebase.Timestamp

@Keep
data class TambahParkirHarianMobil(
    val idZonaParkir: String,
    val userName: String,
    val zoneName: String,
    val numberPlate: String,
    val carBrand: String,
    val carName: String,
    val cancelledStatus: Boolean,
    val notes: String?,
    val startDate: Timestamp,
    val completionDate: Timestamp,
    val proofOfPayment: String?,
    val paymentStatus: String,
    val totalCost: Long
)
