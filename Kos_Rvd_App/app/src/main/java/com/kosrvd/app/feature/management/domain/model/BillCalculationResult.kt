package com.kosrvd.app.feature.management.domain.model

data class BillCalculationResult(
    val finalBill: Long,
    val priceDiscount: Long,
    val sumDayPeriodeBill: Int,
    val prorataDetail: ProrataDetail?
)
