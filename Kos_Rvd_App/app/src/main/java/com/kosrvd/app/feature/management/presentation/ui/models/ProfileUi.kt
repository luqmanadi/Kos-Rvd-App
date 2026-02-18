package com.kosrvd.app.feature.management.presentation.ui.models

import com.kosrvd.app.core.domain.models.Account

data class ProfileUi(
    val idAkun: String,
    val name: String,
    val url: String
)

fun Account.toProfileUi(): ProfileUi{
    return ProfileUi(
        idAkun = this.idAkun,
        name = this.name,
        url = this.photo
    )
}
