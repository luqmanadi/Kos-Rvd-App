package com.kosrvd.app.core.domain.models

import androidx.annotation.Keep

@Keep
data class Harga(
    val onePerson: Long,
    val twoPersons: Long?,
)