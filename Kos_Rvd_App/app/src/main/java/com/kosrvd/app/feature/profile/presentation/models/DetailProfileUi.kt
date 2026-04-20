package com.kosrvd.app.feature.profile.presentation.models

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
    val numberRoom: String?,
    val photoKtp: String?
)

