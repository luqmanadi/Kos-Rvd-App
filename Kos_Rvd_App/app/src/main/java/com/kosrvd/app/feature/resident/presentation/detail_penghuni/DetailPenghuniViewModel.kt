package com.kosrvd.app.feature.resident.presentation.detail_penghuni

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosrvd.app.core.domain.repository.AccountRepository
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.core.domain.utils.onSuccess
import com.kosrvd.app.feature.resident.presentation.models.toDetailPenghuniUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface DetailPenghuniEvent {
    data object NavigateBack : DetailPenghuniEvent
}

sealed interface DetailPenghuniActions {
    data class TryAgain(val idPenghuni: String) : DetailPenghuniActions
    data object NavigateBack : DetailPenghuniActions
}

@HiltViewModel
class DetailPenghuniViewModel @Inject constructor(
    private val accountRepository: AccountRepository
): ViewModel() {
    private val _state = MutableStateFlow(DetailPenghuniUiState())
    val state = _state

    private val _events = Channel<DetailPenghuniEvent>()
    val events = _events.receiveAsFlow()

    fun onActions(actions: DetailPenghuniActions) {
        when (actions) {
            DetailPenghuniActions.NavigateBack -> navigateBack()
            is DetailPenghuniActions.TryAgain -> loadDetailPenghuni(actions.idPenghuni)
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(DetailPenghuniEvent.NavigateBack)
        }
    }

    fun loadDetailPenghuni(idPenghuni: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, loadError = null) }
            accountRepository.getAccountById(idPenghuni)
                .onError { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            loadError = result.message,
                            penghuni = null
                        )
                    }
                }
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            loadError = null,
                            penghuni = result.toDetailPenghuniUi()
                        )
                    }
                }
        }
    }
}