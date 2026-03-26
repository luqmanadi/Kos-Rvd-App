package com.kosrvd.app.feature.rental.domain.model

import com.kosrvd.app.core.domain.models.Account
import com.kosrvd.app.feature.room.domain.model.Kamar
import com.kosrvd.app.feature.parking.zona_parkiran_mobil.domain.model.ZonaParkiran

data class InitialBuatPenyewaan(
    val listKamar: List<Kamar>,
    val listPenghuni: List<Account>,
    val listZoneParking: List<ZonaParkiran>
)