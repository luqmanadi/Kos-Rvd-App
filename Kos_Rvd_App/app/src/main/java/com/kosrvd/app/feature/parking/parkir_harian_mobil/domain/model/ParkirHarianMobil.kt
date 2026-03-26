package com.kosrvd.app.feature.parking.parkir_harian_mobil.domain.model

import com.google.firebase.Timestamp

data class ParkirHarianMobil(
    val idParkirHarianMobil: String,
    val idZonaParkir: String,
    val userName: String,
    val zoneName: String,
    val numberPlate: String,
    val carBrand: String,
    val carName: String,
    val notes: String?,
    val startDate: Timestamp,
    val completionDate: Timestamp,
    val proofOfPayment: String?,
    val paymentStatus: String,
    val totalCost: Long,
    val cancelledStatus: Boolean
)
