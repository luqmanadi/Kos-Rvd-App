package com.kosrvd.app.feature.management.domain.repository


import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.feature.management.domain.model.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun getAllNotification(idAkun: String): Flow<Result<List<Notification>, DataError>>

    suspend fun updateNotificationAlreadyRead(idNotifikasi: String): Result<Unit, DataError>
}