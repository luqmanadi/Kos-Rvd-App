package com.kosrvd.app.feature.billing.data.repository.dto

data class DiskonDto(
    val percent: Int = 0,
    val price: Long = 0,
    val description: String = ""
)