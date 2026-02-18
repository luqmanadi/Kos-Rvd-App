package com.kosrvd.app.feature.management.data.repository.dto

import androidx.annotation.Keep
import com.google.firebase.Timestamp


@Keep
data class UpdatePaymentBillDto (
    val paymentStatus: String,
    val proofOfPayment: String,
    val dateUploadProof: Timestamp
)
