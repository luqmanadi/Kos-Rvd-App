package com.kosrvd.app.feature.resident.presentation.models

import com.kosrvd.app.core.domain.models.Account
import com.kosrvd.app.core.presentation.utils.toRoomFormat

data class ListResidentUi(
    val idPenghuni: String,
    val name: String,
    val numberRoom: String,
    val photo: String
)

fun Account.toListResidentUi() : ListResidentUi{
    return ListResidentUi(
        idPenghuni = this.idAkun,
        name = this.name,
        numberRoom = this.dataPenghuni?.numberRoom?.toRoomFormat() ?: "Belum menempati kamar",
        photo = this.photo
    )
}
