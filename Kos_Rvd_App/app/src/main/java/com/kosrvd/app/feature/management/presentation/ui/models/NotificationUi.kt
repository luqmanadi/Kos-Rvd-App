package com.kosrvd.app.feature.management.presentation.ui.models

import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.feature.management.domain.model.Notification
import com.kosrvd.app.feature.management.presentation.designsystem.utils.TypeNotification
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toAnnouncementDateTimeFormatShort

data class NotificationUi(
    val idNotifikasi: String,
    val idAkun: String,
    val idDetailReferensi: String,
    val title: String,
    val content: String,
    val date: String,
    val alreadyRead: Boolean,
    val typeNotification: TypeNotification
)

fun Notification.toNotificationUi(): NotificationUi {
    return NotificationUi(
        idNotifikasi = this.idNotifikasi,
        idAkun = this.idAkun,
        idDetailReferensi = this.idDetailReferensi,
        title = this.title,
        content = this.content,
        date = this.date.toAnnouncementDateTimeFormatShort(),
        alreadyRead = this.alreadyRead,
        typeNotification = typeNotificationStringToTypeNotificationEnum(this.notificationType)
    )
}

private fun typeNotificationStringToTypeNotificationEnum(typeNotification: String): TypeNotification {
    return when(typeNotification){
        Constant.TYPE_NOTIFICATION_TAGIHAN -> TypeNotification.TAGIHAN
        Constant.TYPE_NOTIFICATION_PENGUMUMAN -> TypeNotification.PENGUMUMAN
        Constant.TYPE_NOTIFICATION_LAPORAN_KELUHAN -> TypeNotification.LAPORAN_KELUHAN
        else -> TypeNotification.TAGIHAN
    }
}
