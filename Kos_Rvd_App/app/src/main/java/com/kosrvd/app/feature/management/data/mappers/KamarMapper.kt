package com.kosrvd.app.feature.management.data.mappers

import com.kosrvd.app.feature.management.data.repository.dto.KamarDto
import com.kosrvd.app.feature.management.domain.model.Kamar

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