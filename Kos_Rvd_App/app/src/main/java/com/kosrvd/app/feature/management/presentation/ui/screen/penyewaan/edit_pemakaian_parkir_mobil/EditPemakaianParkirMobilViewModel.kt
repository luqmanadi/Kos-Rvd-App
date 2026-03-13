package com.kosrvd.app.feature.management.presentation.ui.screen.penyewaan.edit_pemakaian_parkir_mobil

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.core.domain.utils.onSuccess
import com.kosrvd.app.core.navigation.NavigationScreen
import com.kosrvd.app.core.navigation.models.CustomNavTypes
import com.kosrvd.app.core.navigation.models.InfoPakaiParkirMobilBulananSerialize
import com.kosrvd.app.core.navigation.models.InfoZonaParkirSerialize
import com.kosrvd.app.core.navigation.models.toInfoZonaParkir
import com.kosrvd.app.core.presentation.utils.PatternValidation
import com.kosrvd.app.feature.management.domain.model.InfoPakaiParkirMobilBulanan
import com.kosrvd.app.feature.management.domain.model.InfoZonaParkir
import com.kosrvd.app.feature.management.domain.model.ZonaParkiran
import com.kosrvd.app.feature.management.domain.repository.PenyewaanRepository
import com.kosrvd.app.feature.management.domain.usecase.GetDataZonaParkirAvailableForMonthlyParkingUsageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.typeOf

sealed interface EditPemakaianParkirMobilEvents {
    data object NavigateBack : EditPemakaianParkirMobilEvents
    data class ShowSnackBarError(val message: String) : EditPemakaianParkirMobilEvents
    data object NavigateBackSuccessEditPemakaianParkirMobil : EditPemakaianParkirMobilEvents
}

sealed interface EditPemakaianParkirMobilActions {
    data object NavigateBack : EditPemakaianParkirMobilActions
    data object SaveEditPemakaianParkirMobil : EditPemakaianParkirMobilActions
    data object EndPemakaianParkirMobil : EditPemakaianParkirMobilActions
    data class OnNumberPlateChange(val numberPlate: String) : EditPemakaianParkirMobilActions
    data class OnCarBrandChange(val carBrand: String) : EditPemakaianParkirMobilActions
    data class OnCarNameChange(val carName: String) : EditPemakaianParkirMobilActions
    data class OnNotesChange(val notes: String) : EditPemakaianParkirMobilActions
    data class OnSelectedZoneParkingChange(val selectedZoneParking: ZonaParkiran?) :
        EditPemakaianParkirMobilActions
}

@HiltViewModel
class EditPemakaianParkirMobilViewModel @Inject constructor(
    private val penyewaanRepository: PenyewaanRepository,
    private val getDataZonaParkirAvailableForMonthlyParkingUsageUseCase: GetDataZonaParkirAvailableForMonthlyParkingUsageUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow(EditPemakaianParkirMobilUiState())
    val state = _state.asStateFlow()

    private val _events = Channel<EditPemakaianParkirMobilEvents>()
    val events = _events.receiveAsFlow()

    init {
        // Ambil objek argument dari savedStateHandle
        val arguments =
            savedStateHandle.toRoute<NavigationScreen.EditPemakaianParkirMobilBulananScreen>(
                typeMap = mapOf(
                    typeOf<InfoPakaiParkirMobilBulananSerialize?>() to CustomNavTypes.infoPemakaianParkirMobilBulanan,
                )
            )

        initialData(arguments.idPenyewaan, arguments.pemakaianParkirMobilBulanan)
        loadAllZonaParkir()
    }

    fun onActions(actions: EditPemakaianParkirMobilActions) {
        when (actions) {
            EditPemakaianParkirMobilActions.EndPemakaianParkirMobil -> endPemakaianParkirMobil()
            EditPemakaianParkirMobilActions.NavigateBack -> navigateBack()
            is EditPemakaianParkirMobilActions.OnCarBrandChange -> onCarBrandChange(actions.carBrand)
            is EditPemakaianParkirMobilActions.OnCarNameChange -> onCarNameChange(actions.carName)
            is EditPemakaianParkirMobilActions.OnNotesChange -> onNotesChange(actions.notes)
            is EditPemakaianParkirMobilActions.OnNumberPlateChange -> onNumberPlateChange(actions.numberPlate)
            is EditPemakaianParkirMobilActions.OnSelectedZoneParkingChange -> onSelectedZoneParkingChange(
                actions.selectedZoneParking
            )

            EditPemakaianParkirMobilActions.SaveEditPemakaianParkirMobil -> saveEditPemakaianParkirMobil()
        }
    }

    private fun loadAllZonaParkir() {
        viewModelScope.launch {
            getDataZonaParkirAvailableForMonthlyParkingUsageUseCase()
                .onSuccess { listZona ->
                    _state.update { it.copy(listZonaParkir = listZona) }
                }
                .onError { error ->
                    _events.send(EditPemakaianParkirMobilEvents.ShowSnackBarError(error.message))
                }
        }
    }

    private fun onNumberPlateChange(numberPlate: String) {
        val isError =
            if (numberPlate.isEmpty()) false else !PatternValidation.isNumberPlateValid(numberPlate)
        val errorText =
            if (numberPlate.isEmpty()) null else PatternValidation.getNumberPlateError(numberPlate)
        _state.update {
            it.copy(
                numberPlate = numberPlate,
                isNumberPlateError = isError,
                numberPlateError = errorText
            )
        }
        buttonSubmitEnabled()
    }

    private fun onNotesChange(notes: String) {
        _state.update { it.copy(notes = notes) }
        buttonSubmitEnabled()
    }

    private fun onCarNameChange(carName: String) {
        val isError = if (carName.isEmpty()) false else !PatternValidation.isCarNameValid(carName)
        val errorText = if (carName.isEmpty()) null else PatternValidation.getCarNameError(carName)
        _state.update {
            it.copy(
                carName = carName,
                isCarNameError = isError,
                carNameError = errorText
            )
        }
        buttonSubmitEnabled()
    }

    private fun onCarBrandChange(carBrand: String) {
        val isError =
            if (carBrand.isEmpty()) false else !PatternValidation.isCarBrandValid(carBrand)
        val errorText =
            if (carBrand.isEmpty()) null else PatternValidation.getCarBrandError(carBrand)
        _state.update {
            it.copy(
                carBrand = carBrand,
                isCarBrandError = isError,
                carBrandError = errorText
            )
        }
        buttonSubmitEnabled()
    }

    private fun onSelectedZoneParkingChange(selectedZoneParking: ZonaParkiran?) {
        _state.update { it.copy(newSelectedZoneParking = selectedZoneParking) }
        buttonSubmitEnabled()
    }

    private fun saveEditPemakaianParkirMobil() {
        viewModelScope.launch {
            _state.update { it.copy(isButtonSubmitLoading = true) }
            val currentState = _state.value
            
            val selectedInfoZona = if (currentState.newSelectedZoneParking != null) {
                InfoZonaParkir(
                    idZonaParkir = currentState.newSelectedZoneParking.idZonaParkir,
                    zoneName = currentState.newSelectedZoneParking.zoneName,
                    monthlyFee = currentState.newSelectedZoneParking.monthlyFee
                )
            } else {
                currentState.oldSelectedZoneParking
            }

            if (selectedInfoZona != null) {
                val modelInfoParkirMobilBulanan = InfoPakaiParkirMobilBulanan(
                    carName = currentState.carName,
                    numberPlate = currentState.numberPlate,
                    carBrand = currentState.carBrand,
                    notes = currentState.notes.ifEmpty { null },
                    zonaParkir = selectedInfoZona
                )

                penyewaanRepository.editPemakaianParkirMobilBulanan(
                    idPenyewaan = currentState.idPenyewaan,
                    pakaiParkirMobilBulanan = modelInfoParkirMobilBulanan,
                ).onSuccess {
                    _state.update { it.copy(isButtonSubmitLoading = false) }
                    _events.send(EditPemakaianParkirMobilEvents.NavigateBackSuccessEditPemakaianParkirMobil)
                }.onError { error ->
                    _state.update { it.copy(isButtonSubmitLoading = false) }
                    _events.send(EditPemakaianParkirMobilEvents.ShowSnackBarError(error.message))
                }
            } else {
                _state.update {
                    it.copy(isButtonSubmitLoading = false)
                }
                _events.send(EditPemakaianParkirMobilEvents.ShowSnackBarError("Zona parkir tidak boleh kosong"))
            }
        }
    }

    private fun endPemakaianParkirMobil() {
        viewModelScope.launch {
            _state.update { it.copy(isButtonEndRentalLoading = true) }
            val idPenyewaan = _state.value.idPenyewaan

            penyewaanRepository.endRentalPemakaianParkirMobilBulanan(idPenyewaan)
                .onSuccess {
                    _state.update { it.copy(isButtonEndRentalLoading = false) }
                    _events.send(EditPemakaianParkirMobilEvents.NavigateBackSuccessEditPemakaianParkirMobil)
                }
                .onError { error ->
                    _state.update { it.copy(isButtonEndRentalLoading = false) }
                    _events.send(EditPemakaianParkirMobilEvents.ShowSnackBarError(error.message))
                }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(EditPemakaianParkirMobilEvents.NavigateBack)
        }
    }

    private fun initialData(
        idPenyewaan: String,
        pemakaianParkirMobilBulanan: InfoPakaiParkirMobilBulananSerialize?
    ) {
        _state.update {
            it.copy(
                idPenyewaan = idPenyewaan,
                oldSelectedZoneParking = pemakaianParkirMobilBulanan?.zonaParkir?.toInfoZonaParkir(),
                oldCarName = pemakaianParkirMobilBulanan?.carName ?: "",
                carName = pemakaianParkirMobilBulanan?.carName ?: "",
                oldCarBrand = pemakaianParkirMobilBulanan?.carBrand ?: "",
                carBrand = pemakaianParkirMobilBulanan?.carBrand ?: "",
                oldNumberPlate = pemakaianParkirMobilBulanan?.numberPlate ?: "",
                numberPlate = pemakaianParkirMobilBulanan?.numberPlate ?: "",
                oldNotes = pemakaianParkirMobilBulanan?.notes ?: "",
                notes = pemakaianParkirMobilBulanan?.notes ?: ""
            )
        }
        buttonSubmitEnabled()
    }

    private fun buttonSubmitEnabled() {
        val currentState = _state.value
        
        val isMandatoryFieldsFilled = currentState.carName.isNotEmpty() &&
                currentState.carBrand.isNotEmpty() &&
                currentState.numberPlate.isNotEmpty()
                
        val isNoValidationErrors = !currentState.isCarNameError && 
                !currentState.isCarBrandError && 
                !currentState.isNumberPlateError

        val isDataValid = isMandatoryFieldsFilled && isNoValidationErrors

        val isEnabled = if (currentState.oldSelectedZoneParking != null) {
            // Mode Edit: Harus valid DAN ada perubahan dari data awal
            isDataValid && (
                currentState.newSelectedZoneParking != null ||
                currentState.carName != currentState.oldCarName ||
                currentState.carBrand != currentState.oldCarBrand ||
                currentState.numberPlate != currentState.oldNumberPlate ||
                currentState.notes != currentState.oldNotes
            )
        } else {
            // Mode Tambah: Harus valid DAN zona parkir sudah dipilih
            isDataValid && currentState.newSelectedZoneParking != null
        }

        _state.update { it.copy(isButtonSubmitEnabled = isEnabled) }
    }
}
