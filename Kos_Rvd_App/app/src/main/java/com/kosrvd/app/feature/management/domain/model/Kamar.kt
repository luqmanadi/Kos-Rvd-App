package com.kosrvd.app.feature.management.domain.model

data class Kamar(
    val idKamar: String,
    val numberRoom: Int,
    val price: Harga,
    val facility: List<String>,
    val size: String,
    val freeService: List<AlatElektronik>,
    val status: String
)
