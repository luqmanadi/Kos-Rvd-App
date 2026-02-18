package com.kosrvd.app.feature.management.data.mappers

import com.kosrvd.app.feature.management.data.repository.dto.ParkirHarianMobilDto
import com.kosrvd.app.feature.management.domain.model.ParkirHarianMobil

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
        isCancelled = this.isCancelled
    )
}

fun List<ParkirHarianMobilDto>.toListOfParkirHarianMobil(): List<ParkirHarianMobil> {
    return this.map { it.toParkirHarianMobil() }
}