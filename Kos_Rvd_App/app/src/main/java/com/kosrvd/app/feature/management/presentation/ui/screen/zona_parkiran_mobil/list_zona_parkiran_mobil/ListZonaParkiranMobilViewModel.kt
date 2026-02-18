package com.kosrvd.app.feature.management.presentation.ui.screen.zona_parkiran_mobil.list_zona_parkiran_mobil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosrvd.app.R
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.core.domain.utils.onSuccess
import com.kosrvd.app.feature.management.domain.repository.ZonaParkiranMobilRepository
import com.kosrvd.app.feature.management.presentation.ui.models.toListZonaParkiranMobilUi
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

sealed interface ListZonaParkiranMobilEvents {
    data object NavigateBack: ListZonaParkiranMobilEvents
    data class NavigateToDetailZonaParkiranMobil(val idZonaParkir: String): ListZonaParkiranMobilEvents
    data object NavigateToCreateZonaParkiranMobil: ListZonaParkiranMobilEvents
    data class NavigateToPreviewImageScreen(val imageUrl: String): ListZonaParkiranMobilEvents
}

sealed interface ListZonaParkiranMobilActions {
    data object NavigateBack: ListZonaParkiranMobilActions
    data class NavigateToDetailZonaParkiranMobil(val idZonaParkir: String): ListZonaParkiranMobilActions
    data object NavigateToCreateZonaParkiranMobil: ListZonaParkiranMobilActions
    data object TryAgain: ListZonaParkiranMobilActions
    data class NavigateToPreviewZonaParkiranMobil(val packageName: String): ListZonaParkiranMobilActions
}

@HiltViewModel
class ListZonaParkiranMobilViewModel @Inject constructor(
    private val zonaParkiranMobilRepository: ZonaParkiranMobilRepository
): ViewModel() {
    private val _state = MutableStateFlow(ListZonaParkiranMobilUiState())
    val state = _state
        .onStart { loadListZonaParkiranMobil() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            ListZonaParkiranMobilUiState()
        )

    private val _events = Channel<ListZonaParkiranMobilEvents>()
    val events = _events.receiveAsFlow()

    fun onActions(actions: ListZonaParkiranMobilActions){
        when(actions){
            ListZonaParkiranMobilActions.NavigateBack -> navigateBack()
            ListZonaParkiranMobilActions.NavigateToCreateZonaParkiranMobil -> navigateToCreateZonaParkiranMobil()
            is ListZonaParkiranMobilActions.NavigateToDetailZonaParkiranMobil -> navigateToDetailZonaParkiranMobil(actions.idZonaParkir)
            ListZonaParkiranMobilActions.TryAgain -> loadListZonaParkiranMobil()
            is ListZonaParkiranMobilActions.NavigateToPreviewZonaParkiranMobil -> navigateToPreviewZonaParkiranMobil(actions.packageName)
        }
    }

    private fun navigateToPreviewZonaParkiranMobil(packageName: String) {
        viewModelScope.launch {
            val drawableResId = R.drawable.denah_parkir
            val drawableUriString = "android.resource://$packageName/$drawableResId"
            _events.send(ListZonaParkiranMobilEvents.NavigateToPreviewImageScreen(drawableUriString))
        }
    }

    private fun navigateToDetailZonaParkiranMobil(idZonaParkir: String) {
        viewModelScope.launch {
            _events.send(ListZonaParkiranMobilEvents.NavigateToDetailZonaParkiranMobil(idZonaParkir))
        }
    }

    private fun navigateToCreateZonaParkiranMobil() {
        viewModelScope.launch {
            _events.send(ListZonaParkiranMobilEvents.NavigateToCreateZonaParkiranMobil)
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(ListZonaParkiranMobilEvents.NavigateBack)
        }
    }

    private fun loadListZonaParkiranMobil() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, loadError = null) }

            zonaParkiranMobilRepository.getAllZonaParkiranMobil()
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            loadError = null,
                            listZonaParkiranMobil = result.map { data-> data.toListZonaParkiranMobilUi() }
                        )
                    }
                }
                .onError { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            loadError = error.message
                        )
                    }
                }
        }
    }
}