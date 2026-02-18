package com.kosrvd.app.feature.management.presentation.ui.screen.profile.edit_photo_profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.domain.utils.CustomToastHostState
import com.kosrvd.app.core.presentation.designsystem.component.appbar.TopBarCenterTitle
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionButton
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionOutlinePrimaryButton
import com.kosrvd.app.core.presentation.designsystem.component.text.CustomToastHost
import com.kosrvd.app.feature.management.presentation.designsystem.component.image.LoadImage

@Composable
fun EditPhotoProfileScreen(
    editPhotoProfileUiState: EditPhotoProfileUiState,
    editPhotoProfileActions: (EditPhotoProfileActions) -> Unit,
    customToastHostState: CustomToastHostState
) {
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { contetUri ->
        if (contetUri != null){
            editPhotoProfileActions(EditPhotoProfileActions.UpdatePhoto(contetUri))
        }
    }

    val isCanSavePhoto = editPhotoProfileUiState.newPhotoProfile != Uri.EMPTY && editPhotoProfileUiState.oldPhotoProfile != editPhotoProfileUiState.newPhotoProfile.toString()

    val photoProfile = if (editPhotoProfileUiState.oldPhotoProfile == editPhotoProfileUiState.newPhotoProfile.toString()) {
        editPhotoProfileUiState.oldPhotoProfile
    } else {
        editPhotoProfileUiState.newPhotoProfile.toString()
    }

    Scaffold(
        topBar = {
            TopBarCenterTitle(
                title = stringResource(R.string.change_photo_profile),
                onBackClick = { editPhotoProfileActions(EditPhotoProfileActions.NavigateBack) },
                isNeedBackIcon = true,
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ){
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 30.dp, horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item(key = "Photo Profile Card") {
                    LoadImage(
                        url = photoProfile,
                        modifier = Modifier.size(150.dp),
                        contentDescription = "Foto Profile",
                        shape = CircleShape,
                        errorImg = R.drawable.ic_fill_profile
                    )
                }
                item { Spacer(Modifier.height(45.dp)) }
                item(key = "Button Pilih Foto") {
                    ActionButton(
                        modifier = Modifier.width(150.dp),
                        onClick = {
                            photoPicker.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        text = stringResource(R.string.choose_photo),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !editPhotoProfileUiState.isButtonLoading,
                        height = 43.dp
                    )
                }
                item { Spacer(Modifier.height(15.dp)) }
                item(key = "Button Simpan Foto") {
                    ActionOutlinePrimaryButton(
                        modifier = Modifier.width(150.dp),
                        onClick = { editPhotoProfileActions(EditPhotoProfileActions.SavePhoto) },
                        text = stringResource(R.string.save),
                        shape = RoundedCornerShape(12.dp),
                        height = 43.dp,
                        contentPadding = PaddingValues(horizontal = 45.dp),
                        isLoading = editPhotoProfileUiState.isButtonLoading,
                        enabled = isCanSavePhoto,
                        strokeWidth = 1.dp
                    )
                }
            }

            CustomToastHost(
                hostState = customToastHostState,
                color = MaterialTheme.colorScheme.error,
                enter = slideInVertically(
                    // Enters by sliding in from offset -fullHeight to 0.
                    initialOffsetY = { fullHeight -> -fullHeight },
                    animationSpec = tween(durationMillis = 150, easing = LinearOutSlowInEasing)
                ),
                exit = slideOutVertically(
                    // Exits by sliding out from offset 0 to -fullHeight.
                    targetOffsetY = { fullHeight -> -fullHeight },
                    animationSpec = tween(durationMillis = 250, easing = LinearOutSlowInEasing)
                )
            )
        }
    }
}