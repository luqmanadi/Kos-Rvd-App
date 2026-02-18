package com.kosrvd.app.core.domain.models

import com.google.firebase.Timestamp

data class FcmToken(
    val token: String,
    val deviceId: String,
    val deviceName: String,
    val platform: String,
    val lastUpdated: Timestamp,
)
