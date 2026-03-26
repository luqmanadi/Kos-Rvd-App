package com.kosrvd.app.feature.dashboard.data.repository.dto

import androidx.annotation.Keep

@Keep
data class DataDashboardAdminDto(
    val amountOfUnpaidBills: Int = 0,
    val billAmountNeedsVerification: Int = 0,
    val numberOfNewComplaints: Int = 0,
    val numberOfEmptyRooms: Int = 0
)