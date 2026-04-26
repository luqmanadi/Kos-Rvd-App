package com.kosrvd.app.feature.complaint.presentation.detail_keluhan

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosrvd.app.core.data.source.local.SessionStorage
import com.kosrvd.app.core.domain.usecase.CheckNetworkUseCase
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.core.domain.utils.onSuccess
import com.kosrvd.app.presentation.navigation.models.ResultLaporanKeluhan
import com.kosrvd.app.core.presentation.utils.PatternValidation
import com.kosrvd.app.feature.complaint.domain.repository.KeluhanRepository
import com.kosrvd.app.feature.complaint.domain.usecase.ProsesKeluhanUseCase
import com.kosrvd.app.feature.complaint.domain.usecase.SelesaiKeluhanUseCase
import com.kosrvd.app.core.presentation.utils.ImageCompressor
import com.kosrvd.app.core.presentation.utils.TypeResult
import com.kosrvd.app.feature.announcement.presentation.list_pengumuman.PengumumanEvents
import com.kosrvd.app.feature.complaint.presentation.models.toDetailKeluhanUi
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

sealed interface DetailKeluhanEvents {
    data class NavigateToPreviewImage(val imageUri: Uri): DetailKeluhanEvents
    data class ShowBannerError(val message: String): DetailKeluhanEvents
    data class NavigateToResult(val result: TypeResult, val keluhanResult: ResultLaporanKeluhan): DetailKeluhanEvents
    data object NavigateBack: DetailKeluhanEvents
    data object NavigateBackToSendDeleteSnackBar: DetailKeluhanEvents
}

sealed interface DetailKeluhanActions {
    data class SetResponse(val response: String): DetailKeluhanActions
    data class SetResponseImage(val responseImage: Uri): DetailKeluhanActions
    data object ProsesKeluhan: DetailKeluhanActions
    data object SelesaiKeluhan: DetailKeluhanActions
    data object DeleteKeluhan: DetailKeluhanActions
    data object DismissDialog: DetailKeluhanActions
    data object OpenDialog: DetailKeluhanActions
    data class TryAgain(val idKeluhan: String): DetailKeluhanActions
    data object NavigateBack: DetailKeluhanActions
    data class NavigateToPreviewImage(val imageUri: Uri): DetailKeluhanActions
}

@HiltViewModel
class DetailKeluhanViewModel @Inject constructor(
    private val keluhanRepository: KeluhanRepository,
    private val imageCompressor: ImageCompressor,
    private val sessionStorage: SessionStorage,
    private val prosesKeluhanUseCase: ProsesKeluhanUseCase,
    private val selesaiKeluhanUseCase: SelesaiKeluhanUseCase,
    private val checkNetworkUseCase: CheckNetworkUseCase
): ViewModel() {
    private val _state = MutableStateFlow(DetailKeluhanUiState())
    val state = _state
        .onStart { getRole() }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DetailKeluhanUiState()
        )

    private val _events = Channel<DetailKeluhanEvents>()
    val events = _events.receiveAsFlow()

    fun onActions(actions: DetailKeluhanActions){
        when(actions){
            DetailKeluhanActions.DeleteKeluhan -> hapusKeluhan()
            DetailKeluhanActions.DismissDialog -> _state.update { it.copy(isShowDialogDeleteVisible = false) }
            DetailKeluhanActions.NavigateBack -> navigateBack()
            DetailKeluhanActions.OpenDialog -> _state.update { it.copy(isShowDialogDeleteVisible = true) }
            DetailKeluhanActions.ProsesKeluhan -> prosesKeluhan()
            DetailKeluhanActions.SelesaiKeluhan -> selesaiKeluhan()
            is DetailKeluhanActions.SetResponse -> setResponse(actions.response)
            is DetailKeluhanActions.SetResponseImage -> setResponseImage(actions.responseImage)
            is DetailKeluhanActions.TryAgain -> loadDetailKeluhan(actions.idKeluhan)
            is DetailKeluhanActions.NavigateToPreviewImage -> navigateToPreviewImage(actions.imageUri)
        }
    }

    private fun selesaiKeluhan(){
        viewModelScope.launch {
            _state.update { it.copy(isButtonSelesaiLoading = true) }
            val response = _state.value.response

            val responseError = PatternValidation.getResponseError(response)
            val isResponseErrorValid = PatternValidation.isResponseValid(response)
            if (!isResponseErrorValid){
                _state.update {
                    it.copy(
                        isButtonSelesaiLoading = false,
                        responseError = responseError,
                        isResponseError = true,
                        shakeTriggerResponseError = it.shakeTriggerResponseError + 1
                    )
                }
                return@launch
            }

            _state.update {
                it.copy(
                    responseError = null,
                    isResponseError = false
                )
            }
            val responseImage = _state.value.responseImage
            val idKeluhan = _state.value.detailKeluhanUi?.idKeluhan ?: ""
            val nomorKamar = _state.value.detailKeluhanUi?.numberRoom ?: ""
            val namaPelapor = _state.value.detailKeluhanUi?.reporterName ?: ""
            val judulLaporan = _state.value.detailKeluhanUi?.title ?: ""

            val compressedResult = if (responseImage != Uri.EMPTY) {
                imageCompressor.compressImage(imageUri = responseImage, compressionThreshold = 200 * 1024L)
            } else { null}

            selesaiKeluhanUseCase(idKeluhan, response, compressedResult, nomorKamar, namaPelapor, judulLaporan)
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            isButtonSelesaiLoading = false
                        )
                    }
                    navigateToResultScreen(TypeResult.LAPORAN_KELUHAN_SELESAI_DIPROSES, result)
                }
                .onError { result ->
                    _state.update {
                        it.copy(
                            isButtonSelesaiLoading = false,
                            loadError = null
                        )
                    }
                    _events.send(DetailKeluhanEvents.ShowBannerError(result.message))
                }
        }
    }

    private fun prosesKeluhan(){
        viewModelScope.launch {
            _state.update { it.copy(isButtonProsesLoading = true) }

            val idKeluhan = _state.value.detailKeluhanUi?.idKeluhan ?: ""
            val nomorKamar = _state.value.detailKeluhanUi?.numberRoom ?: ""
            val namaPelapor = _state.value.detailKeluhanUi?.reporterName ?: ""
            val judulLaporan = _state.value.detailKeluhanUi?.title ?: ""

            prosesKeluhanUseCase(idKeluhan, nomorKamar, namaPelapor, judulLaporan)
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            isButtonProsesLoading = false
                        )
                    }
                    navigateToResultScreen(TypeResult.KONFIRMASI_SEKALIGUS_MEMPROSES_LAPORAN_KELUHAN, result)
                }
                .onError { result ->
                    _state.update {
                        it.copy(
                            isButtonProsesLoading = false,
                            loadError = null
                        )
                    }
                    _events.send(DetailKeluhanEvents.ShowBannerError(result.message))
                }
        }
    }

    private fun getRole(){
        viewModelScope.launch {
            val role = sessionStorage.getAuthInfo().role
            _state.update {
                it.copy(
                    role = role
                )
            }
        }
    }

    private fun hapusKeluhan() {
        viewModelScope.launch {
            _state.update { it.copy(isButtonDeleteLoading = true) }

            if (!checkNetworkUseCase()){
                _state.update {
                    it.copy(
                        isButtonDeleteLoading = false,
                        isShowDialogDeleteVisible = false
                    )
                }
                _events.send(
                    DetailKeluhanEvents.ShowBannerError("Gagal Hapus Laporan Keluhan dikarenakan Tidak ada koneksi internet. Mohon cek kembali jaringan Anda.")
                )
                return@launch
            }

            val idKeluhan = _state.value.detailKeluhanUi?.idKeluhan ?: ""

            keluhanRepository.delleteKeluhan(idKeluhan)
                .onSuccess { _ ->
                    _state.update {
                        it.copy(
                            isButtonDeleteLoading = false,
                            isShowDialogDeleteVisible = false
                        )
                    }
                    _events.send(DetailKeluhanEvents.NavigateBackToSendDeleteSnackBar)
                }.onError { resultKeluhan ->
                    _state.update {
                        it.copy(
                            isButtonDeleteLoading = false,
                            isShowDialogDeleteVisible = false
                        )
                    }
                    _events.send(DetailKeluhanEvents.ShowBannerError(resultKeluhan.message))
                }
        }
    }

    private fun navigateToPreviewImage(imageUri: Uri) {
        viewModelScope.launch {
            if (_state.value.shakeTriggerResponseError > 0){
                _state.update { it.copy(shakeTriggerResponseError = 0) }
            }
            _events.send(DetailKeluhanEvents.NavigateToPreviewImage(imageUri))
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(DetailKeluhanEvents.NavigateBack)
        }
    }

    private fun setResponse(response: String) {
        if (response.isEmpty()) {
            _state.update {
                it.copy(
                    response = response,
                    responseError = null,
                    isResponseError = false
                )
            }
            return
        }
        val responseError = PatternValidation.getResponseError(response)
        val isResponseErrorValid = PatternValidation.isResponseValid(response)
        _state.update {
            it.copy(
                response = response,
                responseError = responseError,
                isResponseError = !isResponseErrorValid
            )
        }
    }

    private fun setResponseImage(responseImage: Uri){
        _state.update { it.copy(responseImage = responseImage) }
    }

    fun loadDetailKeluhan(idKeluhan: String){
        viewModelScope.launch {
            if (_state.value.detailKeluhanUi != null && _state.value.detailKeluhanUi?.idKeluhan == idKeluhan) {
                return@launch
            }

            _state.update {
                it.copy(
                    isLoading = true,
                    loadError = null,
                )
            }

            keluhanRepository.getDetailKeluhanById(idKeluhan)
                .onSuccess { result ->
                    _state.update { it.copy(isLoading = false, loadError = null, detailKeluhanUi = result.toDetailKeluhanUi()) }
                }
                .onError { result ->
                    _state.update { it.copy(isLoading = false, loadError = result.message) }
                }
        }
    }

    private fun navigateToResultScreen(typeResult: TypeResult, resultLaporanKeluhan: ResultLaporanKeluhan) {
        viewModelScope.launch {
            _events.send(DetailKeluhanEvents.NavigateToResult(typeResult, resultLaporanKeluhan))
        }
    }
}