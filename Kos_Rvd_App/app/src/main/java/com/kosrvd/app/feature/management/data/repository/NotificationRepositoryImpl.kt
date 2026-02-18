package com.kosrvd.app.feature.management.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.data.networking.safeCall
import com.kosrvd.app.core.data.source.local.SessionStorage
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.core.domain.utils.asCollectionFlow
import com.kosrvd.app.feature.management.data.mappers.toNotification
import com.kosrvd.app.feature.management.data.repository.dto.NotificationDto
import com.kosrvd.app.feature.management.domain.model.Notification
import com.kosrvd.app.feature.management.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    val db: FirebaseFirestore,
    val sessionStorage: SessionStorage
): NotificationRepository {
    override fun getAllNotification(idAkun: String): Flow<Result<List<Notification>, DataError>> {
        return db.collection(Constant.NOTIFIKASI_COLLECTION)
            .whereEqualTo(Constant.ID_ACCOUNT_FIELD, idAkun)
            .orderBy(Constant.DATE_FIELD, Query.Direction.DESCENDING)
            .asCollectionFlow<NotificationDto, Notification>(
                logTag = "Notif Repo",
                mapper = { it.toNotification() }
            )
    }

    override suspend fun updateNotificationAlreadyRead(idNotifikasi: String): Result<Unit, DataError> {
        return safeCall {
            db.collection(Constant.NOTIFIKASI_COLLECTION)
                .document(idNotifikasi)
                .update(Constant.ALREADY_READ_FIELD, true)
                .await()
        }
    }
}