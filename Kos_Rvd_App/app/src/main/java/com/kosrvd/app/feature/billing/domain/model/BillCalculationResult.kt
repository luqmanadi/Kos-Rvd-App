package com.kosrvd.app.feature.billing.domain.model

data class BillCalculationResult(
    val finalBill: Long,
    val priceDiscount: Long,
    val sumDayPeriodeBill: Int,
    val prorataDetail: ProrataDetail?
)