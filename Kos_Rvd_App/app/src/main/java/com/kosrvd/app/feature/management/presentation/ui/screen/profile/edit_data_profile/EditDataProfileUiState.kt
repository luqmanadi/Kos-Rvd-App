package com.kosrvd.app.feature.management.presentation.ui.screen.profile.edit_data_profile

import com.kosrvd.app.core.presentation.utils.UiText

data class EditDataProfileUiState(
    val idAkun: String = "",
    val isButtonLoading: Boolean = false,
    val name: String = "",
    val nameError: UiText? = null,
    val isNameError: Boolean = false,
    val email: String = "",
    val emailError: UiText? = null,
    val isEmailError: Boolean = false,
    val phoneNumber: String = "",
    val phoneNumberError: UiText? = null,
    val isPhoneNumberError: Boolean = false,
    val address: String = "",
    val addressError: UiText? = null,
    val isAddressError: Boolean = false,
    val oldPassword: String = "",
    val oldPasswordError: UiText? = null,
    val isOldPasswordError: Boolean = false,
    val newPassword: String = "",
    val newPasswordError: UiText? = null,
    val isNewPasswordError: Boolean = false,
    val newPasswordConfirmation: String = "",
    val newPasswordConfirmationError: UiText? = null,
    val isNewPasswordConfirmationError: Boolean = false,
    val shakeTriggerResponseError: Int = 0,
    val shakeTriggerNewPasswordError: Int = 0,
    val shakeTriggerOldPasswordError: Int = 0,
    val showDialogConfirmationPassword: Boolean = false,
)
