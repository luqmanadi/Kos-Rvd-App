package com.kosrvd.app.feature.management.presentation.ui.screen.tagihan.buat_tagihan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.core.domain.utils.onSuccess
import com.kosrvd.app.core.navigation.models.ResultTagihan
import com.kosrvd.app.feature.management.data.mappers.toResultTagihan
import com.kosrvd.app.feature.management.domain.model.Penyewaan
import com.kosrvd.app.feature.management.domain.repository.PenyewaanRepository
import com.kosrvd.app.feature.management.domain.usecase.BuatTagihanUseCase
import com.kosrvd.app.feature.management.presentation.designsystem.utils.TypeResult
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

sealed interface BuatTagihanEvents{
    data class NavigateToResult(val typeResult: TypeResult, val resultBuatTagihan: ResultTagihan): BuatTagihanEvents
    data object NavigateBack: BuatTagihanEvents
    data class ShowErrorBanner(val message: String): BuatTagihanEvents
}

sealed interface BuatTagihanActions{
    data object BuatTagihan: BuatTagihanActions
    data class UpdateItemSelected(val item: Penyewaan): BuatTagihanActions
    data class UpdateAdminFees(val adminFees: Boolean): BuatTagihanActions
    data object NavigateBack: BuatTagihanActions
    data object TryAgain: BuatTagihanActions
}


@HiltViewModel
class BuatTagihanViewModel @Inject constructor(
    private val buatTagihanUseCase: BuatTagihanUseCase,
    private val penyewaanRepository: PenyewaanRepository
): ViewModel() {
    private val _state = MutableStateFlow(BuatTagihanUiState())
    val state = _state
        .onStart { loadPenyewaan() }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = BuatTagihanUiState()
        )

    private val _events = Channel<BuatTagihanEvents>()
    val events = _events.receiveAsFlow()

    fun onActions(actions: BuatTagihanActions){
        when(actions) {
            BuatTagihanActions.BuatTagihan -> buatTagihan()
            BuatTagihanActions.NavigateBack -> navigateBack()
            BuatTagihanActions.TryAgain -> loadPenyewaan()
            is BuatTagihanActions.UpdateAdminFees -> updateAdminFees(actions.adminFees)
            is BuatTagihanActions.UpdateItemSelected -> updateItemSelected(actions.item)
        }
    }

    private fun updateItemSelected(item: Penyewaan) {
        _state.update {
            it.copy(
                itemSelected = item
            )
        }
    }

    private fun updateAdminFees(adminFees: Boolean) {
        _state.update {
            it.copy(
                adminFees = adminFees
            )
        }
    }

    private fun loadPenyewaan() {
        viewModelScope.launch {
            _state.update { it.copy(isButtonErrorLoading = true) }
            penyewaanRepository.getAllPenyewaanActive()
                .onSuccess { result ->
                    _state.update { it.copy(listPenyewaan = result, loadError = null, isButtonErrorLoading = false) } }
                .onError { result ->
                    _state.update { it.copy(listPenyewaan = emptyList(), loadError = result.message, isButtonErrorLoading = false) }
                }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(BuatTagihanEvents.NavigateBack)
        }
    }

    private fun buatTagihan() {
        viewModelScope.launch {
            _state.update { it.copy(isButtonLoading = true) }

            val itemSelected = _state.value.itemSelected
            val adminFees = _state.value.adminFees

            if (itemSelected == null) {
                _state.update { it.copy(isButtonLoading = false) }
                _events.send(BuatTagihanEvents.ShowErrorBanner("Belum Memilih Penyewa Kamar"))
                return@launch
            }

            buatTagihanUseCase(item = itemSelected, adminFees = adminFees)
                .onSuccess { result->
                    _state.update { it.copy(isButtonLoading = false) }
                    navigateToResult(resultBuatTagihan = result.toResultTagihan())
                }
                .onError { result->
                    _state.update { it.copy(isButtonLoading = false) }
                    _events.send(BuatTagihanEvents.ShowErrorBanner(result.message))
                }
        }
    }

    private fun navigateToResult(resultBuatTagihan: ResultTagihan) {
        viewModelScope.launch {
            _events.send(BuatTagihanEvents.NavigateToResult(TypeResult.PEMBUATAN_TAGIHAN, resultBuatTagihan))
        }
    }
}