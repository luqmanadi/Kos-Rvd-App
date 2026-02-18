package com.kosrvd.app.feature.management.domain.model

data class ZonaParkiran(
    val idZonaParkir: String,
    val zoneName: String,
    val monthlyFee: Long,
    val dailyCosts: Long,
    val status: String
)
