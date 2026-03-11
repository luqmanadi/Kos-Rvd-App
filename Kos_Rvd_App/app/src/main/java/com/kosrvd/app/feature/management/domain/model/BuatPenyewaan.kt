package com.kosrvd.app.feature.management.domain.model

import com.google.firebase.Timestamp

data class BuatPenyewaan(
    val listResident: List<InfoPenghuni>,
    val infoKamar: InfoKamarSewa,
    val pemakaianAlatElektronikBulanan: List<AlatElektronik>,
    val pemakaianParkirMobilBulanan: InfoPakaiParkirMobilBulanan?,
    val rentalStartDate: Timestamp,
    val rentalCompletionDate: Timestamp?,
    val rentalStatus: String
)
