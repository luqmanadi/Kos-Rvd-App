package com.kosrvd.app.feature.management.presentation.ui.models

import com.kosrvd.app.core.domain.models.Account

data class MainScreenUi(
    val numberOfUnreadNotification: Int = 0,
    val name: String = "Terjadi Error",
)

fun Account.toMainScreenUi(): MainScreenUi{
    return MainScreenUi(
        numberOfUnreadNotification = this.numberOfUnreadNotification,
        name = this.name
    )
}