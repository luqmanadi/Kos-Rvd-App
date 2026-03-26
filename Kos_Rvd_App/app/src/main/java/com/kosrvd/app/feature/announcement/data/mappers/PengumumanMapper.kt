package com.kosrvd.app.feature.announcement.data.mappers

import com.kosrvd.app.feature.announcement.data.repository.dto.PengumumanDto
import com.kosrvd.app.feature.announcement.domain.model.Pengumuman

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