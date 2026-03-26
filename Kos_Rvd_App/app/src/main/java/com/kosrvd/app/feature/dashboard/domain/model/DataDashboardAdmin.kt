package com.kosrvd.app.feature.dashboard.domain.model

data class DataDashboardAdmin(
    val amountOfUnpaidBills: Int,
    val billAmountNeedsVerification: Int,
    val numberOfNewComplaints: Int,
    val numberOfEmptyRooms: Int
)