package com.kosrvd.app.feature.management.presentation.ui.screen.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.feature.management.domain.usecase.GetDataDashboardUseCase
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

sealed interface DashboardEvent {

    data object NavigateToPenyewaan: DashboardEvent
    data object NavigateToAkunPengguna: DashboardEvent
    data object NavigateToRiwayatParkirMobil: DashboardEvent
    data object NavigateToZonaParkir: DashboardEvent
    data object NavigateToKamar: DashboardEvent
    data object NavigateToListResident : DashboardEvent
    data object NavigateToAnnouncement : DashboardEvent
    data object NavigateToWhatsAppAdmin : DashboardEvent
    data object NavigateToMapsKosRVD : DashboardEvent
    data class NavigateToDetailBill(val idBill: String) : DashboardEvent
}

sealed interface DashboardActions {
    data object NavigateToPenyewaan: DashboardActions
    data object NavigateToAkunPengguna: DashboardActions
    data object NavigateToRiwayatParkirMobil: DashboardActions
    data object NavigateToZonaParkir: DashboardActions
    data object NavigateToKamar: DashboardActions
    data object NavigateToListResident : DashboardActions
    data object NavigateToAnnouncement : DashboardActions
    data object NavigateToWhatsAppAdmin : DashboardActions
    data object NavigateToMapsKosRVD : DashboardActions
    data class NavigateToDetailBill(val idBill: String) : DashboardActions
    data object TryAgain : DashboardActions
    data object OpenDialogRationale : DashboardActions
    data object CloseDialogRationale : DashboardActions
}

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getDataDashboardUseCase: GetDataDashboardUseCase
): ViewModel() {

    private val _state = MutableStateFlow(DashboardUiState())
    val state = _state
        .onStart { loadDataDashboard() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DashboardUiState()
        )

    private val _events = Channel<DashboardEvent>()
    val events = _events.receiveAsFlow()

    fun onActions(actions: DashboardActions) {
        when (actions) {
            DashboardActions.NavigateToAnnouncement -> navigateToAnnouncement()
            DashboardActions.NavigateToListResident -> navigateToListResident()
            DashboardActions.NavigateToWhatsAppAdmin -> navigateToWhatsAppAdmin()
            DashboardActions.TryAgain -> loadDataDashboard()
            DashboardActions.NavigateToMapsKosRVD -> navigateToMapsKosRVD()
            is DashboardActions.NavigateToDetailBill -> navigateToDetailBill(actions.idBill)
            DashboardActions.NavigateToAkunPengguna -> navigateToAkunPengguna()
            DashboardActions.NavigateToKamar -> navigateToKamar()
            DashboardActions.NavigateToPenyewaan -> navigateToPenyewaan()
            DashboardActions.NavigateToRiwayatParkirMobil -> navigateToRiwayatParkirMobil()
            DashboardActions.NavigateToZonaParkir -> navigateToZonaParkir()
            DashboardActions.CloseDialogRationale -> closeDialogRationale()
            DashboardActions.OpenDialogRationale -> openDialogRationale()
        }
    }

    private fun openDialogRationale() {
        _state.update { it.copy(showRationaleDialog = true) }
    }

    private fun closeDialogRationale() {
        _state.update { it.copy(showRationaleDialog = false) }
    }

    private fun navigateToZonaParkir() {
        viewModelScope.launch {
            _events.send(DashboardEvent.NavigateToZonaParkir)
        }
    }

    private fun navigateToRiwayatParkirMobil() {
        viewModelScope.launch {
            _events.send(DashboardEvent.NavigateToRiwayatParkirMobil)
        }
    }

    private fun navigateToPenyewaan() {
        viewModelScope.launch {
            _events.send(DashboardEvent.NavigateToPenyewaan)
        }
    }

    private fun navigateToKamar() {
        viewModelScope.launch {
            _events.send(DashboardEvent.NavigateToKamar)
        }
    }

    private fun navigateToAkunPengguna() {
        viewModelScope.launch {
            _events.send(DashboardEvent.NavigateToAkunPengguna)
        }
    }

    private fun navigateToMapsKosRVD() {
        viewModelScope.launch {
            _events.send(DashboardEvent.NavigateToMapsKosRVD)
        }
    }

    private fun navigateToAnnouncement() {
        viewModelScope.launch {
            _events.send(DashboardEvent.NavigateToAnnouncement)
        }
    }

    private fun navigateToListResident() {
        viewModelScope.launch {
            _events.send(DashboardEvent.NavigateToListResident)
        }
    }

    private fun navigateToWhatsAppAdmin() {
        viewModelScope.launch {
            _events.send(DashboardEvent.NavigateToWhatsAppAdmin)
        }
    }

    private fun navigateToDetailBill(idBill: String) {
        viewModelScope.launch {
            _events.send(DashboardEvent.NavigateToDetailBill(idBill))
        }
    }

    private fun loadDataDashboard() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, loadError = null) }
            getDataDashboardUseCase().collect { result ->
                when(result){
                    is Result.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                penghuniDashboardUi = result.data.penghuniDashboardUi,
                                adminDashboardUi = result.data.adminDashboardUi,
                                loadError = null
                            )
                        }
                    }
                    is Result.Error -> {
                        _state.update { it.copy(isLoading = false, loadError = result.error.message) }
                    }
                }
            }
        }
    }
}