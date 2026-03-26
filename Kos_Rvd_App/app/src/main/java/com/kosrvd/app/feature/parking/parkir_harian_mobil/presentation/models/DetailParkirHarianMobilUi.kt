package com.kosrvd.app.feature.parking.parkir_harian_mobil.presentation.models

import com.google.firebase.Timestamp
import com.kosrvd.app.feature.parking.parkir_harian_mobil.domain.model.ParkirHarianMobil
import com.kosrvd.app.core.presentation.utils.toDayMonthAndYear
import com.kosrvd.app.core.presentation.utils.toRupiahFormat

data class DetailParkirHarianMobilUi(
    val idParkirHarianMobil: String,
    val idZonaParkir: String,
    val userName: String,
    val zoneName: String,
    val numberPlate: String,
    val carBrand: String,
    val carName: String,
    val notes: String,
    val startDate: String,
    val completionDate: String,
    val proofOfPayment: String?,
    val paymentStatus: String,
    val totalCost: String,
    val statusParkir: String,
    val cancelledStatus: Boolean
)

fun ParkirHarianMobil.toDetailParkirHarianMobilUi(): DetailParkirHarianMobilUi {
    return DetailParkirHarianMobilUi(
        idParkirHarianMobil = this.idParkirHarianMobil,
        idZonaParkir = this.idZonaParkir,
        userName = this.userName,
        zoneName = this.zoneName,
        numberPlate = this.numberPlate,
        carBrand = this.carBrand,
        carName = this.carName,
        notes = this.notes ?: "Tidak ada catatan",
        startDate = this.startDate.toDayMonthAndYear(),
        completionDate = this.completionDate.toDayMonthAndYear(),
        proofOfPayment = this.proofOfPayment,
        paymentStatus = this.paymentStatus,
        totalCost = this.totalCost.toRupiahFormat(),
        statusParkir = determineStatus(
            cancelledStatus = this.cancelledStatus,
            startDate = this.startDate,
            completionDate = this.completionDate
        ),
        cancelledStatus = this.cancelledStatus
    )
}

private fun determineStatus(cancelledStatus: Boolean, startDate: Timestamp, completionDate: Timestamp): String {
    val now = Timestamp.now()
    return when {
        cancelledStatus -> "Dibatalkan"
        now < startDate -> "Dipesan"
        now <= completionDate -> "Dipakai"
        else -> "Selesai"
    }
}
