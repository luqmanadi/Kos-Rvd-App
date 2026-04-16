package com.kosrvd.app.feature.rental.presentation.detail_penyewaan

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.core.domain.utils.onSuccess
import com.kosrvd.app.presentation.navigation.NavigationScreen
import com.kosrvd.app.presentation.navigation.models.AlatElektronikSerialize
import com.kosrvd.app.presentation.navigation.models.InfoPakaiParkirMobilBulananSerialize
import com.kosrvd.app.presentation.navigation.models.toInfoPakaiParkirMobilBulananSerialize
import com.kosrvd.app.feature.rental.domain.repository.PenyewaanRepository
import com.kosrvd.app.feature.rental.presentation.models.toPenyewaUi
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
    data object NavigateBackToSendDeleteRental: DetailPenyewaEvents
    data class NavigateToEditPemakaianParkirMobil(val idPenyewa: String, val pemakaianParkirMobilBulanan: InfoPakaiParkirMobilBulananSerialize?): DetailPenyewaEvents
    data class NavigateToEditPemakaianElektronik(val idPenyewa: String, val pemakaianAlatElektronikBulanan: List<AlatElektronikSerialize>): DetailPenyewaEvents
    data class ShowSnackBarError(val message: String): DetailPenyewaEvents
}

sealed interface DetailPenyewaActions {
    data object NavigateBack : DetailPenyewaActions
    data object TryAgain: DetailPenyewaActions
    data object EndRental: DetailPenyewaActions
    data object ShowDialogEndRental: DetailPenyewaActions
    data object DismissDialogEndRental: DetailPenyewaActions
    data object ShowDialogDeleteRental: DetailPenyewaActions
    data object DismissDialogDeleteRental: DetailPenyewaActions
    data object DeleteRental: DetailPenyewaActions
    data object NavigateToEditPemakaianParkirMobil: DetailPenyewaActions
    data object NavigateToEditPemakaianElektronik: DetailPenyewaActions
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
            DetailPenyewaActions.NavigateToEditPemakaianElektronik -> navigateToEditPemakaianElektronik()
            DetailPenyewaActions.NavigateToEditPemakaianParkirMobil -> navigateToEditPemakaianParkirMobil()
            DetailPenyewaActions.DeleteRental -> deleteRental()
            DetailPenyewaActions.DismissDialogDeleteRental -> dismissDialogDeleteRental()
            DetailPenyewaActions.ShowDialogDeleteRental -> showDialogDeleteRental()
        }
    }

    private fun deleteRental() {
        viewModelScope.launch {
            _state.update { it.copy(isButtonLoadingDeleteRental = true) }
            val idPenyewa = _state.value.penyewaUi?.idPenyewa ?: ""
            penyewaanRepository.deletePenyewaan(idPenyewa)
                .onSuccess {
                    _state.update {
                        it.copy(
                            isButtonLoadingDeleteRental = false,
                            showDialogDeleteRental = false
                        )
                    }
                    _events.send(DetailPenyewaEvents.NavigateBackToSendDeleteRental)
                }
                .onError { error ->
                    _state.update {
                        it.copy(
                            isButtonLoadingDeleteRental = false,
                            showDialogDeleteRental = false
                        )
                    }
                    _events.send(DetailPenyewaEvents.ShowSnackBarError(error.message))
                }
        }
    }

    private fun dismissDialogDeleteRental() {
        _state.update { it.copy(showDialogDeleteRental = false)}
    }

    private fun showDialogDeleteRental() {
        _state.update { it.copy(showDialogDeleteRental = true)}
    }

    private fun navigateToEditPemakaianParkirMobil() {
        viewModelScope.launch {
            val idPenyewa = _state.value.penyewaUi?.idPenyewa ?: ""
            val pemakaianParkirMobilBulanan = _state.value.penyewaUi?.pemakaianParkirMobilBulanan?.toInfoPakaiParkirMobilBulananSerialize()
            _events.send(
                DetailPenyewaEvents.NavigateToEditPemakaianParkirMobil(
                    idPenyewa = idPenyewa,
                    pemakaianParkirMobilBulanan = pemakaianParkirMobilBulanan
                )
            )
        }
    }

    private fun navigateToEditPemakaianElektronik() {
        viewModelScope.launch {
            val idPenyewa = _state.value.penyewaUi?.idPenyewa ?: ""
            val pemakaianAlatElektronikBulanan = _state.value.penyewaUi?.pemakaianAlatElektronikBulanan?.map {
                AlatElektronikSerialize(
                    toolName = it.toolName,
                    cost = it.cost,
                    origin = it.origin
                )
            } ?: emptyList()
            _events.send(
                DetailPenyewaEvents.NavigateToEditPemakaianElektronik(
                    idPenyewa = idPenyewa,
                    pemakaianAlatElektronikBulanan = pemakaianAlatElektronikBulanan
                )
            )
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
        _state.update { it.copy(showDialogEndRental = true)}
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(DetailPenyewaEvents.NavigateBack)
        }
    }

    private fun dismissDialogEndRental() {
        _state.update { it.copy(showDialogEndRental = false)}
    }
}