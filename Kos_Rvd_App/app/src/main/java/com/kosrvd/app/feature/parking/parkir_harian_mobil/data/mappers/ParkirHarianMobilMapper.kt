package com.kosrvd.app.feature.parking.parkir_harian_mobil.data.mappers

import com.kosrvd.app.feature.parking.parkir_harian_mobil.data.repository.dto.ParkirHarianMobilDto
import com.kosrvd.app.feature.parking.parkir_harian_mobil.domain.model.ParkirHarianMobil

fun ParkirHarianMobilDto.toParkirHarianMobil(): ParkirHarianMobil {
    return ParkirHarianMobil(
        idParkirHarianMobil = this.idParkirHarianMobil,
        idZonaParkir = this.idZonaParkir,
        userName = this.userName,
        zoneName = this.zoneName,
        numberPlate = this.numberPlate,
        carBrand = this.carBrand,
        carName = this.carName,
        notes = this.notes,
        startDate = this.startDate,
        completionDate = this.completionDate,
        proofOfPayment = this.proofOfPayment,
        paymentStatus = this.paymentStatus,
        totalCost = this.totalCost,
        cancelledStatus = this.cancelledStatus
    )
}

fun List<ParkirHarianMobilDto>.toListOfParkirHarianMobil(): List<ParkirHarianMobil> {
    return this.map { it.toParkirHarianMobil() }
}