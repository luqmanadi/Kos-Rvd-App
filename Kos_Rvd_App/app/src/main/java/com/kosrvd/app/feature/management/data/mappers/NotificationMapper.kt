package com.kosrvd.app.feature.management.data.mappers

import com.kosrvd.app.feature.management.data.repository.dto.NotificationDto
import com.kosrvd.app.feature.management.domain.model.Notification

fun NotificationDto.toNotification(): Notification {
    return Notification(
        idNotifikasi = this.idNotifikasi,
        idAkun = this.idAkun,
        idDetailReferensi = this.idDetailReferensi,
        title = this.title,
        content = this.content,
        date = this.date,
        alreadyRead = this.alreadyRead,
        notificationType = this.notificationType
    )
}