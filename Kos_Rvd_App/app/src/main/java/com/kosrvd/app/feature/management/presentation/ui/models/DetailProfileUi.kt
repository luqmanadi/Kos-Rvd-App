package com.kosrvd.app.feature.management.presentation.ui.models

data class DetailProfileUi(
    val idAkun: String,
    val idPenyewa: String?,
    val photoProfile: String,
    val name: String,
    val email: String,
    val phoneNumber: String?,
    val address: String?,
    val role: String,
    val status: String,
    val numberRoom: Int?,
    val photoKtp: String?
)

