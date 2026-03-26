package com.kosrvd.app.feature.dashboard.data.mappers

import com.kosrvd.app.feature.dashboard.data.repository.dto.DataDashboardAdminDto
import com.kosrvd.app.feature.dashboard.domain.model.DataDashboardAdmin

fun DataDashboardAdminDto.toDataDashboardAdmin(): DataDashboardAdmin{
    return DataDashboardAdmin(
        amountOfUnpaidBills = this.amountOfUnpaidBills,
        billAmountNeedsVerification = this.billAmountNeedsVerification,
        numberOfNewComplaints = this.numberOfNewComplaints,
        numberOfEmptyRooms = this.numberOfEmptyRooms
    )
}