package com.kosrvd.app.feature.management.data.repository.dto

import androidx.annotation.Keep

@Keep
data class HargaDto(
    val onePerson: Long = 0,
    val twoPersons: Long? = null,
)
