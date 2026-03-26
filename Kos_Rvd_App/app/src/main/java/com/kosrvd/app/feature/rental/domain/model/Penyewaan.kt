package com.kosrvd.app.feature.rental.domain.model

import com.google.firebase.Timestamp
import com.kosrvd.app.core.domain.models.AlatElektronik

data class Penyewaan(
    val idPenyewa: String,
    val listResident: List<InfoPenghuni>,
    val infoKamar: InfoKamarSewa,
    val pemakaianAlatElektronikBulanan: List<AlatElektronik>,
    val pemakaianParkirMobilBulanan: InfoPakaiParkirMobilBulanan?,
    val rentalStartDate: Timestamp,
    val rentalCompletionDate: Timestamp?,
    val rentalStatus: String
)
