package com.kosrvd.app.feature.management.presentation.ui.screen.tagihan.pengaturan_tagihan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.core.domain.utils.onSuccess
import com.kosrvd.app.feature.management.domain.repository.PengaturanRepository
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

sealed interface PengaturanTagihanEvents {
    data object NavigateBack: PengaturanTagihanEvents
    data class ShowSnackBar(val message: String, val isRedColor: Boolean): PengaturanTagihanEvents
}

sealed interface PengaturanTagihanActions {
    data object NavigateBack: PengaturanTagihanActions
    data object TryAgain: PengaturanTagihanActions
    data class UpdateSwitchUseAutoReminder(val useAutoReminder: Boolean): PengaturanTagihanActions
    data class UpdateSwitchUseGenerateOtomatis(val useGenerateOtomatis: Boolean): PengaturanTagihanActions
}

@HiltViewModel
class PengaturanTagihanViewModel @Inject constructor(
    private val pengaturanRepository: PengaturanRepository
): ViewModel() {
    private val _state = MutableStateFlow(PengaturanTagihanUiState())
    val state = _state
        .onStart { loadPengaturanTagihan() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(3000L),
            initialValue = PengaturanTagihanUiState()
        )

    private val _events = Channel<PengaturanTagihanEvents>()
    val events = _events.receiveAsFlow()

    fun onActions(actions: PengaturanTagihanActions) {
        when (actions) {
            PengaturanTagihanActions.NavigateBack -> navigateBack()
            PengaturanTagihanActions.TryAgain -> loadPengaturanTagihan()
            is PengaturanTagihanActions.UpdateSwitchUseAutoReminder -> updateSwitchUseAutoReminder(actions.useAutoReminder)
            is PengaturanTagihanActions.UpdateSwitchUseGenerateOtomatis -> updateSwitchUseGenerateOtomatis(actions.useGenerateOtomatis)
        }
    }

    private fun updateSwitchUseGenerateOtomatis(newValue: Boolean) {
        viewModelScope.launch {
            val oldValue = !newValue

            _state.update { it.copy(useGenerateOtomatis = newValue, isSwitchUseGenerateOtomatisEnabled = false) }

            pengaturanRepository.updateGenerateTagihanOtomatis(newValue)
                .onSuccess {
                    _state.update { it.copy(isSwitchUseGenerateOtomatisEnabled = true) }
                    _events.send(PengaturanTagihanEvents.ShowSnackBar("Generate Tagihan Otomatis diperbarui", isRedColor = false))
                }
                .onError { result ->
                    _state.update { it.copy(useGenerateOtomatis = oldValue, isSwitchUseGenerateOtomatisEnabled = true) }
                    _events.send(PengaturanTagihanEvents.ShowSnackBar(result.message, isRedColor = true))
                }
        }
    }

    private fun updateSwitchUseAutoReminder(newValue: Boolean) {
        viewModelScope.launch {
            val oldValue = !newValue

            _state.update { it.copy(useAutoReminder = newValue, isSwitchUseAutoReminderEnabled = false) }

            pengaturanRepository.updateAutoReminderPaymentBill(newValue)
                .onSuccess {
                    _state.update { it.copy(isSwitchUseAutoReminderEnabled = true) }
                    _events.send(PengaturanTagihanEvents.ShowSnackBar("Auto Reminder Pembayaran diperbarui", isRedColor = false))
                }
                .onError { result ->
                    _state.update { it.copy(useAutoReminder = oldValue, isSwitchUseAutoReminderEnabled = true) }
                    _events.send(PengaturanTagihanEvents.ShowSnackBar(result.message, isRedColor = true))
                }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(PengaturanTagihanEvents.NavigateBack)
        }
    }

    private fun loadPengaturanTagihan(){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            pengaturanRepository.getPengaturanTagihan()
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            useAutoReminder = result.useAutoReminder,
                            isSwitchUseAutoReminderEnabled = true,
                            useGenerateOtomatis = result.useGenerateOtomatis,
                            isSwitchUseGenerateOtomatisEnabled = true
                        )
                    }
                }
                .onError { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }
        }
    }
}