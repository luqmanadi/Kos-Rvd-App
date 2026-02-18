package com.kosrvd.app.feature.auth.presentation.ui.screen.forgot_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosrvd.app.core.domain.repository.AuthRepository
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.core.domain.utils.onSuccess
import com.kosrvd.app.core.presentation.utils.PatternValidation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ForgotPasswordEvent{
    data class ShowToast(val message: String): ForgotPasswordEvent
    data object NavigateUp: ForgotPasswordEvent
}

sealed interface ForgotPasswordAction{
    data object ForgotPassword: ForgotPasswordAction
    data object NavigateUp: ForgotPasswordAction
    data class OnUpdateEmail(val email: String): ForgotPasswordAction
}

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {
    private val _state = MutableStateFlow(ForgotPasswordUiState())
    val state = _state.asStateFlow()

    private val _events = Channel<ForgotPasswordEvent>()
    val events = _events.receiveAsFlow()

    private var initialCheckError = false

    fun onActions(forgotPasswordActions: ForgotPasswordAction) {
        when (forgotPasswordActions) {
            ForgotPasswordAction.NavigateUp -> navigateUp()
            ForgotPasswordAction.ForgotPassword -> forgotPassword()
            is ForgotPasswordAction.OnUpdateEmail -> updateEmail(email = forgotPasswordActions.email)
        }
    }

    private fun forgotPassword() {
        viewModelScope.launch {
            _state.update { it.copy(isButtonLoading = true) }

            val email = state.value.email
            val isEmailValid = PatternValidation.isEmailValid(email)

            if (!isEmailValid){
                _state.update {currentState ->
                    currentState.copy(
                        isButtonLoading = false,
                        // Tampilkan error jika tidak valid
                        isEmailError = true,
                        // Gunakan fungsi getter error Anda!
                        emailError = PatternValidation.getEmailError(email),
                        // Naikkan HANYA trigger email jika email tidak valid
                        emailShakeTrigger = currentState.emailShakeTrigger + 1,
                    )
                }
                initialCheckError = true
                return@launch // Stop
            }

            // --- Jika lolos ---

            // Bersihkan error (jika ada sisa dari live validation)
            _state.update {
                it.copy(
                    isEmailError = false,
                    emailError = null
                )
            }

            // Lakukan Forgot Password
            authRepository.forgotPassword(email = email)
                .onSuccess {
                    _state.update { it.copy(isButtonLoading = false) }
                    _events.send(ForgotPasswordEvent.ShowToast("Email Reset Password Berhasil Dikirim"))
                }
                .onError {result ->
                    _state.update { it.copy(isButtonLoading = false) }
                    _events.send(ForgotPasswordEvent.ShowToast(result.message))
                }
        }
    }

    private fun updateEmail(email: String) {
       if (initialCheckError){
           val emailError = PatternValidation.getEmailError(email)
           val isEmailValid = PatternValidation.isEmailValid(email)
           _state.update {
               it.copy(
                   email = email,
                   emailError = emailError,
                   isEmailError = !isEmailValid
               )
           }
           return
       }

        _state.update {
            it.copy(
                email = email,
                emailError = null,
                isEmailError = false
            )
        }
    }

    private fun navigateUp() {
        viewModelScope.launch {
            _events.send(ForgotPasswordEvent.NavigateUp)
        }
    }
}