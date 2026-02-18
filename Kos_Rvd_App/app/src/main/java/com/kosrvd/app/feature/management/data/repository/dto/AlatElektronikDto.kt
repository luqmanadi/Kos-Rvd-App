package com.kosrvd.app.feature.management.data.repository.dto

import androidx.annotation.Keep


@Keep
data class AlatElektronikDto(
    val toolName: String = "",
    val cost: Long = 0,
    val origin: String = ""
)
