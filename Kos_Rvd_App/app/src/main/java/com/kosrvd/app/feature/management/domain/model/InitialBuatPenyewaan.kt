package com.kosrvd.app.feature.management.domain.model

import com.kosrvd.app.core.domain.models.Account

data class InitialBuatPenyewaan(
    val listKamar: List<Kamar>,
    val listPenghuni: List<Account>,
    val listZoneParking: List<ZonaParkiran>
)
