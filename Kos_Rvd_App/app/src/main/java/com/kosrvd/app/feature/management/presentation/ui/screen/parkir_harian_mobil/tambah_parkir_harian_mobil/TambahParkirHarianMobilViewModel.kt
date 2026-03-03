package com.kosrvd.app.feature.management.presentation.ui.screen.parkir_harian_mobil.tambah_parkir_harian_mobil

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.core.domain.utils.onSuccess
import com.kosrvd.app.core.presentation.utils.PatternValidation
import com.kosrvd.app.feature.management.domain.model.ZonaParkiran
import com.kosrvd.app.feature.management.domain.usecase.GetListZonaParkirHarianUseCase
import com.kosrvd.app.feature.management.domain.usecase.TambahParkirHarianMobilUseCase
import com.kosrvd.app.feature.management.presentation.designsystem.utils.UTC_ZONE_ID
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toDayMonthShortAndYear
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface TambahParkirHarianMobilEvents {
    data object NavigateBack: TambahParkirHarianMobilEvents
    data object NavigateBackSuccessAddParkirHarianMobil: TambahParkirHarianMobilEvents
    data class ShowSnackBarErrorMessage(val message: String): TambahParkirHarianMobilEvents
}

sealed interface TambahParkirHarianMobilActions {
    data object NavigateBack: TambahParkirHarianMobilActions
    data object TambahParkirHarianMobil: TambahParkirHarianMobilActions
    data class UpdateUserName(val userName: String): TambahParkirHarianMobilActions
    data class UpdateNumberPlate(val numberPlate: String): TambahParkirHarianMobilActions
    data class UpdateCarBrand(val carBrand: String): TambahParkirHarianMobilActions
    data class UpdateCarName(val carName: String): TambahParkirHarianMobilActions
    data class UpdateNotes(val notes: String): TambahParkirHarianMobilActions
    data class UpdateSelectedZoneParking(val selectedZoneParking: ZonaParkiran): TambahParkirHarianMobilActions
    data object NextPage: TambahParkirHarianMobilActions
    data object BackPage: TambahParkirHarianMobilActions
    data class UpdateSelectedDate(val startDate: Long?, val completionDate: Long?): TambahParkirHarianMobilActions
    data object TryAgain: TambahParkirHarianMobilActions
}

@HiltViewModel
class TambahParkirHarianMobilViewModel @Inject constructor(
    private val tambahParkirHarianMobilUseCase: TambahParkirHarianMobilUseCase,
    private val getListZonaParkirHarianUseCase: GetListZonaParkirHarianUseCase
): ViewModel() {
    private val _state = MutableStateFlow(TambahParkirHarianMobilUiState())
    val state = _state.asStateFlow()

    private val _events = Channel<TambahParkirHarianMobilEvents>()
    val events = _events.receiveAsFlow()

    fun onActions(actions: TambahParkirHarianMobilActions){
        when(actions){
            TambahParkirHarianMobilActions.NavigateBack -> navigateBack()
            TambahParkirHarianMobilActions.TambahParkirHarianMobil -> tambahParkirHarianMobil()
            TambahParkirHarianMobilActions.TryAgain -> loadListZoneParking()
            TambahParkirHarianMobilActions.BackPage -> backPage()
            TambahParkirHarianMobilActions.NextPage -> nextPage()
            is TambahParkirHarianMobilActions.UpdateCarBrand -> updateCarBrand(actions.carBrand)
            is TambahParkirHarianMobilActions.UpdateCarName -> updateCarName(actions.carName)
            is TambahParkirHarianMobilActions.UpdateNotes -> updateNotes(actions.notes)
            is TambahParkirHarianMobilActions.UpdateNumberPlate -> updateNumberPlate(actions.numberPlate)
            is TambahParkirHarianMobilActions.UpdateSelectedDate -> updateSelectedDate(actions.startDate, actions.completionDate)
            is TambahParkirHarianMobilActions.UpdateSelectedZoneParking -> updateSelectedZoneParking(actions.selectedZoneParking)
            is TambahParkirHarianMobilActions.UpdateUserName -> updateUserName(actions.userName)
        }
    }

    private fun updateSelectedZoneParking(selectedZoneParking: ZonaParkiran) {
        _state.update {
            it.copy(
                selectedZoneParking = selectedZoneParking
            )
        }
        calculateTotalCost()
        updateButtonNextEnabled()
    }

    private fun updateUserName(userName: String) {
        val isError = if (userName.isEmpty()) false else !PatternValidation.isUserNameValid(userName)
        val errorText = if (userName.isEmpty()) null else PatternValidation.getUserNameError(userName)
        _state.update {
            it.copy(
                userName = userName,
                isUserNameError = isError,
                userNameError = errorText
            )
        }
        updateButtonNextEnabled()
    }

    private fun updateSelectedDate(startDate: Long?, completionDate: Long?) {
        _state.update {
            it.copy(
                startDate = startDate,
                startDateFormat = startDate?.toDayMonthShortAndYear(),
                completionDate = completionDate,
                completionDateFormat = completionDate?.toDayMonthShortAndYear()
            )
        }
        calculateSumDayBooking()
        updateButtonNextEnabled()
    }

    private fun updateNumberPlate(numberPlate: String) {
        val isError = if (numberPlate.isEmpty()) false else !PatternValidation.isNumberPlateValid(numberPlate)
        val errorText = if (numberPlate.isEmpty()) null else PatternValidation.getNumberPlateError(numberPlate)
        _state.update {
            it.copy(
                numberPlate = numberPlate,
                isNumberPlateError = isError,
                numberPlateError = errorText
            )
        }
        updateButtonNextEnabled()
    }

    private fun updateNotes(notes: String) {
        _state.update { it.copy(notes = notes) }
    }

    private fun updateCarName(carName: String) {
        val isError = if (carName.isEmpty()) false else !PatternValidation.isCarNameValid(carName)
        val errorText = if (carName.isEmpty()) null else PatternValidation.getCarNameError(carName)
        _state.update {
            it.copy(
                carName = carName,
                isCarNameError = isError,
                carNameError = errorText
            )
        }
        updateButtonNextEnabled()
    }

    private fun updateCarBrand(carBrand: String) {
        val isError = if (carBrand.isEmpty()) false else !PatternValidation.isCarBrandValid(carBrand)
        val errorText = if (carBrand.isEmpty()) null else PatternValidation.getCarBrandError(carBrand)
        _state.update {
            it.copy(
                carBrand = carBrand,
                isCarBrandError = isError,
                carBrandError = errorText
            )
        }
        updateButtonNextEnabled()
    }

    private fun nextPage() {
        val sizePage = _state.value.listNamePage.size
        val currentPage = _state.value.currentPage
        if (currentPage < sizePage) {
            val nextPageIndex = currentPage + 1
            if (nextPageIndex == 2) {
                Log.d("Next Page", "Trigger Next Page == 2")
                loadListZoneParking()
            }
            _state.update {
                it.copy(currentPage = nextPageIndex)
            }
        }
        updateButtonNextEnabled()
    }

    private fun backPage() {
        val currentPage = _state.value.currentPage
        if (currentPage > 1) {
            val prevPageIndex = currentPage - 1
            if (prevPageIndex == 1) {
                Log.d("Back Page", "Trigger Back Page == 1")
                _state.update {
                    it.copy(
                        selectedZoneParking = null,
                        listZoneParking = emptyList(),
                        totalCost = 0L,
                        isListZoneParkingLoading = true
                    )
                }
            }
            _state.update {
                it.copy(currentPage = prevPageIndex)
            }
        }
        updateButtonNextEnabled()
    }

    private fun loadListZoneParking() {
        viewModelScope.launch {
            _state.update { it.copy(isListZoneParkingLoading = true, loadError = null) }

            val startDate = _state.value.startDate
            val completionDate = _state.value.completionDate

            if (startDate == null || completionDate == null) {
                _state.update { it.copy(isListZoneParkingLoading = false, loadError = "Tanggal harus diisi, Silakan kembali ke halaman sebelumnya dan coba lagi") }
                return@launch
            }

            getListZonaParkirHarianUseCase(
                startDate = startDate,
                completionDate = completionDate
            ).onSuccess { result ->
                Log.d("Load List Zone", result.toString())
                _state.update { it.copy(isListZoneParkingLoading = false, listZoneParking = result) }
            }.onError { result ->
                _state.update { it.copy(isListZoneParkingLoading = false, loadError = result.message) }
            }
        }
    }

    private fun tambahParkirHarianMobil() {
        viewModelScope.launch {
            _state.update { it.copy(isButtonSubmitLoading = true) }

            val selectedParkingZone = _state.value.selectedZoneParking
            val userName = _state.value.userName
            val numberPlate = _state.value.numberPlate
            val carBrand = _state.value.carBrand
            val carName = _state.value.carName
            val notes = _state.value.notes.ifEmpty { null }
            val startDate = _state.value.startDate
            val completionDate = _state.value.completionDate
            val totalCost = _state.value.totalCost

            if (selectedParkingZone == null) {
                _state.update { it.copy(isButtonSubmitLoading = false) }
                _events.send(TambahParkirHarianMobilEvents.ShowSnackBarErrorMessage("Pilih Zona Parkir Terlebih Dahulu"))
                return@launch
            }

            if (userName.isEmpty() || numberPlate.isEmpty() || carBrand.isEmpty() || carName.isEmpty()) {
                _state.update { it.copy(isButtonSubmitLoading = false) }
                _events.send(TambahParkirHarianMobilEvents.ShowSnackBarErrorMessage("Semua data harus diisi"))
                return@launch
            }

            if (startDate == null || completionDate == null) {
                _state.update { it.copy(isButtonSubmitLoading = false) }
                _events.send(TambahParkirHarianMobilEvents.ShowSnackBarErrorMessage("Semua tanggal harus diisi"))
                return@launch
            }

            tambahParkirHarianMobilUseCase(
                idZonaParkir = selectedParkingZone.idZonaParkir,
                userName = userName,
                zoneName = selectedParkingZone.zoneName,
                numberPlate = numberPlate,
                carBrand = carBrand,
                carName = carName,
                notes = notes,
                startDate = startDate,
                completionDate = completionDate,
                totalCost = totalCost
            ).onSuccess {
                _state.update { it.copy(isButtonSubmitLoading = false) }
                _events.send(TambahParkirHarianMobilEvents.NavigateBackSuccessAddParkirHarianMobil)
            }.onError { result ->
                _state.update { it.copy(isButtonSubmitLoading = false) }
                _events.send(TambahParkirHarianMobilEvents.ShowSnackBarErrorMessage(result.message))
            }

        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(TambahParkirHarianMobilEvents.NavigateBack)
        }
    }

    private fun updateButtonNextEnabled() {
        val currentState = _state.value
        val isEnabled = when(currentState.currentPage){
            1 -> currentState.startDate != null && currentState.completionDate != null
            2 -> currentState.selectedZoneParking != null
            3 -> {
                currentState.userName.isNotBlank() && !currentState.isUserNameError &&
                currentState.numberPlate.isNotBlank() && !currentState.isNumberPlateError &&
                currentState.carBrand.isNotBlank() && !currentState.isCarBrandError &&
                currentState.carName.isNotBlank() && !currentState.isCarNameError
            }
            4 -> true // Halaman Konfirmasi
            else -> false
        }
        _state.update { it.copy(isButtonNextEnabled = isEnabled) }
    }

    private fun calculateSumDayBooking() {
        val startDate = _state.value.startDate
        val completionDate = _state.value.completionDate
        if (startDate != null && completionDate != null) {
            val diff = completionDate - startDate
            val sumDay = (diff / (1000 * 60 * 60 * 24)).toInt() + 1
            _state.update { it.copy(sumDayBooking = sumDay) }
        } else {
            _state.update { it.copy(sumDayBooking = 0) }
        }
    }

    private fun calculateTotalCost() {
        val totalDay = _state.value.sumDayBooking
        val selectedZoneParking = _state.value.selectedZoneParking
        if (selectedZoneParking != null) {
            val totalCost = totalDay.toLong() * selectedZoneParking.dailyCosts
            _state.update { it.copy(totalCost = totalCost) }
        } else {
            _state.update { it.copy(totalCost = 0L) }
        }
    }

}
