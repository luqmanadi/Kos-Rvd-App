package com.kosrvd.app.feature.management.data.repository.dto

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId


@Keep
data class NotificationDto(
    @DocumentId
    val idNotifikasi: String = "",
    val idAkun: String = "",
    val idDetailReferensi: String = "",
    val title: String = "",
    val content: String = "",
    val date: Timestamp = Timestamp.now(),
    val alreadyRead: Boolean = false,
    val notificationType: String = ""
)
