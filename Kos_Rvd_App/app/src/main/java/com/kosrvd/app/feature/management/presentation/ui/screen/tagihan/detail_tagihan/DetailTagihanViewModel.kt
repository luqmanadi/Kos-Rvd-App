package com.kosrvd.app.feature.management.presentation.ui.screen.tagihan.detail_tagihan

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosrvd.app.core.data.source.local.SessionStorage
import com.kosrvd.app.core.domain.utils.Role
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.core.domain.utils.onSuccess
import com.kosrvd.app.core.navigation.models.ResultTagihan
import com.kosrvd.app.core.presentation.utils.PatternValidation
import com.kosrvd.app.feature.management.domain.repository.TagihanRepository
import com.kosrvd.app.feature.management.domain.usecase.BayarTagihanLangsungLunasUseCase
import com.kosrvd.app.feature.management.domain.usecase.BayarTagihanUseCase
import com.kosrvd.app.feature.management.domain.usecase.TolakTagihanUseCase
import com.kosrvd.app.feature.management.domain.usecase.VerifikasiTagihanUseCase
import com.kosrvd.app.feature.management.presentation.designsystem.utils.ImageCompressor
import com.kosrvd.app.feature.management.presentation.designsystem.utils.TypeResult
import com.kosrvd.app.feature.management.presentation.ui.models.toDetailTagihanUi
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

sealed interface DetailTagihanEvents{
    data object NavigateBack: DetailTagihanEvents
    data class NavigateToPreviewImage(val imageUri: Uri): DetailTagihanEvents
    data class NavigateToResult (val typeResult: TypeResult, val resultTagihan: ResultTagihan) : DetailTagihanEvents
    data class ShowSnackBarError(val message: String) : DetailTagihanEvents
    data object NavigateBackToSendDeleteSnackBar: DetailTagihanEvents
}
sealed interface DetailTagihanActions {
    data object NavigateBack: DetailTagihanActions
    data class UpdateProofOfPayment(val imageUri: Uri): DetailTagihanActions
    data class TryAgain(val idTagihan: String): DetailTagihanActions
    data class NavigateToPreviewImage(val imageUri: Uri): DetailTagihanActions
    data object BayarOrKirimTagihan: DetailTagihanActions
    data object VerifikasiPembayaranTagihan: DetailTagihanActions
    data object KirimTolakPembayaranTagihan: DetailTagihanActions
    data object HapusTagihan: DetailTagihanActions
    data object ShowHapusDialog: DetailTagihanActions
    data object DismissHapusDialog: DetailTagihanActions
    data object ShowTolakDialog: DetailTagihanActions
    data object DismissTolakDialog: DetailTagihanActions
    data class UpdateAlasanPenolakan(val alasanPenolakan: String): DetailTagihanActions
}

@HiltViewModel
class DetailTagihanViewModel @Inject constructor(
    private val tagihanRepository: TagihanRepository,
    private val imageCompressor: ImageCompressor,
    private val bayarTagihanUseCase: BayarTagihanUseCase,
    private val bayarTagihanLangsungLunasUseCase: BayarTagihanLangsungLunasUseCase,
    private val verifikasiTagihanUseCase: VerifikasiTagihanUseCase,
    private val tolakTagihanUseCase: TolakTagihanUseCase,
    private val sessionStorage: SessionStorage
): ViewModel() {

    private val _state = MutableStateFlow(DetailTagihanUiState())
    val state = _state.onStart { getRole() }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DetailTagihanUiState()
    )


    private val _events = Channel<DetailTagihanEvents>()
    val events = _events.receiveAsFlow()

    fun onActions(actions: DetailTagihanActions){
        when(actions){
            DetailTagihanActions.DismissHapusDialog -> {
                _state.update { it.copy(isHapusDialogVisible = false) }
            }
            DetailTagihanActions.DismissTolakDialog -> {
                _state.update { it.copy(isTolakBottomSheetVisible = false) }
            }
            DetailTagihanActions.ShowHapusDialog -> {
                _state.update { it.copy(isHapusDialogVisible = true) }
            }
            DetailTagihanActions.ShowTolakDialog -> {
                _state.update { it.copy(isTolakBottomSheetVisible = true) }
            }
            DetailTagihanActions.BayarOrKirimTagihan -> {
                bayarOrKirimTagihan()
            }
            DetailTagihanActions.NavigateBack -> {
                navigateBack()
            }
            DetailTagihanActions.HapusTagihan -> {
                hapusTagihan()
            }
            DetailTagihanActions.KirimTolakPembayaranTagihan -> {
                kirimTolakPembayaranTagihan()
            }
            DetailTagihanActions.VerifikasiPembayaranTagihan -> {
                verifikasiPembayaranTagihan()
            }
            is DetailTagihanActions.UpdateAlasanPenolakan -> {
                updateAlasanPenolakan(actions.alasanPenolakan)
            }
            is DetailTagihanActions.NavigateToPreviewImage -> {
                navigateToPreviewImage(actions.imageUri)
            }
            is DetailTagihanActions.TryAgain -> {
                loadDetailTagihan(actions.idTagihan)
            }
            is DetailTagihanActions.UpdateProofOfPayment -> {
                updateProofOfPayment(actions.imageUri)
            }
        }
    }

    private fun updateAlasanPenolakan(alasanPenolakan: String) {
        if (alasanPenolakan.isEmpty()) {
            _state.update {
                it.copy(
                    alasanPenolakan = alasanPenolakan,
                    alasanPenolakanError = null,
                    isAlasanPenolakanError = false
                )
            }
            return
        }

        val alasanPenolakanError = PatternValidation.getAlasanPenolakanError(alasanPenolakan)
        val isAlasanPenolakanValid = PatternValidation.isAlasanPenolakanValid(alasanPenolakan)
        _state.update {
            it.copy(
                alasanPenolakan = alasanPenolakan,
                alasanPenolakanError = alasanPenolakanError,
                isAlasanPenolakanError = !isAlasanPenolakanValid
            )
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

    private fun verifikasiPembayaranTagihan() {
        viewModelScope.launch {
            _state.update { it.copy(isButtonVerifikasiLoading = true) }

            val idTagihan = _state.value.detailTagihanUi?.idTagihan ?: ""
            val bulan = _state.value.detailTagihanUi?.billingMonth ?: ""
            val jumlahDibayar = _state.value.detailTagihanUi?.billAmount ?: 0
            val nomorKamar = _state.value.detailTagihanUi?.numberRoom ?: 0

            verifikasiTagihanUseCase(
                idTagihan = idTagihan,
                bulan = bulan,
                jumlahDibayar = jumlahDibayar,
                nomorKamar = nomorKamar
            ).onSuccess { resultTagihan ->
                _state.update {
                    it.copy(isButtonVerifikasiLoading = false)
                }
                navigateToResultScreen(TypeResult.BERHASIL_VERIFIKASI_PEMBAYARAN_TAGIHAN, resultTagihan)
            }.onError { resultTagihan ->
                _state.update {
                    it.copy(isButtonVerifikasiLoading = false)
                }
                _events.send(DetailTagihanEvents.ShowSnackBarError(resultTagihan.message))
            }
        }
    }

    private fun kirimTolakPembayaranTagihan() {
        viewModelScope.launch {
            _state.update { it.copy(isButtonKirimTolakLoading = true) }

            val alasanPenolakan = _state.value.alasanPenolakan
            val alasanPenolakanError = PatternValidation.getAlasanPenolakanError(alasanPenolakan)
            val isAlasanPenolakanValid = PatternValidation.isAlasanPenolakanValid(alasanPenolakan)

            if (!isAlasanPenolakanValid){
                _state.update {
                    it.copy(
                        isButtonKirimTolakLoading = false,
                        alasanPenolakanError = alasanPenolakanError,
                        isAlasanPenolakanError = true,
                        alasanPenolakanShakeTrigger = it.alasanPenolakanShakeTrigger + 1
                    )
                }
                return@launch
            }

            _state.update {
                it.copy(
                    alasanPenolakanError = null,
                    isAlasanPenolakanError = false
                )
            }

            val idTagihan = _state.value.detailTagihanUi?.idTagihan ?: ""
            val bulan = _state.value.detailTagihanUi?.billingMonth ?: ""
            val jumlahDibayar = _state.value.detailTagihanUi?.billAmount ?: 0
            val nomorKamar = _state.value.detailTagihanUi?.numberRoom ?: 0

            tolakTagihanUseCase(
                idTagihan = idTagihan,
                bulan = bulan,
                jumlahDibayar = jumlahDibayar,
                nomorKamar = nomorKamar,
                alasanPenolakan = alasanPenolakan
            ).onSuccess { resultTagihan ->
                _state.update {
                    it.copy(
                        isTolakBottomSheetVisible = false,
                        isButtonKirimTolakLoading = false,
                        loadError = null,
                        alasanPenolakan = ""
                    )
                }

                navigateToResultScreen(TypeResult.MENOLAK_PEMBAYARAN_TAGIHAN, resultTagihan)
            }.onError { resultTagihan ->
                _state.update {
                    it.copy(
                        isButtonKirimTolakLoading = false,
                        loadError = null,
                        alasanPenolakan = "",
                        isTolakBottomSheetVisible = false
                    )
                }
                _events.send(DetailTagihanEvents.ShowSnackBarError(resultTagihan.message))
            }
        }
    }

    private fun hapusTagihan() {
        viewModelScope.launch {
            _state.update { it.copy(isButtonHapusLoading = true) }
            val idTagihan = _state.value.detailTagihanUi?.idTagihan ?: ""

            tagihanRepository.deteleTagihan(idTagihan)
                .onSuccess { _ ->
                    _state.update {
                        it.copy(
                            isButtonHapusLoading = false,
                            isHapusDialogVisible = false
                        )
                    }
                    _events.send(DetailTagihanEvents.NavigateBackToSendDeleteSnackBar)
                }.onError { resultTagihan ->
                    _state.update {
                        it.copy(
                            isButtonHapusLoading = false,
                            isHapusDialogVisible = false
                        )
                    }
                    _events.send(DetailTagihanEvents.ShowSnackBarError(resultTagihan.message))
                }
        }
    }

    private fun updateProofOfPayment(imageUri: Uri) {
        _state.update {
            it.copy(
                proofOfPayment = imageUri
            )
        }
    }

    fun loadDetailTagihan(idTagihan: String) {
        viewModelScope.launch {
            if (_state.value.detailTagihanUi != null && _state.value.detailTagihanUi?.idTagihan == idTagihan) {
                return@launch
            }
            _state.update {
                it.copy(
                    isLoading = true,
                    loadError = null,
                )
            }
            tagihanRepository.getDetailTagihan(idTagihan)
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            detailTagihanUi = result.toDetailTagihanUi(),
                            loadError = null
                        )
                    }
                }
                .onError { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            loadError = result.message,
                            detailTagihanUi = null
                        )
                    }
                }
        }
    }

    private fun navigateToPreviewImage(imageUri: Uri) {
        viewModelScope.launch {
            Log.d("Bukti Foto", "$imageUri")
            _events.send(DetailTagihanEvents.NavigateToPreviewImage(imageUri))
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(DetailTagihanEvents.NavigateBack)
        }
    }

    private fun bayarOrKirimTagihan() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isButtonKirimOrBayarLoading = true,
                    loadError = null
                )
            }
            val idTagihan = _state.value.detailTagihanUi?.idTagihan ?: ""
            val bulan = _state.value.detailTagihanUi?.billingMonth ?: ""
            val jumlahDibayar = _state.value.detailTagihanUi?.billAmount ?: 0
            val nomorKamar = _state.value.detailTagihanUi?.numberRoom ?: 0
            val proofOfPayment = _state.value.proofOfPayment
            val role = _state.value.role

            if (proofOfPayment == Uri.EMPTY){
                _events.send(DetailTagihanEvents.ShowSnackBarError("Bukti pembayaran belum ada"))
                _state.update {
                    it.copy(
                        isButtonKirimOrBayarLoading = false,
                        loadError = null
                    )
                }
                return@launch
            }

            val compressedResult = if (proofOfPayment != Uri.EMPTY) {
                imageCompressor.compressImage(imageUri = proofOfPayment, compressionThreshold = 200 * 1024L)
            } else { null}

            val buktiPembayaranSebelumnya = _state.value.detailTagihanUi?.proofOfPayment

            if (role != Role.ADMIN){
                bayarTagihanUseCase(
                    idTagihan = idTagihan,
                    compressedResult = compressedResult,
                    bulan = bulan,
                    jumlahDibayar = jumlahDibayar,
                    nomorKamar = nomorKamar,
                    buktiPembayaranSebelumnya = buktiPembayaranSebelumnya
                ).onSuccess { resultTagihan ->
                    _state.update {
                        it.copy(
                            isButtonKirimOrBayarLoading = false,
                            loadError = null,
                        )
                    }
                    navigateToResultScreen(TypeResult.BAYAR_TAGIHAN, resultTagihan)
                }.onError { resultTagihan ->
                    _state.update {
                        it.copy(
                            isButtonKirimOrBayarLoading = false,
                            loadError = null
                        )
                    }
                    _events.send(DetailTagihanEvents.ShowSnackBarError(resultTagihan.message))
                }
            } else{
                bayarTagihanLangsungLunasUseCase(
                    idTagihan = idTagihan,
                    compressedResult = compressedResult,
                    bulan = bulan,
                    jumlahDibayar = jumlahDibayar,
                    nomorKamar = nomorKamar
                ).onSuccess { resultTagihan ->
                    _state.update {
                        it.copy(
                            isButtonKirimOrBayarLoading = false,
                            loadError = null,
                        )
                    }
                    navigateToResultScreen(TypeResult.BAYAR_TAGIHAN_LANGSUNG_LUNAS, resultTagihan)
                }.onError { resultTagihan ->
                    _state.update {
                        it.copy(
                            isButtonKirimOrBayarLoading = false,
                            loadError = null
                        )
                    }
                    _events.send(DetailTagihanEvents.ShowSnackBarError(resultTagihan.message))
                }
            }
        }
    }

    private fun navigateToResultScreen(typeResult: TypeResult, resultTagihan: ResultTagihan) {
        viewModelScope.launch {
            _events.trySend(DetailTagihanEvents.NavigateToResult(typeResult, resultTagihan))
        }
    }
}