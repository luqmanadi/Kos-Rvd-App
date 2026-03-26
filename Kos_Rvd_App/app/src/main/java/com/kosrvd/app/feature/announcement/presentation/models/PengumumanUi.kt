package com.kosrvd.app.feature.announcement.presentation.models

import com.kosrvd.app.feature.announcement.domain.model.Pengumuman
import com.kosrvd.app.core.presentation.utils.toAnnouncementDateTimeFormat

data class PengumumanUi(
    val idPengumuman: String,
    val createdById: String,
    val madeBy: String,
    val title: String,
    val content: String,
    val dateCreated: String,
)

fun Pengumuman.toPengumumanUi(): PengumumanUi{
    return PengumumanUi(
        idPengumuman = this.idPengumuman,
        createdById = this.createdById,
        madeBy = this.madeBy,
        title = this.title,
        content = this.content,
        dateCreated = this.dateCreated.toAnnouncementDateTimeFormat()
    )
}