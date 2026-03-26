package com.kosrvd.app.feature.profile.presentation.main_page

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.domain.utils.CustomToastHostState
import com.kosrvd.app.core.presentation.designsystem.component.card.ErrorCard
import com.kosrvd.app.core.presentation.designsystem.component.dialog.GeneralDialogConfirmationDanger
import com.kosrvd.app.core.presentation.designsystem.component.text.CustomToastHost
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme
import com.kosrvd.app.core.presentation.utils.shimmerEffect
import com.kosrvd.app.core.presentation.designsystem.atom.image.LoadImage
import com.kosrvd.app.feature.profile.presentation.models.ProfileUi
import com.kosrvd.app.feature.profile.presentation.component.MenuProfileCard

@Composable
fun ProfileScreen(
    profileUiState: ProfileUiState,
    profileActions: (ProfileActions) -> Unit,
    customToastHostState: CustomToastHostState,
) {
    when {
        profileUiState.loading -> {
            LoadingProfile(modifier = Modifier)
        }

        profileUiState.loadError != null -> {
            ErrorCard(
                onRetry = { profileActions(ProfileActions.TryAgain) },
                modifier = Modifier

            )
        }

        else -> {
            ContentProfile(
                modifier = Modifier,
                profileUiState = profileUiState,
                profileActions = profileActions,
                customToastHostState = customToastHostState
            )
        }
    }
}

@Composable
fun LoadingProfile(modifier: Modifier = Modifier) {
    Column(
        modifier.fillMaxSize()
            .padding(horizontal = 25.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        Box(Modifier
            .size(120.dp)
            .clip(CircleShape)
            .shimmerEffect()
        )
        Spacer(modifier = Modifier.height(30.dp))
        Box(Modifier
            .size(width = 250.dp, height = 20.dp)
            .clip(RoundedCornerShape(10.dp))
            .shimmerEffect()
        )
        Spacer(modifier = Modifier.height(45.dp))
        Box(Modifier
            .fillMaxWidth()
            .height(222.dp)
            .clip(RoundedCornerShape(20.dp))
            .shimmerEffect()
        )
    }
}

@Composable
fun ContentProfile(
    modifier: Modifier = Modifier,
    profileUiState: ProfileUiState,
    profileActions: (ProfileActions) -> Unit,
    customToastHostState: CustomToastHostState
) {
    Box(
        modifier = modifier.fillMaxSize()
    ){
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(20.dp)
        ) {
            item { Spacer(modifier = Modifier.height(30.dp)) }
            item {
                LoadImage(
                    contentDescription = "Photo Profile",
                    url = profileUiState.profileUi?.url ?: "",
                    errorImg = R.drawable.ic_fill_profile,
                    modifier = Modifier.size(120.dp)
                )
            }
            item { Spacer(modifier = Modifier.height(30.dp)) }
            item {
                Text(
                    text = profileUiState.profileUi?.name ?: "",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.width(250.dp),
                    textAlign = TextAlign.Center
                )
            }
            item { Spacer(modifier = Modifier.height(45.dp)) }
            item {
                MenuProfileCard(
                    modifier = Modifier.fillMaxWidth(),
                    navigateChangePassword = { profileActions(ProfileActions.NavigateToResetPassword) },
                    navigateDetailAkun = { profileActions(ProfileActions.NavigateToDetailAkun) },
                    navigateLogout = {
                        profileActions(ProfileActions.ShowLogoutDialog)
                    }
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

    if (profileUiState.isLogoutDialogVisible) {
        GeneralDialogConfirmationDanger(
            onConfirm = {
                profileActions(ProfileActions.LogOut)
            },
            onDismiss = {
                profileActions(ProfileActions.DismissLogoutDialog)
            },
            title = stringResource(R.string.title_dialog_confirmation_logout),
            description = stringResource(R.string.description_dialog_confirmation_logout),
            isLoadingButton = profileUiState.buttonLoading
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ProfileScreenPreview() {
    KosRvdAppTheme {
        ProfileScreen(
            profileUiState = ProfileUiState(
                loading = false,
                profileUi = ProfileUi(
                    url = "",
                    name = "Ndiman Js",
                    idAkun = "1"
                ),
                loadError = null
            ),
            profileActions = {},
            customToastHostState = CustomToastHostState()
        )
    }
}