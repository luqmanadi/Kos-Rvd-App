package com.kosrvd.app.feature.room.presentation.list_kamar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.core.domain.utils.onSuccess
import com.kosrvd.app.feature.room.domain.repository.KamarRepository
import com.kosrvd.app.feature.room.presentation.models.toListKamarUi
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

sealed interface ListKamarEvents {
    data object NavigateBack : ListKamarEvents
    data class NavigateToDetailKamar(val idKamar: String) : ListKamarEvents
    data object NavigateAddKamar : ListKamarEvents
}

sealed interface ListKamarActions {
    data object NavigateBack : ListKamarActions
    data class NavigateToDetailKamar(val idKamar: String) : ListKamarActions
    data object NavigateAddKamar : ListKamarActions
    data object TryAgain: ListKamarActions
}

@HiltViewModel
class ListKamarViewModel @Inject constructor(
    private val kamarRepository: KamarRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ListKamarUiState())
    val state = _state
        .onStart { loadDataListKamar() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(2000L),
            ListKamarUiState()
        )

    private val _events = Channel<ListKamarEvents>()
    val events = _events.receiveAsFlow()

    fun onActions(actions: ListKamarActions){
        when(actions){
            ListKamarActions.NavigateAddKamar -> navigateAddKamar()
            is ListKamarActions.NavigateToDetailKamar -> navigateToDetailKamar(actions.idKamar)
            ListKamarActions.NavigateBack -> navigateBack()
            ListKamarActions.TryAgain -> loadDataListKamar()
        }
    }

    private fun navigateToDetailKamar(idKamar: String) {
        viewModelScope.launch {
            _events.send(ListKamarEvents.NavigateToDetailKamar(idKamar))
        }
    }

    private fun navigateAddKamar() {
        viewModelScope.launch {
            _events.send(ListKamarEvents.NavigateAddKamar)
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(ListKamarEvents.NavigateBack)
        }
    }

    private fun loadDataListKamar(){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, loadError = null) }

            kamarRepository.getAllKamar()
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            loadError = null,
                            listKamarUi = result.map { data -> data.toListKamarUi() }
                        )
                    }
                }
                .onError { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            loadError = result.message,
                            listKamarUi = emptyList()
                        )
                    }
                }
        }
    }
}