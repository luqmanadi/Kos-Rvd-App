package com.kosrvd.app.feature.complaint.presentation.buat_laporan_keluhan

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.core.domain.utils.onSuccess
import com.kosrvd.app.presentation.navigation.models.ResultLaporanKeluhan
import com.kosrvd.app.core.presentation.utils.PatternValidation
import com.kosrvd.app.core.presentation.utils.ShakeTargetBuatLaporanKeluhan
import com.kosrvd.app.feature.complaint.data.mappers.toResultLaporanKeluhan
import com.kosrvd.app.feature.complaint.domain.usecase.BuatLaporanKeluhanUseCase
import com.kosrvd.app.core.presentation.utils.ImageCompressor
import com.kosrvd.app.core.presentation.utils.TypeResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface BuatLaporanKeluhanEvents {
    data object NavigateUp : BuatLaporanKeluhanEvents
    data class NavigateToResult (val typeResult: TypeResult, val resultLaporanKeluhan: ResultLaporanKeluhan) : BuatLaporanKeluhanEvents
    data class NavigateToPreviewImage(val imageUri: Uri) : BuatLaporanKeluhanEvents
    data class ShakeTextField(val target: ShakeTargetBuatLaporanKeluhan) : BuatLaporanKeluhanEvents
    data class ShowSnackBarError(val message: String) : BuatLaporanKeluhanEvents
    data class ShowToast(val message: String) : BuatLaporanKeluhanEvents
}

sealed interface BuatLaporanKeluhanActions {
    data object NavigateUp : BuatLaporanKeluhanActions
    data object BuatLaporanKeluhan : BuatLaporanKeluhanActions
    data class UpdateTitle(val title: String) : BuatLaporanKeluhanActions
    data class UpdateDescription(val description: String) : BuatLaporanKeluhanActions
    data class UpdateImage(val imageUri: Uri) : BuatLaporanKeluhanActions
    data class OnPreviewImage(val imageUri: Uri): BuatLaporanKeluhanActions
}


@HiltViewModel
class BuatLaporanKeluhanViewModel @Inject constructor(
    private val buatLaporanKeluhanUseCase: BuatLaporanKeluhanUseCase,
    private val imageCompressor: ImageCompressor
): ViewModel() {
    private val _state = MutableStateFlow(BuatLaporanKeluhanUiState())
    val state = _state.asStateFlow()

    private val _events = Channel<BuatLaporanKeluhanEvents>()
    val events = _events.receiveAsFlow()

    fun onActions(actions: BuatLaporanKeluhanActions) {
        when (actions) {
            BuatLaporanKeluhanActions.BuatLaporanKeluhan -> buatLaporanKeluhan()
            BuatLaporanKeluhanActions.NavigateUp -> navigateUp()
            is BuatLaporanKeluhanActions.UpdateDescription -> updateDescription(actions.description)
            is BuatLaporanKeluhanActions.UpdateImage -> updateImage(actions.imageUri)
            is BuatLaporanKeluhanActions.UpdateTitle -> updateTitle(actions.title)
            is BuatLaporanKeluhanActions.OnPreviewImage -> onPreviewImage(actions.imageUri)
        }
    }

    private fun onPreviewImage(imageUri: Uri) {
        viewModelScope.launch {
            _events.send(BuatLaporanKeluhanEvents.NavigateToPreviewImage(imageUri))
        }
    }

    private fun updateTitle(title: String) {
        if (title.isEmpty()) {
            _state.update {
                it.copy(
                    title = title,
                    titleError = null,
                    isTitleError = false
                )
            }
            return
        }

        val isTitleValid = PatternValidation.isTitleValid(title)
        val titleError = PatternValidation.getTitleError(title)
        _state.update {
            it.copy(
                title = title,
                titleError = titleError,
                isTitleError = !isTitleValid
            )
        }
    }

    private fun updateImage(image: Uri) {
        _state.update {
            it.copy(
                photoReport = image
            )
        }
    }

    private fun updateDescription(description: String) {
        if (description.isEmpty()) {
            _state.update {
                it.copy(
                    description = description,
                    descriptionError = null,
                    isDescriptionError = false
                )
            }
            return
        }

        val isDescriptionValid = PatternValidation.isDescriptionValid(description)
        val descriptionError = PatternValidation.getDescriptionError(description)
        _state.update {
            it.copy(
                description = description,
                descriptionError = descriptionError,
                isDescriptionError = !isDescriptionValid
            )
        }
    }

    private fun navigateUp() {
        viewModelScope.launch {
            _events.send(BuatLaporanKeluhanEvents.NavigateUp)
        }
    }

    private fun buatLaporanKeluhan() {
        viewModelScope.launch {
            _state.update { it.copy(isButtonLoading = true) }

            val title = state.value.title
            val description = state.value.description
            val imageUri = state.value.photoReport
            val isTitleValid = PatternValidation.isTitleValid(title)
            val isDescriptionValid = PatternValidation.isDescriptionValid(description)

            if (!isTitleValid || !isDescriptionValid) {
                val shakeTarget = when {
                    !isTitleValid && !isDescriptionValid -> ShakeTargetBuatLaporanKeluhan.BOTH
                    !isTitleValid -> ShakeTargetBuatLaporanKeluhan.TITLE
                    else -> ShakeTargetBuatLaporanKeluhan.DESCRIPTION
                }

                shakeTarget.let {
                    _events.send(BuatLaporanKeluhanEvents.ShakeTextField(it))
                    Log.d("Trigger Shake", "Trigger ${shakeTarget.name}")
                }

                // --- 2. UPDATE STATE UNTUK ERROR TEXT ---
                _state.update { currentState ->
                    currentState.copy(
                        isButtonLoading = false,
                        isTitleError = !isTitleValid,
                        titleError = if (!isTitleValid) PatternValidation.getTitleError(title) else null,
                        isDescriptionError = !isDescriptionValid,
                        descriptionError = if (!isDescriptionValid) PatternValidation.getDescriptionError(description) else null
                    )
                }
                return@launch
            }

            _state.update {
                it.copy(
                    isTitleError = false,
                    titleError = null,
                    isDescriptionError = false,
                    descriptionError = null
                )
            }

            val compressedResult = if (imageUri != Uri.EMPTY) {
                imageCompressor.compressImage(imageUri = imageUri, compressionThreshold = 200 * 1024L)
            } else { null}

            buatLaporanKeluhanUseCase(title = title, description = description, compressedResult = compressedResult)
                .onSuccess { result ->
                    _state.update {
                        it.copy(isButtonLoading = false)
                    }
                    navigateToResultScreen(result.toResultLaporanKeluhan())
                }
                .onError {result ->
                    _state.update {
                        it.copy(isButtonLoading = false)
                    }
                    _events.send(BuatLaporanKeluhanEvents.ShowSnackBarError(result.message))
                }

        }
    }

    private fun navigateToResultScreen(resultLaporanKeluhan: ResultLaporanKeluhan) {
        viewModelScope.launch {
            _events.send(BuatLaporanKeluhanEvents.NavigateToResult(TypeResult.BUAT_KELUHAN, resultLaporanKeluhan))
        }
    }
}