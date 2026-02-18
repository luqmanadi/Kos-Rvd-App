package com.kosrvd.app.feature.management.presentation.ui.screen.akun.list_akun

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosrvd.app.core.data.source.local.SessionStorage
import com.kosrvd.app.core.domain.repository.AccountRepository
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.feature.management.presentation.ui.models.toListAkunUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ListAkunEvents {
    data object NavigateBack: ListAkunEvents
    data class NavigateToDetailAkun(val idAkun: String): ListAkunEvents
    data object NavigateToCreateAkun: ListAkunEvents
}

sealed interface ListAkunActions {
    data object NavigateBack: ListAkunActions
    data class NavigateToDetailAkun(val idAkun: String): ListAkunActions
    data object NavigateToCreateAkun: ListAkunActions
    data object TryAgain: ListAkunActions
}

@HiltViewModel
class ListAkunViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val sessionStorage: SessionStorage
): ViewModel() {

    private val _state = MutableStateFlow(ListAkunUiState())
    val state = _state
        .onStart { loadListAkun() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            ListAkunUiState()
        )

    private val _events = Channel<ListAkunEvents>()
    val events = _events.receiveAsFlow()

    fun onActions(actions: ListAkunActions){
        when(actions){
            ListAkunActions.NavigateBack -> navigateBack()
            ListAkunActions.NavigateToCreateAkun -> navigateToCreateAkun()
            is ListAkunActions.NavigateToDetailAkun -> navigateToDetailAkun(actions.idAkun)
            ListAkunActions.TryAgain -> loadListAkun()
        }
    }

    private fun loadListAkun() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, loadError = null) }
            val sessionIdAkun = sessionStorage.getAuthInfo().idAkun
            accountRepository.getAllAccountWithFlow(sessionIdAkun).collect { result ->
                when(result){
                    is Result.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                loadError = result.error.message,
                                listAkun = emptyList()
                            )
                        }
                    }
                    is Result.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                loadError = null,
                                listAkun = result.data.map { account -> account.toListAkunUi() }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun navigateToDetailAkun(idAkun: String) {
        viewModelScope.launch {
            _events.send(ListAkunEvents.NavigateToDetailAkun(idAkun))
        }
    }

    private fun navigateToCreateAkun() {
        viewModelScope.launch {
            _events.send(ListAkunEvents.NavigateToCreateAkun)
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(ListAkunEvents.NavigateBack)
        }
    }
}