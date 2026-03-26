package com.kosrvd.app.feature.room.presentation.edit_kamar

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.core.domain.utils.onSuccess
import com.kosrvd.app.presentation.navigation.NavigationScreen
import com.kosrvd.app.presentation.navigation.models.CustomNavTypes
import com.kosrvd.app.presentation.navigation.models.EditTypeKamar
import com.kosrvd.app.core.presentation.utils.PatternValidation
import com.kosrvd.app.core.domain.models.AlatElektronik
import com.kosrvd.app.core.domain.models.Harga
import com.kosrvd.app.feature.room.domain.repository.KamarRepository
import com.kosrvd.app.feature.rental.domain.repository.PenyewaanRepository
import com.kosrvd.app.feature.room.domain.usecase.UpdateNomorKamarUseCase
import com.kosrvd.app.feature.room.domain.utils.TypeEditKamar
import com.kosrvd.app.core.presentation.utils.toNumberRoomFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.typeOf

sealed interface EditKamarEvents {
    data object NavigateBack: EditKamarEvents
    data class ShowSnackBarError(val message: String): EditKamarEvents
    data class NavigateBackSuccessEditKamar(val typeEditKamar: TypeEditKamar): EditKamarEvents
}

sealed interface EditKamarActions {
    data object NavigateBack: EditKamarActions
    data class UpdateNumberRoom(val numberRoom: String): EditKamarActions
    data class UpdateUkuranKamar(val ukuranKamar: String?): EditKamarActions
    data class UpdateTarifSatuOrang(val tarifSatuOrang: String): EditKamarActions
    data class UpdateTarifDuaOrang(val tarifDuaOrang: String): EditKamarActions
    data class UpdateSelectJumlahOrang(val jumlahOrang: Int): EditKamarActions
    data class UpdateNamaAlatElektronik(val namaAlatElektronik: String): EditKamarActions
    data class UpdateNamaFasilitas(val namaFasilitas: String): EditKamarActions
    data object AddPemakaianAlatElektronikGratis: EditKamarActions
    data class RemovePemakaianAlatElektronikGratis(val index: Int): EditKamarActions
    data object AddFasilitasKamar: EditKamarActions
    data class RemoveFasilitasKamar(val index: Int): EditKamarActions
    data object SaveEditKamar: EditKamarActions
}

@HiltViewModel
class EditKamarViewModel @Inject constructor(
    private val kamarRepository: KamarRepository,
    private val updateNomorKamarUseCase: UpdateNomorKamarUseCase,
    private val penyewaanRepository: PenyewaanRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _state = MutableStateFlow(EditKamarUiState())
    val state = _state.asStateFlow()

    private val _events = Channel<EditKamarEvents>()
    val events = _events.receiveAsFlow()

    init {
        // Ambil objek argument dari savedStateHandle
        val arguments = savedStateHandle.toRoute<NavigationScreen.EditKamarScreen>(
            typeMap = mapOf(typeOf<EditTypeKamar>() to CustomNavTypes.EditTypeKamarType)
        )

        initialDataEditShow(arguments.editTypeKamar, arguments.typeEditKamar, arguments.idKamar)
    }

    fun onActions(actions: EditKamarActions){
        when(actions){
            EditKamarActions.AddFasilitasKamar -> addFasilitasKamar()
            EditKamarActions.AddPemakaianAlatElektronikGratis -> addPemakaianAlatElektronikGratis()
            EditKamarActions.NavigateBack -> navigateBack()
            is EditKamarActions.RemoveFasilitasKamar -> removeFasilitasKamar(actions.index)
            is EditKamarActions.RemovePemakaianAlatElektronikGratis -> removePemakaianAlatElektronikGratis(actions.index)
            EditKamarActions.SaveEditKamar -> saveEditKamar()
            is EditKamarActions.UpdateNamaAlatElektronik -> updateNamaAlatElektronik(actions.namaAlatElektronik)
            is EditKamarActions.UpdateNamaFasilitas -> updateNamaFasilitas(actions.namaFasilitas)
            is EditKamarActions.UpdateNumberRoom -> updateNumberRoom(actions.numberRoom)
            is EditKamarActions.UpdateSelectJumlahOrang -> updateSelectJumlahOrang(actions.jumlahOrang)
            is EditKamarActions.UpdateTarifDuaOrang -> updateTarifDuaOrang(actions.tarifDuaOrang)
            is EditKamarActions.UpdateTarifSatuOrang -> updateTarifSatuOrang(actions.tarifSatuOrang)
            is EditKamarActions.UpdateUkuranKamar -> updateUkuranKamar(actions.ukuranKamar)
        }
    }

    private fun updateUkuranKamar(ukuranKamar: String?) {
        _state.update { it.copy(ukuranKamar = ukuranKamar) }
    }

    private fun updateTarifSatuOrang(tarifSatuOrang: String) {
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

    private fun saveEditKamar() {
        viewModelScope.launch {
            _state.update { it.copy(isButtonLoading = true) }
            val idKamar = _state.value.idKamar
            when(val typeEditKamar = _state.value.typeEditKamar) {
                TypeEditKamar.EDIT_NOMOR_KAMAR -> {
                    val numberRoom = _state.value.numberRoom
                    val numberRoomError = PatternValidation.getNomorKamarError(numberRoom)
                    val isNumberRoomValid = PatternValidation.isNomorKamarValid(numberRoom)
                    if (!isNumberRoomValid){
                        _state.update {
                            it.copy(
                                isButtonLoading = false,
                                numberRoomError = numberRoomError,
                                isnumberRoomError = true,
                                shakeTriggerNomorKamarError = it.shakeTriggerNomorKamarError + 1
                            )
                        }
                        return@launch
                    }
                    _state.update {
                        it.copy(
                            numberRoomError = null,
                            isnumberRoomError = false
                        )
                    }

                    updateNomorKamarUseCase(idKamar, nomorKamarBaru = numberRoom.toInt())
                        .onSuccess {
                            _state.update {
                                it.copy(
                                    isButtonLoading = false
                                )
                            }
                            _events.send(
                                EditKamarEvents.NavigateBackSuccessEditKamar(typeEditKamar)
                            )
                        }.onError { result ->
                            _state.update {
                                it.copy(
                                    isButtonLoading = false
                                )
                            }
                            _events.send(
                                EditKamarEvents.ShowSnackBarError(result.message)
                            )
                        }
                }
                TypeEditKamar.EDIT_UKURAN_KAMAR -> {
                    val ukuranKamar = _state.value.ukuranKamar
                    if (ukuranKamar.isNullOrBlank()){
                        _events.send(
                            EditKamarEvents.ShowSnackBarError("Ukuran kamar belum dipilih")
                        )
                        return@launch
                    }

                    kamarRepository.updateUkuranKamar(
                        idKamar = idKamar,
                        ukuranKamarBaru = ukuranKamar
                    ).onSuccess {
                        _state.update {
                            it.copy(
                                isButtonLoading = false
                            )
                        }
                        _events.send(
                            EditKamarEvents.NavigateBackSuccessEditKamar(typeEditKamar)
                        )
                    }.onError { result ->
                        _state.update {
                            it.copy(
                                isButtonLoading = false
                            )
                        }
                        _events.send(
                            EditKamarEvents.ShowSnackBarError(result.message)
                        )
                    }

                }
                TypeEditKamar.EDIT_TARIF_KAMAR -> {
                    val jumlahOrang = _state.value.jumlahOrang
                    val tarifSatuOrang = _state.value.tarifSatuOrang
                    val tarifDuaOrang = _state.value.tarifDuaOrang ?: ""

                    if (jumlahOrang == 2){
                        val tarifSatuOrangError = PatternValidation.getTarifError(tarifSatuOrang)
                        val isTarifSatuOrangValid = PatternValidation.isTarifValid(tarifSatuOrang)
                        val tarifDuaOrangError = PatternValidation.getTarifError(tarifDuaOrang)
                        val isTarifDuaOrangValid = PatternValidation.isTarifValid(tarifDuaOrang)
                        val shakeTriggerTarifDuaOrangError = if (isTarifDuaOrangValid) _state.value.shakeTriggerTarifDuaOrangError else _state.value.shakeTriggerTarifDuaOrangError + 1
                        val shakeTriggerTarifSatuOrangError = if (isTarifSatuOrangValid) _state.value.shakeTriggerTarifSatuOrangError else _state.value.shakeTriggerTarifSatuOrangError + 1

                        if (!isTarifDuaOrangValid || !isTarifSatuOrangValid){
                            _state.update {
                                it.copy(
                                    isButtonLoading = false,
                                    tarifSatuOrangError = tarifSatuOrangError,
                                    isTarifSatuOrangError = !isTarifSatuOrangValid,
                                    tarifDuaOrangError = tarifDuaOrangError,
                                    isTarifDuaOrangError = !isTarifDuaOrangValid,
                                    shakeTriggerTarifDuaOrangError = shakeTriggerTarifDuaOrangError,
                                    shakeTriggerTarifSatuOrangError = shakeTriggerTarifSatuOrangError
                                )
                            }
                            return@launch
                        }

                        _state.update {
                            it.copy(
                                tarifSatuOrangError = null,
                                isTarifSatuOrangError = false,
                                tarifDuaOrangError = null,
                                isTarifDuaOrangError = false
                            )
                        }

                        val tarifKamarBaru = Harga(
                            onePerson = tarifSatuOrang.toLong(),
                            twoPersons = tarifDuaOrang.toLong()
                        )

                        kamarRepository.updateTarifKamar(
                            idKamar = idKamar,
                            tarifKamarBaru = tarifKamarBaru
                        ).onSuccess {
                            _state.update {
                                it.copy(
                                    isButtonLoading = false
                                )
                            }
                            _events.send(
                                EditKamarEvents.NavigateBackSuccessEditKamar(typeEditKamar)
                            )
                        }.onError { result ->
                            _state.update {
                                it.copy(
                                    isButtonLoading = false
                                )
                            }
                            _events.send(
                                EditKamarEvents.ShowSnackBarError(result.message)
                            )
                        }

                    } else {
                        val tarifSatuOrangError = PatternValidation.getTarifError(tarifSatuOrang)
                        val isTarifSatuOrangValid = PatternValidation.isTarifValid(tarifSatuOrang)
                        if (!isTarifSatuOrangValid){
                            _state.update {
                                it.copy(
                                    isButtonLoading = false,
                                    tarifSatuOrangError = tarifSatuOrangError,
                                    isTarifSatuOrangError = true,
                                    shakeTriggerTarifSatuOrangError = it.shakeTriggerTarifSatuOrangError + 1
                                )
                            }
                            return@launch
                        }
                        _state.update {
                            it.copy(
                                tarifSatuOrangError = null,
                                isTarifSatuOrangError = false
                                )
                        }

                        val tarifKamarBaru = Harga(
                            onePerson = tarifSatuOrang.toLong(),
                            twoPersons = null
                        )

                        kamarRepository.updateTarifKamar(
                            idKamar = idKamar,
                            tarifKamarBaru = tarifKamarBaru
                        ).onSuccess {
                            _state.update {
                                it.copy(
                                    isButtonLoading = false
                                )
                            }
                            _events.send(
                                EditKamarEvents.NavigateBackSuccessEditKamar(typeEditKamar)
                            )
                        }.onError { result ->
                            _state.update {
                                it.copy(
                                    isButtonLoading = false
                                )
                            }
                            _events.send(
                                EditKamarEvents.ShowSnackBarError(result.message)
                            )
                        }
                    }
                }
                TypeEditKamar.EDIT_LAYANAN_ALAT_ELEKTRONIK_GRATIS -> {
                    val dataLayananAlatGratis = _state.value.layananElektronikGratisKamar

                    kamarRepository.updateLayananAlatElektronikGratis(
                        idKamar = idKamar,
                        layananAlatElektronikGratisBaru = dataLayananAlatGratis
                    ).onSuccess {
                        _state.update {
                            it.copy(
                                isButtonLoading = false
                            )
                        }
                        _events.send(
                            EditKamarEvents.NavigateBackSuccessEditKamar(typeEditKamar)
                        )
                    }.onError { result ->
                        _state.update {
                            it.copy(
                                isButtonLoading = false
                            )
                        }
                        _events.send(
                            EditKamarEvents.ShowSnackBarError(result.message)
                        )
                    }
                }
                TypeEditKamar.EDIT_FASILITAS_KAMAR -> {
                    val dataFasilitasKamar = _state.value.fasilitasKamar

                    kamarRepository.updateFasilitasKamar(
                        idKamar = idKamar,
                        fasilitasKamarBaru = dataFasilitasKamar
                    ).onSuccess {
                        _state.update {
                            it.copy(
                                isButtonLoading = false
                            )
                        }
                        _events.send(
                            EditKamarEvents.NavigateBackSuccessEditKamar(typeEditKamar)
                        )
                    }.onError { result ->
                        _state.update {
                            it.copy(
                                isButtonLoading = false
                            )
                        }
                        _events.send(
                            EditKamarEvents.ShowSnackBarError(result.message)
                        )
                    }
                }
                null -> {
                    _state.update { it.copy(isButtonLoading = false) }
                    _events.send(
                        EditKamarEvents.ShowSnackBarError("Type Edit Kosong")
                    )
                }
            }
        }
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
            _events.send(EditKamarEvents.NavigateBack)
        }
    }

    private fun initialDataEditShow(data: EditTypeKamar, typeEdit: TypeEditKamar, idKamar: String){
        viewModelScope.launch {

            when(typeEdit){
                TypeEditKamar.EDIT_NOMOR_KAMAR -> {
                    _state.update {
                        it.copy(
                            typeEditKamar = typeEdit,
                            nomorKamarConstant = data.nomorKamar?.toNumberRoomFormat() ?: 0.toNumberRoomFormat(),
                            numberRoom = data.nomorKamar.toString(),
                            idKamar = idKamar
                        )
                    }
                }
                TypeEditKamar.EDIT_UKURAN_KAMAR -> {
                    _state.update {
                        it.copy(
                            typeEditKamar = typeEdit,
                            nomorKamarConstant = data.nomorKamar?.toNumberRoomFormat() ?: 0.toNumberRoomFormat(),
                            ukuranKamar = data.ukuranKamar ?: "",
                            idKamar = idKamar
                        )
                    }
                }
                TypeEditKamar.EDIT_TARIF_KAMAR -> {
                    _state.update { it.copy(isLoadingTarifKamar = true) }
                    if (data.tarifKamar.twoPersons != null && data.statusKamar == "Dipakai"){
                        _state.update {
                            it.copy(
                                idKamar = idKamar,
                                nomorKamarConstant = data.nomorKamar?.toNumberRoomFormat() ?: 0.toNumberRoomFormat(),
                                typeEditKamar = typeEdit,
                                jumlahOrang = 2,
                                oldJumlahOrang = 2,
                                tarifSatuOrang = data.tarifKamar.onePerson.toString(),
                                tarifDuaOrang = data.tarifKamar.twoPersons.toString(),
                                status = data.statusKamar
                            )
                        }
                        penyewaanRepository.getJumlahPenghuniByIdKamarAndStatusPenyewaanAktif(idKamar)
                            .onSuccess { result ->
                                Log.d("Success Edit Tarif", "initialDataEditShow: $result, tarif satu orang: ${data.tarifKamar.onePerson}, tarif dua orang: ${data.tarifKamar.twoPersons}")
                                _state.update {
                                    it.copy(
                                        isLoadingTarifKamar = false,
                                        enableEditCapacity = result != 2
                                    )
                                }
                            }
                            .onError { result ->
                                Log.e("Error Edit Tarif", "initialDataEditShow: $result")
                                _events.send(
                                    EditKamarEvents.ShowSnackBarError(result.message)
                                )
                                _state.update {
                                    it.copy(
                                        isLoadingTarifKamar = false,
                                        enableEditCapacity = true,
                                    )
                                }
                            }
                    } else {
                        _state.update {
                            it.copy(
                                idKamar = idKamar,
                                isLoadingTarifKamar = false,
                                enableEditCapacity = true,
                                nomorKamarConstant = data.nomorKamar?.toNumberRoomFormat() ?: 0.toNumberRoomFormat(),
                                typeEditKamar = typeEdit,
                                jumlahOrang = data.tarifKamar.twoPersons.let { value -> if (value == null) 1 else 2 },
                                oldJumlahOrang = data.tarifKamar.twoPersons.let { value -> if (value == null) 1 else 2 },
                                tarifSatuOrang = data.tarifKamar.onePerson.toString(),
                                tarifDuaOrang = data.tarifKamar.twoPersons.toString(),
                                status = data.statusKamar ?: ""
                            )
                        }
                    }
                }
                TypeEditKamar.EDIT_LAYANAN_ALAT_ELEKTRONIK_GRATIS -> {
                    _state.update {
                        it.copy(
                            idKamar = idKamar,
                            typeEditKamar = typeEdit,
                            nomorKamarConstant = data.nomorKamar?.toNumberRoomFormat() ?: 0.toNumberRoomFormat(),
                            layananElektronikGratisKamar = data.layananElektronikKamar?.map { layanan ->
                                AlatElektronik(
                                    toolName = layanan.toolName,
                                    cost = layanan.cost,
                                    origin = layanan.origin
                                )
                            }?: emptyList()
                        )
                    }
                }
                TypeEditKamar.EDIT_FASILITAS_KAMAR -> {
                    _state.update {
                        it.copy(
                            idKamar = idKamar,
                            nomorKamarConstant = data.nomorKamar?.toNumberRoomFormat() ?: 0.toNumberRoomFormat(),
                            typeEditKamar = typeEdit,
                            fasilitasKamar = data.fasilitasKamar ?: emptyList()
                        )
                    }
                }
            }
        }
    }
}