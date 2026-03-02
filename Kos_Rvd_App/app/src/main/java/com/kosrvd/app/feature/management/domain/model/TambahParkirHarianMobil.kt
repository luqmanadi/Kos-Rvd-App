package com.kosrvd.app.feature.management.domain.model

import com.google.firebase.Timestamp
import com.kosrvd.app.core.data.constant.Constant

data class TambahParkirHarianMobil(
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
    val isCancelled: Boolean
)
