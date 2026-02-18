package com.kosrvd.app.feature.management.presentation.ui.models

import com.kosrvd.app.core.domain.models.Account
import com.kosrvd.app.core.domain.models.TagihanTerakhir
import com.kosrvd.app.feature.management.domain.model.DataDashboardAdmin
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toDayMonthAndYear
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toMonth
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
    val billingMonth: String,
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
        billingMonth = this.billingMonth?.toMonth() ?: "Bulan tidak tersedia",
        total = this.total.toRupiahFormat(),
        paymentStatus = this.paymentStatus,
        dueDate = this.dueDate?.toDayMonthAndYear() ?: "Tanggal tidak tersedia"
    )
}
