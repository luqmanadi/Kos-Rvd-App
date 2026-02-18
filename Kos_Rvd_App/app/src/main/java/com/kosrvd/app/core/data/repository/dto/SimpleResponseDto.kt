package com.kosrvd.app.core.data.repository.dto

import kotlinx.serialization.Serializable

@Serializable
data class SimpleResponseDto(
    val success: Boolean,
    val message: String? = null
)
