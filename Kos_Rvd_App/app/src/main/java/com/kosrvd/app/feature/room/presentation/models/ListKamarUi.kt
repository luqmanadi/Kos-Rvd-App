package com.kosrvd.app.feature.room.presentation.models

import com.kosrvd.app.core.presentation.utils.toNumberRoomFormat
import com.kosrvd.app.feature.room.domain.model.Kamar

data class ListKamarUi(
    val idKamar: String,
    val numberRoom: String,
    val status: String
)

fun Kamar.toListKamarUi(): ListKamarUi{
    return ListKamarUi(
        idKamar = this.idKamar,
        numberRoom = this.numberRoom.toNumberRoomFormat(),
        status = this.status
    )
}