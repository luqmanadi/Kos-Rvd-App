package com.kosrvd.app.feature.management.domain.model

data class ProrataDetail(
    val proSewaKamar: Long,
    val proSewaParkir: Long?,
    val proSewaElektronik: List<Long>
)
