package com.kosrvd.app.core.data.mappers

import com.kosrvd.app.core.data.repository.dto.AuthInfoDto
import com.kosrvd.app.core.domain.models.AuthInfo

fun AuthInfoDto.toAuthInfo(): AuthInfo {
    return AuthInfo(
        idAkun = idAkun,
        role = role
    )
}

fun AuthInfo.toAuthInfoDto(): AuthInfoDto {
    return AuthInfoDto(
        idAkun = idAkun,
        role = role
    )
}