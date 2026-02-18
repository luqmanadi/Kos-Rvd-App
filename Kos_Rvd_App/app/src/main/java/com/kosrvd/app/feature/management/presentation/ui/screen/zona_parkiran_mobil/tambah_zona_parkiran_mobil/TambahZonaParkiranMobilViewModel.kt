package com.kosrvd.app.feature.management.presentation.ui.screen.zona_parkiran_mobil.tambah_zona_parkiran_mobil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.core.domain.utils.onSuccess
import com.kosrvd.app.core.presentation.utils.PatternValidation
import com.kosrvd.app.feature.management.domain.repository.ZonaParkiranMobilRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface TambahZonaParkiranMobilEvents {
    data object NavigateBack : TambahZonaParkiranMobilEvents
    data class ShowSnackBarError(val message: String) : TambahZonaParkiranMobilEvents
    data object NavigateBackSendSuccessTambahZonaParkiranMobil : TambahZonaParkiranMobilEvents
}

sealed interface TambahZonaParkiranMobilActions {
    data object NavigateBack : TambahZonaParkiranMobilActions
    data class UpdateNamaZona(val namaZona: String): TambahZonaParkiranMobilActions
    data class UpdateBiayaBulanan(val biayaBulanan: String): TambahZonaParkiranMobilActions
    data class UpdateBiayaHarian(val biayaHarian: String): TambahZonaParkiranMobilActions
    data object TambahZonaParkiranMobil: TambahZonaParkiranMobilActions
}

@HiltViewModel
class TambahZonaParkiranMobilViewModel @Inject constructor(
    private val zonaParkiranMobilRepository: ZonaParkiranMobilRepository
) : ViewModel() {
    private val _state = MutableStateFlow(TambahZonaParkiranMobilUiState())
    val state = _state.asStateFlow()

    private val _events = Channel<TambahZonaParkiranMobilEvents>()
    val events = _events.receiveAsFlow()

    fun onActions(actions: TambahZonaParkiranMobilActions){
        when(actions){
            TambahZonaParkiranMobilActions.NavigateBack -> navigateBack()
            TambahZonaParkiranMobilActions.TambahZonaParkiranMobil -> tambahZonaParkiranMobil()
            is TambahZonaParkiranMobilActions.UpdateBiayaBulanan -> updateBiayaBulanan(actions.biayaBulanan)
            is TambahZonaParkiranMobilActions.UpdateBiayaHarian -> updateBiayaHarian(actions.biayaHarian)
            is TambahZonaParkiranMobilActions.UpdateNamaZona -> updateNamaZona(actions.namaZona)
        }
    }

    private fun updateNamaZona(namaZona: String) {
        if (namaZona.isEmpty()){
            _state.update {
                it.copy(
                    zoneName = namaZona,
                    isZoneNameError = false,
                    zoneNameError = null
                )
            }
            return
        }

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
        if (biayaHarian.isEmpty()){
            _state.update {
                it.copy(
                    biayaHarian = biayaHarian,
                    isBiayaHarianError = false,
                    biayaHarianError = null
                )
            }
            return
        }

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
        if (biayaBulanan.isEmpty()){
            _state.update {
                it.copy(
                    biayaBulanan = biayaBulanan,
                    isBiayaBulananError = false,
                    biayaBulananError = null
                )
            }
            return
        }

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

    private fun tambahZonaParkiranMobil() {
        viewModelScope.launch {
            _state.update { it.copy(isButtonLoading = true) }

            val namaZona = _state.value.zoneName
            val namaZonaError = PatternValidation.getNamaZonaError(namaZona)
            val isNamaZonaValid = PatternValidation.isNamaZonaValid(namaZona)
            val shakeTriggerZoneNameError = if (!isNamaZonaValid) _state.value.shakeTriggerZoneNameError + 1 else _state.value.shakeTriggerZoneNameError

            val biayaBulanan = _state.value.biayaBulanan
            val biayaBulananError = PatternValidation.getBiayaBulananError(biayaBulanan)
            val isBiayaBulananValid = PatternValidation.isBiayaBulananValid(biayaBulanan)
            val shakeTriggerBiayaBulananError = if (!isBiayaBulananValid) _state.value.shakeTriggerBiayaBulananError + 1 else _state.value.shakeTriggerBiayaBulananError

            val biayaHarian = _state.value.biayaHarian
            val biayaHarianError = PatternValidation.getBiayaHarianError(biayaHarian)
            val isBiayaHarianValid = PatternValidation.isBiayaHarianValid(biayaHarian)
            val shakeTriggerBiayaHarianError = if (!isBiayaHarianValid) _state.value.shakeTriggerBiayaHarianError + 1 else _state.value.shakeTriggerBiayaHarianError

            if (!isNamaZonaValid || !isBiayaBulananValid || !isBiayaHarianValid){
                _state.update {
                    it.copy(
                        isButtonLoading = false,
                        zoneNameError = namaZonaError,
                        isZoneNameError = !isNamaZonaValid,
                        shakeTriggerZoneNameError = shakeTriggerZoneNameError,
                        biayaBulananError = biayaBulananError,
                        isBiayaBulananError = !isBiayaBulananValid,
                        shakeTriggerBiayaBulananError = shakeTriggerBiayaBulananError,
                        biayaHarianError = biayaHarianError,
                        isBiayaHarianError = !isBiayaHarianValid,
                        shakeTriggerBiayaHarianError = shakeTriggerBiayaHarianError
                    )
                }
                return@launch
            }

            _state.update {
                it.copy(
                    zoneNameError = null,
                    isZoneNameError = false,
                    biayaBulananError = null,
                    isBiayaBulananError = false,
                    biayaHarianError = null,
                    isBiayaHarianError = false
                )
            }

            zonaParkiranMobilRepository.addZonaParkiranMobil(
                zoneName = namaZona,
                monthlyFee = biayaBulanan.toLong(),
                dailyCosts = biayaHarian.toLong()
            ).onSuccess {
                _state.update { it.copy(isButtonLoading = false) }
                _events.send(TambahZonaParkiranMobilEvents.NavigateBackSendSuccessTambahZonaParkiranMobil)
            }.onError { error ->
                _state.update { it.copy(isButtonLoading = false) }
                _events.send(TambahZonaParkiranMobilEvents.ShowSnackBarError(error.message))
            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(TambahZonaParkiranMobilEvents.NavigateBack)
        }
    }
}