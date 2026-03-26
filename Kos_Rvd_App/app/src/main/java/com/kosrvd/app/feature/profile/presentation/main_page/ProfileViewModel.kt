package com.kosrvd.app.feature.profile.presentation.main_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.core.domain.utils.onSuccess
import com.kosrvd.app.feature.profile.domain.usecase.GetProfileUseCase
import com.kosrvd.app.feature.auth.domain.LogOutUseCase
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

sealed interface ProfileEvent {
    data object NavigateToResetPassword : ProfileEvent
    data object NavigateToDetailAkun : ProfileEvent
    data class ShowErrorBanner(val message: String): ProfileEvent
}

sealed interface ProfileActions {
    data object NavigateToResetPassword : ProfileActions
    data object NavigateToDetailAkun : ProfileActions
    data object LogOut : ProfileActions
    data object TryAgain : ProfileActions
    data object ShowLogoutDialog: ProfileActions
    data object DismissLogoutDialog: ProfileActions
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val logOutUseCase: LogOutUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileUiState())
    val state = _state
        .onStart {
            loadData()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProfileUiState()
        )

    private val _events = Channel<ProfileEvent>()
    val events = _events.receiveAsFlow()

    fun onEvent(event: ProfileActions) {
        when (event) {
            is ProfileActions.NavigateToResetPassword -> navigateToResetPassword()
            is ProfileActions.NavigateToDetailAkun -> navigateToDetailAkun()
            is ProfileActions.TryAgain -> loadData()
            is ProfileActions.LogOut -> logOut()
            is ProfileActions.ShowLogoutDialog -> _state.update { it.copy(isLogoutDialogVisible = true) }
            is ProfileActions.DismissLogoutDialog -> _state.update { it.copy(isLogoutDialogVisible = false) }
        }
    }

    private fun logOut() {
        viewModelScope.launch {
            _state.update { it.copy(buttonLoading = true, loadError = null ) }
            logOutUseCase.invoke()
                .onSuccess {
                    _state.update { it.copy(buttonLoading = false, loadError = null, isLogoutDialogVisible = false) }
                }
                .onError { result ->
                    _state.update {
                        it.copy(
                            buttonLoading = false,
                            isLogoutDialogVisible = false
                        )
                    }
                    _events.send(ProfileEvent.ShowErrorBanner(result.message))
                }
        }
    }

    private fun navigateToDetailAkun() {
        viewModelScope.launch {
            _events.send(ProfileEvent.NavigateToDetailAkun)
        }
    }

    private fun navigateToResetPassword() {
        viewModelScope.launch {
            _events.send(ProfileEvent.NavigateToResetPassword)
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, loadError = null) }
            getProfileUseCase.invoke()
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            loading = false,
                            profileUi = result,
                            loadError = null
                        )
                    }
                }
                .onError { result ->
                    _state.update {
                        it.copy(
                            loading = false,
                            loadError = result.message
                        )
                    }
                }
        }
    }
}