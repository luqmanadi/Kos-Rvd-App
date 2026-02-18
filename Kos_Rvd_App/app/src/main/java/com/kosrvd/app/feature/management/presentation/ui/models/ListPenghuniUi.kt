package com.kosrvd.app.feature.management.presentation.ui.models

import com.kosrvd.app.core.domain.models.Account
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toRoomFormat

data class ListPenghuniUi(
    val idPenghuni: String,
    val name: String,
    val numberRoom: String,
    val photo: String
)

fun Account.toListPenghuniUi() : ListPenghuniUi{
    return ListPenghuniUi(
        idPenghuni = this.idAkun,
        name = this.name,
        numberRoom = this.dataPenghuni?.numberRoom?.toRoomFormat() ?: "Belum menempati kamar",
        photo = this.photo
    )
}
