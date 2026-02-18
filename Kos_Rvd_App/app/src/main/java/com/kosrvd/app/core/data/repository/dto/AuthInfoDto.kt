package com.kosrvd.app.core.data.repository.dto

import androidx.annotation.Keep
import com.kosrvd.app.core.domain.utils.Role
import kotlinx.serialization.Serializable


@Keep
@Serializable
data class AuthInfoDto(
    val idAkun: String = "",
    val role: Role = Role.EMPTY
){
    companion object {
        // Default empty instance
        val Empty = AuthInfoDto()
    }
}