package com.kosrvd.app.feature.notification.domain.model

import com.google.firebase.Timestamp

data class Notification(
    val idNotifikasi: String,
    val idAkun: String,
    val idDetailReferensi: String,
    val title: String,
    val content: String,
    val date: Timestamp,
    val alreadyRead: Boolean,
    val notificationType: String
)