package com.kosrvd.app.feature.management.presentation.ui.screen.pengumuman.buat_pengumuman

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.core.domain.utils.onSuccess
import com.kosrvd.app.core.presentation.utils.PatternValidation
import com.kosrvd.app.feature.management.domain.repository.PengumumanRepository
import com.kosrvd.app.feature.management.domain.usecase.BuatPengumumanUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface BuatPengumumanEvents {
    data object NavigateBack : BuatPengumumanEvents
    data class ShowSnackBarError(val message: String): BuatPengumumanEvents
    data object NavigateBackToSendSuccessCreatePengumuman : BuatPengumumanEvents
}

sealed interface BuatPengumumanActions {
    data object NavigateBack : BuatPengumumanActions
    data object CreatePengumuman : BuatPengumumanActions
    data class UpdateTitle(val title: String) : BuatPengumumanActions
    data class UpdateDescription(val description: String) : BuatPengumumanActions
}

@HiltViewModel
class BuatPengumumanViewModel @Inject constructor(
    private val pengumumanRepository: PengumumanRepository,
    private val buatPengumumanUseCase: BuatPengumumanUseCase
): ViewModel() {

    private val _state = MutableStateFlow(BuatPengumumanUiState())
    val state = _state.asStateFlow()

    private val _events = Channel<BuatPengumumanEvents>()
    val events = _events.receiveAsFlow()

    fun onActions(actions: BuatPengumumanActions) {
        when (actions) {
            BuatPengumumanActions.CreatePengumuman -> createPengumuman()
            BuatPengumumanActions.NavigateBack -> navigateBack()
            is BuatPengumumanActions.UpdateDescription -> updateDescription(actions.description)
            is BuatPengumumanActions.UpdateTitle -> updateTitle(actions.title)
        }

    }

    private fun updateTitle(title: String) {
        _state.update {
            it.copy(
                title = title,
                isTitleError = false,
                titleError = null
            )
        }
    }

    private fun updateDescription(description: String) {
        _state.update {
            it.copy(
                description = description,
                isDescriptionError = false,
                descriptionError = null
            )
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(BuatPengumumanEvents.NavigateBack)
        }
    }

    private fun createPengumuman() {
        viewModelScope.launch {
            _state.update { it.copy(isButtonLoading = true) }
            val title = _state.value.title
            val description = _state.value.description

            val titleError = PatternValidation.getTitleError(title)
            val isTitleValid = PatternValidation.isTitleValid(title)
            val descriptionError = PatternValidation.getDescriptionError(description)
            val isDescriptionValid = PatternValidation.isDescriptionValid(description)
            val shakeTargetTitle = if (!isTitleValid) _state.value.shakeTargetTitle + 1 else _state.value.shakeTargetTitle
            val shakeTargetDescription = if (!isDescriptionValid) _state.value.shakeTargetDescription + 1 else _state.value.shakeTargetDescription

            if (!isTitleValid || !isDescriptionValid){
                _state.update {
                    it.copy(
                        isButtonLoading = false,
                        titleError = titleError,
                        isTitleError = !isTitleValid,
                        shakeTargetTitle = shakeTargetTitle,
                        descriptionError = descriptionError,
                        isDescriptionError = !isDescriptionValid,
                        shakeTargetDescription = shakeTargetDescription
                    )
                }
                return@launch
            }

            _state.update {
                it.copy(
                    titleError = null,
                    isTitleError = false,
                    descriptionError = null,
                    isDescriptionError = false
                )
            }

            buatPengumumanUseCase(title, description)
                .onSuccess {
                    _state.update { it.copy(isButtonLoading = false) }
                    _events.send(BuatPengumumanEvents.NavigateBackToSendSuccessCreatePengumuman)
                }
                .onError { result ->
                    _state.update { it.copy(isButtonLoading = false) }
                    _events.send(BuatPengumumanEvents.ShowSnackBarError(result.message)) }
                }
        }
}