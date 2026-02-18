package com.kosrvd.app.feature.management.presentation.ui.models

import com.kosrvd.app.core.domain.models.Account

data class ListAkunUi(
    val idAkun: String,
    val name: String,
    val photo: String,
    val role: String,
    val status: String
)

fun Account.toListAkunUi(): ListAkunUi{
    return ListAkunUi(
        idAkun = this.idAkun,
        name = this.name,
        photo = this.photo,
        role = this.role,
        status = this.status
    )
}
