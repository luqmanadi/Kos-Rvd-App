package com.kosrvd.app.feature.profile.presentation.edit_photo_profile

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.core.domain.utils.onSuccess
import com.kosrvd.app.feature.profile.domain.usecase.SavePhotoProfileUseCase
import com.kosrvd.app.core.presentation.utils.ImageCompressor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface EditPhotoProfileEvents {
    data object NavigateBackToSendUpdatePhoto: EditPhotoProfileEvents
    data object NavigateBack: EditPhotoProfileEvents
    data class ShowSnackBarError(val message: String): EditPhotoProfileEvents
}

sealed interface EditPhotoProfileActions {
    data object SavePhoto: EditPhotoProfileActions
    data object NavigateBack: EditPhotoProfileActions
    data class UpdatePhoto(val photoUri: Uri): EditPhotoProfileActions
}

@HiltViewModel
class EditPhotoProfileViewModel @Inject constructor(
    private val imageCompressor: ImageCompressor,
    private val savePhotoProfileUseCase: SavePhotoProfileUseCase
): ViewModel() {
    private val _state = MutableStateFlow(EditPhotoProfileUiState())
    val state = _state.asStateFlow()

    private val _events = Channel<EditPhotoProfileEvents>()
    val events = _events.receiveAsFlow()

    fun onActions(actions: EditPhotoProfileActions){
        when(actions){
            EditPhotoProfileActions.NavigateBack -> navigateBack()
            EditPhotoProfileActions.SavePhoto -> savePhoto()
            is EditPhotoProfileActions.UpdatePhoto -> updatePhoto(actions.photoUri)
        }
    }

    private fun savePhoto() {
        viewModelScope.launch {
            _state.update { it.copy(isButtonLoading = true) }

            val newPhotoProfile = _state.value.newPhotoProfile
            val oldPhotoProfile = _state.value.oldPhotoProfile
            val idAkun = _state.value.idAkun
            val compressedResult = imageCompressor.compressImage(imageUri = newPhotoProfile, compressionThreshold = 200 * 1024L)

            savePhotoProfileUseCase(
                idAkun = idAkun,
                oldPhotoUri = oldPhotoProfile,
                compressedResult = compressedResult
            ).onError { result ->
                _state.update {
                    it.copy(isButtonLoading = false)
                }
                _events.send(EditPhotoProfileEvents.ShowSnackBarError(result.message))
            }.onSuccess {
                _state.update {
                    it.copy(isButtonLoading = false)
                }
                _events.send(EditPhotoProfileEvents.NavigateBackToSendUpdatePhoto)
            }
        }
    }

    fun initialPhotoAndIdAkun(oldPhotoUri: String, idAkun: String){
        _state.update { it.copy(oldPhotoProfile = oldPhotoUri, idAkun = idAkun, newPhotoProfile = oldPhotoUri.toUri()) }
    }

    private fun updatePhoto(photoUri: Uri) {
        _state.update { it.copy(newPhotoProfile = photoUri) }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(EditPhotoProfileEvents.NavigateBack)
        }
    }

}