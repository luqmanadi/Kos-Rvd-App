package com.kosrvd.app.core.domain.models

import androidx.annotation.Keep

@Keep
data class AlatElektronik(
    val toolName: String,
    val cost: Long,
    val origin: String
)