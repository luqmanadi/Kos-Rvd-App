package com.kosrvd.app.feature.announcement.data.repository.dto

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

@Keep
data class PengumumanDto(
    @DocumentId
    val idPengumuman: String = "",
    val createdById: String = "",
    val madeBy: String = "",
    val title: String = "",
    val content: String = "",
    val dateCreated: Timestamp = Timestamp.now(),
)