package com.kosrvd.app.feature.management.domain.model

import com.google.firebase.Timestamp

data class BuatTagihan(
    val idPenyewa: String,
    val numberRoom: Int,
    val residentAccountIdList: List<String>,
    val residentNameList: List<String>,
    val periodStart: Timestamp,
    val periodEnd: Timestamp,
    val diskon: Diskon?,
    val dueDate: Timestamp,
    val roomRentalFee: Long,
    val carParkingRentalFeeMonthly: Long?,
    val highPowerElectronicEquipmentUsageCostsMonthly: List<AlatElektronik>,
    val adminFees: Boolean,
    val billAmount: Long,
    val sumDayPeriodeBill: Int,
    val prorataDetail: ProrataDetail?,
    val paymentStatus: String,
    val proofOfPayment: String? = null,
    val rejectionStatement: String? = null,
    val dateUploadProof: Timestamp? = null,
    val verificationDate: Timestamp? = null,
    val datePaidOff: Timestamp? = null,
    val dateCreated: Timestamp,
)
