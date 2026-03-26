package com.kosrvd.app.feature.auth.presentation.forgot_password

import com.kosrvd.app.core.presentation.utils.UiText

data class ForgotPasswordUiState(
    val email: String = "",
    val emailError: UiText? = null,
    val isEmailError: Boolean = false,
    val isButtonLoading: Boolean = false,
    val emailShakeTrigger: Int = 0,
)
