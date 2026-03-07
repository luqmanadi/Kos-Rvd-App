package com.kosrvd.app.feature.management.presentation.ui.screen.penyewaan.list_penyewaan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.feature.management.domain.repository.PenyewaanRepository
import com.kosrvd.app.feature.management.presentation.ui.models.toListPenyewaanUi
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

sealed interface ListPenyewaanEvents {
    data object NavigateBack: ListPenyewaanEvents
    data class NavigateToDetailPenyewaan(val idPenyewa: String): ListPenyewaanEvents
    data object NavigateToCreatePenyewaan: ListPenyewaanEvents
}

sealed interface ListPenyewaanActions {
    data object NavigateBack: ListPenyewaanActions
    data object TryAgain: ListPenyewaanActions
    data object NavigateToCreatePenyewaan: ListPenyewaanActions
    data class NavigateToDetailPenyewaan(val idPenyewaan: String): ListPenyewaanActions
}

@HiltViewModel
class ListPenyewaanViewModel @Inject constructor(
    private val penyewaanRepository: PenyewaanRepository
): ViewModel() {
    private val _state = MutableStateFlow(ListPenyewaanUiState())
    val state = _state
        .onStart { loadListPenyewaan() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = ListPenyewaanUiState()
        )

    private val _events = Channel<ListPenyewaanEvents>()
    val events = _events.receiveAsFlow()

    fun onActions(actions: ListPenyewaanActions){
        when(actions){
            ListPenyewaanActions.NavigateBack -> navigateBack()
            ListPenyewaanActions.NavigateToCreatePenyewaan -> navigateToCreatePenyewaan()
            is ListPenyewaanActions.NavigateToDetailPenyewaan -> navigateToDetailPenyewaan(actions.idPenyewaan)
            ListPenyewaanActions.TryAgain -> loadListPenyewaan()
        }
    }

    private fun navigateToDetailPenyewaan(idPenyewaan: String) {
        viewModelScope.launch {
            _events.send(ListPenyewaanEvents.NavigateToDetailPenyewaan(idPenyewaan))
        }
    }

    private fun navigateToCreatePenyewaan() {
        viewModelScope.launch {
            _events.send(ListPenyewaanEvents.NavigateToCreatePenyewaan)
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(ListPenyewaanEvents.NavigateBack)
        }
    }

    private fun loadListPenyewaan() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, loadError = null) }

            penyewaanRepository.getAllPenyewaanFlow().collect { result ->
                    when(result){
                        is Result.Success -> {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    loadError = null,
                                    listPenyewaanUi = result.data.map { data -> data.toListPenyewaanUi() }
                                )
                            }
                        }
                        is Result.Error -> {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    loadError = result.error.message,
                                    listPenyewaanUi = emptyList()
                                )
                            }
                        }
                    }
                }
        }
    }
}