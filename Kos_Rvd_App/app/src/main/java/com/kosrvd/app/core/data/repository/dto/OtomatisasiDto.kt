package com.kosrvd.app.core.data.repository.dto

import androidx.annotation.Keep

@Keep
data class OtomatisasiDto(
    val useAutoReminder: Boolean = false,
    val useGenerateOtomatis: Boolean = false
)