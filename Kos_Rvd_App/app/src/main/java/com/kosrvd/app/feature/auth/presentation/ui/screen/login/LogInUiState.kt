package com.kosrvd.app.feature.auth.presentation.ui.screen.login

import com.kosrvd.app.core.presentation.utils.UiText

data class LogInUiState(
    val isButtonLoading: Boolean = false,
    val email: String = "",
    val password: String = "",
    val emailError: UiText? = null,
    val passwordError: UiText? = null,
    val isEmailError: Boolean = false,
    val isPasswordError: Boolean = false,
)
