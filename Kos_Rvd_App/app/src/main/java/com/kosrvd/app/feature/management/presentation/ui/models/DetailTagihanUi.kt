package com.kosrvd.app.feature.management.presentation.ui.models

import com.google.firebase.Timestamp
import com.kosrvd.app.feature.management.domain.model.AlatElektronik
import com.kosrvd.app.feature.management.domain.model.Diskon
import com.kosrvd.app.feature.management.domain.model.Tagihan
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toDayMonthAndYear

data class DetailTagihanUi(
    val adminFees: Boolean,
    val billAmount: Long,
    val periodStart: Timestamp,
    val periodEnd: Timestamp,
    val carParkingRentalFeeMonthly: Long?,
    val dateCreated: String,
    val datePaidOff: String? = null,
    val dateUploadProof: String? = null,
    val dueDate: Timestamp,
    val diskon: Diskon?,
    val highPowerElectronicEquipmentUsageCostsMonthly: List<AlatElektronik>,
    val idPenyewa: String,
    val idTagihan: String,
    val numberRoom: Int,
    val paymentStatus: String,
    val proofOfPayment: String? = null,
    val rejectionStatement: String? = null,
    val residentAccountIdList: List<String>,
    val residentNameList: List<String>,
    val roomRentalFee: Long,
    val verificationDate: String? = null
)

fun Tagihan.toDetailTagihanUi(): DetailTagihanUi{
    return DetailTagihanUi(
        adminFees = this.adminFees,
        billAmount = this.billAmount,
        periodStart = this.periodStart,
        periodEnd = this.periodEnd,
        carParkingRentalFeeMonthly = this.carParkingRentalFeeMonthly,
        dateCreated = this.dateCreated.toDayMonthAndYear(),
        datePaidOff = this.datePaidOff?.toDayMonthAndYear(),
        dateUploadProof = this.dateUploadProof?.toDayMonthAndYear(),
        dueDate = this.dueDate,
        highPowerElectronicEquipmentUsageCostsMonthly = this.highPowerElectronicEquipmentUsageCostsMonthly,
        idPenyewa = this.idPenyewa,
        idTagihan = this.idTagihan,
        numberRoom = this.numberRoom,
        paymentStatus = this.paymentStatus,
        proofOfPayment = this.proofOfPayment,
        rejectionStatement = this.rejectionStatement,
        residentAccountIdList = this.residentAccountIdList,
        residentNameList = this.residentNameList,
        roomRentalFee = this.roomRentalFee,
        verificationDate = this.verificationDate?.toDayMonthAndYear(),
        diskon = this.diskon
    )
}
