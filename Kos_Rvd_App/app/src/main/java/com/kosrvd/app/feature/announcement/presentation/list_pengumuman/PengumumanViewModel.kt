package com.kosrvd.app.feature.announcement.presentation.list_pengumuman

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosrvd.app.core.data.source.local.SessionStorage
import com.kosrvd.app.core.domain.usecase.CheckNetworkUseCase
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.core.domain.utils.onSuccess
import com.kosrvd.app.feature.announcement.domain.repository.PengumumanRepository
import com.kosrvd.app.feature.announcement.presentation.models.toPengumumanUi
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

sealed interface PengumumanEvents{
    data object NavigateBack : PengumumanEvents
    data object NavigateCreatePengumuman: PengumumanEvents
    data class ShowSnackBar(val message: String, val isError: Boolean) : PengumumanEvents
}
sealed interface PengumumanActions {
    data object NavigateBack : PengumumanActions
    data object DeletePengumuman : PengumumanActions
    data object NavigateCreatePengumuman: PengumumanActions
    data object TryAgain : PengumumanActions
    data class ShowDeleteDialog(val idPengumuman: String): PengumumanActions
    data object DismissDeleteDialog: PengumumanActions
}

@HiltViewModel
class PengumumanViewModel @Inject constructor(
    private val pengumumanRepository: PengumumanRepository,
    private val sessionStorage: SessionStorage,
    private val checkNetworkUseCase: CheckNetworkUseCase
): ViewModel() {
    private val _state = MutableStateFlow(PengumumanUiState())
    val state = _state
        .onStart {
            getRole()
            loadPengumuman()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PengumumanUiState()
        )

    private val _events = Channel<PengumumanEvents>()
    val events = _events.receiveAsFlow()

    fun onActions(actions: PengumumanActions){
        when(actions){
            PengumumanActions.DeletePengumuman -> deletePengumuman()
            PengumumanActions.NavigateBack -> navigateBack()
            PengumumanActions.TryAgain -> loadPengumuman()
            PengumumanActions.NavigateCreatePengumuman -> navigateCreatePengumuman()
            PengumumanActions.DismissDeleteDialog -> dismissDeleteDialog()
            is PengumumanActions.ShowDeleteDialog -> showDeleteDialog(actions.idPengumuman)
        }
    }

    private fun showDeleteDialog(idPengumuman: String) {
        _state.update {
            it.copy(
                isDeleteDialogVisible = true,
                idPengumumanForDelete = idPengumuman
            )
        }
    }

    private fun dismissDeleteDialog() {
        _state.update {
            it.copy(
                isDeleteDialogVisible = false,
                idPengumumanForDelete = ""
            )
        }
    }

    private fun deletePengumuman() {
        viewModelScope.launch {
            _state.update { it.copy(
                buttonLoading = true,
                buttonCancelEnabled = false
            )}

            if (!checkNetworkUseCase()){
                _state.update {
                    it.copy(
                        buttonLoading = false,
                        buttonCancelEnabled = true,
                        isDeleteDialogVisible = false,
                        idPengumumanForDelete = ""
                    )
                }
                _events.send(
                    PengumumanEvents.ShowSnackBar("Gagal Hapus Pengumuman dikarenakan Tidak ada koneksi internet. Mohon cek kembali jaringan Anda.", true)
                )
                return@launch
            }
            val idPengumuman = _state.value.idPengumumanForDelete

            pengumumanRepository.deletePengumuman(idPengumuman)
                .onSuccess {
                    _state.update {
                        it.copy(
                            buttonLoading = false,
                            buttonCancelEnabled = true,
                            isDeleteDialogVisible = false,
                            idPengumumanForDelete = ""
                        )
                    }
                    Log.d("PengumumanViewModel", "deletePengumuman")
                    _events.send(PengumumanEvents.ShowSnackBar("Pengumuman berhasil dihapus", false))
                }
                .onError { result ->
                    _state.update {
                        it.copy(
                            buttonLoading = false,
                            buttonCancelEnabled = true,
                            isDeleteDialogVisible = false,
                            idPengumumanForDelete = ""
                        )
                    }
                    Log.d("PengumumanViewModel", "deletePengumuman: ${result.message}")
                    _events.send(PengumumanEvents.ShowSnackBar(result.message, true))
                }
        }
    }

    private fun navigateCreatePengumuman() {
        viewModelScope.launch {
            _events.send(PengumumanEvents.NavigateCreatePengumuman)
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.trySend(PengumumanEvents.NavigateBack)
        }
    }

    private fun getRole(){
        viewModelScope.launch {
            _state.value = _state.value.copy(
                role = sessionStorage.getAuthInfo().role
            )
        }
    }

    private fun loadPengumuman() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                loadError = null
            )

            pengumumanRepository.getAllPengumuman().collect { result ->
                when(result){
                    is Result.Success -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            loadError = null,
                            listPengumuman = result.data.map { it.toPengumumanUi() }
                        )
                    }
                    is Result.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            loadError = result.error.message,
                            listPengumuman = emptyList()
                        )
                    }
                }
            }
        }
    }
}