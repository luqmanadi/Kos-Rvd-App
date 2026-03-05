package com.kosrvd.app.feature.management.data.repository.dto

import androidx.annotation.Keep

@Keep
data class OtomatisasiDto(
    val useAutoReminder: Boolean = false,
    val useGenerateOtomatis: Boolean = false
)
