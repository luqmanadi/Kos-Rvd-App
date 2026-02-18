package com.kosrvd.app.feature.management.data.mappers

import com.kosrvd.app.feature.management.data.repository.dto.DataDashboardAdminDto
import com.kosrvd.app.feature.management.domain.model.DataDashboardAdmin

fun DataDashboardAdminDto.toDataDashboardAdmin(): DataDashboardAdmin{
    return DataDashboardAdmin(
        amountOfUnpaidBills = this.amountOfUnpaidBills,
        billAmountNeedsVerification = this.billAmountNeedsVerification,
        numberOfNewComplaints = this.numberOfNewComplaints,
        numberOfEmptyRooms = this.numberOfEmptyRooms
    )
}