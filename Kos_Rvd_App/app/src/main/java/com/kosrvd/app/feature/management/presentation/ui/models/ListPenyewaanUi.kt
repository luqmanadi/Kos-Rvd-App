package com.kosrvd.app.feature.management.presentation.ui.models

import com.kosrvd.app.feature.management.domain.model.Penyewaan

data class ListPenyewaanUi(
    val idPenyewa: String,
    val listNamePenghuni: List<String>,
    val numberRoom: Int,
    val rentalStatus: String
)

fun Penyewaan.toListPenyewaanUi(): ListPenyewaanUi{
    return ListPenyewaanUi(
        idPenyewa = this.idPenyewa,
        listNamePenghuni = this.listResident.map { it.name },
        numberRoom = this.infoKamar.numberRoom,
        rentalStatus = this.rentalStatus
    )
}
