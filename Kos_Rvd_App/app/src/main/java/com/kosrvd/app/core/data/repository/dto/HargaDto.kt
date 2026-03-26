package com.kosrvd.app.core.data.repository.dto

import androidx.annotation.Keep

@Keep
data class HargaDto(
    val onePerson: Long = 0,
    val twoPersons: Long? = null,
)
