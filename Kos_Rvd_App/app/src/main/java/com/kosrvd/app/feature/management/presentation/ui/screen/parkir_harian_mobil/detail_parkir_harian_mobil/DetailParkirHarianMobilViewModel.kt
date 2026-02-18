package com.kosrvd.app.feature.management.presentation.ui.screen.parkir_harian_mobil.detail_parkir_harian_mobil

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.core.domain.utils.onSuccess
import com.kosrvd.app.core.navigation.NavigationScreen
import com.kosrvd.app.feature.management.domain.repository.ParkirHarianMobilRepository
import com.kosrvd.app.feature.management.domain.usecase.UploadProofOfPaymentParkirHarianMobilUseCase
import com.kosrvd.app.feature.management.presentation.designsystem.utils.ImageCompressor
import com.kosrvd.app.feature.management.presentation.ui.models.toDetailParkirHarianMobilUi
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

sealed interface DetailParkirHarianMobilEvents {
    data object NavigateBack: DetailParkirHarianMobilEvents
    data class ShowSnackBarError(val message: String): DetailParkirHarianMobilEvents
    data class NavigateToPreviewImage(val imageUri: Uri): DetailParkirHarianMobilEvents
    data object NavigateBackSuccessUploadProofOfPayment: DetailParkirHarianMobilEvents
    data object NavigateBackSuccessDelete: DetailParkirHarianMobilEvents
    data object NavigateBackSuccessCancel: DetailParkirHarianMobilEvents
}

sealed interface DetailParkirHarianMobilActions {
    data object NavigateBack: DetailParkirHarianMobilActions
    data object UploadProofOfPayment: DetailParkirHarianMobilActions
    data object DeleteParkirHarianMobil: DetailParkirHarianMobilActions
    data object ShowHapusDialog: DetailParkirHarianMobilActions
    data object DismissHapusDialog: DetailParkirHarianMobilActions
    data class NavigateToPreviewImage(val imageUri: Uri): DetailParkirHarianMobilActions
    data object TryAgain: DetailParkirHarianMobilActions
    data class UpdateProofOfPayment(val imageUri: Uri): DetailParkirHarianMobilActions
    data object ShowCancelledDialog: DetailParkirHarianMobilActions
    data object DismissCancelledDialog: DetailParkirHarianMobilActions
    data object CancelParkirHarianMobil: DetailParkirHarianMobilActions
}

@HiltViewModel
class DetailParkirHarianMobilViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val parkirHarianMobilRepository: ParkirHarianMobilRepository,
    private val imageCompressor: ImageCompressor,
    private val uploadProofOfPaymentParkirHarianMobilUseCase: UploadProofOfPaymentParkirHarianMobilUseCase
): ViewModel() {
    private val _state = MutableStateFlow(DetailParkirHarianMobilUiState())
    val state = _state
        .onStart { loadDetailParkirHarianMobil()  }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = DetailParkirHarianMobilUiState()
        )

    private val _events = Channel<DetailParkirHarianMobilEvents>()
    val events = _events.receiveAsFlow()

    fun onActions(actions: DetailParkirHarianMobilActions){
        when(actions){
            DetailParkirHarianMobilActions.DeleteParkirHarianMobil -> deleteParkirHarianMobil()
            DetailParkirHarianMobilActions.DismissHapusDialog -> dismissHapusDialog()
            DetailParkirHarianMobilActions.NavigateBack -> navigateBack()
            is DetailParkirHarianMobilActions.NavigateToPreviewImage -> navigateToPreviewImage(actions.imageUri)
            DetailParkirHarianMobilActions.ShowHapusDialog -> showHapusDialog()
            DetailParkirHarianMobilActions.TryAgain -> loadDetailParkirHarianMobil()
            is DetailParkirHarianMobilActions.UpdateProofOfPayment -> updateProofOfPayment(actions.imageUri)
            DetailParkirHarianMobilActions.UploadProofOfPayment -> uploadProofOfPayment()
            DetailParkirHarianMobilActions.CancelParkirHarianMobil -> cancelParkirHarianMobil()
            DetailParkirHarianMobilActions.DismissCancelledDialog -> dismissCancelledDialog()
            DetailParkirHarianMobilActions.ShowCancelledDialog -> showCancelledDialog()
        }
    }

    private fun showCancelledDialog() {
        _state.update { it.copy(isCancelledDialogVisible = true) }
    }

    private fun dismissCancelledDialog() {
        _state.update { it.copy(isCancelledDialogVisible = false) }
    }

    private fun cancelParkirHarianMobil() {
        viewModelScope.launch {
            _state.update { it.copy(isButtonCancelledLoading = true) }
            val idParkirHarianMobil = _state.value.dataDetailParkirHarianMobil?.idParkirHarianMobil ?: ""
            val updateDataParkirHarianMobil = mapOf(Constant.IS_CANCELLED_FIELD to true)

            parkirHarianMobilRepository.updateDataParkirHarianMobil(
                idParkirHarianMobil = idParkirHarianMobil,
                updateDataParkirHarianMobil = updateDataParkirHarianMobil
            ).onSuccess {
                _state.update {
                    it.copy(
                        isButtonCancelledLoading = false,
                        isCancelledDialogVisible = false
                    )
                }
                _events.send(DetailParkirHarianMobilEvents.NavigateBackSuccessCancel)
            }.onError { result ->
                _state.update {
                    it.copy(
                        isButtonCancelledLoading = false,
                        isCancelledDialogVisible = false
                    )
                }
                _events.send(DetailParkirHarianMobilEvents.ShowSnackBarError(result.message))
            }
        }
    }

    private fun deleteParkirHarianMobil() {
        viewModelScope.launch {
            _state.update { it.copy(isButtonHapusLoading = true) }
            val idParkirHarianMobil = _state.value.dataDetailParkirHarianMobil?.idParkirHarianMobil ?: ""

            parkirHarianMobilRepository.deleteParkirHarianMobil(idParkirHarianMobil)
                .onSuccess {
                    _state.update {
                        it.copy(
                            isButtonHapusLoading = false,
                            isHapusDialogVisible = false
                        )
                    }
                    _events.send(DetailParkirHarianMobilEvents.NavigateBackSuccessDelete)
                }
                .onError { result ->
                    _state.update {
                        it.copy(
                            isButtonHapusLoading = false,
                            isHapusDialogVisible = false
                        )
                    }
                    _events.send(DetailParkirHarianMobilEvents.ShowSnackBarError(result.message))
                }
        }
    }

    private fun uploadProofOfPayment() {
        viewModelScope.launch {
            _state.update { it.copy(isButtonUploadLoading = true, loadError = null) }
            val proofOfPayment = _state.value.proofOfPayment

            if (proofOfPayment == Uri.EMPTY){
                _events.send(DetailParkirHarianMobilEvents.ShowSnackBarError("Bukti pembayaran belum ada"))
                _state.update {
                    it.copy(
                        isButtonUploadLoading = false,
                        loadError = null
                    )
                }
                return@launch
            }

            val compressedResult = if (proofOfPayment != Uri.EMPTY) {
                imageCompressor.compressImage(imageUri = proofOfPayment, compressionThreshold = 200 * 1024L)
            } else { null }

            val idParkirHarianMobil = _state.value.dataDetailParkirHarianMobil?.idParkirHarianMobil
            if (idParkirHarianMobil != null) {
                uploadProofOfPaymentParkirHarianMobilUseCase(
                    idParkirHarianMobil = idParkirHarianMobil,
                    compressedResult = compressedResult
                ).onSuccess {
                    _state.update {
                        it.copy(
                            isButtonUploadLoading = false,
                            loadError = null
                        )
                    }
                    _events.send(DetailParkirHarianMobilEvents.NavigateBackSuccessUploadProofOfPayment)
                }.onError { result ->
                    _state.update {
                        it.copy(
                            isButtonUploadLoading = false,
                            loadError = null
                        )
                    }
                    _events.send(DetailParkirHarianMobilEvents.ShowSnackBarError(result.message))
                }
            }
        }
    }

    private fun updateProofOfPayment(imageUri: Uri) {
        _state.update { it.copy(proofOfPayment = imageUri) }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(DetailParkirHarianMobilEvents.NavigateBack)
        }
    }

    private fun navigateToPreviewImage(imageUri: Uri) {
        viewModelScope.launch {
            _events.send(DetailParkirHarianMobilEvents.NavigateToPreviewImage(imageUri))
        }
    }

    private fun showHapusDialog() {
        _state.update { it.copy(isHapusDialogVisible = true) }
    }

    private fun dismissHapusDialog() {
        _state.update { it.copy(isHapusDialogVisible = false) }
    }

    private fun getIdFromSavedStateHandle(): String {
        // Ambil objek argument dari savedStateHandle
        val arguments = savedStateHandle.toRoute<NavigationScreen.DetailParkirHarianMobilScreen>()

        // Ambil idZonaParkir dari argument
        val idParkirHarianMobil = arguments.idParkirHarianMobil
        return idParkirHarianMobil
    }

    private fun loadDetailParkirHarianMobil(){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, loadError = null) }
            val idParkirHarianMobil = getIdFromSavedStateHandle()

            parkirHarianMobilRepository.getParkirHarianMobilById(idParkirHarianMobil)
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            loadError = null,
                            dataDetailParkirHarianMobil = result.toDetailParkirHarianMobilUi()
                        )
                    }
                }
                .onError { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            loadError = result.message,
                            dataDetailParkirHarianMobil = null
                        )
                    }
                }
        }
    }
}