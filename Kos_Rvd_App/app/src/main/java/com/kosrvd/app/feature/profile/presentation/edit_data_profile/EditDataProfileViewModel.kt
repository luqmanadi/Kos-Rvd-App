package com.kosrvd.app.feature.profile.presentation.edit_data_profile

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kosrvd.app.core.domain.repository.AccountRepository
import com.kosrvd.app.core.domain.repository.AuthRepository
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.core.domain.utils.onSuccess
import com.kosrvd.app.presentation.navigation.NavigationScreen
import com.kosrvd.app.presentation.navigation.models.CustomNavTypes
import com.kosrvd.app.presentation.navigation.models.TemporaryData
import com.kosrvd.app.core.presentation.utils.PatternValidation
import com.kosrvd.app.core.presentation.utils.UiText
import com.kosrvd.app.feature.profile.domain.utils.TypeEdit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.typeOf

sealed interface EditDataProfileEvents {
    data object NavigateBack: EditDataProfileEvents
    data class ShowSnackBarError(val message: String): EditDataProfileEvents
    data class NavigateBackSuccessEditDataProfile(val typeEdit: TypeEdit): EditDataProfileEvents
}

sealed interface EditDataProfileActions {
    data object NavigateBack: EditDataProfileActions
    data object EditName: EditDataProfileActions
    data object EditEmail: EditDataProfileActions
    data object EditPhoneNumber: EditDataProfileActions
    data object EditAddress: EditDataProfileActions
    data object EditPassword: EditDataProfileActions
    data object OpenDialogConfirmationPassword: EditDataProfileActions
    data object DismissDialogConfirmationPassword: EditDataProfileActions
    data class UpdateOldPassword(val oldPassword: String): EditDataProfileActions
    data class UpdateName(val name: String): EditDataProfileActions
    data class UpdateEmail(val email: String): EditDataProfileActions
    data class UpdatePhoneNumber(val phoneNumber: String): EditDataProfileActions
    data class UpdateAddress(val address: String): EditDataProfileActions
    data class UpdatePassword(val password: String): EditDataProfileActions
    data class UpdatePasswordConfirmation(val passwordConfirmation: String): EditDataProfileActions
}

@HiltViewModel
class EditDataProfileViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val authRepository: AuthRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _state = MutableStateFlow(EditDataProfileUiState())
    val state = _state.asStateFlow()

    private val _events = Channel<EditDataProfileEvents>()
    val events = _events.receiveAsFlow()

    private var initialCheckError = false

    init {
        // Ambil objek argument dari savedStateHandle
        val arguments = savedStateHandle.toRoute<NavigationScreen.EditDataProfileScreen>(
            typeMap = mapOf(typeOf<TemporaryData?>() to CustomNavTypes.TemporaryDataType)
        )

        arguments.temporaryData?.let { data ->
            // Panggil fungsi inisialisasi di sini, di dalam init block.
            initialDataEditShow(data, arguments.typeEdit, arguments.idAkun)
        }
    }

    fun onActions(actions: EditDataProfileActions) {
        when (actions) {
            EditDataProfileActions.NavigateBack -> navigateBack()
            EditDataProfileActions.EditAddress -> editAddress()
            EditDataProfileActions.EditEmail -> editEmail()
            EditDataProfileActions.EditName -> editName()
            EditDataProfileActions.EditPassword -> editPassword()
            EditDataProfileActions.EditPhoneNumber -> editPhoneNumber()
            EditDataProfileActions.DismissDialogConfirmationPassword -> onDismissDialogConfirmationPassword()
            EditDataProfileActions.OpenDialogConfirmationPassword -> checkValidationEmail()
            is EditDataProfileActions.UpdateOldPassword -> updateOldPassword(actions.oldPassword)
            is EditDataProfileActions.UpdateAddress -> updateAddress(actions.address)
            is EditDataProfileActions.UpdateEmail -> updateEmail(actions.email)
            is EditDataProfileActions.UpdateName -> updateName(actions.name)
            is EditDataProfileActions.UpdatePassword -> updatePassword(actions.password)
            is EditDataProfileActions.UpdatePasswordConfirmation -> updatePasswordConfirmation(actions.passwordConfirmation)
            is EditDataProfileActions.UpdatePhoneNumber -> updatePhoneNumber(actions.phoneNumber)
        }
    }

    private fun onDismissDialogConfirmationPassword() {
        _state.update {
            it.copy(
                oldPassword = "",
                oldPasswordError = null,
                isOldPasswordError = false,
                shakeTriggerOldPasswordError = 0,
                showDialogConfirmationPassword = false
            )
        }
    }

    private fun updatePhoneNumber(phoneNumber: String) {
        val phoneNumberError = PatternValidation.getPhoneNumberError(phoneNumber)
        val isPhoneNumberError = PatternValidation.isPhoneNumberValid(phoneNumber)
        _state.update {
            it.copy(
                phoneNumber = phoneNumber,
                phoneNumberError = phoneNumberError,
                isPhoneNumberError = !isPhoneNumberError
            )
        }
    }

    private fun updatePasswordConfirmation(passwordConfirmation: String) {
        val password = _state.value.newPassword
        if (passwordConfirmation.isEmpty()) {
            _state.update {
                it.copy(
                    newPasswordConfirmation = passwordConfirmation,
                    newPasswordConfirmationError = null,
                    isNewPasswordConfirmationError = false
                )
            }
            return
        }

        val newPasswordConfirmationError = PatternValidation.getRepeatPasswordError(password, passwordConfirmation)
        val isNewPasswordConfirmationError = PatternValidation.isRepeatPasswordValid(password, passwordConfirmation)
        _state.update {
            it.copy(
                newPasswordConfirmation = passwordConfirmation,
                newPasswordConfirmationError = newPasswordConfirmationError,
                isNewPasswordConfirmationError = !isNewPasswordConfirmationError
            )
        }
    }

    private fun updatePassword(password: String) {
        if (password.isEmpty()) {
            _state.update {
                it.copy(
                    newPassword = password,
                    newPasswordError = null,
                    isNewPasswordError = false
                )
            }
            return
        }

        val newPasswordError = PatternValidation.getPasswordError(password)
        val isNewPasswordError = PatternValidation.isPasswordValid(password)
        _state.update {
            it.copy(
                newPassword = password,
                newPasswordError = newPasswordError,
                isNewPasswordError = !isNewPasswordError
            )
        }
    }

    private fun updateName(name: String) {
        val nameError = PatternValidation.getNameError(name)
        val isNameError = PatternValidation.isNameValid(name)
        _state.update {
            it.copy(
                name = name,
                nameError = nameError,
                isNameError = !isNameError
            )
        }
    }

    private fun updateEmail(email: String) {
        val emailError = PatternValidation.getEmailError(email)
        val isEmailError = PatternValidation.isEmailValid(email)
        _state.update {
            it.copy(
                email = email,
                emailError = emailError,
                isEmailError = !isEmailError
            )
        }
    }

    private fun updateAddress(address: String) {
        val addressError = PatternValidation.getAddressError(address)
        val isAddressError = PatternValidation.isAddressValid(address)
        _state.update {
            it.copy(
                address = address,
                addressError = addressError,
                isAddressError = !isAddressError
            )
        }
    }

    private fun editPhoneNumber() {
        viewModelScope.launch {
            _state.update { it.copy(isButtonLoading = true) }
            val idAkun = _state.value.idAkun
            val phoneNumber = _state.value.phoneNumber

            val phoneNumberError = PatternValidation.getPhoneNumberError(phoneNumber)
            val isPhoneNumberValid = PatternValidation.isPhoneNumberValid(phoneNumber)
            if (!isPhoneNumberValid){
                _state.update {
                    it.copy(
                        isButtonLoading = false,
                        phoneNumberError = phoneNumberError,
                        isPhoneNumberError = true,
                        shakeTriggerResponseError = it.shakeTriggerResponseError + 1
                    )
                }
                return@launch
            }

            _state.update {
                it.copy(
                    phoneNumberError = null,
                    isPhoneNumberError = false
                )
            }
            accountRepository.updatePhoneNumber(idAkun, phoneNumber)
                .onSuccess {
                    _state.update {
                        it.copy(
                            isButtonLoading = false
                        )
                    }
                    _events.send(EditDataProfileEvents.NavigateBackSuccessEditDataProfile(TypeEdit.NO_HP))
                }
                .onError { result ->
                    _state.update {
                        it.copy(
                            isButtonLoading = false
                        )
                    }
                    _events.send(EditDataProfileEvents.ShowSnackBarError(result.message))
                }
        }
    }

    private fun updateOldPassword(oldPassword: String){
        if (initialCheckError){
            val oldPasswordError = PatternValidation.getPasswordLogInError(oldPassword)
            val isOldPasswordValid = PatternValidation.isPasswordLogInValid(oldPassword)
            _state.update {
                it.copy(
                    oldPassword = oldPassword,
                    oldPasswordError = oldPasswordError,
                    isOldPasswordError = !isOldPasswordValid
                )
            }
            return
        }

        _state.update {
            it.copy(
                oldPassword = oldPassword,
                oldPasswordError = null,
                isOldPasswordError = false
            )
        }
    }

    private fun editPassword() {
        viewModelScope.launch {
            _state.update { it.copy(isButtonLoading = true) }
            val oldPassword = _state.value.oldPassword
            val newPassword = _state.value.newPassword
            val newPasswordConfirmation = _state.value.newPasswordConfirmation

            val oldPasswordError = PatternValidation.getPasswordLogInError(oldPassword)
            val isOldPasswordValid = PatternValidation.isPasswordLogInValid(oldPassword)
            val newPasswordError = PatternValidation.getPasswordError(newPassword)
            val isNewPasswordValid = PatternValidation.isPasswordValid(newPassword)
            val newPasswordConfirmationError = PatternValidation.getRepeatPasswordError(newPassword, newPasswordConfirmation)
            val isNewPasswordConfirmationValid = PatternValidation.isRepeatPasswordValid(newPassword, newPasswordConfirmation)
            val shakeTriggerNewPasswordError = if (!isNewPasswordValid) _state.value.shakeTriggerNewPasswordError + 1 else _state.value.shakeTriggerNewPasswordError
            val shakeTriggerResponseError = if (!isNewPasswordConfirmationValid) _state.value.shakeTriggerResponseError + 1 else _state.value.shakeTriggerResponseError
            val shakeTriggerOldPasswordError = if (!isOldPasswordValid) _state.value.shakeTriggerOldPasswordError + 1 else _state.value.shakeTriggerOldPasswordError

            if (!isNewPasswordValid || !isNewPasswordConfirmationValid || !isOldPasswordValid){
                Log.d("TAG", "!isNewPasswordValid || !isNewPasswordConfirmationValid || !isOldPasswordValid: $newPasswordError || $newPasswordConfirmation || $oldPassword")
                _state.update {
                    it.copy(
                        isButtonLoading = false,
                        oldPasswordError = oldPasswordError,
                        isOldPasswordError = !isOldPasswordValid,
                        newPasswordError = newPasswordError,
                        isNewPasswordError = !isNewPasswordValid,
                        newPasswordConfirmationError = newPasswordConfirmationError,
                        isNewPasswordConfirmationError = !isNewPasswordConfirmationValid,
                        shakeTriggerNewPasswordError = shakeTriggerNewPasswordError,
                        shakeTriggerResponseError = shakeTriggerResponseError,
                        shakeTriggerOldPasswordError = shakeTriggerOldPasswordError
                    )
                }
                return@launch
            }

            _state.update {
                it.copy(
                    newPasswordError = null,
                    isNewPasswordError = false,
                    newPasswordConfirmationError = null,
                    isNewPasswordConfirmationError = false,
                    oldPasswordError = null,
                    isOldPasswordError = false
                )
            }

            authRepository.changePassword(newPassword = newPassword, oldPassword = oldPassword)
                .onSuccess {
                    _state.update {
                        it.copy(
                            isButtonLoading = false
                        )
                    }
                    _events.send(EditDataProfileEvents.NavigateBackSuccessEditDataProfile(TypeEdit.GANTI_PASSWORD))
                }
                .onError { result ->
                    if (result == DataError.AUTH_INVALID_CREDENTIAL){
                        _state.update {
                            it.copy(
                                isButtonLoading = false,
                                oldPasswordError = UiText.DynamicString("Password salah"),
                                isOldPasswordError = true,
                                shakeTriggerOldPasswordError = it.shakeTriggerOldPasswordError + 1
                            )
                        }
                    } else {
                        _state.update {
                            it.copy(
                                isButtonLoading = false
                            )
                        }
                        _events.send(EditDataProfileEvents.ShowSnackBarError(result.message))
                    }
                }
        }
    }

    private fun editName() {
        viewModelScope.launch {
            _state.update { it.copy(isButtonLoading = true) }
            val idAkun = _state.value.idAkun
            val name = _state.value.name

            val nameError = PatternValidation.getNameError(name)
            val isNameValid = PatternValidation.isNameValid(name)
            if (!isNameValid){
                _state.update {
                    it.copy(
                        isButtonLoading = false,
                        nameError = nameError,
                        isNameError = true,
                        shakeTriggerResponseError = it.shakeTriggerResponseError + 1
                    )
                }
                return@launch
            }

            _state.update {
                it.copy(
                    nameError = null,
                    isNameError = false
                )
            }
            accountRepository.updateName(idAkun, name)
                .onSuccess {
                    _state.update {
                        it.copy(
                            isButtonLoading = false
                        )
                    }
                    _events.send(EditDataProfileEvents.NavigateBackSuccessEditDataProfile(TypeEdit.NAMA))
                }
                .onError { result ->
                    _state.update {
                        it.copy(
                            isButtonLoading = false
                        )
                    }
                    _events.send(EditDataProfileEvents.ShowSnackBarError(result.message))
                }
        }
    }

    private fun checkValidationEmail(){
        val email = _state.value.email

        val emailError = PatternValidation.getEmailError(email)
        val isEmailValid = PatternValidation.isEmailValid(email)
        if (!isEmailValid) {
            _state.update {
                it.copy(
                    emailError = emailError,
                    isEmailError = true,
                    shakeTriggerResponseError = it.shakeTriggerResponseError + 1
                )
            }
            return
        }
        _state.update {
            it.copy(
                emailError = null,
                isEmailError = false,
                showDialogConfirmationPassword = true
            )
        }
    }

    private fun editEmail() {
        viewModelScope.launch {
            _state.update { it.copy(isButtonLoading = true) }
            val email = _state.value.email
            val oldPassword = _state.value.oldPassword

            val oldPasswordError = PatternValidation.getPasswordLogInError(oldPassword)
            val oldPasswordValid = PatternValidation.isPasswordLogInValid(oldPassword)
            if (!oldPasswordValid){
                _state.update {
                    it.copy(
                        isButtonLoading = false,
                        oldPasswordError = oldPasswordError,
                        isOldPasswordError = true,
                        shakeTriggerOldPasswordError = it.shakeTriggerOldPasswordError + 1
                    )
                }
                return@launch
            }
            _state.update {
                it.copy(
                    oldPasswordError = null,
                    isOldPasswordError = false,
                )
            }
            authRepository.changeEmail(newEmail = email, currentPassword = oldPassword)
                .onSuccess {
                    _state.update {
                        it.copy(
                            isButtonLoading = false,
                            showDialogConfirmationPassword = false
                        )
                    }
                    _events.send(EditDataProfileEvents.NavigateBackSuccessEditDataProfile(TypeEdit.EMAIL))
                }
                .onError { result ->
                    if (result == DataError.AUTH_INVALID_CREDENTIAL){
                        _state.update {
                            it.copy(
                                isButtonLoading = false,
                                oldPasswordError = UiText.DynamicString("Password salah"),
                                isOldPasswordError = true,
                                shakeTriggerOldPasswordError = it.shakeTriggerOldPasswordError + 1
                            )
                        }
                    } else {
                        _state.update {
                            it.copy(
                                isButtonLoading = false,
                                showDialogConfirmationPassword = false
                            )
                        }
                        _events.send(EditDataProfileEvents.ShowSnackBarError(result.message))
                    }
                }
        }
    }

    private fun editAddress() {
        viewModelScope.launch {
            _state.update { it.copy(isButtonLoading = true) }
            val idAkun = _state.value.idAkun
            val address = _state.value.address

            val addressError = PatternValidation.getAddressError(address)
            val isAddressValid = PatternValidation.isAddressValid(address)
            if (!isAddressValid){
                _state.update {
                    it.copy(
                        isButtonLoading = false,
                        addressError = addressError,
                        isAddressError = true,
                        shakeTriggerResponseError = it.shakeTriggerResponseError + 1
                    )
                }
                return@launch
            }

            _state.update {
                it.copy(
                    addressError = null,
                    isAddressError = false
                )
            }
            accountRepository.updateAddress(idAkun, address)
                .onSuccess {
                    _state.update {
                        it.copy(
                            isButtonLoading = false
                        )
                    }
                    _events.send(EditDataProfileEvents.NavigateBackSuccessEditDataProfile(TypeEdit.ALAMAT))
                }
                .onError { result ->
                    _state.update {
                        it.copy(
                            isButtonLoading = false
                        )
                    }
                    _events.send(EditDataProfileEvents.ShowSnackBarError(result.message))
                }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(EditDataProfileEvents.NavigateBack)
        }
    }

    // Fungsi initialDataEditShow tetap sama, tetapi sekarang dipanggil dari init.
    private fun initialDataEditShow(data: TemporaryData, typeEdit: TypeEdit, idAkun: String) {
        when (typeEdit) {
            TypeEdit.NAMA -> {
                _state.update { it.copy(name = data.name?: "", idAkun = idAkun) }
            }
            TypeEdit.EMAIL -> {
                _state.update { it.copy(email = data.email?: "", idAkun = idAkun) }
            }
            TypeEdit.NO_HP -> {
                _state.update { it.copy(phoneNumber = data.phoneNumber?: "", idAkun = idAkun) }
            }
            TypeEdit.ALAMAT -> {
                _state.update { it.copy(address = data.address?: "", idAkun = idAkun) }
            }
            TypeEdit.GANTI_PASSWORD -> {
                // Tidak ada data awal untuk ganti password
            }
        }
    }
}