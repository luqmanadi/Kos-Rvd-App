package com.kosrvd.app.feature.management.presentation.ui.screen.tagihan.buat_tagihan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.core.domain.utils.onSuccess
import com.kosrvd.app.core.navigation.models.ResultTagihan
import com.kosrvd.app.core.presentation.utils.PatternValidation
import com.kosrvd.app.core.presentation.utils.UiText
import com.kosrvd.app.core.presentation.utils.convertMillisToTimeStamp
import com.kosrvd.app.feature.management.data.mappers.toResultTagihan
import com.kosrvd.app.feature.management.domain.model.Diskon
import com.kosrvd.app.feature.management.domain.model.Penyewaan
import com.kosrvd.app.feature.management.domain.repository.PenyewaanRepository
import com.kosrvd.app.feature.management.domain.usecase.BuatTagihanUseCase
import com.kosrvd.app.feature.management.domain.usecase.CalculateTotalBillUseCase
import com.kosrvd.app.feature.management.presentation.designsystem.utils.TypeResult
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

sealed interface BuatTagihanEvents{
    data class NavigateToResult(val typeResult: TypeResult, val resultBuatTagihan: ResultTagihan): BuatTagihanEvents
    data object NavigateBack: BuatTagihanEvents
    data class ShowErrorBanner(val message: String): BuatTagihanEvents
}

sealed interface BuatTagihanActions{
    data object BuatTagihan: BuatTagihanActions
    data class UpdateItemSelected(val item: Penyewaan): BuatTagihanActions
    data class UpdateAdminFees(val adminFees: Boolean): BuatTagihanActions
    data object NavigateBack: BuatTagihanActions
    data object TryAgain: BuatTagihanActions
    data class UpdatePeriod(val periodStart: Long, val periodEnd: Long, val periodString: String): BuatTagihanActions
    data class UpdateUseDiscount(val useDiscount: Boolean): BuatTagihanActions
    data class UpdatePercentageDiscount(val percentageDiscount: String): BuatTagihanActions
    data class UpdateDescriptionDiscount(val descriptionDiscount: String): BuatTagihanActions
    data object ShowDateRangePickerDialog: BuatTagihanActions
    data object HideDateRangePickerDialog: BuatTagihanActions
}


@HiltViewModel
class BuatTagihanViewModel @Inject constructor(
    private val buatTagihanUseCase: BuatTagihanUseCase,
    private val penyewaanRepository: PenyewaanRepository,
    private val calculateTotalBillUseCase: CalculateTotalBillUseCase
): ViewModel() {
    private val _state = MutableStateFlow(BuatTagihanUiState())
    val state = _state
        .onStart { loadPenyewaan() }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = BuatTagihanUiState()
        )

    private val _events = Channel<BuatTagihanEvents>()
    val events = _events.receiveAsFlow()

    fun onActions(actions: BuatTagihanActions){
        when(actions) {
            BuatTagihanActions.BuatTagihan -> buatTagihan()
            BuatTagihanActions.NavigateBack -> navigateBack()
            BuatTagihanActions.TryAgain -> loadPenyewaan()
            is BuatTagihanActions.UpdateAdminFees -> updateAdminFees(actions.adminFees)
            is BuatTagihanActions.UpdateItemSelected -> updateItemSelected(actions.item)
            is BuatTagihanActions.UpdateDescriptionDiscount -> updateDescriptionDiscount(actions.descriptionDiscount)
            is BuatTagihanActions.UpdatePercentageDiscount -> updatePercentageDiscount(actions.percentageDiscount)
            is BuatTagihanActions.UpdatePeriod -> updatePeriod(actions.periodStart, actions.periodEnd, actions.periodString)
            is BuatTagihanActions.UpdateUseDiscount -> updateUseDiscount(actions.useDiscount)
            BuatTagihanActions.HideDateRangePickerDialog -> hideDateRangePickerDialog()
            BuatTagihanActions.ShowDateRangePickerDialog -> showDateRangePickerDialog()
        }
    }

    private fun showDateRangePickerDialog() {
        _state.update { it.copy(showDialogDatePickerRange = true) }
    }

    private fun hideDateRangePickerDialog() {
        _state.update { it.copy(showDialogDatePickerRange = false) }
    }

    // FUNGSI BARU: Master Kalkulasi Live Preview
    private fun calculateLivePreview() {
        val currentState = _state.value

        // Pastikan syarat minimal terpenuhi untuk bisa menghitung
        if (currentState.itemSelected == null || currentState.selectedPeriodStart == null || currentState.selectedPeriodEnd == null) {
            _state.update { it.copy(totalBill = 0L, priceDiscount = 0L, isShowContent = false) }
            return
        }

        val roomRentalFee = if (currentState.itemSelected.listResident.size == 2) {
            currentState.itemSelected.infoKamar.currentRoomRentalCost.twoPersons
        } else {
            currentState.itemSelected.infoKamar.currentRoomRentalCost.onePerson
        } ?: 0

        val parkingFee = currentState.itemSelected.pemakaianParkirMobilBulanan?.zonaParkir?.monthlyFee
        val electronics = currentState.itemSelected.pemakaianAlatElektronikBulanan

        // Jalankan UseCase
        val result = calculateTotalBillUseCase(
            roomRentalFee = roomRentalFee,
            parkingFee = parkingFee,
            electronics = electronics,
            adminFees = currentState.adminFees,
            periodStart = currentState.selectedPeriodStart,
            periodEnd = currentState.selectedPeriodEnd,
            useDiscount = currentState.useDiscount,
            percentageDiscount = currentState.percentageDiscount
        )

        val discount = if (!currentState.useDiscount) null else Diskon(
            percent = if (currentState.percentageDiscount.isEmpty()) 0 else currentState.percentageDiscount.toInt(),
            price = result.priceDiscount,
            description = currentState.descriptionDiscount
        )

        // Update State secara live!
        _state.update {
            it.copy(
                totalBill = result.finalBill,
                priceDiscount = result.priceDiscount,
                isShowContent = true,
                currentRentalCostBySumResident = roomRentalFee,
                discount = discount,
                sumDayPeriodeBill = result.sumDayPeriodeBill,
                prorataDetail = result.prorataDetail
            )
        }
    }

    private fun updatePeriod(periodStart: Long, periodEnd: Long, periodString: String) {
        _state.update {
            it.copy(
                selectedPeriodStart = periodStart,
                selectedPeriodEnd = periodEnd,
                selectedPeriod = periodString,
                isSelectedPeriodError = false,
                selectedPeriodError = null,
                showDialogDatePickerRange = false
            )
        }
        calculateLivePreview()
    }

    private fun updateUseDiscount(useDiscount: Boolean) {
        _state.update {
            it.copy(
                useDiscount = useDiscount,
                // Opsional: Jika diskon dimatikan, reset error dan isiannya
                isPercentageDiscountError = if (!useDiscount) false else it.isPercentageDiscountError,
                isDescriptionDiscountError = if (!useDiscount) false else it.isDescriptionDiscountError
            )
        }
        calculateLivePreview()
    }

    private fun updatePercentageDiscount(percentageDiscount: String) {
        val isError = if (percentageDiscount.isEmpty()) false else !PatternValidation.isPercentageDiscountValid(percentageDiscount)
        val errorText = if (percentageDiscount.isEmpty()) null else PatternValidation.getPercentageDiscountError(percentageDiscount)

        _state.update {
            it.copy(
                percentageDiscount = percentageDiscount,
                isPercentageDiscountError = isError,
                percentageDiscountError = errorText
            )
        }
        calculateLivePreview()
    }

    private fun updateDescriptionDiscount(descriptionDiscount: String) {
        val descriptionDiscountError = if (descriptionDiscount.isEmpty()) null else PatternValidation.getDescriptionDiscountError(descriptionDiscount)
        val isDescriptionDiscountError = if (descriptionDiscount.isEmpty()) false else !PatternValidation.isDescriptionDiscountValid(descriptionDiscount)
        _state.update {
            it.copy(
                descriptionDiscount = descriptionDiscount,
                isDescriptionDiscountError = isDescriptionDiscountError,
                descriptionDiscountError = descriptionDiscountError
            )
        }
        calculateLivePreview()
    }

    private fun updateItemSelected(item: Penyewaan) {
        _state.update {
            it.copy(
                itemSelected = item
            )
        }
        calculateLivePreview()
    }

    private fun updateAdminFees(adminFees: Boolean) {
        _state.update {
            it.copy(
                adminFees = adminFees
            )
        }
        calculateLivePreview()
    }

    private fun loadPenyewaan() {
        viewModelScope.launch {
            _state.update { it.copy(isButtonErrorLoading = true) }
            penyewaanRepository.getAllPenyewaanActive()
                .onSuccess { result ->
                    _state.update { it.copy(listPenyewaan = result, loadError = null, isButtonErrorLoading = false) } }
                .onError { result ->
                    _state.update { it.copy(listPenyewaan = emptyList(), loadError = result.message, isButtonErrorLoading = false) }
                }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(BuatTagihanEvents.NavigateBack)
        }
    }

    private fun buatTagihan() {
        viewModelScope.launch {
            _state.update { it.copy(isButtonLoading = true) }

            val itemSelected = _state.value.itemSelected
            val adminFees = _state.value.adminFees
            val periodStart = _state.value.selectedPeriodStart
            val periodEnd = _state.value.selectedPeriodEnd
            val isUseDiscount = _state.value.useDiscount
            val percentageDiscount = _state.value.percentageDiscount
            val descriptionDiscount = _state.value.descriptionDiscount
            val priceDiscount = _state.value.priceDiscount

            if (itemSelected == null) {
                _state.update { it.copy(isButtonLoading = false) }
                _events.send(BuatTagihanEvents.ShowErrorBanner("Belum Memilih Penyewa Kamar"))
                return@launch
            }

            if (periodStart == null || periodEnd == null) {
                _state.update {
                    it.copy(
                        isButtonLoading = false,
                        isSelectedPeriodError = true,
                        selectedPeriodError = UiText.DynamicString("Pilih tanggal periode tagihan")
                    )
                }
                return@launch
            }

            if (isUseDiscount){
                val percentageDiscountError = PatternValidation.getPercentageDiscountError(percentageDiscount)
                val isPercentageDiscountValid = PatternValidation.isPercentageDiscountValid(percentageDiscount)
                val descriptionDiscountError = PatternValidation.getDescriptionDiscountError(descriptionDiscount)
                val isDescriptionDiscountValid = PatternValidation.isDescriptionDiscountValid(descriptionDiscount)
                if (!isPercentageDiscountValid || !isDescriptionDiscountValid) {
                    _state.update {
                        it.copy(
                            isButtonLoading = false,
                            isPercentageDiscountError = !isPercentageDiscountValid,
                            percentageDiscountError = percentageDiscountError,
                            isDescriptionDiscountError = !isDescriptionDiscountValid,
                            descriptionDiscountError = descriptionDiscountError
                        )
                    }
                    return@launch
                }
            }

            _state.update {
                it.copy(
                    isSelectedPeriodError = false,
                    selectedPeriodError = null,
                    isPercentageDiscountError = false,
                    percentageDiscountError = null,
                    isDescriptionDiscountError = false,
                    descriptionDiscountError = null
                )
            }

            val totalBill = _state.value.totalBill
            val diskon = if (isUseDiscount){
                Diskon(
                    percent = percentageDiscount.toInt(),
                    price = priceDiscount,
                    description = descriptionDiscount
                )
            } else { null }
            val periodStartConvert = convertMillisToTimeStamp(periodStart)
            val periodEndConvert = convertMillisToTimeStamp(periodEnd)
            val sumDayPeriodeBill = _state.value.sumDayPeriodeBill
            val prorataDetail = _state.value.prorataDetail

            buatTagihanUseCase(
                item = itemSelected,
                adminFees = adminFees,
                periodStart = periodStartConvert,
                periodEnd = periodEndConvert,
                diskon = diskon,
                totalBill = totalBill,
                sumDayPeriodeBill = sumDayPeriodeBill,
                prorataDetail = prorataDetail
            )
                .onSuccess { result->
                    _state.update { it.copy(isButtonLoading = false) }
                    navigateToResult(resultBuatTagihan = result.toResultTagihan())
                }
                .onError { result->
                    _state.update { it.copy(isButtonLoading = false) }
                    _events.send(BuatTagihanEvents.ShowErrorBanner(result.message))
                }
        }
    }

    private fun navigateToResult(resultBuatTagihan: ResultTagihan) {
        viewModelScope.launch {
            _events.send(BuatTagihanEvents.NavigateToResult(TypeResult.PEMBUATAN_TAGIHAN, resultBuatTagihan))
        }
    }
}