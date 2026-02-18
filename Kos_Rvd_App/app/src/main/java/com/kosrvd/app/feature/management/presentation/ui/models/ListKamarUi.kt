package com.kosrvd.app.feature.management.presentation.ui.models

import com.kosrvd.app.feature.management.domain.model.Kamar
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toNumberRoomFormat

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