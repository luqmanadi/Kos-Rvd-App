package com.kosrvd.app.feature.account.presentation.detail_akun

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.kosrvd.app.R
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.domain.utils.CustomToastHostState
import com.kosrvd.app.core.presentation.designsystem.component.appbar.TopBarLeftTitle
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionButton
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionDangerButton
import com.kosrvd.app.core.presentation.designsystem.component.card.ErrorCard
import com.kosrvd.app.core.presentation.designsystem.component.dialog.GeneralDialogConfirmation
import com.kosrvd.app.core.presentation.designsystem.component.dialog.GeneralDialogConfirmationDanger
import com.kosrvd.app.core.presentation.designsystem.component.text.BackgroundInfoText
import com.kosrvd.app.core.presentation.designsystem.component.text.CustomToastHost
import com.kosrvd.app.core.presentation.utils.shimmerEffect
import com.kosrvd.app.core.presentation.designsystem.organism.card.InfoSectionCard
import com.kosrvd.app.core.presentation.designsystem.atom.image.LoadImage
import com.kosrvd.app.core.presentation.utils.CardAction
import com.kosrvd.app.feature.account.presentation.models.DetailAkunUi
import com.kosrvd.app.feature.account.presentation.component.InfoDasarAkunCard

@Composable
fun DetailAkunScreen(
    detailAkunUiState: DetailAkunUiState,
    detailAkunActions: (DetailAkunActions) -> Unit,
    customToastHostState: CustomToastHostState,
    idAkun: String
) {
    Scaffold(
        topBar = {
            TopBarLeftTitle(
                title = stringResource(R.string.detail_account),
                onBackClick = { detailAkunActions(DetailAkunActions.NavigateBack) }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ){
            when {
                detailAkunUiState.isLoading -> {
                    DetailAkunLoadingContent()
                }
                detailAkunUiState.loadError != null -> {
                    ErrorCard(
                        message = detailAkunUiState.loadError,
                        onRetry = { detailAkunActions(DetailAkunActions.TryAgain(idAkun)) }
                    )
                }
                detailAkunUiState.detailAkun != null -> {
                    DetailAkunMainContent(
                        detailAkunUi = detailAkunUiState.detailAkun,
                        detailAkunActions = detailAkunActions
                    )
                }
            }

            CustomToastHost(
                modifier = Modifier.align(Alignment.TopCenter),
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


        if (detailAkunUiState.showDialogNonAktifAkun) {
            GeneralDialogConfirmationDanger(
                onConfirm = {
                    detailAkunActions(DetailAkunActions.NonActivateAccount)
                },
                onDismiss = {
                    detailAkunActions(DetailAkunActions.CloseDialogNonAktifAkun)
                },
                title = stringResource(R.string.title_dialog_confirmation_nonactive_account),
                description = stringResource(R.string.description_dialog_confirmation_nonactive_account),
                isLoadingButton = detailAkunUiState.buttonNonAktifIsLoading,
                buttonCancelEnabled = !detailAkunUiState.buttonNonAktifIsLoading
            )
        }

        if (detailAkunUiState.showDialogActivateAccount){
            GeneralDialogConfirmation(
                onConfirm = {
                    detailAkunActions(DetailAkunActions.ActivateAkun)
                },
                onDismiss = {
                    detailAkunActions(DetailAkunActions.CloseDialogActivateAccount)
                },
                title = stringResource(R.string.title_dialog_confirmation_activate_account),
                description = stringResource(R.string.description_dialog_confirmation_activate_account),
                isLoadingButton = detailAkunUiState.buttonAktifIsLoading,
                buttonCancelEnabled = !detailAkunUiState.buttonAktifIsLoading
            )
        }
    }
}

@Composable
private fun DetailAkunLoadingContent() {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 40.dp),
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Box(
                Modifier
                    .size(120.dp)
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
        item { Spacer(Modifier.height(5.dp)) }
        item {
            Box(
                Modifier
                    .size(width = 60.dp, height = 24.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .shimmerEffect()
            )
        }
        item { Spacer(Modifier.height(30.dp)) }
        item {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .shimmerEffect()
            )
        }
        item { Spacer(Modifier.height(15.dp)) }
        item {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .shimmerEffect()
            )
        }
        item { Spacer(Modifier.height(15.dp)) }
        item {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .shimmerEffect()
            )
        }
        item { Spacer(Modifier.height(15.dp)) }
        item {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .shimmerEffect()
            )
        }
        item { Spacer(Modifier.height(15.dp)) }
        item {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .shimmerEffect()
            )
        }
    }
}


@Composable
private fun DetailAkunMainContent(
    detailAkunUi: DetailAkunUi,
    detailAkunActions: (DetailAkunActions) -> Unit
) {
    val colorBg = if (detailAkunUi.status == "Aktif") {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.error
    }

    BoxWithConstraints(
        Modifier.fillMaxSize()
    ) {
        val screenHeight = maxHeight

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 20.dp)
                // KUNCI: Paksa tinggi minimal setinggi layar
                .heightIn(min = screenHeight - 40.dp)
        ) {
            val (foto, nama, status, cardUtama, cardKontak, cardAlamat, photoKtp, buttonNonActivate, buttonActivate) = createRefs()

            LoadImage(
                url = detailAkunUi.photo,
                modifier = Modifier.size(120.dp)
                    .constrainAs(foto){
                        top.linkTo(parent.top, margin = 20.dp)
                        centerHorizontallyTo(parent)
                    },
                contentDescription = "Foto Akun",
                shape = CircleShape,
                errorImg = R.drawable.ic_fill_profile
            )

            Text(
                text = detailAkunUi.name,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.constrainAs(nama){
                    top.linkTo(foto.bottom, margin = 16.dp)
                    centerHorizontallyTo(parent)
                }
            )

            BackgroundInfoText(
                text = detailAkunUi.status,
                colorBg = colorBg,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.constrainAs(status){
                    top.linkTo(nama.bottom, margin = 16.dp)
                    centerHorizontallyTo(parent)
                }
            )

            InfoDasarAkunCard(
                isBasicInformation = true,
                dataAkun = detailAkunUi,
                titleInfoCard = stringResource(R.string.basic_information),
                iconInfoCard = Icons.Filled.Info,
                modifier = Modifier.constrainAs(cardUtama){
                    top.linkTo(status.bottom, margin = 16.dp)
                    centerHorizontallyTo(parent)
                }
            )

            if (detailAkunUi.role == Constant.PENGHUNI_ROLE) {
                InfoDasarAkunCard(
                    isContact = true,
                    dataAkun = detailAkunUi,
                    titleInfoCard = stringResource(R.string.contact),
                    iconInfoCard = Icons.Filled.Phone,
                    modifier = Modifier.constrainAs(cardKontak){
                        top.linkTo(cardUtama.bottom, margin = 16.dp)
                        centerHorizontallyTo(parent)
                    }
                )
                InfoDasarAkunCard(
                    isAddress = true,
                    dataAkun = detailAkunUi,
                    titleInfoCard = stringResource(R.string.home_address),
                    iconInfoCard = Icons.Filled.LocationOn,
                    modifier = Modifier.constrainAs(cardAlamat){
                        top.linkTo(cardKontak.bottom, margin = 16.dp)
                        centerHorizontallyTo(parent)
                    }
                )

                InfoSectionCard(
                    modifier = Modifier.constrainAs(photoKtp){
                        top.linkTo(cardAlamat.bottom, margin = 16.dp)
                        centerHorizontallyTo(parent)
                    },
                    icon = Icons.Filled.Badge,
                    title = stringResource(R.string.document),
                    action = CardAction.NavigationIconSide(
                        onClick = {
                            detailAkunActions(DetailAkunActions.NavigateToPreviewImageKtp(detailAkunUi.dataPenghuni?.photoKtp ?: ""))
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

            if (detailAkunUi.status == "Aktif"){
                ActionDangerButton(
                    modifier = Modifier.fillMaxWidth().constrainAs(buttonNonActivate){
                        val anchorTop = if (detailAkunUi.role == Constant.PENGHUNI_ROLE) photoKtp.bottom else cardUtama.bottom
                        top.linkTo(anchorTop, margin = 30.dp)
                        bottom.linkTo(parent.bottom)
                        verticalBias = 1f
                        centerHorizontallyTo(parent)
                    },
                    height = 43.dp,
                    onClick = { detailAkunActions(DetailAkunActions.OpenDialogNonAktifAkun) },
                    text = stringResource(R.string.non_active_account),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            if (detailAkunUi.status == "Tidak Aktif"){
                ActionButton(
                    modifier = Modifier.fillMaxWidth().constrainAs(buttonActivate){
                        val anchorTop = if (detailAkunUi.role == Constant.PENGHUNI_ROLE) photoKtp.bottom else cardUtama.bottom
                        top.linkTo(anchorTop, margin = 30.dp)
                        bottom.linkTo(parent.bottom)
                        verticalBias = 1f
                        centerHorizontallyTo(parent)
                    },
                    height = 43.dp,
                    onClick = { detailAkunActions(DetailAkunActions.OpenDialogActivateAccount) },
                    text = stringResource(R.string.active_account),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
    }
}