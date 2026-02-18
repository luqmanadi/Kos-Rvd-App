package com.kosrvd.app.feature.management.presentation.ui.models

import com.kosrvd.app.feature.management.domain.model.ZonaParkiran

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