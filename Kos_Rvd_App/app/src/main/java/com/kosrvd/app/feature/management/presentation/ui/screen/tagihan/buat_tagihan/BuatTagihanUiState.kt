package com.kosrvd.app.feature.management.presentation.ui.screen.tagihan.buat_tagihan

import com.kosrvd.app.core.presentation.utils.UiText
import com.kosrvd.app.feature.management.domain.model.Diskon
import com.kosrvd.app.feature.management.domain.model.Penyewaan

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
    val selectedPeriodStart: Long = 0,
    val selectedPeriodEnd: Long = 0,
    val selectedPeriodError: UiText? = null,
    val isSelectedPeriodError: Boolean = false,
    val totalBill: Long = 0,
    val showDialogDatePickerRange: Boolean = false,
    val itemSelected: Penyewaan? = null,
    val loadError: String? = null,
    val listPenyewaan: List<Penyewaan> = emptyList(),
    val currentRentalCostBySumResident: Long = 0,
    val discount: Diskon? = null
)