package com.kosrvd.app.feature.profile.presentation.detail_profile

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirlineSeatFlat
import androidx.compose.material.icons.filled.AssignmentInd
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.domain.utils.CustomToastHostState
import com.kosrvd.app.core.presentation.designsystem.component.appbar.TopBarCenterTitle
import com.kosrvd.app.core.presentation.designsystem.component.card.ErrorCard
import com.kosrvd.app.core.presentation.designsystem.component.text.CustomToastHost
import com.kosrvd.app.core.presentation.utils.shimmerEffect
import com.kosrvd.app.core.presentation.designsystem.organism.card.InfoSectionCard
import com.kosrvd.app.core.presentation.designsystem.atom.image.LoadImage
import com.kosrvd.app.core.presentation.utils.CardAction
import com.kosrvd.app.core.presentation.utils.toNumberRoomFormat
import com.kosrvd.app.feature.profile.presentation.models.DetailProfileUi

@Composable
fun DetailProfileScreen(
    detailProfileUiState: DetailProfileUiState,
    detailProfileActions: (DetailProfileActions) -> Unit,
    customToastHostState: CustomToastHostState
) {
    val lazyState = rememberLazyListState()

    Scaffold(
        topBar = {
            TopBarCenterTitle(
                title = stringResource(R.string.detail_account),
                onBackClick = { detailProfileActions(DetailProfileActions.NavigateBack) },
                isNeedBackIcon = true
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        when{
            detailProfileUiState.isLoading -> {
                LoadingDetailProfile(Modifier.padding(innerPadding))
            }
            detailProfileUiState.loadError != null -> {
                ErrorCard(
                    modifier = Modifier.padding(innerPadding),
                    onRetry = { detailProfileActions(DetailProfileActions.TryAgain) },
                    message = detailProfileUiState.loadError
                )
            }
            detailProfileUiState.profileUi != null -> {
                DetailProfileContent(
                    modifier = Modifier.padding(innerPadding),
                    detailProfileUi = detailProfileUiState.profileUi,
                    detailProfileActions = detailProfileActions,
                    customToastHostState = customToastHostState,
                    state = lazyState
                )
            }
        }
    }
}

@Composable
private fun LoadingDetailProfile(modifier: Modifier = Modifier) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 40.dp),
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Box(
                Modifier
                    .size(130.dp)
                    .clip(CircleShape)
                    .shimmerEffect()
            )
        }
        item { Spacer(Modifier.height(16.dp)) }
        item {
            Box(
                Modifier
                    .size(width = 180.dp, height = 24.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .shimmerEffect()
            )
        }
        item { Spacer(Modifier.height(35.dp)) }
        items(8) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(73.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .shimmerEffect()
            )
            if (it < 8) {
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun DetailProfileContent(
    modifier: Modifier = Modifier,
    detailProfileUi: DetailProfileUi,
    state: LazyListState,
    detailProfileActions: (DetailProfileActions) -> Unit,
    customToastHostState: CustomToastHostState
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ){
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 40.dp),
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = state
        ) {
            item { Spacer(Modifier.height(20.dp)) }
            item(key = "Photo Profile Card") {
                LoadImage(
                    url = detailProfileUi.photoProfile,
                    modifier = Modifier.size(130.dp),
                    contentDescription = "Foto Penghuni",
                    shape = CircleShape,
                    errorImg = R.drawable.ic_fill_profile
                )
            }
            item { Spacer(Modifier.height(15.dp)) }
            item(key = "Button Ubah Foto") {
                TextButton(
                    shape = RoundedCornerShape(12.dp),
                    onClick = { detailProfileActions(DetailProfileActions.NavigateToEditPhotoProfile)}
                ) {
                    Text(
                        text = stringResource(R.string.change_photo),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            item { Spacer(Modifier.height(35.dp)) }
            item(key = "Name Info Card") {
                InfoSectionCard(
                    icon = ImageVector.vectorResource(R.drawable.id_card),
                    title = stringResource(R.string.name),
                    action = CardAction.NavigationIconSide(
                        onClick = {
                            detailProfileActions(DetailProfileActions.NavigateToEditNameProfile)
                        }
                    ),
                    content = {
                        Text(
                            text = detailProfileUi.name,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                )
            }
            item { Spacer(Modifier.height(20.dp)) }
            item(key = "Email Info Card") {
                InfoSectionCard(
                    icon = Icons.Filled.Email,
                    title = stringResource(R.string.email),
                    action = CardAction.NavigationIconSide(
                        onClick = {
                            detailProfileActions(DetailProfileActions.NavigateToEditEmailProfile)
                        }
                    ),
                    content = {
                        Text(
                            text = detailProfileUi.email,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                )
            }
            item { Spacer(Modifier.height(20.dp)) }

            if (detailProfileUi.role == Constant.PENGHUNI_ROLE){
                item(key = "No Hp Info Card") {
                    InfoSectionCard(
                        icon = Icons.Filled.Call,
                        title = stringResource(R.string.phone_number),
                        action = CardAction.NavigationIconSide(
                            onClick = {
                                detailProfileActions(DetailProfileActions.NavigateToEditPhoneNumberProfile)
                            }
                        ),
                        content = {
                            Text(
                                text = detailProfileUi.phoneNumber ?: "Belum Diatur",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    )
                }
                item { Spacer(Modifier.height(20.dp)) }
                item(key = "Alamat Info Card") {
                    InfoSectionCard(
                        icon = Icons.Filled.LocationOn,
                        title = stringResource(R.string.address),
                        action = CardAction.NavigationIconSide(
                            onClick = {
                                detailProfileActions(DetailProfileActions.NavigateToEditAddressProfile)
                            }
                        ),
                        content = {
                            Text(
                                text = detailProfileUi.address ?: "Belum Diatur",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    )
                }
                item { Spacer(Modifier.height(20.dp)) }
                item(key = "Dokumen Ktp Info Card") {
                    InfoSectionCard(
                        icon = Icons.Filled.Badge,
                        title = stringResource(R.string.document),
                        action = CardAction.NavigationIconSide(
                            onClick = {
                                detailProfileActions(DetailProfileActions.NavigateToPreviewKtp)
                            }
                        ),
                        content = {
                            Text(
                                text = stringResource(R.string.ktp),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    )
                }
                item { Spacer(Modifier.height(20.dp)) }
                item(key = "Sewa Info Card") {
                    InfoSectionCard(
                        icon = Icons.Filled.AirlineSeatFlat,
                        title = stringResource(R.string.rent),
                        action = if (detailProfileUi.numberRoom != null && !detailProfileUi.idPenyewa.isNullOrEmpty()) CardAction.NavigationIconSide(
                            onClick = {
                                detailProfileActions(DetailProfileActions.NavigateToDetailSewa)
                            }
                        )else CardAction.None,
                        content = {
                            Text(
                                text = detailProfileUi.numberRoom?.toNumberRoomFormat() ?: "Saat ini sedang tidak menyewa Kos",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    )
                }
                item { Spacer(Modifier.height(20.dp)) }
            }

            item(key = "Status Akun Info Card") {
                InfoSectionCard(
                    icon = Icons.Filled.FiberManualRecord,
                    title = stringResource(R.string.status_account),
                    action = CardAction.None,
                    content = {
                        Text(
                            text = detailProfileUi.status,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                )
            }
            item { Spacer(Modifier.height(20.dp)) }
            item(key = "Role Info Card") {
                InfoSectionCard(
                    icon = Icons.Filled.AssignmentInd,
                    title = stringResource(R.string.role),
                    action = CardAction.None,
                    content = {
                        Text(
                            text = detailProfileUi.role.capitalize(Locale.current),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                )
            }
        }

        CustomToastHost(
            hostState = customToastHostState,
            color = MaterialTheme.colorScheme.primary,
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