package com.kosrvd.app.feature.management.data.repository.dto

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId


@Keep
data class TagihanDto(
    @DocumentId
    val idTagihan: String = "",
    val idPenyewa: String = "",
    val numberRoom: Int = 0,
    val residentAccountIdList: List<String> = emptyList(),
    val residentNameList: List<String> = emptyList(),
    val periodStart: Timestamp = Timestamp.now(),
    val periodEnd: Timestamp = Timestamp.now(),
    val diskon: DiskonDto? = null,
    val roomRentalFee: Long = 0,
    val carParkingRentalFeeMonthly: Long? = null,
    val highPowerElectronicEquipmentUsageCostsMonthly: List<AlatElektronikDto> = emptyList(),
    val adminFees: Boolean = false,
    val billAmount: Long = 0,
    val sumDayPeriodeBill: Int = 0,
    val prorataDetail: ProrataDetailDto? = null,
    val paymentStatus: String = "",
    val proofOfPayment: String? = null,
    val rejectionStatement: String? = null,
    val dateCreated: Timestamp = Timestamp.now(),
    val dueDate: Timestamp = Timestamp.now(),
    val dateUploadProof: Timestamp? = null,
    val verificationDate: Timestamp? = null,
    val datePaidOff: Timestamp? = null,
)
