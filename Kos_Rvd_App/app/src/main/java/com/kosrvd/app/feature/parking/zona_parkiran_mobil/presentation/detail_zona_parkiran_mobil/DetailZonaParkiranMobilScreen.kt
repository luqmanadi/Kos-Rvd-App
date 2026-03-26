package com.kosrvd.app.feature.parking.zona_parkiran_mobil.presentation.detail_zona_parkiran_mobil

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.domain.utils.CustomToastHostState
import com.kosrvd.app.core.presentation.designsystem.component.appbar.TopBarLeftTitle
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionDangerButton
import com.kosrvd.app.core.presentation.designsystem.component.card.ErrorCard
import com.kosrvd.app.core.presentation.designsystem.component.dialog.GeneralDialogConfirmationDanger
import com.kosrvd.app.core.presentation.designsystem.component.dialog.GeneralDialogOnlyDismissDanger
import com.kosrvd.app.core.presentation.designsystem.component.text.CustomToastHost
import com.kosrvd.app.core.presentation.utils.shimmerEffect
import com.kosrvd.app.feature.parking.zona_parkiran_mobil.domain.model.ZonaParkiran
import com.kosrvd.app.core.presentation.designsystem.organism.card.InfoSectionCard
import com.kosrvd.app.core.presentation.utils.CardAction
import com.kosrvd.app.core.presentation.utils.ZonaParkirFormatter
import com.kosrvd.app.core.presentation.utils.toRupiahFormat

@Composable
fun DetailZonaParkiranMobilScreen(
    detailZonaParkiranMobilUiState: DetailZonaParkiranMobilUiState,
    detailZonaParkiranMobilActions: (DetailZonaParkiranMobilActions) -> Unit,
    customToastHostState: CustomToastHostState,
    colorToast: Color
) {
    Scaffold(
        topBar = {
            TopBarLeftTitle(
                title = stringResource(R.string.detail_zone_parking),
                onBackClick = { detailZonaParkiranMobilActions(DetailZonaParkiranMobilActions.NavigateBack) }
            )
        }
    ) { innerPadding ->
        Box(Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ){
            when{
                detailZonaParkiranMobilUiState.isLoading -> {
                    LoadingDetailZonaParkirMobil()
                }
                detailZonaParkiranMobilUiState.loadError != null -> {
                    ErrorCard(
                        message = detailZonaParkiranMobilUiState.loadError,
                        onRetry = { detailZonaParkiranMobilActions(DetailZonaParkiranMobilActions.TryAgain) }
                    )
                }
                detailZonaParkiranMobilUiState.detailZonaParkiranMobilUi != null -> {
                    DetailZonaParkiranMobilContent(
                        zonaParkiran = detailZonaParkiranMobilUiState.detailZonaParkiranMobilUi,
                        detailZonaParkiranMobilActions = detailZonaParkiranMobilActions
                    )
                }
            }

            CustomToastHost(
                modifier = Modifier.align(Alignment.TopCenter),
                hostState = customToastHostState,
                color = colorToast,
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

        if (detailZonaParkiranMobilUiState.detailZonaParkiranMobilUi?.status == Constant.KOSONG){
            if (detailZonaParkiranMobilUiState.showDialogDeleteZonaParkir){
                GeneralDialogConfirmationDanger(
                    onConfirm = {
                        detailZonaParkiranMobilActions(DetailZonaParkiranMobilActions.DeleteZonaParkir)
                    },
                    onDismiss = {
                        detailZonaParkiranMobilActions(DetailZonaParkiranMobilActions.DismissDialogDeleteZonaParkir)
                    },
                    title = stringResource(R.string.title_dialog_delete_zone_parking),
                    description = stringResource(R.string.description_dialog_delete_zone_parking),
                    isLoadingButton = detailZonaParkiranMobilUiState.buttonDeleteIsLoading,
                    buttonCancelEnabled = !detailZonaParkiranMobilUiState.buttonDeleteIsLoading
                )
            }
        } else {
            if (detailZonaParkiranMobilUiState.showDialogDeleteZonaParkir){
                GeneralDialogOnlyDismissDanger(
                    title = stringResource(R.string.title_dialog_cannot_delete_zone_parking),
                    description1 = buildAnnotatedString {
                        append("Zona parkir  ini tidak dapat dihapus dikarenakan ")
                        withStyle(style = SpanStyle(
                            fontWeight = FontWeight.Bold
                        )){
                            append("masih digunakan ")
                        }
                        append("oleh penyewa dan berstatus DIPAKAI.")
                    },
                    description2 = buildAnnotatedString {
                        withStyle(style = SpanStyle(
                            fontWeight = FontWeight.Bold
                        )){
                            append("Akhiri sewa parkir ini terlebih dahulu ")
                        }
                        append("apabila ingin menghapus kamar ini.")
                    },
                    onDismiss = { detailZonaParkiranMobilActions(DetailZonaParkiranMobilActions.DismissDialogDeleteZonaParkir) }
                )
            }
        }
    }
}

@Composable
private fun LoadingDetailZonaParkirMobil() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(70.dp)
                .clip(RoundedCornerShape(20.dp))
                .shimmerEffect()
        )
        Spacer(Modifier.height(30.dp))
        Box(
            Modifier
                .fillMaxWidth()
                .height(70.dp)
                .clip(RoundedCornerShape(20.dp))
                .shimmerEffect()
        )
        Spacer(Modifier.height(30.dp))
        Box(
            Modifier
                .fillMaxWidth()
                .height(70.dp)
                .clip(RoundedCornerShape(20.dp))
                .shimmerEffect()
        )
        Spacer(Modifier.height(30.dp))
        Box(
            Modifier
                .fillMaxWidth()
                .height(70.dp)
                .clip(RoundedCornerShape(20.dp))
                .shimmerEffect()
        )
        Spacer(Modifier.weight(1f))
        Box(
            Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(20.dp))
                .shimmerEffect()
        )
    }
}

@Composable
fun DetailZonaParkiranMobilContent(
    zonaParkiran: ZonaParkiran,
    detailZonaParkiranMobilActions: (DetailZonaParkiranMobilActions) -> Unit,
) {
    val verticalScrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 30.dp)
            .verticalScroll(verticalScrollState),
    ) {
        InfoSectionCard(
            icon = ImageVector.vectorResource(R.drawable.ic_zona_parkir),
            title = stringResource(R.string.name_zone),
            action = CardAction.NavigationIconSide(
                onClick = {
                    detailZonaParkiranMobilActions(DetailZonaParkiranMobilActions.NavigateToEditZoneName)
                }
            ),
            content = {
                Text(
                    text = ZonaParkirFormatter.format(zonaParkiran.zoneName),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        )
        Spacer(Modifier.height(20.dp))
        InfoSectionCard(
            icon = Icons.Filled.Paid,
            title = stringResource(R.string.bill_month),
            action = CardAction.NavigationIconSide(
                onClick = {
                    detailZonaParkiranMobilActions(DetailZonaParkiranMobilActions.NavigateToEditMonthlyFee)
                }
            ),
            content = {
                Text(
                    text = zonaParkiran.monthlyFee.toRupiahFormat(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        )
        Spacer(Modifier.height(20.dp))
        InfoSectionCard(
            icon = Icons.Filled.Paid,
            title = stringResource(R.string.bill_daily),
            action = CardAction.NavigationIconSide(
                onClick = {
                    detailZonaParkiranMobilActions(DetailZonaParkiranMobilActions.NavigateToEditDailyCosts)
                }
            ),
            content = {
                Text(
                    text = zonaParkiran.dailyCosts.toRupiahFormat(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        )
        Spacer(Modifier.height(20.dp))
        InfoSectionCard(
            icon = Icons.Filled.Circle,
            title = stringResource(R.string.status),
            action = CardAction.None,
            content = {
                Text(
                    text = zonaParkiran.status,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        )
        Spacer(Modifier.weight(1f))
        Spacer(Modifier.height(20.dp))
        ActionDangerButton(
            modifier = Modifier.fillMaxWidth(),
            height = 43.dp,
            onClick = { detailZonaParkiranMobilActions(DetailZonaParkiranMobilActions.ShowDialogDeleteZonaParkir) },
            text = stringResource(R.string.delete),
            shape = RoundedCornerShape(12.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(R.string.delete)
                )
            }
        )
    }
}