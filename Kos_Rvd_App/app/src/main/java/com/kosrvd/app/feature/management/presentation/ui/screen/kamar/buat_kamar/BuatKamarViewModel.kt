package com.kosrvd.app.feature.management.presentation.ui.screen.kamar.buat_kamar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.core.domain.utils.onSuccess
import com.kosrvd.app.core.presentation.utils.PatternValidation
import com.kosrvd.app.core.presentation.utils.UiText
import com.kosrvd.app.feature.management.domain.model.AlatElektronik
import com.kosrvd.app.feature.management.domain.model.BuatKamar
import com.kosrvd.app.feature.management.domain.model.Harga
import com.kosrvd.app.feature.management.domain.usecase.BuatKamarUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface BuatKamarEvents {
    data object NavigateBack: BuatKamarEvents
    data class ShowSnackBarError(val message: String): BuatKamarEvents
    data object NavigateBackSuccessBuatKamarBaru: BuatKamarEvents
}

sealed interface BuatKamarActions {
    data object NavigateBack: BuatKamarActions
    data object BuatKamarBaru: BuatKamarActions
    data class UpdateNumberRoom(val numberRoom: String): BuatKamarActions
    data class UpdateUkuranKamar(val ukuranKamar: String): BuatKamarActions
    data class UpdateTarifSatuOrang(val tarifSatuOrang: String): BuatKamarActions
    data class UpdateTarifDuaOrang(val tarifDuaOrang: String): BuatKamarActions
    data class UpdateSelectJumlahOrang(val jumlahOrang: Int): BuatKamarActions
    data class UpdateNamaAlatElektronik(val namaAlatElektronik: String): BuatKamarActions
    data class UpdateNamaFasilitas(val namaFasilitas: String): BuatKamarActions
    data object AddPemakaianAlatElektronikGratis: BuatKamarActions
    data class RemovePemakaianAlatElektronikGratis(val index: Int): BuatKamarActions
    data object AddFasilitasKamar: BuatKamarActions
    data class RemoveFasilitasKamar(val index: Int): BuatKamarActions
}

@HiltViewModel
class BuatKamarViewModel @Inject constructor(
    private val buatKamarUseCase: BuatKamarUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(BuatKamarUiState())
    val state = _state.asStateFlow()

    private val _events = Channel<BuatKamarEvents>()
    val events = _events.receiveAsFlow()

    fun onActions(actions: BuatKamarActions){
        when(actions){
            BuatKamarActions.NavigateBack -> navigateBack()
            BuatKamarActions.AddFasilitasKamar -> addFasilitasKamar()
            BuatKamarActions.AddPemakaianAlatElektronikGratis -> addPemakaianAlatElektronikGratis()
            BuatKamarActions.BuatKamarBaru -> buatKamarBaru()
            is BuatKamarActions.RemoveFasilitasKamar -> removeFasilitasKamar(actions.index)
            is BuatKamarActions.RemovePemakaianAlatElektronikGratis -> removePemakaianAlatElektronikGratis(actions.index)
            is BuatKamarActions.UpdateNamaAlatElektronik -> updateNamaAlatElektronik(actions.namaAlatElektronik)
            is BuatKamarActions.UpdateNamaFasilitas -> updateNamaFasilitas(actions.namaFasilitas)
            is BuatKamarActions.UpdateNumberRoom -> updateNumberRoom(actions.numberRoom)
            is BuatKamarActions.UpdateSelectJumlahOrang -> updateSelectJumlahOrang(actions.jumlahOrang)
            is BuatKamarActions.UpdateTarifDuaOrang -> updateTarifDuaOrang(actions.tarifDuaOrang)
            is BuatKamarActions.UpdateTarifSatuOrang -> updateTarifSatuOrang(actions.tarifSatuOrang)
            is BuatKamarActions.UpdateUkuranKamar -> updateUkuranKamar(actions.ukuranKamar)
        }
    }

    private fun updateUkuranKamar(ukuranKamar: String) {
        _state.update { it.copy(ukuranKamar = ukuranKamar) }
    }

    private fun updateTarifSatuOrang(tarifSatuOrang: String) {
        if (tarifSatuOrang.isEmpty()){
            _state.update {
                it.copy(
                    tarifSatuOrang = tarifSatuOrang,
                    tarifSatuOrangError = null,
                    isTarifSatuOrangError = false
                )
            }
            return
        }

        val tarifSatuOrangError = PatternValidation.getTarifError(tarifSatuOrang)
        val isTarifSatuOrangValid = PatternValidation.isTarifValid(tarifSatuOrang)
        _state.update {
            it.copy(
                tarifSatuOrang = tarifSatuOrang,
                tarifSatuOrangError = tarifSatuOrangError,
                isTarifSatuOrangError = !isTarifSatuOrangValid
            )
        }
    }

    private fun updateTarifDuaOrang(tarifDuaOrang: String) {
        if (tarifDuaOrang.isEmpty()){
            _state.update {
                it.copy(
                    tarifDuaOrang = tarifDuaOrang,
                    tarifDuaOrangError = null,
                    isTarifDuaOrangError = false
                )
            }
            return
        }

        val tarifDuaOrangError = PatternValidation.getTarifError(tarifDuaOrang)
        val isTarifDuaOrangValid = PatternValidation.isTarifValid(tarifDuaOrang)
        _state.update {
            it.copy(
                tarifDuaOrang = tarifDuaOrang,
                tarifDuaOrangError = tarifDuaOrangError,
                isTarifDuaOrangError = !isTarifDuaOrangValid
            )
        }
    }

    private fun updateSelectJumlahOrang(jumlahOrang: Int) {
        _state.update { it.copy(jumlahOrang = jumlahOrang) }
    }

    private fun updateNumberRoom(numberRoom: String) {
        if (numberRoom.isEmpty()){
            _state.update {
                it.copy(
                    numberRoom = numberRoom,
                    numberRoomError = null,
                    isnumberRoomError = false
                )
            }
            return
        }

        val numberRoomError = PatternValidation.getNomorKamarError(numberRoom)
        val isNumberRoomValid = PatternValidation.isNomorKamarValid(numberRoom)
        _state.update {
            it.copy(
                numberRoom = numberRoom,
                numberRoomError = numberRoomError,
                isnumberRoomError = !isNumberRoomValid
            )
        }
    }

    private fun updateNamaFasilitas(namaFasilitas: String) {
        _state.update { it.copy(namaFasilitas = namaFasilitas) }
    }

    private fun updateNamaAlatElektronik(namaAlatElektronik: String) {
        _state.update { it.copy(namaAlatElektronik = namaAlatElektronik) }
    }

    private fun removePemakaianAlatElektronikGratis(index: Int) {
        val dataLayananAlatGratis = _state.value.layananElektronikGratisKamar
        val dataLayananAlatGratisBaru = dataLayananAlatGratis.toMutableList()
        dataLayananAlatGratisBaru.removeAt(index)
        _state.update {
            it.copy(
                layananElektronikGratisKamar = dataLayananAlatGratisBaru
            )
        }
    }

    private fun removeFasilitasKamar(index: Int) {
        val dataFasilitasKamar = _state.value.fasilitasKamar
        val dataFasilitasKamarBaru = dataFasilitasKamar.toMutableList()
        dataFasilitasKamarBaru.removeAt(index)
        _state.update {
            it.copy(
                fasilitasKamar = dataFasilitasKamarBaru
            )
        }
    }

    private fun buatKamarBaru() {
        viewModelScope.launch {
            _state.update { it.copy(isButtonLoading = true) }

            val numberRoom = _state.value.numberRoom
            val ukuranKamar = _state.value.ukuranKamar
            val tarifSatuOrang = _state.value.tarifSatuOrang
            val tarifDuaOrang = _state.value.tarifDuaOrang ?: ""
            val jumlahOrang = _state.value.jumlahOrang
            val layananElektronikGratisKamar = _state.value.layananElektronikGratisKamar
            val fasilitasKamar = _state.value.fasilitasKamar

            if (fasilitasKamar.isEmpty()){
                _state.update {
                    it.copy(
                        isButtonLoading = false
                    )
                }
                _events.send(
                    BuatKamarEvents.ShowSnackBarError("Silakan tambahkan fasilitas kamar dulu")
                )
                return@launch
            }

            if (ukuranKamar.isEmpty()){
                _state.update {
                    it.copy(
                        isButtonLoading = false
                    )
                }
                _events.send(
                    BuatKamarEvents.ShowSnackBarError("Ukuran kamar belum dipilih")
                )
                return@launch
            }

            val numberRoomError = PatternValidation.getNomorKamarError(numberRoom)
            val isNumberRoomValid = PatternValidation.isNomorKamarValid(numberRoom)
            val tarifSatuOrangError = PatternValidation.getTarifError(tarifSatuOrang)
            val isTarifSatuOrangValid = PatternValidation.isTarifValid(tarifSatuOrang)
            val shakeTriggerTarifSatuOrangError = if (isTarifSatuOrangValid) _state.value.shakeTriggerTarifSatuOrangError else _state.value.shakeTriggerTarifSatuOrangError + 1
            val shakeTriggerNumberRoomError = if (isNumberRoomValid) _state.value.shakeTriggerNomorKamarError else _state.value.shakeTriggerNomorKamarError + 1

            if (jumlahOrang == 2) {
                val tarifDuaOrangError = PatternValidation.getTarifError(tarifDuaOrang)
                val isTarifDuaOrangValid = PatternValidation.isTarifValid(tarifDuaOrang)
                val shakeTriggerTarifDuaOrangError = if (isTarifDuaOrangValid) _state.value.shakeTriggerTarifDuaOrangError else _state.value.shakeTriggerTarifDuaOrangError + 1

                if (!isTarifDuaOrangValid || !isTarifSatuOrangValid || !isNumberRoomValid){
                    _state.update {
                        it.copy(
                            isButtonLoading = false,
                            tarifSatuOrangError = tarifSatuOrangError,
                            isTarifSatuOrangError = !isTarifSatuOrangValid,
                            tarifDuaOrangError = tarifDuaOrangError,
                            isTarifDuaOrangError = !isTarifDuaOrangValid,
                            numberRoomError = numberRoomError,
                            isnumberRoomError = !isNumberRoomValid,
                            shakeTriggerTarifDuaOrangError = shakeTriggerTarifDuaOrangError,
                            shakeTriggerTarifSatuOrangError = shakeTriggerTarifSatuOrangError,
                            shakeTriggerNomorKamarError = shakeTriggerNumberRoomError
                        )
                    }
                    return@launch
                }

                _state.update {
                    it.copy(
                        tarifSatuOrangError = null,
                        isTarifSatuOrangError = false,
                        tarifDuaOrangError = null,
                        isTarifDuaOrangError = false,
                        numberRoomError = null,
                        isnumberRoomError = false
                    )
                }
                val buatKamarFormat = BuatKamar(
                    numberRoom = numberRoom.toInt(),
                    price = Harga(
                        onePerson = tarifSatuOrang.toLong(),
                        twoPersons = tarifDuaOrang.toLong()
                    ),
                    facility = fasilitasKamar,
                    size = ukuranKamar,
                    freeService = layananElektronikGratisKamar,
                    status = "Kosong"
                )

                buatKamarUseCase(buatKamarFormat)
                    .onSuccess {
                        _state.update {
                            it.copy(
                                isButtonLoading = false
                            )
                        }
                        _events.send(
                            BuatKamarEvents.NavigateBackSuccessBuatKamarBaru
                        )
                    }.onError { result ->
                        val shakeTriggerNomorKamar = if (result == DataError.NOMOR_KAMAR_TERPAKAI) _state.value.shakeTriggerNomorKamarError + 1 else _state.value.shakeTriggerNomorKamarError
                        _state.update {
                            it.copy(
                                isButtonLoading = false,
                                shakeTriggerNomorKamarError = shakeTriggerNomorKamar,
                                isnumberRoomError = result == DataError.NOMOR_KAMAR_TERPAKAI,
                                numberRoomError = if (result == DataError.NOMOR_KAMAR_TERPAKAI) UiText.DynamicString("Nomor kamar sudah terpakai") else null
                            )
                        }
                        _events.send(
                            BuatKamarEvents.ShowSnackBarError(result.message)
                        )
                    }

            } else {
                if (!isTarifSatuOrangValid || !isNumberRoomValid){
                    _state.update {
                        it.copy(
                            isButtonLoading = false,
                            tarifSatuOrangError = tarifSatuOrangError,
                            isTarifSatuOrangError = !isTarifSatuOrangValid,
                            numberRoomError = numberRoomError,
                            isnumberRoomError = !isNumberRoomValid,
                            shakeTriggerTarifSatuOrangError = shakeTriggerTarifSatuOrangError,
                            shakeTriggerNomorKamarError = shakeTriggerNumberRoomError
                        )
                    }
                    return@launch
                }

                _state.update {
                    it.copy(
                        tarifSatuOrangError = null,
                        isTarifSatuOrangError = false,
                        numberRoomError = null,
                        isnumberRoomError = false
                    )
                }
                val buatKamarFormat = BuatKamar(
                    numberRoom = numberRoom.toInt(),
                    price = Harga(
                        onePerson = tarifSatuOrang.toLong(),
                        twoPersons = null
                    ),
                    facility = fasilitasKamar,
                    size = ukuranKamar,
                    freeService = layananElektronikGratisKamar,
                    status = "Kosong"
                )

                buatKamarUseCase(buatKamarFormat)
                    .onSuccess {
                        _state.update {
                            it.copy(
                                isButtonLoading = false
                            )
                        }
                        _events.send(
                            BuatKamarEvents.NavigateBackSuccessBuatKamarBaru
                        )
                    }.onError { result ->
                        if (result == DataError.NOMOR_KAMAR_TERPAKAI){
                            _state.update {
                                it.copy(
                                    isButtonLoading = false,
                                    shakeTriggerNomorKamarError = _state.value.shakeTriggerNomorKamarError + 1,
                                    isnumberRoomError = true,
                                    numberRoomError = UiText.DynamicString("Nomor kamar sudah terpakai")
                                )
                            }
                        } else {
                            _state.update {
                                it.copy(
                                    isButtonLoading = false,
                                )
                            }
                            _events.send(
                                BuatKamarEvents.ShowSnackBarError(result.message)
                            )
                        }
                    }
            }
        }
    }

    private fun addPemakaianAlatElektronikGratis() {
        val dataLayananAlatGratis = _state.value.layananElektronikGratisKamar
        val dataLayananAlatGratisBaru = AlatElektronik(
            toolName = _state.value.namaAlatElektronik,
            cost = 0,
            origin = Constant.ORIGIN_KAMAR_DEFAULT
        )
        _state.update {
            it.copy(
                layananElektronikGratisKamar = dataLayananAlatGratis + dataLayananAlatGratisBaru,
                namaAlatElektronik = ""
            )
        }
    }

    private fun addFasilitasKamar() {
        val dataFasilitasKamar = _state.value.fasilitasKamar
        val dataFasilitasKamarBaru = _state.value.namaFasilitas
        _state.update {
            it.copy(
                fasilitasKamar = dataFasilitasKamar + dataFasilitasKamarBaru,
                namaFasilitas = ""
            )
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(BuatKamarEvents.NavigateBack)
        }
    }
}