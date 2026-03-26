package com.kosrvd.app.feature.billing.domain.model

import androidx.annotation.Keep

@Keep
data class ProrataDetail(
    val proSewaKamar: Long,
    val proSewaParkir: Long?,
    val proSewaElektronik: List<Long>
)
