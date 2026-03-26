package com.kosrvd.app.feature.parking.zona_parkiran_mobil.presentation.models

import com.kosrvd.app.feature.parking.zona_parkiran_mobil.domain.model.ZonaParkiran

data class ListZonaParkiranMobilUi(
    val idZonaParkir: String,
    val zoneName: String,
    val status: String
)

fun ZonaParkiran.toListZonaParkiranMobilUi() : ListZonaParkiranMobilUi {
    return ListZonaParkiranMobilUi(
        idZonaParkir = this.idZonaParkir,
        zoneName = this.zoneName,
        status = this.status
    )
}