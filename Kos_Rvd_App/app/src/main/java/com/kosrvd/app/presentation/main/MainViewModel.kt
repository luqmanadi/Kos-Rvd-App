package com.kosrvd.app.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.domain.usecase.CheckSessionUseCase
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.core.domain.utils.onSuccess
import com.kosrvd.app.feature.notification.domain.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface DeepLinkDestination {
    data class DetailTagihan(val idTagihan: String) : DeepLinkDestination
    data class DetailKeluhan(val idKeluhan: String) : DeepLinkDestination
    data object Pengumuman : DeepLinkDestination
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val checkSessionUseCase: CheckSessionUseCase,
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MainUiState())
    val state = _state.asStateFlow()

    private val _pendingDeepLink = MutableStateFlow<DeepLinkDestination?>(null)
    val pendingDeepLink = _pendingDeepLink.asStateFlow()

    init {
        observeAuthState()
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            // Splash Screen Berjalan
            _state.update { it.copy(isCheckingAuth = true) }
            checkSessionUseCase()
                .onSuccess {result ->
                    if (result){
                        Log.d("MainViewModel", "observeAuthState: sesi aktif")
                        _state.update {
                            it.copy(
                                isCheckingAuth = false,
                                isAuthenticated = true
                            )
                        }
                    } else {
                        Log.d("MainViewModel", "observeAuthState: sesi berakhir")
                        _state.update {
                            it.copy(
                                isCheckingAuth = false,
                                isAuthenticated = false
                            )
                        }
                    }
                }
                .onError {result ->
                    Log.d("MainViewModel", "observeAuthState: ${result.message}")
                    _state.update {
                        it.copy(
                            isCheckingAuth = false,
                            isAuthenticated = false
                        )
                    }
                }
        }
    }

    fun handleDeepLink(type: String?, refId: String?, notificationId: String?) {

        if (!notificationId.isNullOrEmpty()){
            viewModelScope.launch {
                notificationRepository.updateNotificationAlreadyRead(notificationId)
                    .onSuccess {
                        Log.d("MainViewModel", "handleDeepLink Update Notification Already Read : notif sudah dibaca")
                    }
                    .onError {
                        Log.d("MainViewModel", "handleDeepLink Update Notification Already Read : ${it.message}")
                    }
            }
        }

        if (type != null) {
            val destination = when(type) {
                Constant.TYPE_NOTIFICATION_TAGIHAN -> refId?.let { DeepLinkDestination.DetailTagihan(it) }
                Constant.TYPE_NOTIFICATION_LAPORAN_KELUHAN -> refId?.let { DeepLinkDestination.DetailKeluhan(it) } // Sesuaikan string dgn backend
                Constant.TYPE_NOTIFICATION_PENGUMUMAN -> DeepLinkDestination.Pengumuman
                else -> null
            }
            if (destination != null) {
                _pendingDeepLink.value = destination
            }
        }
    }

    // 3. Fungsi untuk mereset state setelah navigasi berhasil dilakukan
    fun onDeepLinkConsumed() {
        _pendingDeepLink.value = null
    }
}