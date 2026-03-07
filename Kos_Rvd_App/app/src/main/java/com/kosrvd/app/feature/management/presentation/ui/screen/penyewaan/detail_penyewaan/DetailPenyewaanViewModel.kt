package com.kosrvd.app.feature.management.presentation.ui.screen.penyewaan.detail_penyewaan

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.core.domain.utils.onSuccess
import com.kosrvd.app.core.navigation.NavigationScreen
import com.kosrvd.app.feature.management.domain.repository.PenyewaanRepository
import com.kosrvd.app.feature.management.presentation.ui.models.toPenyewaUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface DetailPenyewaEvents {
    data object NavigateBack: DetailPenyewaEvents
    data object NavigateBackToSendEndedRental: DetailPenyewaEvents
    data class ShowSnackBarError(val message: String): DetailPenyewaEvents
}

sealed interface DetailPenyewaActions {
    data object NavigateBack : DetailPenyewaActions
    data object TryAgain: DetailPenyewaActions
    data object EndRental: DetailPenyewaActions
    data object ShowDialogEndRental: DetailPenyewaActions
    data object DismissDialogEndRental: DetailPenyewaActions
}

@HiltViewModel
class DetailPenyewaanViewModel @Inject constructor(
    private val penyewaanRepository: PenyewaanRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _state = MutableStateFlow(DetailPenyewaanUiState())
    val state = _state
        .onStart { loadDetailPenyewa() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(3000L),
            initialValue = DetailPenyewaanUiState()
        )

    private val _events = Channel<DetailPenyewaEvents>()
    val events = _events.receiveAsFlow()

    fun onActions(actions: DetailPenyewaActions) {
        when(actions){
            DetailPenyewaActions.DismissDialogEndRental -> dismissDialogEndRental()
            DetailPenyewaActions.NavigateBack -> navigateBack()
            DetailPenyewaActions.ShowDialogEndRental -> showDialogEndRental()
            DetailPenyewaActions.EndRental -> endRental()
            DetailPenyewaActions.TryAgain -> loadDetailPenyewa()
        }
    }

    private fun endRental() {
        viewModelScope.launch {
            _state.update { it.copy(isButtonLoadingEndRental = true) }
            val idPenyewa = _state.value.penyewaUi?.idPenyewa ?: ""

            penyewaanRepository.endPenyewaan(idPenyewa)
                .onSuccess {
                    _state.update {
                        it.copy(
                            isButtonLoadingEndRental = false,
                            showDialogEndRental = false
                        )
                    }
                    _events.send(DetailPenyewaEvents.NavigateBackToSendEndedRental)
                }
                .onError { result ->
                    _state.update {
                        it.copy(
                            isButtonLoadingEndRental = false,
                            showDialogEndRental = false
                        )
                    }
                    _events.send(DetailPenyewaEvents.ShowSnackBarError(result.message))
                }
        }
    }

    private fun getIdFromSavedStateHandle(): String {
        // Ambil objek argument dari savedStateHandle
        val arguments = savedStateHandle.toRoute<NavigationScreen.DetailPenyewaanScreen>()

        // Ambil idPenyewa dari argument
        val idPenyewa = arguments.idPenyewa
        return idPenyewa
    }

    private fun loadDetailPenyewa() {
        viewModelScope.launch {
            _state.update {  it.copy(isLoading = true, loadError = null) }

            val idPenyewa = getIdFromSavedStateHandle()

            penyewaanRepository.getPenyewaanById(idPenyewa)
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            loadError = null,
                            penyewaUi = result.toPenyewaUi()
                        )
                    }
                }
                .onError { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            penyewaUi = null,
                            loadError = result.message
                        )
                    }
                }
        }
    }

    private fun showDialogEndRental() {
        _state.value = _state.value.copy(showDialogEndRental = true)
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(DetailPenyewaEvents.NavigateBack)
        }
    }

    private fun dismissDialogEndRental() {
        _state.value = _state.value.copy(showDialogEndRental = false)
    }
}