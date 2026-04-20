package com.kosrvd.app.feature.billing.domain.model

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.kosrvd.app.core.domain.models.AlatElektronik

@Keep
data class BuatTagihan(
    val idPenyewa: String,
    val numberRoom: String,
    val residentAccountIdList: List<String>,
    val residentNameList: List<String>,
    val periodStart: Timestamp,
    val periodEnd: Timestamp,
    val diskon: Diskon?,
    val dueDate: Timestamp,
    val roomRentalFee: Long,
    val carParkingRentalFeeMonthly: Long?,
    val highPowerElectronicEquipmentUsageCostsMonthly: List<AlatElektronik>,
    val maintenanceFee: Boolean,
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
