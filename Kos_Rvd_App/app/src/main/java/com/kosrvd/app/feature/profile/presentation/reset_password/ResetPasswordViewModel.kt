package com.kosrvd.app.feature.profile.presentation.reset_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosrvd.app.core.domain.repository.AuthRepository
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.core.domain.utils.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ResetPasswordEvents {
    data object NavigateBack: ResetPasswordEvents
    data class NavigateToEditDataProfileChangePassword(val idAkun: String): ResetPasswordEvents
    data class ShowSnackBar(val message: String): ResetPasswordEvents

}

sealed interface ResetPasswordActions {
    data object NavigateBack: ResetPasswordActions
    data object SendEmailReset: ResetPasswordActions
    data object NavigateToEditDataProfileChangePassword: ResetPasswordActions
}

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {
    private val _state = MutableStateFlow(ResetPasswordUiState())
    val state = _state.asStateFlow()

    private val _events = Channel<ResetPasswordEvents>()
    val events = _events.receiveAsFlow()

    fun onActions(actions: ResetPasswordActions){
        when(actions){
            ResetPasswordActions.NavigateBack -> navigateBack()
            ResetPasswordActions.SendEmailReset -> sendEmailReset()
            ResetPasswordActions.NavigateToEditDataProfileChangePassword -> navigateToEditDataProfileChangePassword()
        }
    }

    private fun navigateToEditDataProfileChangePassword() {
        viewModelScope.launch {
            val idAkun = authRepository.currentUser?.uid
            if (idAkun != null){
                _events.send(ResetPasswordEvents.NavigateToEditDataProfileChangePassword(idAkun))
            }else{
                _events.send(ResetPasswordEvents.ShowSnackBar("Id akun tidak ditemukan, silahkan login kembali"))
            }

        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(ResetPasswordEvents.NavigateBack)
        }
    }

    private fun sendEmailReset() {
        viewModelScope.launch {
            _state.update {  it.copy(isShowLoading = true) }

            val email = authRepository.currentUser?.email
            if (email != null) {
                authRepository.forgotPassword(email)
                    .onSuccess {
                        _state.update { it.copy(isShowLoading = false) }
                        _events.send(ResetPasswordEvents.ShowSnackBar("Email Reset Password berhasil dikirim"))
                    }
                    .onError { result ->
                        _state.update { it.copy(isShowLoading = false) }
                        _events.send(ResetPasswordEvents.ShowSnackBar(result.message))
                    }
            } else {
                _state.update { it.copy(isShowLoading = false) }
                _events.send(ResetPasswordEvents.ShowSnackBar("Email tidak ditemukan, silahkan login kembali"))
            }

        }
    }
}