package com.kosrvd.app.feature.billing.data.repository.dto

import androidx.annotation.Keep

@Keep
data class ProrataDetailDto(
    val proSewaKamar: Long = 0L,
    val proSewaParkir: Long? = null,
    val proSewaElektronik: List<Long> = emptyList()
)