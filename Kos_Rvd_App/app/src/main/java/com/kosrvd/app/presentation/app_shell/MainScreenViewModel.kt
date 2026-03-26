package com.kosrvd.app.presentation.app_shell

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosrvd.app.core.data.source.local.SessionStorage
import com.kosrvd.app.core.domain.repository.AccountRepository
import com.kosrvd.app.core.domain.repository.AuthRepository
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.presentation.app_shell.models.MainScreenUi
import com.kosrvd.app.presentation.app_shell.models.toMainScreenUi
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

sealed interface MainScreenEvent {
    data object NavigateToAuthGraph : MainScreenEvent
    data object NavigateToNotificationScreen : MainScreenEvent
    data object NavigateToCreateTagihan: MainScreenEvent
    data object NavigateToCreateKeluhan: MainScreenEvent
    data object NavigateToPengaturanScreen: MainScreenEvent
}
sealed interface MainScreenActions{
    data object NavigateToPengaturanScreen: MainScreenActions
    data object NavigateToCreateTagihan: MainScreenActions
    data object NavigateToNotificationScreen: MainScreenActions
    data object NavigateToCreateKeluhan: MainScreenActions
}
@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionStorage: SessionStorage,
    private val accountRepository: AccountRepository
): ViewModel() {
    private val _state = MutableStateFlow(MainScreenUiState())
    val state = _state
        .onStart {
            getNumberOfUnreadNotification()
            authState()
            getRole()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MainScreenUiState()
        )

    private val _events = Channel<MainScreenEvent>()
    val events = _events.receiveAsFlow()

    fun onActions(actions: MainScreenActions){
        when(actions) {
            MainScreenActions.NavigateToNotificationScreen -> navigateToNotificationScreen()
            MainScreenActions.NavigateToCreateKeluhan -> navigateCreateKeluhan()
            MainScreenActions.NavigateToCreateTagihan -> navigateCreateTagihan()
            MainScreenActions.NavigateToPengaturanScreen -> navigateToPengaturanScreen()
        }
    }

    private fun navigateToPengaturanScreen() {
        viewModelScope.launch {
            _events.send(MainScreenEvent.NavigateToPengaturanScreen)
        }
    }

    private fun getRole(){
        viewModelScope.launch {
            val role = sessionStorage.getAuthInfo().role
            _state.update { it.copy(userRole = role) }
        }
    }

    private fun navigateCreateKeluhan() {
        viewModelScope.launch {
            _events.send(MainScreenEvent.NavigateToCreateKeluhan)
        }
    }

    private fun navigateCreateTagihan() {
        viewModelScope.launch {
            _events.send(MainScreenEvent.NavigateToCreateTagihan)
        }
    }

    private fun navigateToNotificationScreen() {
        viewModelScope.launch {
            _events.send(MainScreenEvent.NavigateToNotificationScreen)
        }
    }

    private fun getNumberOfUnreadNotification() {
        viewModelScope.launch {
            _state.update { it.copy(isLoadingTopBarDashboard = true) }
            val idAkun = sessionStorage.getAuthInfo().idAkun
            accountRepository.getAccountByIdWithFlow(idAkun).collect { result ->
                when(result){
                    is Result.Success -> {
                        _state.update {
                            it.copy(
                                mainScreenUi = result.data.toMainScreenUi(),
                                isLoadingTopBarDashboard = false
                            )
                        }
                    }
                    is Result.Error -> {
                        _state.update {
                            it.copy(
                                isLoadingTopBarDashboard = false,
                                mainScreenUi = MainScreenUi()
                            )
                        }
                        Log.e("MainScreenViewModel", "getDataTopBarMainScreenUseCase: ${result.error.message}")
                    }
                }
            }
        }
    }

    private fun authState() {
        viewModelScope.launch {
            authRepository.getAuthState().collect { state ->
                if (state) {
                    _events.send(MainScreenEvent.NavigateToAuthGraph)
                }
            }
        }
    }
}