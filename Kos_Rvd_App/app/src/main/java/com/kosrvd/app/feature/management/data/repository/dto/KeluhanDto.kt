package com.kosrvd.app.feature.management.data.repository.dto

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId


@Keep
data class KeluhanDto(
    @DocumentId
    val idKeluhan: String = "",
    val idAkun: String = "",
    val reporterName: String = "",
    val numberRoom: Int? = null,
    val title: String = "",
    val description: String = "",
    val photoComplaint: String? = null,
    val complaintStatus: String = "",
    val reportDate: Timestamp = Timestamp.now(),
    val processDate: Timestamp? = null,
    val completionDate: Timestamp? = null,
    val response: String? = null,
    val photoResponse: String? = null,
)
