package com.kosrvd.app.feature.notification.presentation.list_notification

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosrvd.app.core.data.source.local.SessionStorage
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.core.domain.utils.onSuccess
import com.kosrvd.app.feature.notification.domain.repository.NotificationRepository
import com.kosrvd.app.feature.notification.domain.utils.TypeNotification
import com.kosrvd.app.feature.notification.presentation.models.NotificationUi
import com.kosrvd.app.feature.notification.presentation.models.toNotificationUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface NotificationEvents {
    data class NavigateToDetailNotification (val typeNotification: TypeNotification, val idDetailReferensi: String): NotificationEvents
    data object NavigateBack: NotificationEvents
}

sealed interface NotificationActions {
    data class NavigateToDetailNotification (val notification: NotificationUi): NotificationActions
    data object TryAgain: NotificationActions
    data object NavigateBack: NotificationActions
}

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val sessionStorage: SessionStorage
): ViewModel() {
    private val _state = MutableStateFlow(NotificationUiState())
    val state= _state
        .onStart { loadNotification() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NotificationUiState()
        )

    private val _events = Channel<NotificationEvents>()
    val events = _events.receiveAsFlow()


    fun onActions(actions: NotificationActions){
        when(actions){
            is NotificationActions.NavigateToDetailNotification -> navigateToDetailNotification(actions.notification)
            NotificationActions.TryAgain -> loadNotification()
            NotificationActions.NavigateBack -> navigateBack()
        }
    }

    private fun navigateToDetailNotification(
        notification: NotificationUi
    ) {
        viewModelScope.launch {
            if (!notification.alreadyRead){
                notificationRepository.updateNotificationAlreadyRead(notification.idNotifikasi)
                    .onSuccess {
                        Log.d("NotificationViewModel", "navigateToDetailNotification: notif sudah diupdate ke dibaca")
                    }
                    .onError {
                        Log.e("NotificationViewModel", "navigateToDetailNotification: ${it.message}")
                    }
            }

            _events.send(NotificationEvents.NavigateToDetailNotification(notification.typeNotification, notification.idDetailReferensi))
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(NotificationEvents.NavigateBack)
        }
    }

    private fun loadNotification() {
        viewModelScope.launch {
            _state.update { it.copy(
                isLoading = true,
                loadError = null)
            }

            val idAkun = sessionStorage.getAuthInfo().idAkun

            notificationRepository.getAllNotification(idAkun).collect { result ->
                when(result){
                    is Result.Success -> {
                        _state.update { notificationUiState ->
                            notificationUiState.copy(
                                isLoading = false,
                                loadError = null,
                                listNotification = result.data.map { it.toNotificationUi() }
                            )
                        }
                    }
                    is Result.Error -> {
                        _state.update { notificationUiState ->
                            notificationUiState.copy(
                                isLoading = false,
                                loadError = result.error.message,
                                listNotification = emptyList()
                            )
                        }
                    }
                }
            }
        }
    }
}