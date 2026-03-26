package com.kosrvd.app.feature.billing.domain.model

import androidx.annotation.Keep

@Keep
data class Diskon(
    val percent: Int,
    val price: Long,
    val description: String
)
