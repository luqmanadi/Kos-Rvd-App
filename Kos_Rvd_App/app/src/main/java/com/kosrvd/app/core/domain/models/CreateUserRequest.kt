package com.kosrvd.app.core.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserRequest(
    val idAkun: String,
    val email: String,
    val password: String,
    val name: String,
    val role: String,
    // Field di bawah ini opsional, hanya diisi jika role Penghuni
    val address: String? = null,
    val phoneNumber: String? = null,
    val photoKtp: String? = null,
    val photoKtpFileName: String? = null
)