package com.kosrvd.app.feature.management.presentation.ui.screen.profile.detail_sewa_kamar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.core.domain.utils.onSuccess
import com.kosrvd.app.feature.management.domain.repository.PenyewaanRepository
import com.kosrvd.app.feature.management.presentation.ui.models.toDetailSewaKamarUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface DetailSewaKamarEvents {
    data object NavigateBack: DetailSewaKamarEvents
}

sealed interface DetailSewaKamarActions {
    data class TryAgain(val idPenyewa: String): DetailSewaKamarActions
    data object NavigateBack: DetailSewaKamarActions
}

@HiltViewModel
class DetailSewaKamarViewModel @Inject constructor(
    private val penyewaanRepository: PenyewaanRepository
): ViewModel() {
    private val _state = MutableStateFlow(DetailSewaKamarUiState())
    val state = _state.asStateFlow()

    private val _events = Channel<DetailSewaKamarEvents>()
    val events = _events.receiveAsFlow()

    fun onActions(actions: DetailSewaKamarActions){
        when(actions){
            is DetailSewaKamarActions.TryAgain -> loadDataPenyewaan(actions.idPenyewa)
            DetailSewaKamarActions.NavigateBack -> navigateBack()
        }
    }

    fun loadDataPenyewaan(idPenyewa: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            penyewaanRepository.getPenyewaanById(idPenyewa)
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = null,
                            detailSewaKamarUi = result.toDetailSewaKamarUi()
                        )
                    }
                }
                .onError { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.message,
                            detailSewaKamarUi = null
                        )
                    }
                }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(DetailSewaKamarEvents.NavigateBack)
        }
    }

}