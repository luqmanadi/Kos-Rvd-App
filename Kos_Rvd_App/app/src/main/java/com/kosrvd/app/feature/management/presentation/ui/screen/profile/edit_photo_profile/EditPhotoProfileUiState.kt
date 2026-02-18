package com.kosrvd.app.feature.management.presentation.ui.screen.profile.edit_photo_profile

import android.net.Uri

data class EditPhotoProfileUiState(
    val isButtonLoading: Boolean = false,
    val newPhotoProfile: Uri = Uri.EMPTY,
    val idAkun: String = "",
    val oldPhotoProfile: String = ""
)
