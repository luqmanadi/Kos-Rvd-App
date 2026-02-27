package com.kosrvd.app.feature.management.presentation.ui.models

import com.kosrvd.app.core.domain.models.Account
import com.kosrvd.app.core.domain.models.TagihanTerakhir
import com.kosrvd.app.feature.management.domain.model.DataDashboardAdmin
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toDayMonthAndYear
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toDayMonthShortAndYear
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toNumberRoomFormat
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toRupiahFormat

data class AdminDashboardUi(
    val name: String,
    val amountOfUnpaidBills: String,
    val billAmountNeedsVerification: String,
    val numberOfNewComplaints: String,
    val numberOfEmptyRooms: String
)

data class PenghuniDashboardUi(
    val name: String,
    val numberRoom: String,
    val lastBill: LastBill? = null,
)

data class CombineDataDashboardUi(
    val adminDashboardUi: AdminDashboardUi?,
    val penghuniDashboardUi: PenghuniDashboardUi?
)

data class LastBill(
    val idTagihan: String,
    val periodStart: String,
    val periodEnd: String,
    val total: String,
    val paymentStatus: String,
    val dueDate: String
)

fun DataDashboardAdmin.toAdminDashboardUi() = AdminDashboardUi(
    amountOfUnpaidBills = this.amountOfUnpaidBills.toString(),
    billAmountNeedsVerification = this.billAmountNeedsVerification.toString(),
    numberOfNewComplaints = this.numberOfNewComplaints.toString(),
    numberOfEmptyRooms = this.numberOfEmptyRooms.toString(),
    name = "Admin"
)

fun Account.toPenghuniDashboardUi() = PenghuniDashboardUi(
    name = this.name,
    numberRoom = this.dataPenghuni?.numberRoom?.toNumberRoomFormat()?: "Tidak Menempati Kamar",
    lastBill = this.dataPenghuni?.finalBill?.toLastBill()
)

fun TagihanTerakhir.toLastBill(): LastBill{
    return LastBill(
        idTagihan = this.idTagihan,
        periodStart = this.periodStart.toDayMonthShortAndYear(),
        periodEnd = this.periodEnd.toDayMonthShortAndYear(),
        total = this.total.toRupiahFormat(),
        paymentStatus = this.paymentStatus,
        dueDate = this.dueDate.toDayMonthAndYear()
    )
}
