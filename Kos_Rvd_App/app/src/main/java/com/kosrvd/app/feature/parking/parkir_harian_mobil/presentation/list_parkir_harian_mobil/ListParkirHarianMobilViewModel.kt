package com.kosrvd.app.feature.parking.parkir_harian_mobil.presentation.list_parkir_harian_mobil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.core.domain.utils.onSuccess
import com.kosrvd.app.feature.parking.parkir_harian_mobil.domain.usecase.GetListParkirHarianMobilUseCase
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

sealed interface ListParkirHarianMobilEvents {
    data object NavigateBack: ListParkirHarianMobilEvents
    data object NavigateToTambahParkirHarianMobil: ListParkirHarianMobilEvents
    data class NavigateToDetailParkirHarianMobil(val idParkirHarianMobil: String): ListParkirHarianMobilEvents
}

sealed interface ListParkirHarianMobilActions {
    data object NavigateBack: ListParkirHarianMobilActions
    data object TryAgain: ListParkirHarianMobilActions
    data class NavigateToDetailParkirHarianMobil(val idParkirHarianMobil: String): ListParkirHarianMobilActions
    data object NavigateToTambahParkirHarianMobil: ListParkirHarianMobilActions
}

@HiltViewModel
class ListParkirHarianMobilViewModel @Inject constructor(
    private val getListParkirHarianMobilUseCase: GetListParkirHarianMobilUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ListParkirHarianMobilUiState())
    val state = _state
        .onStart { loadDataListParkirHarianMobil() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(1000L),
            initialValue = ListParkirHarianMobilUiState()
        )

    private val _events = Channel<ListParkirHarianMobilEvents>()
    val events = _events.receiveAsFlow()

    fun onActions(actions: ListParkirHarianMobilActions){
        when(actions){
            ListParkirHarianMobilActions.NavigateBack -> navigateBack()
            is ListParkirHarianMobilActions.NavigateToDetailParkirHarianMobil -> navigateToDetailParkirHarianMobil(actions.idParkirHarianMobil)
            ListParkirHarianMobilActions.NavigateToTambahParkirHarianMobil -> navigateToTambahParkirHarianMobil()
            ListParkirHarianMobilActions.TryAgain -> loadDataListParkirHarianMobil()
        }
    }

    private fun navigateToTambahParkirHarianMobil() {
        viewModelScope.launch {
            _events.send(ListParkirHarianMobilEvents.NavigateToTambahParkirHarianMobil)
        }
    }

    private fun navigateToDetailParkirHarianMobil(idParkirHarianMobil: String) {
        viewModelScope.launch {
            _events.send(ListParkirHarianMobilEvents.NavigateToDetailParkirHarianMobil(idParkirHarianMobil))
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(ListParkirHarianMobilEvents.NavigateBack)
        }
    }

    private fun loadDataListParkirHarianMobil() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, loadError = null) }

            getListParkirHarianMobilUseCase()
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            loadError = null,
                            listParkirHarianMobil = result
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