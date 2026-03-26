package com.kosrvd.app.feature.profile.presentation.detail_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.presentation.navigation.models.TemporaryData
import com.kosrvd.app.feature.profile.domain.usecase.GetDetailProfileUseCase
import com.kosrvd.app.feature.profile.domain.utils.TypeEdit
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

sealed interface DetailProfileEvents {
    data class NavigateToEditDataProfile(val idAkun: String, val typeEdit: TypeEdit, val temporaryData: TemporaryData): DetailProfileEvents
    data class NavigateToEditPhotoProfile(val photoUrl: String, val idAkun: String): DetailProfileEvents
    data class NavigateToDetailSewa(val idPenyewa: String): DetailProfileEvents
    data class NavigateToPreviewImage(val imageUrl: String): DetailProfileEvents
    data object NavigateBack: DetailProfileEvents
}

sealed interface DetailProfileActions {
    data object TryAgain: DetailProfileActions
    data object NavigateBack: DetailProfileActions
    data object NavigateToEditPhotoProfile: DetailProfileActions
    data object NavigateToEditNameProfile: DetailProfileActions
    data object NavigateToEditEmailProfile: DetailProfileActions
    data object NavigateToEditPhoneNumberProfile: DetailProfileActions
    data object NavigateToEditAddressProfile: DetailProfileActions
    data object NavigateToPreviewKtp: DetailProfileActions
    data object NavigateToDetailSewa: DetailProfileActions
}

@HiltViewModel
class DetailProfileViewModel @Inject constructor(
    private val getDetailProfileUseCase: GetDetailProfileUseCase
): ViewModel() {
    private val _state = MutableStateFlow(DetailProfileUiState())
    val state = _state
        .onStart { loadDetailProfile() }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DetailProfileUiState()
        )

    private val _events = Channel<DetailProfileEvents>()
    val events = _events.receiveAsFlow()

    fun onActions(actions: DetailProfileActions){
        when(actions){
            DetailProfileActions.NavigateBack -> navigateBack()
            DetailProfileActions.NavigateToDetailSewa -> navigateToDetailSewa()
            DetailProfileActions.NavigateToEditAddressProfile -> navigateToEditAddress()
            DetailProfileActions.NavigateToEditEmailProfile -> navigateToEditEmail()
            DetailProfileActions.NavigateToEditNameProfile -> navigateToEditName()
            DetailProfileActions.NavigateToEditPhoneNumberProfile -> navigateToEditPhoneNumber()
            DetailProfileActions.NavigateToEditPhotoProfile -> navigateToEditPhotoProfile()
            DetailProfileActions.NavigateToPreviewKtp -> navigateToPreviewImage()
            DetailProfileActions.TryAgain -> loadDetailProfile()
        }
    }

    private fun navigateToEditPhoneNumber() {
        val phoneNumber = state.value.profileUi?.phoneNumber
        navigateToEditDataProfile(
            typeEdit = TypeEdit.NO_HP,
            temporaryData = TemporaryData(phoneNumber = phoneNumber)
        )
    }

    private fun navigateToEditEmail() {
        val email = state.value.profileUi?.email
        navigateToEditDataProfile(
            typeEdit = TypeEdit.EMAIL,
            temporaryData = TemporaryData(email = email)
        )
    }

    private fun navigateToEditAddress() {
        val address = state.value.profileUi?.address
        navigateToEditDataProfile(
            typeEdit = TypeEdit.ALAMAT,
            temporaryData = TemporaryData(address = address)
        )
    }

    private fun navigateToEditName() {
        val name = state.value.profileUi?.name
        navigateToEditDataProfile(
            typeEdit = TypeEdit.NAMA,
            temporaryData = TemporaryData(name = name)
        )
    }

    private fun navigateToEditPhotoProfile() {
        viewModelScope.launch {
            val idAkun = state.value.profileUi?.idAkun ?: return@launch
            val photoProfile = state.value.profileUi?.photoProfile ?: ""
            _events.send(DetailProfileEvents.NavigateToEditPhotoProfile(photoUrl = photoProfile, idAkun = idAkun))
        }
    }

    private fun navigateToPreviewImage() {
        viewModelScope.launch {
            val imageUrl = state.value.profileUi?.photoKtp ?: ""
            _events.send(DetailProfileEvents.NavigateToPreviewImage(imageUrl))
        }
    }

    private fun navigateToDetailSewa() {
        viewModelScope.launch {
            val idPenyewa = state.value.profileUi?.idPenyewa ?: return@launch
            _events.send(DetailProfileEvents.NavigateToDetailSewa(idPenyewa = idPenyewa))
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(DetailProfileEvents.NavigateBack)
        }
    }

    private fun loadDetailProfile(){
        viewModelScope.launch {
            if (_state.value.profileUi != null) return@launch

            _state.update { it.copy(isLoading = true) }

            getDetailProfileUseCase.invoke().collect { result ->
                when(result){
                    is Result.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                loadError = result.error.message
                            )
                        }
                    }
                    is Result.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                loadError = null,
                                profileUi = result.data
                            )
                        }
                    }
                }
            }
        }
    }

    private fun navigateToEditDataProfile(typeEdit: TypeEdit, temporaryData: TemporaryData){
        viewModelScope.launch {
            val idAkun = state.value.profileUi?.idAkun ?: return@launch
            _events.send(
                DetailProfileEvents.NavigateToEditDataProfile(
                    idAkun = idAkun,
                    typeEdit = typeEdit,
                    temporaryData = temporaryData
                )
            )
        }
    }
}