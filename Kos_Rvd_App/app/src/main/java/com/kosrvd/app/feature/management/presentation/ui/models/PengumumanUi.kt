package com.kosrvd.app.feature.management.presentation.ui.models

import com.kosrvd.app.feature.management.domain.model.Pengumuman
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toAnnouncementDateTimeFormat

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