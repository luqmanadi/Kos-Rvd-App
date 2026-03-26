package com.kosrvd.app.feature.room.presentation.detail_kamar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.core.domain.utils.onSuccess
import com.kosrvd.app.presentation.navigation.models.AlatElektronikSerialize
import com.kosrvd.app.presentation.navigation.models.EditTypeKamar
import com.kosrvd.app.presentation.navigation.models.HargaSewaKamar
import com.kosrvd.app.feature.room.domain.repository.KamarRepository
import com.kosrvd.app.feature.room.domain.utils.TypeEditKamar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface DetailKamarEvents {
    data object NavigateBack: DetailKamarEvents
    data class ShowSnackBarError(val message: String): DetailKamarEvents
    data class NavigateToEditKamar(val idKamar: String, val typeEditKamar: TypeEditKamar, val editTypeKamar: EditTypeKamar): DetailKamarEvents
    data object NavigateBackSuccessDeleteKamar: DetailKamarEvents
}

sealed interface DetailKamarActions {
    data object NavigateBack: DetailKamarActions
    data object ShowDeleteDialog: DetailKamarActions
    data object DismissDeleteDialog: DetailKamarActions
    data class TryAgain(val idKamar: String): DetailKamarActions
    data object DeleteKamar: DetailKamarActions
    data object NavigateToEditNomorKamar: DetailKamarActions
    data object NavigateToEditFasilitasKamar: DetailKamarActions
    data object NavigateToEditTarifAndCapacityKamar: DetailKamarActions
    data object NavigateToEditUkuranKamar: DetailKamarActions
    data object NavigateToEditLayananKamarGratis: DetailKamarActions
}

@HiltViewModel
class DetailKamarViewModel @Inject constructor(
    private val kamarRepository: KamarRepository
): ViewModel() {
    private val _state = MutableStateFlow(DetailKamarUiState())
    val state = _state.asStateFlow()

    private val _events = Channel<DetailKamarEvents>()
    val events = _events.receiveAsFlow()

    fun onActions(actions: DetailKamarActions){
        when(actions){
            DetailKamarActions.DeleteKamar -> deleteKamar()
            DetailKamarActions.DismissDeleteDialog -> dismissDeleteDialog()
            DetailKamarActions.NavigateBack -> navigateBack()
            DetailKamarActions.NavigateToEditFasilitasKamar -> navigateToEditFasilitasKamar()
            DetailKamarActions.NavigateToEditLayananKamarGratis -> navigateToEditLayananKamarGratis()
            DetailKamarActions.NavigateToEditNomorKamar -> navigateToEditNomorKamar()
            DetailKamarActions.NavigateToEditTarifAndCapacityKamar -> navigateToEditTarifAndCapacityKamar()
            DetailKamarActions.NavigateToEditUkuranKamar -> navigateToEditUkuranKamar()
            DetailKamarActions.ShowDeleteDialog -> showDeleteDialog()
            is DetailKamarActions.TryAgain -> loadKamarById(actions.idKamar)
        }
    }

    private fun navigateToEditUkuranKamar() {
        viewModelScope.launch {
            val idKamar = _state.value.kamarUi?.idKamar ?: ""
            val ukuranKamar = _state.value.kamarUi?.size
            val nomorKamar = _state.value.kamarUi?.numberRoom
            _events.send(
                DetailKamarEvents.NavigateToEditKamar(
                    idKamar = idKamar,
                    typeEditKamar = TypeEditKamar.EDIT_UKURAN_KAMAR,
                    editTypeKamar = EditTypeKamar(
                        ukuranKamar = ukuranKamar,
                        nomorKamar = nomorKamar
                    )
                )
            )
        }
    }

    private fun navigateToEditTarifAndCapacityKamar() {
        viewModelScope.launch {
            val idKamar = _state.value.kamarUi?.idKamar ?: ""
            val harga = _state.value.kamarUi?.price
            val status = _state.value.kamarUi?.status
            val nomorKamar = _state.value.kamarUi?.numberRoom
            _events.send(
                DetailKamarEvents.NavigateToEditKamar(
                    idKamar = idKamar,
                    typeEditKamar = TypeEditKamar.EDIT_TARIF_KAMAR,
                    editTypeKamar = EditTypeKamar(
                        tarifKamar = HargaSewaKamar(
                            onePerson = harga?.onePerson ?: 0,
                            twoPersons = harga?.twoPersons
                        ),
                        statusKamar = status,
                        nomorKamar = nomorKamar
                    )
                )
            )
        }
    }

    private fun navigateToEditNomorKamar() {
        viewModelScope.launch {
            val idKamar = _state.value.kamarUi?.idKamar ?: ""
            val nomorKamar = _state.value.kamarUi?.numberRoom
            _events.send(
                DetailKamarEvents.NavigateToEditKamar(
                    idKamar = idKamar,
                    typeEditKamar = TypeEditKamar.EDIT_NOMOR_KAMAR,
                    editTypeKamar = EditTypeKamar(
                        nomorKamar = nomorKamar
                    )
                )
            )
        }
    }

    private fun navigateToEditLayananKamarGratis() {
        viewModelScope.launch {
            val idKamar = _state.value.kamarUi?.idKamar ?: ""
            val layananKamarGratis = _state.value.kamarUi?.freeService
            val nomorKamar = _state.value.kamarUi?.numberRoom
            _events.send(
                DetailKamarEvents.NavigateToEditKamar(
                    idKamar = idKamar,
                    typeEditKamar = TypeEditKamar.EDIT_LAYANAN_ALAT_ELEKTRONIK_GRATIS,
                    editTypeKamar = EditTypeKamar(
                        layananElektronikKamar = layananKamarGratis?.map {
                            AlatElektronikSerialize(
                                toolName = it.toolName,
                                cost = it.cost,
                                origin = it.origin
                            )
                        },
                        nomorKamar = nomorKamar
                    )
                )
            )
        }
    }

    private fun navigateToEditFasilitasKamar() {
        viewModelScope.launch {
            val idKamar = _state.value.kamarUi?.idKamar ?: ""
            val fasilitasKamar = _state.value.kamarUi?.facility
            val nomorKamar = _state.value.kamarUi?.numberRoom
            _events.send(
                DetailKamarEvents.NavigateToEditKamar(
                    idKamar = idKamar,
                    typeEditKamar = TypeEditKamar.EDIT_FASILITAS_KAMAR,
                    editTypeKamar = EditTypeKamar(
                        fasilitasKamar = fasilitasKamar,
                        nomorKamar = nomorKamar
                    )
                )
            )
        }
    }

    private fun deleteKamar() {
        viewModelScope.launch {
            _state.update { it.copy(isButtonDeleteLoading = true) }

            val idKamar = _state.value.kamarUi?.idKamar ?: ""
            kamarRepository.deleteKamar(idKamar)
                .onSuccess {
                    _state.update {
                        it.copy(
                            isButtonDeleteLoading = false,
                            showDialogDeleteKamar = false
                        )
                    }
                    _events.send(DetailKamarEvents.NavigateBackSuccessDeleteKamar)
                }
                .onError { result ->
                    _state.update {
                        it.copy(
                            isButtonDeleteLoading = false,
                            showDialogDeleteKamar = false
                        )
                    }
                    _events.send(DetailKamarEvents.ShowSnackBarError(result.message))
                }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(DetailKamarEvents.NavigateBack)
        }
    }

    private fun dismissDeleteDialog() {
        _state.update { it.copy(showDialogDeleteKamar = false) }
    }

    private fun showDeleteDialog() {
        _state.update { it.copy(showDialogDeleteKamar = true) }
    }

    fun loadKamarById(idKamar: String){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, loadError = null) }

            kamarRepository.getKamarById(idKamar)
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            loadError = null,
                            kamarUi = result
                        )
                    }
                }
                .onError { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            loadError = result.message,
                            kamarUi = null
                        )
                    }
                }
        }
    }
}