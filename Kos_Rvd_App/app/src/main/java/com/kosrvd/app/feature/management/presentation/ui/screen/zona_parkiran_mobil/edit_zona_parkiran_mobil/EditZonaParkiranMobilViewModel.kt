package com.kosrvd.app.feature.management.presentation.ui.screen.zona_parkiran_mobil.edit_zona_parkiran_mobil

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.core.domain.utils.onSuccess
import com.kosrvd.app.core.navigation.NavigationScreen
import com.kosrvd.app.core.presentation.utils.PatternValidation
import com.kosrvd.app.feature.management.domain.repository.ZonaParkiranMobilRepository
import com.kosrvd.app.feature.management.presentation.designsystem.utils.TypeEditZonaParkir
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface EditZonaParkiranMobilEvents {
    data object NavigateBack: EditZonaParkiranMobilEvents
    data class ShowSnackBarError(val message: String): EditZonaParkiranMobilEvents
    data class NavigateBackSuccessEditZonaParkiranMobil(val typeEditZonaParkiranMobil: TypeEditZonaParkir): EditZonaParkiranMobilEvents
}

sealed interface EditZonaParkiranMobilActions {
    data object NavigateBack: EditZonaParkiranMobilActions
    data class UpdateNamaZona(val namaZona: String): EditZonaParkiranMobilActions
    data class UpdateBiayaBulanan(val biayaBulanan: String): EditZonaParkiranMobilActions
    data class UpdateBiayaHarian(val biayaHarian: String): EditZonaParkiranMobilActions
    data object SaveEditZonaParkiranMobil: EditZonaParkiranMobilActions
}

@HiltViewModel
class EditZonaParkiranMobilViewModel @Inject constructor(
    private val zonaParkiranMobilRepository: ZonaParkiranMobilRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _state = MutableStateFlow(EditZonaParkiranMobilUiState())
    val state = _state.asStateFlow()

    private val _events = Channel<EditZonaParkiranMobilEvents>()
    val events = _events.receiveAsFlow()

    init {
        val arguments = savedStateHandle.toRoute<NavigationScreen.EditZonaParkirScreen>()
        initialDataState(
            idZonaParkiran = arguments.idZonaParkir,
            typeEditZonaParkir = arguments.typeEditZonaParkir,
            biayaBulanan = arguments.biayaBulanan.toString(),
            biayaHarian = arguments.biayaHarian.toString(),
            namaZona = arguments.namaZona ?: ""
        )
    }

    fun onActions(actions: EditZonaParkiranMobilActions){
        when(actions){
            EditZonaParkiranMobilActions.SaveEditZonaParkiranMobil -> editZonaParkiranMobil()
            EditZonaParkiranMobilActions.NavigateBack -> navigateBack()
            is EditZonaParkiranMobilActions.UpdateBiayaBulanan -> updateBiayaBulanan(actions.biayaBulanan)
            is EditZonaParkiranMobilActions.UpdateBiayaHarian -> updateBiayaHarian(actions.biayaHarian)
            is EditZonaParkiranMobilActions.UpdateNamaZona -> updateNamaZona(actions.namaZona)
        }
    }

    private fun updateNamaZona(namaZona: String) {
        val namaZonaError = PatternValidation.getNamaZonaError(namaZona)
        val isNamaZonaValid = PatternValidation.isNamaZonaValid(namaZona)
        _state.update {
            it.copy(
                zoneName = namaZona,
                isZoneNameError = !isNamaZonaValid,
                zoneNameError = namaZonaError
            )
        }
    }

    private fun updateBiayaHarian(biayaHarian: String) {
        val biayaHarianError = PatternValidation.getBiayaHarianError(biayaHarian)
        val isBiayaHarianValid = PatternValidation.isBiayaHarianValid(biayaHarian)
        _state.update {
            it.copy(
                biayaHarian = biayaHarian,
                isBiayaHarianError = !isBiayaHarianValid,
                biayaHarianError = biayaHarianError
            )
        }
    }

    private fun updateBiayaBulanan(biayaBulanan: String) {
        val biayaBulananError = PatternValidation.getBiayaBulananError(biayaBulanan)
        val isBiayaBulananValid = PatternValidation.isBiayaBulananValid(biayaBulanan)
        _state.update {
            it.copy(
                biayaBulanan = biayaBulanan,
                isBiayaBulananError = !isBiayaBulananValid,
                biayaBulananError = biayaBulananError
            )
        }
    }

    private fun editZonaParkiranMobil() {
        viewModelScope.launch {
            _state.update { it.copy(isButtonLoading = true) }
            val idZonaParkiran = _state.value.idZonaParkir
            when(_state.value.typeEditZonaParkir){
                TypeEditZonaParkir.EDIT_BIAYA_BULANAN -> {
                    val biayaBulanan = _state.value.biayaBulanan
                    val biayaBulananError = PatternValidation.getBiayaBulananError(biayaBulanan)
                    val isBiayaBulananValid = PatternValidation.isBiayaBulananValid(biayaBulanan)

                    if (!isBiayaBulananValid){
                        _state.update {
                            it.copy(
                                isButtonLoading = false,
                                isBiayaBulananError = true,
                                shakeTriggerBiayaBulananError = _state.value.shakeTriggerBiayaBulananError + 1,
                                biayaBulananError = biayaBulananError
                            )
                        }
                        return@launch
                    }

                    _state.update {
                        it.copy(
                            isBiayaBulananError = false,
                            biayaBulananError = null
                        )
                    }

                    zonaParkiranMobilRepository.updateMonthlyFee(
                        idZonaParkir = idZonaParkiran,
                        monthlyFeeBaru = biayaBulanan.toLong()
                    )
                        .onSuccess {
                            _state.update { it.copy(isButtonLoading = false) }
                            _events.send(EditZonaParkiranMobilEvents
                                .NavigateBackSuccessEditZonaParkiranMobil(TypeEditZonaParkir.EDIT_BIAYA_BULANAN))
                        }
                        .onError { result ->
                            _state.update { it.copy(isButtonLoading = false) }
                            _events.send(EditZonaParkiranMobilEvents.ShowSnackBarError(result.message))
                        }
                }
                TypeEditZonaParkir.EDIT_BIAYA_HARIAN -> {
                    val biayaHarian = _state.value.biayaHarian
                    val biayaHarianError = PatternValidation.getBiayaHarianError(biayaHarian)
                    val isBiayaHarianValid = PatternValidation.isBiayaHarianValid(biayaHarian)

                    if (!isBiayaHarianValid){
                        _state.update {
                            it.copy(
                                isButtonLoading = false,
                                isBiayaHarianError = true,
                                shakeTriggerBiayaHarianError = _state.value.shakeTriggerBiayaHarianError + 1,
                                biayaHarianError = biayaHarianError
                            )
                        }
                        return@launch
                    }

                    _state.update {
                        it.copy(
                            isBiayaHarianError = false,
                            biayaHarianError = null
                        )
                    }

                    zonaParkiranMobilRepository.updateDailyCosts(
                        idZonaParkir = idZonaParkiran,
                        dailyCostsBaru = biayaHarian.toLong()
                    )
                        .onSuccess {
                            _state.update { it.copy(isButtonLoading = false) }
                            _events.send(EditZonaParkiranMobilEvents
                                .NavigateBackSuccessEditZonaParkiranMobil(TypeEditZonaParkir.EDIT_BIAYA_HARIAN))
                        }
                        .onError { result ->
                            _state.update { it.copy(isButtonLoading = false) }
                            _events.send(EditZonaParkiranMobilEvents.ShowSnackBarError(result.message))
                        }
                }
                TypeEditZonaParkir.EDIT_NAMA_ZONA -> {
                    val namaZona = _state.value.zoneName
                    val namaZonaError = PatternValidation.getNamaZonaError(namaZona)
                    val isNamaZonaValid = PatternValidation.isNamaZonaValid(namaZona)

                    if (!isNamaZonaValid){
                        _state.update {
                            it.copy(
                                isButtonLoading = false,
                                isZoneNameError = true,
                                shakeTriggerZoneNameError = _state.value.shakeTriggerZoneNameError + 1,
                                zoneNameError = namaZonaError
                            )
                        }
                        return@launch
                    }

                    _state.update {
                        it.copy(
                            isZoneNameError = false,
                            zoneNameError = null
                        )
                    }

                    zonaParkiranMobilRepository.updateZoneName(
                        idZonaParkir = idZonaParkiran,
                        zoneNameBaru = namaZona
                    )
                        .onSuccess {
                            _state.update { it.copy(isButtonLoading = false) }
                            _events.send(EditZonaParkiranMobilEvents
                                .NavigateBackSuccessEditZonaParkiranMobil(TypeEditZonaParkir.EDIT_NAMA_ZONA))
                        }
                        .onError { result ->
                            _state.update { it.copy(isButtonLoading = false) }
                            _events.send(EditZonaParkiranMobilEvents.ShowSnackBarError(result.message))
                        }
                }
                null -> {
                    _state.update { it.copy(isButtonLoading = false) }
                    _events.send(EditZonaParkiranMobilEvents.ShowSnackBarError("Type Edit Zona Parkiran Kosong"))
                }
            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(EditZonaParkiranMobilEvents.NavigateBack)
        }
    }

    private fun initialDataState(idZonaParkiran: String, typeEditZonaParkir: TypeEditZonaParkir, biayaBulanan: String, biayaHarian: String, namaZona: String){
        when (typeEditZonaParkir) {
            TypeEditZonaParkir.EDIT_BIAYA_BULANAN -> {
                _state.update {
                    it.copy(
                        idZonaParkir = idZonaParkiran,
                        typeEditZonaParkir = typeEditZonaParkir,
                        oldBiayaBulanan = biayaBulanan,
                        biayaBulanan = biayaBulanan,
                        oldZoneName = namaZona,
                    )
                }
            }
            TypeEditZonaParkir.EDIT_BIAYA_HARIAN -> {
                _state.update {
                    it.copy(
                        idZonaParkir = idZonaParkiran,
                        typeEditZonaParkir = typeEditZonaParkir,
                        oldBiayaHarian = biayaHarian,
                        biayaHarian = biayaHarian,
                        oldZoneName = namaZona,
                    )
                }
            }
            TypeEditZonaParkir.EDIT_NAMA_ZONA -> {
                _state.update {
                    it.copy(
                        idZonaParkir = idZonaParkiran,
                        typeEditZonaParkir = typeEditZonaParkir,
                        oldZoneName = namaZona,
                        zoneName = namaZona,
                    )
                }
            }
        }
    }
}