package com.kosrvd.app.feature.auth.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.core.domain.utils.onSuccess
import com.kosrvd.app.core.presentation.utils.PatternValidation
import com.kosrvd.app.core.presentation.utils.ShakeTargetLogIn
import com.kosrvd.app.feature.auth.domain.LogInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface LogInEvents {
    data object NavigateToForgotPassword : LogInEvents
    data class ShowToast(val message: String) : LogInEvents
    data object NavigateUp : LogInEvents
    data object NavigateToManagementGraph : LogInEvents
    data class ShakeTextField(val target: ShakeTargetLogIn) : LogInEvents
}

sealed interface LogInActions {
    data object NavigateToForgotPassword : LogInActions
    data object NavigateUp : LogInActions
    data object LogIn : LogInActions
    data class UpdateEmail(val email: String) : LogInActions
    data class UpdatePassword(val password: String) : LogInActions
}

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val logInUseCase: LogInUseCase
): ViewModel() {
    private val _state = MutableStateFlow(LogInUiState())
    val state = _state.asStateFlow()

    private val _events = Channel<LogInEvents>()
    val events = _events.receiveAsFlow()

    private var initialCheckError = false

    fun onActions(logInActions: LogInActions) {
        when (logInActions) {
            LogInActions.NavigateToForgotPassword -> navigateToForgotPassword()
            LogInActions.NavigateUp -> navigateUp()
            LogInActions.LogIn -> logIn()
            is LogInActions.UpdateEmail -> updateEmail(email = logInActions.email)
            is LogInActions.UpdatePassword -> updatePassword(password = logInActions.password)
        }
    }

    private fun navigateUp() {
        viewModelScope.launch {
            _events.send(LogInEvents.NavigateUp)
        }
    }

    private fun updatePassword(password: String) {
        if (initialCheckError){
            val passwordError = PatternValidation.getPasswordLogInError(password)
            val isPasswordValid = PatternValidation.isPasswordLogInValid(password)
            _state.update {
                it.copy(
                    password = password,
                    passwordError = passwordError,
                    isPasswordError = !isPasswordValid
                )
            }
            return
        }

        _state.update {
            it.copy(
                password = password,
                passwordError = null,
                isPasswordError = false
            )
        }
    }

    private fun updateEmail(email: String) {
        if (initialCheckError) {
            val isPasswordValid = PatternValidation.isEmailValid(email)
            val emailError = PatternValidation.getEmailError(email)
            _state.update {
                it.copy(
                    email = email,
                    emailError = emailError,
                    isEmailError = !isPasswordValid
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

    private fun logIn() {
        viewModelScope.launch {
            _state.update { it.copy(isButtonLoading = true) }

            val email = state.value.email
            val password = state.value.password
            val isEmailValid = PatternValidation.isEmailValid(email)
            val isPasswordValid = PatternValidation.isPasswordLogInValid(password)

            // Jika salah satu (atau keduanya) tidak valid...
            if (!isEmailValid || !isPasswordValid) {
                // --- 1. KIRIM EVENT SHAKE DULU ---
                val shakeTarget = when {
                    !isEmailValid && !isPasswordValid -> ShakeTargetLogIn.BOTH
                    !isEmailValid -> ShakeTargetLogIn.EMAIL
                    else -> ShakeTargetLogIn.PASSWORD
                }

                shakeTarget.let {
                    _events.send(LogInEvents.ShakeTextField(it))
                    Log.d("Trigger Shake", "Trigger ${shakeTarget.name}")
                }

                // --- 2. UPDATE STATE UNTUK ERROR TEXT ---
                _state.update { currentState ->
                    currentState.copy(
                        isButtonLoading = false,
                        isEmailError = !isEmailValid,
                        emailError = if (!isEmailValid) PatternValidation.getEmailError(email) else null,
                        isPasswordError = !isPasswordValid,
                        passwordError = if (!isPasswordValid) PatternValidation.getPasswordLogInError(password) else null
                        // Tidak ada lagi trigger yang di-update
                    )
                }
                // (Sembunyikan keyboard juga bisa dikirim sebagai event dari sini jika mau)
                initialCheckError = true
                return@launch
            }

            // --- Jika lolos ---

            // Bersihkan error (jika ada sisa dari live validation)
            _state.update {
                it.copy(
                    isEmailError = false,
                    emailError = null,
                    isPasswordError = false,
                    passwordError = null
                )
            }

            // Lakukan proses login
            logInUseCase.invoke(email = email, password = password)
                .onSuccess {
                    _state.update { it.copy(isButtonLoading = false) }
                    navigateToManagementGraph()
                }
                .onError {result ->
                    _state.update { it.copy(isButtonLoading = false) }
                    _events.send(LogInEvents.ShowToast(result.message))
                }
        }
    }

    private fun navigateToManagementGraph(){
        viewModelScope.launch {
            _events.send(LogInEvents.NavigateToManagementGraph)
        }
    }

    private fun navigateToForgotPassword() {
        viewModelScope.launch {
            _events.send(LogInEvents.NavigateToForgotPassword)
        }
    }
}