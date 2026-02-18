package com.kosrvd.app.feature.management.presentation.ui.models

import com.google.firebase.Timestamp
import com.kosrvd.app.feature.management.domain.model.Keluhan
import java.util.Date

data class ListKeluhanUi(
    val idKeluhan: String,
    val idAkun: String,
    val reporterName: String,
    val title: String,
    val photoComplaint: String? = null,
    val complaintStatus: String,
    val reportDate: Timestamp,
    val processDate: Timestamp? = null,
    val completionDate: Timestamp? = null
)

data class KeluhanGroupUi(
    val dateTitleGroup: String,
    val groupDate: Date,
    val items: List<ListKeluhanUi>
)

fun Keluhan.toListKeluhanUi(): ListKeluhanUi {
    return ListKeluhanUi(
        idKeluhan = this.idKeluhan,
        idAkun = this.idAkun,
        reporterName = this.reporterName,
        title = this.title,
        photoComplaint = this.photoComplaint,
        complaintStatus = this.complaintStatus,
        reportDate = this.reportDate,
        processDate = this.processDate,
        completionDate = this.completionDate
    )
}
