package com.kosrvd.app.feature.account.presentation.detail_akun

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosrvd.app.core.domain.models.NonActiveAccountRequest
import com.kosrvd.app.core.domain.repository.AccountRepository
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.core.domain.utils.onSuccess
import com.kosrvd.app.feature.account.presentation.models.toDetailAkunUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface DetailAkunEvents {
    data object NavigateBack: DetailAkunEvents
    data class NavigateToPreviewImage(val imageUrl: Uri): DetailAkunEvents
    data object NavigateBackToSendNonActivateAccountSnackBar: DetailAkunEvents
    data object NavigateBackToSendActivateAccountSnackBar: DetailAkunEvents
    data class ShowBannerError(val message: String): DetailAkunEvents
}

sealed interface DetailAkunActions {
    data object NavigateBack: DetailAkunActions
    data class TryAgain(val idAkun: String): DetailAkunActions
    data object NonActivateAccount: DetailAkunActions
    data class NavigateToPreviewImageKtp(val imageUrl: String): DetailAkunActions
    data object OpenDialogNonAktifAkun: DetailAkunActions
    data object CloseDialogNonAktifAkun: DetailAkunActions
    data object OpenDialogActivateAccount: DetailAkunActions
    data object CloseDialogActivateAccount: DetailAkunActions
    data object ActivateAkun: DetailAkunActions
}

@HiltViewModel
class DetailAkunViewModel @Inject constructor(
    private val accountRepository: AccountRepository
): ViewModel() {
    private val _state = MutableStateFlow(DetailAkunUiState())
    val state = _state.asStateFlow()

    private val _events = Channel<DetailAkunEvents>()
    val events = _events.receiveAsFlow()

    fun onActions(actions: DetailAkunActions){
        when(actions){
            DetailAkunActions.NavigateBack -> navigateBack()
            is DetailAkunActions.NavigateToPreviewImageKtp -> navigateToPreviewImage(actions.imageUrl)
            DetailAkunActions.NonActivateAccount -> nonAktifAkun()
            is DetailAkunActions.TryAgain -> loadDetailAkun(actions.idAkun)
            DetailAkunActions.CloseDialogNonAktifAkun -> closeDialogNonAktifAkun()
            DetailAkunActions.OpenDialogNonAktifAkun -> openDialogNonAktifAkun()
            DetailAkunActions.ActivateAkun -> activateAkun()
            DetailAkunActions.CloseDialogActivateAccount -> closeDialogActivateAccount()
            DetailAkunActions.OpenDialogActivateAccount -> openDialogActivateAccount()
        }
    }

    private fun openDialogActivateAccount() {
        _state.update { it.copy(showDialogActivateAccount = true) }
    }

    private fun closeDialogActivateAccount() {
        _state.update { it.copy(showDialogActivateAccount = false) }
    }

    private fun activateAkun() {
        viewModelScope.launch {
            _state.update { it.copy(buttonAktifIsLoading = true) }

            val idAkun = _state.value.detailAkun?.idAkun ?: ""
            accountRepository.reActiveAccount(idAkun)
                .onSuccess {
                    _state.update {
                        it.copy(
                            buttonAktifIsLoading = false,
                            showDialogActivateAccount = false
                        )
                    }
                    _events.send(DetailAkunEvents.NavigateBackToSendActivateAccountSnackBar)
                }
                .onError { result ->
                    _state.update {
                        it.copy(
                            buttonAktifIsLoading = false,
                            showDialogActivateAccount = false
                        )
                    }
                    _events.send(DetailAkunEvents.ShowBannerError(result.message))
                }
        }
    }

    fun loadDetailAkun(idAkun: String) {
        viewModelScope.launch {
            if (_state.value.detailAkun!= null && _state.value.detailAkun?.idAkun == idAkun){
                return@launch
            }
            _state.update { it.copy(isLoading = true, loadError = null) }

            accountRepository.getDetailAkunPengguna(idAkun)
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            loadError = null,
                            detailAkun = result.toDetailAkunUi()
                        )
                    }
                }
                .onError { result ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            loadError = result.message,
                            detailAkun = null
                        )
                    }
                }
        }
    }

    private fun closeDialogNonAktifAkun() {
        _state.update { it.copy(showDialogNonAktifAkun = false) }
    }

    private fun openDialogNonAktifAkun() {
        _state.update { it.copy(showDialogNonAktifAkun = true) }
    }

    private fun nonAktifAkun() {
        viewModelScope.launch {
            _state.update { it.copy(buttonNonAktifIsLoading = true) }

            val idAkun = _state.value.detailAkun?.idAkun ?: ""
            val role = _state.value.detailAkun?.role ?: ""
            val photoUrl = _state.value.detailAkun?.photo ?: ""
            val request = NonActiveAccountRequest(
                idAkun = idAkun,
                role = role,
                photoUrl = photoUrl
            )

            accountRepository.nonActiveAccount(request)
                .onSuccess {
                    _state.update { it.copy(buttonNonAktifIsLoading = false, showDialogNonAktifAkun = false) }
                    _events.send(DetailAkunEvents.NavigateBackToSendNonActivateAccountSnackBar)
                }
                .onError { result ->
                    _state.update { it.copy(buttonNonAktifIsLoading = false, showDialogNonAktifAkun = false) }
                    _events.send(DetailAkunEvents.ShowBannerError(result.message))
                }
        }
    }

    private fun navigateToPreviewImage(imageUrl: String) {
        viewModelScope.launch {
            _events.send(DetailAkunEvents.NavigateToPreviewImage(imageUrl.toUri()))
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(DetailAkunEvents.NavigateBack)
        }
    }
}