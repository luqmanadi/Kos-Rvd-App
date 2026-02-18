package com.kosrvd.app.core.data.repository.dto

import androidx.annotation.Keep
import com.google.firebase.Timestamp

@Keep
data class FcmTokenDataDto(
    val token: String = "",
    val deviceId: String = "",
    val deviceName: String = "",
    val platform: String = "",
    val lastUpdated: Timestamp = Timestamp.now(),
)
