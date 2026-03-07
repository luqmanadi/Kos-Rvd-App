package com.kosrvd.app.feature.management.presentation.ui.models

import com.google.firebase.Timestamp
import com.kosrvd.app.core.presentation.utils.calculateTotalBill
import com.kosrvd.app.feature.management.domain.model.AlatElektronik
import com.kosrvd.app.feature.management.domain.model.InfoKamarSewa
import com.kosrvd.app.feature.management.domain.model.InfoPakaiParkirMobilBulanan
import com.kosrvd.app.feature.management.domain.model.InfoPenghuni
import com.kosrvd.app.feature.management.domain.model.Penyewaan
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toRupiahFormat

data class PenyewaUi(
    val idPenyewa: String,
    val listResident: List<InfoPenghuni>,
    val infoKamar: InfoKamarSewa,
    val pemakaianAlatElektronikBulanan: List<AlatElektronik>,
    val pemakaianParkirMobilBulanan: InfoPakaiParkirMobilBulanan?,
    val rentalStartDate: Timestamp,
    val rentalCompletionDate: Timestamp?,
    val rentalStatus: String,
    val totalMonthlyBill: String
)

fun Penyewaan.toPenyewaUi(): PenyewaUi{
    return PenyewaUi(
        idPenyewa = this.idPenyewa,
        listResident = this.listResident,
        infoKamar = this.infoKamar,
        pemakaianAlatElektronikBulanan = this.pemakaianAlatElektronikBulanan,
        pemakaianParkirMobilBulanan = this.pemakaianParkirMobilBulanan,
        rentalStartDate = this.rentalStartDate,
        rentalCompletionDate = this.rentalCompletionDate,
        rentalStatus = this.rentalStatus,
        totalMonthlyBill = calculateTotalBill(
            this.infoKamar.currentRoomRentalCost.onePerson,
            this.pemakaianParkirMobilBulanan?.zonaParkir?.monthlyFee,
            this.pemakaianAlatElektronikBulanan
        ).toRupiahFormat()
    )
}
