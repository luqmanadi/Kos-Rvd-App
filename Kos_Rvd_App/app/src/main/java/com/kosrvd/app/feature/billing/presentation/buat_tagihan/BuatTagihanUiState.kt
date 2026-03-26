package com.kosrvd.app.feature.billing.presentation.buat_tagihan

import com.kosrvd.app.core.presentation.utils.UiText
import com.kosrvd.app.feature.rental.domain.model.Penyewaan
import com.kosrvd.app.feature.billing.domain.model.Diskon
import com.kosrvd.app.feature.billing.domain.model.ProrataDetail

data class BuatTagihanUiState(
    val isButtonErrorLoading: Boolean = false,
    val isButtonLoading: Boolean = false,
    val isShowContent: Boolean = false,
    val adminFees: Boolean = false,
    val useDiscount: Boolean = false,
    val priceDiscount: Long = 0,
    val percentageDiscount: String = "",
    val percentageDiscountError: UiText? = null,
    val isPercentageDiscountError: Boolean = false,
    val descriptionDiscount: String = "",
    val descriptionDiscountError: UiText? = null,
    val isDescriptionDiscountError: Boolean = false,
    val selectedPeriod: String? = null,
    val selectedPeriodStart: Long? = null,
    val selectedPeriodEnd: Long? = null,
    val selectedPeriodError: UiText? = null,
    val isSelectedPeriodError: Boolean = false,
    val totalBill: Long = 0,
    val showDialogDatePickerRange: Boolean = false,
    val itemSelected: Penyewaan? = null,
    val loadError: String? = null,
    val listPenyewaan: List<Penyewaan> = emptyList(),
    val currentRentalCostBySumResident: Long = 0,
    val discount: Diskon? = null,
    val sumDayPeriodeBill: Int = 0,
    val prorataDetail: ProrataDetail? = null,
)