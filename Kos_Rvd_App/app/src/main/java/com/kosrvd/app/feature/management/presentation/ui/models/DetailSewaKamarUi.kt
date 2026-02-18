package com.kosrvd.app.feature.management.presentation.ui.models

import com.kosrvd.app.feature.management.domain.model.AlatElektronik
import com.kosrvd.app.feature.management.domain.model.InfoPakaiParkirMobilBulanan
import com.kosrvd.app.feature.management.domain.model.Penyewaan
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toDayMonthAndYear
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toNumberRoomFormat
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toRupiahFormat

data class DetailSewaKamarUi(
    val listResident: List<String>,
    val numberRoom: String,
    val rentalStartDate: String,
    val rentalStatus: String,
    val currentRoomRentalCost: String,
    val pemakaianAlatElektronik: List<AlatElektronik>,
    val pemakaianParkirMobil: InfoPakaiParkirMobilBulanan?,
    val totalMonthlyBill: String
)


fun Penyewaan.toDetailSewaKamarUi(): DetailSewaKamarUi {
    return DetailSewaKamarUi(
        listResident = this.listResident.map { it.name },
        numberRoom = this.infoKamar.numberRoom.toNumberRoomFormat(),
        rentalStartDate = this.rentalStartDate.toDayMonthAndYear(),
        rentalStatus = this.rentalStatus,
        currentRoomRentalCost = if (this.listResident.size > 1 && this.infoKamar.currentRoomRentalCost.twoPersons != null ) this.infoKamar.currentRoomRentalCost.twoPersons.toRupiahFormat() else this.infoKamar.currentRoomRentalCost.onePerson.toRupiahFormat(),
        pemakaianAlatElektronik = this.pemakaianAlatElektronikBulanan,
        pemakaianParkirMobil = this.pemakaianParkirMobilBulanan,
        totalMonthlyBill = this.totalMonthlyBill.toRupiahFormat()
    )
}