package com.kosrvd.app.feature.rental.presentation.buat_penyewaan

import androidx.compose.ui.util.fastJoinToString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.domain.models.Account
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.core.domain.utils.onSuccess
import com.kosrvd.app.presentation.navigation.models.ResultCreatePenyewaan
import com.kosrvd.app.core.presentation.utils.PatternValidation
import com.kosrvd.app.core.presentation.utils.calculateTotalBill
import com.kosrvd.app.core.domain.models.AlatElektronik
import com.kosrvd.app.core.domain.usecase.CheckNetworkUseCase
import com.kosrvd.app.feature.rental.domain.model.BuatPenyewaan
import com.kosrvd.app.feature.rental.domain.model.InfoKamarSewa
import com.kosrvd.app.feature.rental.domain.model.InfoPakaiParkirMobilBulanan
import com.kosrvd.app.feature.rental.domain.model.InfoPenghuni
import com.kosrvd.app.feature.rental.domain.model.InfoZonaParkir
import com.kosrvd.app.feature.room.domain.model.Kamar
import com.kosrvd.app.feature.parking.zona_parkiran_mobil.domain.model.ZonaParkiran
import com.kosrvd.app.feature.rental.domain.repository.PenyewaanRepository
import com.kosrvd.app.feature.rental.domain.usecase.GetDataInitalBuatPenyewaanUseCase
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

sealed interface BuatPenyewaanEvents {
    data object NavigateBack : BuatPenyewaanEvents
    data class NavigateToResultScreen(val resultCreatePenyewaan: ResultCreatePenyewaan) :
        BuatPenyewaanEvents

    data class ShowSnackBarError(val message: String) : BuatPenyewaanEvents
}

sealed interface BuatPenyewaanActions {
    data object NavigateBack : BuatPenyewaanActions
    data object AddPenyewaan : BuatPenyewaanActions
    data object NextPage : BuatPenyewaanActions
    data object BackPage : BuatPenyewaanActions
    data object TryAgain : BuatPenyewaanActions
    data class OnNumberPlateChange(val numberPlate: String) : BuatPenyewaanActions
    data class OnCarBrandChange(val carBrand: String) : BuatPenyewaanActions
    data class OnCarNameChange(val carName: String) : BuatPenyewaanActions
    data class OnNotesChange(val notes: String) : BuatPenyewaanActions
    data class OnSelectedKamarChange(val selectedKamar: Kamar) : BuatPenyewaanActions
    data class OnSelectedPenghuniPertamaChange(val selectedPenghuniPertama: Account?) :
        BuatPenyewaanActions

    data class OnSelectedPenghuniKeduaChange(val selectedPenghuniKedua: Account?) :
        BuatPenyewaanActions

    data class OnSelectedZoneParkingChange(val selectedZoneParking: ZonaParkiran?) :
        BuatPenyewaanActions

    data class OnUpdateNamaAlatElektronik(val namaAlatElektronik: String) : BuatPenyewaanActions
    data class OnUpdatePriceAlatElektronik(val priceAlatElektronik: String) : BuatPenyewaanActions
    data object AddAlatElektronik : BuatPenyewaanActions
    data class RemoveAlatElektronik(val index: Int) : BuatPenyewaanActions
}

@HiltViewModel
class BuatPenyewaanViewModel @Inject constructor(
    private val getDataInitalBuatPenyewaanUseCase: GetDataInitalBuatPenyewaanUseCase,
    private val penyewaanRepository: PenyewaanRepository,
    private val checkNetworkUseCase: CheckNetworkUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(BuatPenyewaanUiState())
    val state = _state
        .onStart { loadInitalDataPenyewaan() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = BuatPenyewaanUiState()
        )

    private val _events = Channel<BuatPenyewaanEvents>()
    val events = _events.receiveAsFlow()

    fun onActions(actions: BuatPenyewaanActions) {
        when (actions) {
            BuatPenyewaanActions.AddAlatElektronik -> addAlatElektronik()
            BuatPenyewaanActions.AddPenyewaan -> addPenyewaan()
            BuatPenyewaanActions.NavigateBack -> navigateBack()
            BuatPenyewaanActions.NextPage -> nextPage()
            BuatPenyewaanActions.BackPage -> backPage()
            BuatPenyewaanActions.TryAgain -> loadInitalDataPenyewaan()
            is BuatPenyewaanActions.OnCarBrandChange -> onCarBrandChange(actions.carBrand)
            is BuatPenyewaanActions.OnCarNameChange -> onCarNameChange(actions.carName)
            is BuatPenyewaanActions.OnNotesChange -> onNotesChange(actions.notes)
            is BuatPenyewaanActions.OnNumberPlateChange -> onNumberPlateChange(actions.numberPlate)
            is BuatPenyewaanActions.OnSelectedKamarChange -> onSelectedKamarChange(actions.selectedKamar)
            is BuatPenyewaanActions.OnSelectedPenghuniKeduaChange -> onSelectedPenghuniKeduaChange(
                actions.selectedPenghuniKedua
            )

            is BuatPenyewaanActions.OnSelectedPenghuniPertamaChange -> onSelectedPenghuniPertamaChange(
                actions.selectedPenghuniPertama
            )

            is BuatPenyewaanActions.OnSelectedZoneParkingChange -> onSelectedZoneParkingChange(
                actions.selectedZoneParking
            )

            is BuatPenyewaanActions.OnUpdateNamaAlatElektronik -> onUpdateNamaAlatElektronik(actions.namaAlatElektronik)
            is BuatPenyewaanActions.OnUpdatePriceAlatElektronik -> onUpdatePriceAlatElektronik(
                actions.priceAlatElektronik
            )

            is BuatPenyewaanActions.RemoveAlatElektronik -> removeAlatElektronik(actions.index)
        }
    }

    private fun backPage() {
        val currentState = _state.value
        val currentPage = currentState.currentPage
        if (currentPage > 1) {
            _state.update {
                var newState = it.copy(currentPage = currentPage - 1)
                // Reset input elektronik jika meninggalkan halaman 3
                if (currentPage == 3) {
                    newState = newState.copy(
                        namaAlatElektronik = "",
                        priceAlatElektronik = ""
                    )
                }
                // Update roomPrice jika mau ke page 1
                else if (currentPage == 2) {
                    newState = newState.updateRoomPrice()
                }
                newState
            }
        }
        updateButtonNextEnabled()
    }

    private fun nextPage() {
        val currentState = _state.value
        val sizePage = currentState.listNamePage.size
        val currentPage = currentState.currentPage
        if (currentPage < sizePage) {
            _state.update {
                var newState = it.copy(currentPage = currentPage + 1)
                // Reset input elektronik jika meninggalkan halaman 3
                if (currentPage == 3) {
                    newState = newState.copy(
                        namaAlatElektronik = "",
                        priceAlatElektronik = ""
                    )
                } else if (currentPage == 2) {
                    val names = mutableListOf<String>()
                    // Penghuni pertama (Wajib)
                    currentState.selectedPenghuniPertama?.let { p1 -> names.add(p1.name) }
                    // Penghuni kedua (Opsional)
                    currentState.selectedPenghuniKedua?.let { p2 -> names.add(p2.name) }

                    newState = newState.copy(
                        listNamePenghuni = names
                    )
                    // Update roomPrice berdasarkan pilihan penghuni final
                    newState = newState.updateRoomPrice()
                }
                newState
            }
        }
        updateButtonNextEnabled()
        calculateBill()
    }

    private fun BuatPenyewaanUiState.updateRoomPrice(): BuatPenyewaanUiState {
        val price =
            if (this.selectedPenghuniKedua != null && this.selectedKamar?.price?.twoPersons != null) {
                this.selectedKamar.price.twoPersons
            } else {
                this.selectedKamar?.price?.onePerson ?: 0L
            }
        return this.copy(roomPrice = price)
    }

    private fun updateButtonNextEnabled() {
        val currentState = _state.value
        val isEnabled = when (currentState.currentPage) {
            1 -> currentState.selectedKamar != null
            2 -> {
                currentState.selectedPenghuniPertama != null &&
                        currentState.selectedPenghuniPertama != currentState.selectedPenghuniKedua
            }

            3 -> true
            4 -> {
                if (currentState.selectedZoneParking != null) {
                    currentState.carName.isNotBlank() &&
                            currentState.carBrand.isNotBlank() &&
                            currentState.numberPlate.isNotBlank() &&
                            !currentState.isCarNameError &&
                            !currentState.isCarBrandError &&
                            !currentState.isNumberPlateError
                } else true
            }

            5 -> true // Halaman Konfirmasi
            else -> false
        }
        _state.update { it.copy(isButtonNextEnabled = isEnabled) }
    }

    private fun onUpdatePriceAlatElektronik(priceAlatElektronik: String) {
        val isError =
            if (priceAlatElektronik.isEmpty()) false else !PatternValidation.isHargaSewaValid(
                priceAlatElektronik
            )
        val errorText =
            if (priceAlatElektronik.isEmpty()) null else PatternValidation.getHargaSewaError(
                priceAlatElektronik
            )
        _state.update {
            it.copy(
                priceAlatElektronik = priceAlatElektronik,
                isPriceAlatElektronikError = isError,
                priceAlatElektronikError = errorText
            )
        }
        buttonAddAlatElektronikEnabled()
    }

    private fun onUpdateNamaAlatElektronik(namaAlatElektronik: String) {
        val isError =
            if (namaAlatElektronik.isEmpty()) false else !PatternValidation.isNamaElektronikValid(
                namaAlatElektronik
            )
        val errorText =
            if (namaAlatElektronik.isEmpty()) null else PatternValidation.getNamaElektronikError(
                namaAlatElektronik
            )
        _state.update {
            it.copy(
                namaAlatElektronik = namaAlatElektronik,
                isNamaAlatElektronikError = isError,
                namaAlatElektronikError = errorText
            )
        }
        buttonAddAlatElektronikEnabled()
    }

    private fun buttonAddAlatElektronikEnabled() {
        val currentState = _state.value
        val isEnabled = currentState.namaAlatElektronik.isNotEmpty() &&
                !currentState.isNamaAlatElektronikError &&
                currentState.priceAlatElektronik.isNotEmpty() &&
                !currentState.isPriceAlatElektronikError
        _state.update {
            it.copy(
                isButtonAddAlatElektronikEnabled = isEnabled
            )
        }
    }

    private fun onSelectedZoneParkingChange(selectedZoneParking: ZonaParkiran?) {
        _state.update {
            var newState = it.copy(selectedZoneParking = selectedZoneParking)
            // Reset data mobil jika zona parkir dipilih null
            if (selectedZoneParking == null) {
                newState = newState.copy(
                    carName = "",
                    carNameError = null,
                    isCarNameError = false,
                    carBrand = "",
                    carBrandError = null,
                    isCarBrandError = false,
                    numberPlate = "",
                    numberPlateError = null,
                    isNumberPlateError = false,
                    notes = ""
                )
            }
            newState
        }
        updateButtonNextEnabled()
        calculateBill()
    }

    private fun onSelectedPenghuniPertamaChange(selectedPenghuniPertama: Account?) {
        _state.update { it.copy(selectedPenghuniPertama = selectedPenghuniPertama) }
        updateButtonNextEnabled()
    }

    private fun onSelectedPenghuniKeduaChange(selectedPenghuniKedua: Account?) {
        _state.update { it.copy(selectedPenghuniKedua = selectedPenghuniKedua) }
    }

    private fun onSelectedKamarChange(selectedKamar: Kamar) {
        _state.update { currentState ->
            // Filter listAlatElektronik yang sudah ada, ambil yang tipenya ADD_ON saja
            val currentAddOns =
                currentState.listAlatElektronik.filter { it.origin == Constant.ADD_ON }

            // Gabungkan freeService dari kamar baru dengan ADD_ON yang sudah ada
            val updatedListElektronik =
                selectedKamar.freeService.map { it.copy(origin = Constant.ORIGIN_KAMAR_DEFAULT) } + currentAddOns

            currentState.copy(
                selectedKamar = selectedKamar,
                listAlatElektronik = updatedListElektronik
            ).updateRoomPrice() // Update roomPrice sekalian saat ganti kamar
        }
        updateButtonNextEnabled()
        calculateBill()
    }

    private fun removeAlatElektronik(index: Int) {
        val currentList = _state.value.listAlatElektronik
        if (index in currentList.indices) {
            val newList = currentList.toMutableList().apply { removeAt(index) }
            _state.update { it.copy(listAlatElektronik = newList) }
            calculateBill()
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
        updateButtonNextEnabled()
    }

    private fun onNotesChange(notes: String) {
        _state.update { it.copy(notes = notes) }
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
        updateButtonNextEnabled()
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
        updateButtonNextEnabled()
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(BuatPenyewaanEvents.NavigateBack)
        }
    }

    private fun addPenyewaan() {
        viewModelScope.launch {
            val currentState = _state.value

            // Kondisi checking sebelum memproses
            if (currentState.selectedKamar == null || currentState.selectedPenghuniPertama == null) {
                _events.send(BuatPenyewaanEvents.ShowSnackBarError("Data kamar dan penghuni pertama wajib diisi"))
                return@launch
            }

            _state.update { it.copy(isButtonSubmitLoading = true) }

            if (!checkNetworkUseCase()){
                _state.update {
                    it.copy(isButtonSubmitLoading = false)
                }
                _events.send(
                    BuatPenyewaanEvents.ShowSnackBarError("Tidak ada koneksi internet. Mohon cek kembali jaringan Anda.")
                )
                return@launch
            }

            // 1. Persiapan List Resident (InfoPenghuni)
            val listResident = mutableListOf<InfoPenghuni>()
            listResident.add(
                InfoPenghuni(
                    idAkun = currentState.selectedPenghuniPertama.idAkun,
                    name = currentState.selectedPenghuniPertama.name
                )
            )
            currentState.selectedPenghuniKedua?.let {
                listResident.add(InfoPenghuni(idAkun = it.idAkun, name = it.name))
            }

            // 2. Persiapan Info Kamar (InfoKamarSewa)
            val infoKamar = InfoKamarSewa(
                idKamar = currentState.selectedKamar.idKamar,
                currentRoomRentalCost = currentState.selectedKamar.price,
                numberRoom = currentState.selectedKamar.numberRoom
            )

            // 3. Persiapan Pemakaian Parkir (InfoPakaiParkirMobilBulanan)
            val pemakaianParkirMobil = currentState.selectedZoneParking?.let { zone ->
                InfoPakaiParkirMobilBulanan(
                    carName = currentState.carName,
                    numberPlate = currentState.numberPlate,
                    carBrand = currentState.carBrand,
                    notes = currentState.notes.ifBlank { null },
                    zonaParkir = InfoZonaParkir(
                        idZonaParkir = zone.idZonaParkir,
                        zoneName = zone.zoneName,
                        monthlyFee = zone.monthlyFee
                    )
                )
            }

            // 4. Model BuatPenyewaan
            val buatPenyewaanModel = BuatPenyewaan(
                listResident = listResident,
                infoKamar = infoKamar,
                pemakaianAlatElektronikBulanan = currentState.listAlatElektronik,
                pemakaianParkirMobilBulanan = pemakaianParkirMobil,
                rentalStartDate = Timestamp.now(),
                rentalCompletionDate = null,
                rentalStatus = Constant.ACTIVE
            )

            // 5. Eksekusi Repository
            penyewaanRepository.buatPenyewaan(buatPenyewaanModel)
                .onSuccess { penyewaan ->
                    _state.update { it.copy(isButtonSubmitLoading = false) }
                    _events.send(
                        BuatPenyewaanEvents.NavigateToResultScreen(
                            ResultCreatePenyewaan(
                                idPenyewaan = penyewaan.idPenyewa,
                                status = penyewaan.rentalStatus,
                                nomorKamar = penyewaan.infoKamar.numberRoom,
                                penghuni = penyewaan.listResident.map { it.name }
                                    .fastJoinToString(separator = " & "),
                                totalTagihan = calculateTotalBill(
                                    hargaSewaKamar = if (penyewaan.listResident.size == 2 && penyewaan.infoKamar.currentRoomRentalCost.twoPersons != null) penyewaan.infoKamar.currentRoomRentalCost.twoPersons else penyewaan.infoKamar.currentRoomRentalCost.onePerson,
                                    hargaSewaParkirMobil = penyewaan.pemakaianParkirMobilBulanan?.zonaParkir?.monthlyFee,
                                    hargaPemakaianElektronik = penyewaan.pemakaianAlatElektronikBulanan
                                )
                            )
                        )
                    )
                }
                .onError { error ->
                    _state.update { it.copy(isButtonSubmitLoading = false) }
                    _events.send(BuatPenyewaanEvents.ShowSnackBarError(error.message))
                }
        }
    }

    private fun addAlatElektronik() {
        val dataAlatElektronik = _state.value.listAlatElektronik
        val dataAlatElektronikBaru = AlatElektronik(
            toolName = _state.value.namaAlatElektronik,
            cost = _state.value.priceAlatElektronik.toLongOrNull() ?: 0L,
            origin = Constant.ADD_ON
        )
        _state.update {
            it.copy(
                listAlatElektronik = dataAlatElektronik + dataAlatElektronikBaru,
                namaAlatElektronik = "",
                priceAlatElektronik = "",
                isButtonAddAlatElektronikEnabled = false // Reset setelah tambah
            )
        }
        calculateBill()
    }

    private fun calculateBill() {
        val currentState = _state.value
        val total = calculateTotalBill(
            hargaSewaKamar = currentState.roomPrice,
            hargaSewaParkirMobil = currentState.selectedZoneParking?.monthlyFee,
            hargaPemakaianElektronik = currentState.listAlatElektronik
        )
        _state.update { it.copy(totalBiaya = total) }
    }


    private fun loadInitalDataPenyewaan() {
        viewModelScope.launch {
            _state.update { it.copy(isListKamarLoading = true, loadKamarError = null) }

            if (!checkNetworkUseCase()){
                _state.update {
                    it.copy(
                        isListKamarLoading = false,
                        loadKamarError = "Tidak ada koneksi internet. Mohon cek kembali jaringan Anda.",
                        listKamar = emptyList()
                    )
                }
                return@launch
            }

            getDataInitalBuatPenyewaanUseCase()
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            isListKamarLoading = false,
                            listKamar = result.listKamar,
                            listPenghuni = if (result.listPenghuni.isNotEmpty()) listOf(null) + result.listPenghuni else emptyList(),
                            listZoneParking = if (result.listZoneParking.isNotEmpty()) listOf(null) + result.listZoneParking else emptyList(),
                            loadKamarError = null
                        )
                    }
                }
                .onError { result ->
                    _state.update {
                        it.copy(
                            isListKamarLoading = false,
                            loadKamarError = result.message,
                            listKamar = emptyList()
                        )
                    }
                }
        }
    }
}
