package com.kosrvd.app.feature.account.presentation.buat_akun

import android.net.Uri
import com.kosrvd.app.core.presentation.utils.UiText

data class BuatAkunUiState(
    val isButtonLoading: Boolean = false,
    val name: String = "",
    val nameError: UiText? = null,
    val isNameError: Boolean = false,
    val nameShakeTrigger: Int = 0,
    val phoneNumber: String = "",
    val phoneNumberError: UiText? = null,
    val isPhoneNumberError: Boolean = false,
    val phoneNumberShakeTrigger: Int = 0,
    val email: String = "",
    val emailError: UiText? = null,
    val isEmailError: Boolean = false,
    val emailShakeTrigger: Int = 0,
    val newPassword: String = "",
    val newPasswordError: UiText? = null,
    val isNewPasswordError: Boolean = false,
    val newPasswordShakeTrigger: Int = 0,
    val confirmPassword: String = "",
    val confirmPasswordError: UiText? = null,
    val isConfirmPasswordError: Boolean = false,
    val confirmPasswordShakeTrigger: Int = 0,
    val address: String = "",
    val addressError: UiText? = null,
    val isAddressError: Boolean = false,
    val addressShakeTrigger: Int = 0,
    val role: String = "admin",
    val photoKtp: Uri = Uri.EMPTY,
)
