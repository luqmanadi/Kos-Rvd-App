package com.kosrvd.app.core.domain.models

data class NonActiveAccountRequest(
    val idAkun: String,
    val role: String,
    val photoUrl: String
)
