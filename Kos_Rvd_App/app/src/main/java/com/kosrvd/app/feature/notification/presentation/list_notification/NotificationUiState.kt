package com.kosrvd.app.feature.notification.presentation.list_notification

import com.kosrvd.app.feature.notification.presentation.models.NotificationUi

data class NotificationUiState(
    val isLoading: Boolean = true,
    val loadError: String? = null,
    val listNotification: List<NotificationUi> = emptyList()
)
