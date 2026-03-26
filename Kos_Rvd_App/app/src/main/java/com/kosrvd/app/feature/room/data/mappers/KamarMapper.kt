package com.kosrvd.app.feature.room.data.mappers

import com.kosrvd.app.feature.room.data.repository.dto.KamarDto
import com.kosrvd.app.feature.room.domain.model.Kamar
import com.kosrvd.app.feature.rental.data.mappers.toHarga
import com.kosrvd.app.feature.billing.data.mappers.toAlatElektronik

fun KamarDto.toKamar(): Kamar{
    return Kamar(
        idKamar = this.idKamar,
        numberRoom = this.numberRoom,
        price = this.price.toHarga(),
        facility = this.facility,
        size = this.size,
        freeService = this.freeService.map { it.toAlatElektronik() },
        status = this.status
    )
}

fun List<KamarDto>.toListKamar(): List<Kamar> {
    return this.map { it.toKamar() }
}