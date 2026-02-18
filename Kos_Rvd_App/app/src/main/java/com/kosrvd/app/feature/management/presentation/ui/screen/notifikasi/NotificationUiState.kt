package com.kosrvd.app.feature.management.presentation.ui.screen.notifikasi

import com.kosrvd.app.feature.management.presentation.ui.models.NotificationUi

data class NotificationUiState(
    val isLoading: Boolean = true,
    val loadError: String? = null,
    val listNotification: List<NotificationUi> = emptyList()
)
