package com.kosrvd.app.core.domain.models

data class DeleteAccountRequest(
    val idAkun: String,
    val role: String,
    val ktpUrl: String
)
