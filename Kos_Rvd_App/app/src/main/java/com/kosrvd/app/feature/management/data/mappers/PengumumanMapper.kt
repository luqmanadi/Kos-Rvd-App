package com.kosrvd.app.feature.management.data.mappers

import com.kosrvd.app.feature.management.data.repository.dto.PengumumanDto
import com.kosrvd.app.feature.management.domain.model.Pengumuman

fun PengumumanDto.toPengumuman(): Pengumuman {
    return Pengumuman(
        idPengumuman = this.idPengumuman,
        createdById = this.createdById,
        madeBy = this.madeBy,
        title = this.title,
        content = this.content,
        dateCreated = this.dateCreated,
    )
}

fun List<PengumumanDto>.toListPengumuman(): List<Pengumuman> {
    return this.map { it.toPengumuman() }
}