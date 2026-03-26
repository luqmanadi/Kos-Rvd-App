package com.kosrvd.app.feature.rental.domain.model

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.kosrvd.app.core.domain.models.AlatElektronik

@Keep
data class BuatPenyewaan(
    val listResident: List<InfoPenghuni>,
    val infoKamar: InfoKamarSewa,
    val pemakaianAlatElektronikBulanan: List<AlatElektronik>,
    val pemakaianParkirMobilBulanan: InfoPakaiParkirMobilBulanan?,
    val rentalStartDate: Timestamp,
    val rentalCompletionDate: Timestamp?,
    val rentalStatus: String
)
