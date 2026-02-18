package com.kosrvd.app.feature.management.presentation.ui.screen.penghuni.list_penghuni

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosrvd.app.core.data.source.local.SessionStorage
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.core.domain.utils.onSuccess
import com.kosrvd.app.feature.management.domain.usecase.GetListPenghuniUseCase
import com.kosrvd.app.feature.management.presentation.ui.models.toListPenghuniUi
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

sealed interface ListPenghuniEvent {
    data class NavigateToDetailPenghuni(val idPenghuni: String) : ListPenghuniEvent
    data object NavigateBack : ListPenghuniEvent
}

sealed interface ListPenghuniActions {
    data object TryAgain : ListPenghuniActions
    data object NavigateBack : ListPenghuniActions
    data class NavigateToDetailPenghuni(val idPenghuni: String) : ListPenghuniActions
}

@HiltViewModel
class ListPenghuniViewModel @Inject constructor(
    private val getListPenghuniUseCase: GetListPenghuniUseCase,
    private val sessionStorage: SessionStorage
): ViewModel() {
    private val _state = MutableStateFlow(ListPenghuniUiState())
    val state = _state
        .onStart {
            getRole()
            loadListPenghuni()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ListPenghuniUiState()
        )

    private val _events = Channel<ListPenghuniEvent>()
    val events = _events.receiveAsFlow()

    fun onActions(actions: ListPenghuniActions) {
        when (actions) {
            ListPenghuniActions.NavigateBack -> navigateBack()
            is ListPenghuniActions.NavigateToDetailPenghuni -> navigateToDetailPenghuni(actions.idPenghuni)
            ListPenghuniActions.TryAgain -> loadListPenghuni()
        }
    }

    private fun getRole() {
        viewModelScope.launch {
            _state.update { it.copy(role = sessionStorage.getAuthInfo().role) }
        }
    }

    private fun navigateToDetailPenghuni(idPenghuni: String) {
        viewModelScope.launch {
            _events.send(ListPenghuniEvent.NavigateToDetailPenghuni(idPenghuni))
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(ListPenghuniEvent.NavigateBack)
        }
    }

    private fun loadListPenghuni(){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, loadError = null) }

            getListPenghuniUseCase()
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            loadError = null,
                            listPenghuni = result.map { listPenghuni -> listPenghuni.toListPenghuniUi() }
                        )
                    }
                }
                .onError { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            loadError = result.message,
                            listPenghuni = emptyList()
                        )
                    }
                }
        }
    }
}