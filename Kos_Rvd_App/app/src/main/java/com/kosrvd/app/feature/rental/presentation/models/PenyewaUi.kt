package com.kosrvd.app.feature.rental.presentation.models

import com.google.firebase.Timestamp
import com.kosrvd.app.core.presentation.utils.calculateTotalBill
import com.kosrvd.app.core.domain.models.AlatElektronik
import com.kosrvd.app.feature.rental.domain.model.InfoKamarSewa
import com.kosrvd.app.feature.rental.domain.model.InfoPakaiParkirMobilBulanan
import com.kosrvd.app.feature.rental.domain.model.InfoPenghuni
import com.kosrvd.app.feature.rental.domain.model.Penyewaan
import com.kosrvd.app.core.presentation.utils.toRupiahFormat

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
    val hargaSewaKamar = if (this.listResident.size == 2 && this.infoKamar.currentRoomRentalCost.twoPersons != null){
        this.infoKamar.currentRoomRentalCost.twoPersons
    } else this.infoKamar.currentRoomRentalCost.onePerson
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
            hargaSewaKamar = hargaSewaKamar,
            hargaSewaParkirMobil = this.pemakaianParkirMobilBulanan?.zonaParkir?.monthlyFee,
            hargaPemakaianElektronik = this.pemakaianAlatElektronikBulanan
        ).toRupiahFormat()
    )
}
