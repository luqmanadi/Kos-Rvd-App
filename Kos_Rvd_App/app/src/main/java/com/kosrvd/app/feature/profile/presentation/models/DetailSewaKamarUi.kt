package com.kosrvd.app.feature.profile.presentation.models

import com.kosrvd.app.core.presentation.utils.calculateTotalBill
import com.kosrvd.app.core.domain.models.AlatElektronik
import com.kosrvd.app.feature.rental.domain.model.InfoPakaiParkirMobilBulanan
import com.kosrvd.app.feature.rental.domain.model.Penyewaan
import com.kosrvd.app.core.presentation.utils.toDayMonthAndYear
import com.kosrvd.app.core.presentation.utils.toNumberRoomFormat
import com.kosrvd.app.core.presentation.utils.toRupiahFormat

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
        totalMonthlyBill = calculateTotalBill(
            if (this.listResident.size == 2 && this.infoKamar.currentRoomRentalCost.twoPersons != null) this.infoKamar.currentRoomRentalCost.twoPersons else this.infoKamar.currentRoomRentalCost.onePerson,
            this.pemakaianParkirMobilBulanan?.zonaParkir?.monthlyFee,
            this.pemakaianAlatElektronikBulanan
        ).toRupiahFormat()
    )
}