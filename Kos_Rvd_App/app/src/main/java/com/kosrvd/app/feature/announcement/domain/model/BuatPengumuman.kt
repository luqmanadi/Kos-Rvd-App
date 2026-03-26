package com.kosrvd.app.feature.announcement.domain.model

import androidx.annotation.Keep
import com.google.firebase.Timestamp

@Keep
data class BuatPengumuman(
    val createdById: String,
    val madeBy: String,
    val title: String,
    val content: String,
    val dateCreated: Timestamp
)
