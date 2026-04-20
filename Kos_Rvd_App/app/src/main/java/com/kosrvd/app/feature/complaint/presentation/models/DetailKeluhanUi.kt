package com.kosrvd.app.feature.complaint.presentation.models

import com.kosrvd.app.feature.complaint.domain.model.Keluhan
import com.kosrvd.app.core.presentation.utils.toDayMonthAndYear

data class DetailKeluhanUi(
    val complaintStatus: String,
    val completionDate: String?,
    val description: String,
    val idKeluhan: String,
    val idAkun: String,
    val numberRoom: String,
    val photoComplaint: String?,
    val photoResponse: String?,
    val processDate: String?,
    val reportDate: String,
    val reporterName: String,
    val response: String?,
    val title: String
)

fun Keluhan.toDetailKeluhanUi(): DetailKeluhanUi{
    return DetailKeluhanUi(
        complaintStatus = this.complaintStatus,
        completionDate = this.completionDate?.toDayMonthAndYear(),
        description = this.description,
        idKeluhan = this.idKeluhan,
        idAkun = this.idAkun,
        numberRoom = this.numberRoom ?: "",
        photoComplaint = this.photoComplaint,
        photoResponse = this.photoResponse,
        processDate = this.processDate?.toDayMonthAndYear(),
        reportDate = this.reportDate.toDayMonthAndYear(),
        reporterName = this.reporterName,
        response = this.response,
        title = this.title
    )
}
