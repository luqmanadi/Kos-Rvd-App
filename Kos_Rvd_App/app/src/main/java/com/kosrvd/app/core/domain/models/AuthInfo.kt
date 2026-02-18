package com.kosrvd.app.core.domain.models

import com.kosrvd.app.core.domain.utils.Role

data class AuthInfo(
    val idAkun: String,
    val role: Role
){
    companion object{
        val empty = AuthInfo(
            idAkun = "",
            role = Role.EMPTY
        )
    }
}
