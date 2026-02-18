package com.kosrvd.app.feature.management.presentation.ui.screen.akun.buat_akun

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.domain.usecase.CreateUserAccountUseCase
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.core.domain.utils.onSuccess
import com.kosrvd.app.core.presentation.utils.PatternValidation
import com.kosrvd.app.feature.management.presentation.designsystem.utils.ImageCompressor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface BuatAkunEvents {
    data object NavigateBack : BuatAkunEvents
    data object NavigateBackToSendCreateAkunSnackBar : BuatAkunEvents
    data class NavigateToPreviewImage(val imageUri: Uri): BuatAkunEvents
    data class ShowSnackBarError(val message: String) : BuatAkunEvents
}

sealed interface BuatAkunActions {
    data class UpdateFillName(val name: String): BuatAkunActions
    data class UpdateFillPhoneNumber(val phoneNumber: String) : BuatAkunActions
    data class UpdateFillEmail(val email: String) : BuatAkunActions
    data class UpdateFillNewPassword(val newPassword: String) : BuatAkunActions
    data class UpdateFillConfirmPassword(val confirmPassword: String) : BuatAkunActions
    data class UpdateFillAddress(val address: String) : BuatAkunActions
    data class UpdateSelectRole(val role: String) : BuatAkunActions
    data class UpdateFillPhotoKtp(val photoKtp: Uri) : BuatAkunActions
    data class NavigateToPreviewImage(val imageUri: Uri): BuatAkunActions
    data object AddAkun : BuatAkunActions
    data object NavigateBack : BuatAkunActions
}


@HiltViewModel
class BuatAkunViewModel @Inject constructor(
    private val createAccountUseCase: CreateUserAccountUseCase,
    private val imageCompressor: ImageCompressor
): ViewModel() {

    private val _state = MutableStateFlow(BuatAkunUiState())
    val state = _state.asStateFlow()

    private val _events = Channel<BuatAkunEvents>()
    val events = _events.receiveAsFlow()

    fun onActions(actions: BuatAkunActions){
        when(actions){
            BuatAkunActions.AddAkun -> addAkun()
            BuatAkunActions.NavigateBack -> navigateBack()
            is BuatAkunActions.NavigateToPreviewImage -> navigateToPreviewImage(actions.imageUri)
            is BuatAkunActions.UpdateFillAddress -> updateFillAddress(actions.address)
            is BuatAkunActions.UpdateFillConfirmPassword -> updateFillConfirmPassword(actions.confirmPassword)
            is BuatAkunActions.UpdateFillEmail -> updateFillEmail(actions.email)
            is BuatAkunActions.UpdateFillName -> updateFillName(actions.name)
            is BuatAkunActions.UpdateFillNewPassword -> updateFillNewPassword(actions.newPassword)
            is BuatAkunActions.UpdateFillPhoneNumber -> updateFillPhoneNumber(actions.phoneNumber)
            is BuatAkunActions.UpdateFillPhotoKtp -> updateFillPhotoKtp(actions.photoKtp)
            is BuatAkunActions.UpdateSelectRole -> updateSelectRole(actions.role)
        }
    }

    private fun updateFillPhotoKtp(photoKtp: Uri) {
        _state.update {
            it.copy(photoKtp = photoKtp)
        }
    }

    private fun updateFillPhoneNumber(phoneNumber: String) {
        if (phoneNumber.isEmpty()){
            _state.update {
                it.copy(
                    phoneNumber = phoneNumber,
                    phoneNumberError = null,
                    isPhoneNumberError = false
                )
            }
            return
        }
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

    private fun updateFillNewPassword(newPassword: String) {
        if (newPassword.isEmpty()){
            _state.update {
                it.copy(
                    newPassword = newPassword,
                    newPasswordError = null,
                    isNewPasswordError = false
                )
            }
            return
        }
        val newPasswordError = PatternValidation.getPasswordError(newPassword)
        val isNewPasswordError = PatternValidation.isPasswordValid(newPassword)
        _state.update {
            it.copy(
                newPassword = newPassword,
                newPasswordError = newPasswordError,
                isNewPasswordError = !isNewPasswordError
            )
        }
    }

    private fun updateFillName(name: String) {
        if (name.isEmpty()){
            _state.update {
                it.copy(
                    name = name,
                    nameError = null,
                    isNameError = false
                )
            }
            return
        }
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

    private fun updateFillEmail(email: String) {
        if (email.isEmpty()){
            _state.update {
                it.copy(
                    email = email,
                    emailError = null,
                    isEmailError = false
                )
            }
            return
        }
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

    private fun updateFillConfirmPassword(confirmPassword: String) {
        if (confirmPassword.isEmpty()){
            _state.update {
                it.copy(
                    confirmPassword = confirmPassword,
                    confirmPasswordError = null,
                    isConfirmPasswordError = false
                )
            }
            return
        }
        val confirmPasswordError = PatternValidation.getRepeatPasswordError(_state.value.newPassword, confirmPassword)
        val isConfirmPasswordError = PatternValidation.isRepeatPasswordValid(_state.value.newPassword, confirmPassword)
        _state.update {
            it.copy(
                confirmPassword = confirmPassword,
                confirmPasswordError = confirmPasswordError,
                isConfirmPasswordError = !isConfirmPasswordError
            )
        }
    }

    private fun updateFillAddress(address: String) {
        if (address.isEmpty()){
            _state.update {
                it.copy(
                    address = address,
                    addressError = null,
                    isAddressError = false
                )
            }
            return
        }
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

    private fun updateSelectRole(role: String) {
        viewModelScope.launch {
            _state.update { it.copy(role = role) }
        }
    }

    private fun navigateToPreviewImage(imageUri: Uri) {
        viewModelScope.launch {
            _events.send(BuatAkunEvents.NavigateToPreviewImage(imageUri))
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(BuatAkunEvents.NavigateBack)
        }
    }

    private fun addAkun() {
        viewModelScope.launch {
            _state.update { it.copy(isButtonLoading = true) }
            val role = _state.value.role

            if (role == Constant.ADMIN_ROLE){
                val name = _state.value.name
                val email = _state.value.email
                val newPassword = _state.value.newPassword
                val confirmPassword = _state.value.confirmPassword

                // Pesan Error
                val nameError = PatternValidation.getNameError(name)
                val emailError = PatternValidation.getEmailError(email)
                val newPasswordError = PatternValidation.getPasswordError(newPassword)
                val confirmPasswordError = PatternValidation.getRepeatPasswordError(newPassword, confirmPassword)

                // Validasi
                val isNameValid = PatternValidation.isNameValid(name)
                val isEmailValid = PatternValidation.isEmailValid(email)
                val isNewPasswordValid = PatternValidation.isPasswordValid(newPassword)
                val isConfirmPasswordValid = PatternValidation.isRepeatPasswordValid(newPassword,confirmPassword)

                // Shake Trigger Error
                val nameShakeTrigger = if (isNameValid) _state.value.nameShakeTrigger else _state.value.nameShakeTrigger + 1
                val emailShakeTrigger = if (isEmailValid) _state.value.emailShakeTrigger else _state.value.emailShakeTrigger + 1
                val newPasswordShakeTrigger = if (isNewPasswordValid) _state.value.newPasswordShakeTrigger else _state.value.newPasswordShakeTrigger + 1
                val confirmPasswordShakeTrigger = if (isConfirmPasswordValid) _state.value.confirmPasswordShakeTrigger else _state.value.confirmPasswordShakeTrigger + 1

                if (!isNameValid || !isEmailValid || !isNewPasswordValid || !isConfirmPasswordValid){
                    _state.update {
                        it.copy(
                            isButtonLoading = false,
                            nameError = nameError,
                            emailError = emailError,
                            newPasswordError = newPasswordError,
                            confirmPasswordError = confirmPasswordError,
                            isNameError = !isNameValid,
                            isEmailError = !isEmailValid,
                            isNewPasswordError = !isNewPasswordValid,
                            isConfirmPasswordError = !isConfirmPasswordValid,
                            nameShakeTrigger = nameShakeTrigger,
                            emailShakeTrigger = emailShakeTrigger,
                            newPasswordShakeTrigger = newPasswordShakeTrigger,
                            confirmPasswordShakeTrigger = confirmPasswordShakeTrigger
                        )
                    }
                    return@launch
                }

                _state.update {
                    it.copy(
                        nameError = null,
                        emailError = null,
                        newPasswordError = null,
                        confirmPasswordError = null,
                        isNameError = false,
                        isEmailError = false,
                        isNewPasswordError = false,
                        isConfirmPasswordError = false
                    )
                }

                createAccountUseCase(
                    email = email,
                    password = newPassword,
                    name = name,
                    role = role,
                    address = null,
                    phoneNumber = null,
                    compressedResult = null
                )
                    .onSuccess {
                        _state.update {
                            it.copy(isButtonLoading = false)
                        }
                        _events.send(BuatAkunEvents.NavigateBackToSendCreateAkunSnackBar)
                    }
                    .onError { result ->
                        _state.update {
                            it.copy(isButtonLoading = false)
                        }
                        _events.send(BuatAkunEvents.ShowSnackBarError(result.message))
                    }

            } else {
                val name = _state.value.name
                val email = _state.value.email
                val newPassword = _state.value.newPassword
                val confirmPassword = _state.value.confirmPassword
                val phoneNumber = _state.value.phoneNumber
                val address = _state.value.address
                val photoKtp = _state.value.photoKtp

                // Pesan Error
                val nameError = PatternValidation.getNameError(name)
                val emailError = PatternValidation.getEmailError(email)
                val newPasswordError = PatternValidation.getPasswordError(newPassword)
                val confirmPasswordError = PatternValidation.getRepeatPasswordError(newPassword, confirmPassword)
                val phoneNumberError = PatternValidation.getPhoneNumberError(phoneNumber)
                val addressError = PatternValidation.getAddressError(address)


                // Validasi
                val isNameValid = PatternValidation.isNameValid(name)
                val isEmailValid = PatternValidation.isEmailValid(email)
                val isNewPasswordValid = PatternValidation.isPasswordValid(newPassword)
                val isConfirmPasswordValid = PatternValidation.isRepeatPasswordValid(newPassword,confirmPassword)
                val isPhoneNumberValid = PatternValidation.isPhoneNumberValid(phoneNumber)
                val isAddressValid = PatternValidation.isAddressValid(address)
                val isPhotoKtpValid = photoKtp != Uri.EMPTY

                // Shake Trigger Error
                val nameShakeTrigger = if (isNameValid) _state.value.nameShakeTrigger else _state.value.nameShakeTrigger + 1
                val emailShakeTrigger = if (isEmailValid) _state.value.emailShakeTrigger else _state.value.emailShakeTrigger + 1
                val newPasswordShakeTrigger = if (isNewPasswordValid) _state.value.newPasswordShakeTrigger else _state.value.newPasswordShakeTrigger + 1
                val confirmPasswordShakeTrigger = if (isConfirmPasswordValid) _state.value.confirmPasswordShakeTrigger else _state.value.confirmPasswordShakeTrigger + 1
                val phoneNumberShakeTrigger = if (isPhoneNumberValid) _state.value.phoneNumberShakeTrigger else _state.value.phoneNumberShakeTrigger + 1
                val addressShakeTrigger = if (isAddressValid) _state.value.addressShakeTrigger else _state.value.addressShakeTrigger + 1

                if (!isNameValid || !isEmailValid || !isNewPasswordValid || !isConfirmPasswordValid || !isPhoneNumberValid || !isAddressValid){
                    _state.update {
                        it.copy(
                            isButtonLoading = false,
                            nameError = nameError,
                            emailError = emailError,
                            newPasswordError = newPasswordError,
                            confirmPasswordError = confirmPasswordError,
                            phoneNumberError = phoneNumberError,
                            addressError = addressError,
                            isNameError = !isNameValid,
                            isEmailError = !isEmailValid,
                            isNewPasswordError = !isNewPasswordValid,
                            isConfirmPasswordError = !isConfirmPasswordValid,
                            isPhoneNumberError = !isPhoneNumberValid,
                            isAddressError = !isAddressValid,
                            nameShakeTrigger = nameShakeTrigger,
                            emailShakeTrigger = emailShakeTrigger,
                            newPasswordShakeTrigger = newPasswordShakeTrigger,
                            confirmPasswordShakeTrigger = confirmPasswordShakeTrigger,
                            phoneNumberShakeTrigger = phoneNumberShakeTrigger,
                            addressShakeTrigger = addressShakeTrigger
                        )
                    }
                    return@launch
                }
                if (!isPhotoKtpValid){
                    _state.update {
                        it.copy(
                            isButtonLoading = false
                        )
                    }
                    _events.send(BuatAkunEvents.ShowSnackBarError("Foto KTP Harus Diisi"))
                    return@launch
                }

                _state.update {
                    it.copy(
                        nameError = null,
                        emailError = null,
                        newPasswordError = null,
                        confirmPasswordError = null,
                        phoneNumberError = null,
                        addressError = null,
                        isNameError = false,
                        isEmailError = false,
                        isNewPasswordError = false,
                        isConfirmPasswordError = false,
                        isPhoneNumberError = false,
                        isAddressError = false
                    )
                }

                val compressedResult = if (photoKtp != Uri.EMPTY) {
                    imageCompressor.compressImage(imageUri = photoKtp, compressionThreshold = 200 * 1024L)
                } else { null}

                createAccountUseCase(
                    email = email,
                    password = newPassword,
                    name = name,
                    role = role,
                    address = address,
                    phoneNumber = phoneNumber,
                    compressedResult = compressedResult
                )
                    .onSuccess {
                        _state.update {
                            it.copy(isButtonLoading = false)
                        }
                        _events.send(BuatAkunEvents.NavigateBackToSendCreateAkunSnackBar)
                    }
                    .onError { result ->
                        _state.update {
                            it.copy(isButtonLoading = false)
                        }
                        _events.send(BuatAkunEvents.ShowSnackBarError(result.message))
                    }
            }
        }
    }
}