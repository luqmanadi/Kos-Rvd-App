package com.kosrvd.app.feature.rental.presentation.edit_pemakaian_elektronik

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.domain.models.AlatElektronik
import com.kosrvd.app.core.domain.usecase.CheckNetworkUseCase
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.core.domain.utils.onSuccess
import com.kosrvd.app.core.presentation.utils.PatternValidation
import com.kosrvd.app.feature.rental.domain.repository.PenyewaanRepository
import com.kosrvd.app.presentation.navigation.NavigationScreen
import com.kosrvd.app.presentation.navigation.models.AlatElektronikSerialize
import com.kosrvd.app.presentation.navigation.models.CustomNavTypes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.typeOf

sealed interface EditPemakaianElektronikEvents {
    data object NavigateBack : EditPemakaianElektronikEvents
    data class ShowSnackBarError(val message: String) : EditPemakaianElektronikEvents
    data object NavigateBackSuccessEditPemakaianElektronik : EditPemakaianElektronikEvents
}

sealed interface EditPemakaianElektronikActions {
    data object NavigateBack : EditPemakaianElektronikActions
    data class OnNamaAlatElektronikChange(val namaAlatElektronik: String) :
        EditPemakaianElektronikActions

    data class OnPriceAlatElektronikChange(val priceAlatElektronik: String) :
        EditPemakaianElektronikActions

    data object AddAlatElektronik : EditPemakaianElektronikActions
    data class RemoveAlatElektronik(val index: Int) : EditPemakaianElektronikActions
    data object SaveEditPemakaianElektronik : EditPemakaianElektronikActions
}

@HiltViewModel
class EditPemakaianElektronikViewModel @Inject constructor(
    private val penyewaanRepository: PenyewaanRepository,
    private val savedStateHandle: SavedStateHandle,
    private val checkNetworkUseCase: CheckNetworkUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(EditPemakaianElektronikUiState())
    val state = _state.asStateFlow()

    private val _events = Channel<EditPemakaianElektronikEvents>()
    val events = _events.receiveAsFlow()

    init {
        // Ambil objek argument dari savedStateHandle
        val arguments =
            savedStateHandle.toRoute<NavigationScreen.EditPemakaianAlatEleketronikScreen>(
                typeMap = mapOf(typeOf<List<AlatElektronikSerialize>>() to CustomNavTypes.listAlatElektronik)
            )

        initialData(arguments.idPenyewaan, arguments.listAlatElektronik)
    }

    fun onActions(actions: EditPemakaianElektronikActions) {
        when (actions) {
            EditPemakaianElektronikActions.AddAlatElektronik -> addAlatElektronik()
            EditPemakaianElektronikActions.NavigateBack -> navigateBack()
            is EditPemakaianElektronikActions.OnNamaAlatElektronikChange -> onNamaAlatElektronikChange(
                actions.namaAlatElektronik
            )

            is EditPemakaianElektronikActions.OnPriceAlatElektronikChange -> onPriceAlatElektronikChange(
                actions.priceAlatElektronik
            )

            is EditPemakaianElektronikActions.RemoveAlatElektronik -> removeAlatElektronik(actions.index)
            EditPemakaianElektronikActions.SaveEditPemakaianElektronik -> saveEditPemakaianElektronik()
        }
    }

    private fun saveEditPemakaianElektronik() {
        viewModelScope.launch {
            _state.update { it.copy(isButtonLoading = true) }

            if (!checkNetworkUseCase()){
                _state.update {
                    it.copy(
                        isButtonLoading = false
                    )
                }
                _events.send(
                    EditPemakaianElektronikEvents.ShowSnackBarError("Gagal Edit Pemakaian Elektronik dikarenakan Tidak ada koneksi internet. Mohon cek kembali jaringan Anda.")
                )
                return@launch
            }
            val idPenyewaan = _state.value.idPenyewaan
            val listAlatElektronik = _state.value.listAlatElektronik

            penyewaanRepository.editPemakaianElektronikBulanan(
                idPenyewaan = idPenyewaan,
                listAlatElektronik = listAlatElektronik
            ).onSuccess {
                _state.update { it.copy(isButtonLoading = false) }
                _events.send(EditPemakaianElektronikEvents.NavigateBackSuccessEditPemakaianElektronik)
            }.onError { result ->
                _state.update { it.copy(isButtonLoading = false) }
                _events.send(EditPemakaianElektronikEvents.ShowSnackBarError(result.message))
            }
        }
    }

    private fun onPriceAlatElektronikChange(priceAlatElektronik: String) {
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

    private fun onNamaAlatElektronikChange(namaAlatElektronik: String) {
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

    private fun removeAlatElektronik(index: Int) {
        val currentList = _state.value.listAlatElektronik
        if (index in currentList.indices){
            val newList = currentList.toMutableList().apply { removeAt(index) }
            _state.update { it.copy(listAlatElektronik = newList) }
            buttonSubmitEnabled()
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
        buttonSubmitEnabled()
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(EditPemakaianElektronikEvents.NavigateBack)
        }
    }

    private fun initialData(
        idPenyewaan: String,
        listAlatElektronik: List<AlatElektronikSerialize>
    ) {
        _state.update {
            it.copy(
                idPenyewaan = idPenyewaan,
                listAlatElektronik = listAlatElektronik.map { data ->
                    AlatElektronik(
                        toolName = data.toolName,
                        cost = data.cost,
                        origin = data.origin
                    )
                },
                oldListAlatElektronik = listAlatElektronik.map { data ->
                    AlatElektronik(
                        toolName = data.toolName,
                        cost = data.cost,
                        origin = data.origin
                    )
                }
            )
        }
        buttonSubmitEnabled()
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

    private fun buttonSubmitEnabled() {
        val currentState = _state.value
        val isEnabled = currentState.listAlatElektronik != currentState.oldListAlatElektronik
        _state.update { it.copy(isButtonSubmitEnabled = isEnabled) }
    }
}