package com.kosrvd.app.feature.management.presentation.ui.screen.zona_parkiran_mobil.detail_zona_parkiran_mobil

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.core.domain.utils.onSuccess
import com.kosrvd.app.core.navigation.NavigationScreen
import com.kosrvd.app.feature.management.domain.repository.ZonaParkiranMobilRepository
import com.kosrvd.app.feature.management.presentation.designsystem.utils.TypeEditZonaParkir
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

sealed interface DetailZonaParkiranMobilEvents {
    data object NavigateBack: DetailZonaParkiranMobilEvents
    data object NavigateBackToSendMessageDeleteZonaParkir: DetailZonaParkiranMobilEvents
    data class NavigateToEditEditZonaParkiranMobil(
        val idZonaParkir: String,
        val typeEditZonaParkir: TypeEditZonaParkir,
        val biayaBulanan: Long? = null,
        val biayaHarian: Long? = null,
        val namaZona: String? = null
    ): DetailZonaParkiranMobilEvents
    data class ShowSnackBarError(val message: String): DetailZonaParkiranMobilEvents
}

sealed interface DetailZonaParkiranMobilActions {
    data object NavigateBack: DetailZonaParkiranMobilActions
    data object DeleteZonaParkir: DetailZonaParkiranMobilActions
    data object TryAgain: DetailZonaParkiranMobilActions
    data object NavigateToEditZoneName: DetailZonaParkiranMobilActions
    data object NavigateToEditMonthlyFee: DetailZonaParkiranMobilActions
    data object NavigateToEditDailyCosts: DetailZonaParkiranMobilActions
    data object ShowDialogDeleteZonaParkir: DetailZonaParkiranMobilActions
    data object DismissDialogDeleteZonaParkir: DetailZonaParkiranMobilActions
}

@HiltViewModel
class DetailZonaParkiranMobilViewModel @Inject constructor(
    private val zonaParkiranMobilRepository: ZonaParkiranMobilRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _state = MutableStateFlow(DetailZonaParkiranMobilUiState())
    val state = _state
        .onStart { loadDetailZonaParkiranMobil() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = DetailZonaParkiranMobilUiState()
        )

    private val _events = Channel<DetailZonaParkiranMobilEvents>()
    val events = _events.receiveAsFlow()

    fun onActions(actions: DetailZonaParkiranMobilActions){
        when(actions) {
            DetailZonaParkiranMobilActions.DeleteZonaParkir -> deleteZonaParkir()
            DetailZonaParkiranMobilActions.DismissDialogDeleteZonaParkir -> dismissDialogDeleteZonaParkir()
            DetailZonaParkiranMobilActions.NavigateBack -> navigateBack()
            DetailZonaParkiranMobilActions.NavigateToEditDailyCosts -> navigateToEditDailyCosts()
            DetailZonaParkiranMobilActions.NavigateToEditMonthlyFee -> navigateToEditMonthlyFee()
            DetailZonaParkiranMobilActions.NavigateToEditZoneName -> navigateToEditZoneName()
            DetailZonaParkiranMobilActions.ShowDialogDeleteZonaParkir -> showDialogDeleteZonaParkir()
            DetailZonaParkiranMobilActions.TryAgain -> loadDetailZonaParkiranMobil()
        }
    }

    private fun showDialogDeleteZonaParkir() {
        _state.update { it.copy(showDialogDeleteZonaParkir = true) }
    }

    private fun navigateToEditZoneName() {
        viewModelScope.launch {
            val idZonaParkir = _state.value.detailZonaParkiranMobilUi?.idZonaParkir ?: ""
            val namaZona = _state.value.detailZonaParkiranMobilUi?.zoneName ?: ""
            _events.send(
                DetailZonaParkiranMobilEvents.NavigateToEditEditZonaParkiranMobil(
                    idZonaParkir = idZonaParkir,
                    typeEditZonaParkir = TypeEditZonaParkir.EDIT_NAMA_ZONA,
                    namaZona = namaZona
                )
            )
        }
    }

    private fun navigateToEditMonthlyFee() {
        viewModelScope.launch {
            val idZonaParkir = _state.value.detailZonaParkiranMobilUi?.idZonaParkir ?: ""
            val biayaBulanan = _state.value.detailZonaParkiranMobilUi?.monthlyFee ?: 0
            _events.send(
                DetailZonaParkiranMobilEvents.NavigateToEditEditZonaParkiranMobil(
                    idZonaParkir = idZonaParkir,
                    typeEditZonaParkir = TypeEditZonaParkir.EDIT_BIAYA_BULANAN,
                    biayaBulanan = biayaBulanan
                )
            )
        }
    }

    private fun navigateToEditDailyCosts() {
        viewModelScope.launch {
            val idZonaParkir = _state.value.detailZonaParkiranMobilUi?.idZonaParkir ?: ""
            val biayaHarian = _state.value.detailZonaParkiranMobilUi?.dailyCosts ?: 0
            _events.send(
                DetailZonaParkiranMobilEvents.NavigateToEditEditZonaParkiranMobil(
                    idZonaParkir = idZonaParkir,
                    typeEditZonaParkir = TypeEditZonaParkir.EDIT_BIAYA_HARIAN,
                    biayaHarian = biayaHarian
                )
            )
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(DetailZonaParkiranMobilEvents.NavigateBack)
        }
    }

    private fun dismissDialogDeleteZonaParkir() {
        _state.update { it.copy(showDialogDeleteZonaParkir = false) }
    }

    private fun deleteZonaParkir() {
        viewModelScope.launch {
            _state.update { it.copy(buttonDeleteIsLoading = true) }
            val idZonaParkir = _state.value.detailZonaParkiranMobilUi?.idZonaParkir ?: ""

            zonaParkiranMobilRepository.deleteZonaParkiranMobil(idZonaParkir)
                .onSuccess {
                    _state.update {
                        it.copy(
                            buttonDeleteIsLoading = false,
                            showDialogDeleteZonaParkir = false
                        )
                    }
                    _events.send(DetailZonaParkiranMobilEvents.NavigateBackToSendMessageDeleteZonaParkir)
                }
                .onError {result ->
                    _state.update {
                        it.copy(
                            buttonDeleteIsLoading = false,
                            showDialogDeleteZonaParkir = false
                        )
                    }
                    _events.send(DetailZonaParkiranMobilEvents.ShowSnackBarError(result.message))
                }
        }
    }

    private fun getIdFromSavedStateHandle(): String {
        // Ambil objek argument dari savedStateHandle
        val arguments = savedStateHandle.toRoute<NavigationScreen.DetailZonaParkirScreen>()

        // Ambil idZonaParkir dari argument
        val idZonaParkir = arguments.idZonaParkir
        return idZonaParkir
    }

    private fun loadDetailZonaParkiranMobil(){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, loadError = null) }
            val idZonaParkir = getIdFromSavedStateHandle()

            zonaParkiranMobilRepository.getZonaParkiranMobilById(idZonaParkir)
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            loadError = null,
                            detailZonaParkiranMobilUi = result
                        )
                    }
                }
                .onError { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            loadError = result.message
                        )
                    }
                }
        }
    }
}