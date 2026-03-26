package com.kosrvd.app.feature.room.presentation.detail_kamar

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirlineSeatFlat
import androidx.compose.material.icons.filled.Bathroom
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ElectricalServices
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.kosrvd.app.R
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.domain.utils.CustomToastHostState
import com.kosrvd.app.core.presentation.designsystem.component.appbar.TopBarLeftTitle
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionDangerButton
import com.kosrvd.app.core.presentation.designsystem.component.card.ErrorCard
import com.kosrvd.app.core.presentation.designsystem.component.dialog.GeneralDialogConfirmationDanger
import com.kosrvd.app.core.presentation.designsystem.component.dialog.GeneralDialogOnlyDismissDanger
import com.kosrvd.app.core.presentation.designsystem.component.text.CustomToastHost
import com.kosrvd.app.core.presentation.designsystem.component.text.OutlineBackgroundInfoText
import com.kosrvd.app.core.presentation.utils.shimmerEffect
import com.kosrvd.app.feature.room.domain.model.Kamar
import com.kosrvd.app.core.presentation.designsystem.organism.card.InfoSectionCard
import com.kosrvd.app.core.presentation.utils.CardAction
import com.kosrvd.app.core.presentation.utils.toNumberRoomFormat
import com.kosrvd.app.core.presentation.utils.toRupiahFormat

@Composable
fun DetailKamarScreen(
    idKamar: String,
    colorBgToast: Color,
    detailKamarUiState: DetailKamarUiState,
    detailKamarActions: (DetailKamarActions) -> Unit,
    customToastHostState: CustomToastHostState
) {
    Scaffold(
        topBar = {
            TopBarLeftTitle(
                title = stringResource(R.string.detail_room),
                onBackClick = { detailKamarActions(DetailKamarActions.NavigateBack) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ){
            when{
                detailKamarUiState.isLoading -> {
                    LoadingDetailKamar()
                }
                detailKamarUiState.loadError != null -> {
                    ErrorCard(
                        message = detailKamarUiState.loadError,
                        onRetry = { detailKamarActions(DetailKamarActions.TryAgain(idKamar = idKamar)) }
                    )
                }
                detailKamarUiState.kamarUi != null -> {
                    DetailKamarMainContent(
                        kamarUi = detailKamarUiState.kamarUi,
                        detailKamarActions = detailKamarActions
                    )
                }
            }
            CustomToastHost(
                modifier = Modifier.align(Alignment.TopCenter),
                hostState = customToastHostState,
                color = colorBgToast,
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

        if (detailKamarUiState.kamarUi?.status == Constant.KOSONG){
            if (detailKamarUiState.showDialogDeleteKamar){
                GeneralDialogConfirmationDanger(
                    onConfirm = {
                        detailKamarActions(DetailKamarActions.DeleteKamar)
                    },
                    onDismiss = {
                        detailKamarActions(DetailKamarActions.DismissDeleteDialog)
                    },
                    title = stringResource(R.string.title_dialog_confirmation_delete_room),
                    description = stringResource(R.string.description_dialog_confirmation_delete_room),
                    isLoadingButton = detailKamarUiState.isButtonDeleteLoading,
                    buttonCancelEnabled = !detailKamarUiState.isButtonDeleteLoading
                )
            }
        } else {
            if (detailKamarUiState.showDialogDeleteKamar){
                GeneralDialogOnlyDismissDanger(
                    title = stringResource(R.string.title_not_delete_room),
                    description1 = buildAnnotatedString {
                        append("Kamar ini tidak dapat dihapus dikarenakan ")
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
                            append("Akhiri sewa terlebih dahulu ")
                        }
                        append("apabila ingin menghapus kamar ini.")
                    },
                    onDismiss = { detailKamarActions(DetailKamarActions.DismissDeleteDialog) }
                )
            }
        }
    }
}

@Composable
private fun DetailKamarMainContent(
    kamarUi: Kamar,
    detailKamarActions: (DetailKamarActions) -> Unit
){
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 25.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item(key = "Nomor Kamar Card") {
            InfoSectionCard(
                icon = Icons.Filled.AirlineSeatFlat,
                title = stringResource(R.string.room_number_completed),
                action = CardAction.NavigationIconSide(
                    onClick = {
                        detailKamarActions(DetailKamarActions.NavigateToEditNomorKamar)
                    }
                ),
                content = {
                    Text(
                        text = kamarUi.numberRoom.toNumberRoomFormat(),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        }
        item { Spacer(Modifier.height(20.dp)) }
        item(key = "Ukuran Kamar Card") {
            InfoSectionCard(
                icon = Icons.Filled.Title,
                title = stringResource(R.string.size),
                action = CardAction.NavigationIconSide(
                    onClick = {
                        detailKamarActions(DetailKamarActions.NavigateToEditUkuranKamar)
                    }
                ),
                content = {
                    Text(
                        text = kamarUi.size,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        }
        item { Spacer(Modifier.height(20.dp)) }
        item(key = "Tarif dan Kapasitas Kamar Card") {
            InfoSectionCard(
                icon = Icons.Filled.Paid,
                title = stringResource(R.string.room_cost_capacity_monthly),
                action = CardAction.NavigationIconSide(
                    onClick = {
                        detailKamarActions(DetailKamarActions.NavigateToEditTarifAndCapacityKamar)
                    }
                ),
                content = {
                    Column {
                        Text(
                            text = "1 Orang - ${kamarUi.price.onePerson.toRupiahFormat()}/bulan",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (kamarUi.price.twoPersons != null){
                            Text(
                                text = "2 Orang - ${kamarUi.price.twoPersons.toRupiahFormat()}/bulan",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                }
            )
        }
        item { Spacer(Modifier.height(20.dp)) }
        item(key = "Pemakaian Alat Elektronik Gratis Pada Kamar Card") {
            InfoSectionCard(
                icon = Icons.Filled.ElectricalServices,
                title = stringResource(R.string.usage_tools_electronic_free),
                action = CardAction.NavigationIconSide(
                    onClick = {
                        detailKamarActions(DetailKamarActions.NavigateToEditLayananKamarGratis)
                    }
                ),
                content = {
                    if (kamarUi.freeService.isNotEmpty()){
                        kamarUi.freeService.fastForEachIndexed { index, freeService ->
                            Text(
                                text = "${index + 1}. ${freeService.toolName} (Free) - ${freeService.cost.toRupiahFormat()}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    } else {
                        Text(
                            text = stringResource(R.string.no_electronic_usage_facility_free),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
        item { Spacer(Modifier.height(20.dp)) }
        item(key = "Fasilitas Kamar Card") {
            InfoSectionCard(
                icon = Icons.Filled.Bathroom,
                title = stringResource(R.string.facility),
                action = CardAction.NavigationIconSide(
                    onClick = {
                        detailKamarActions(DetailKamarActions.NavigateToEditFasilitasKamar)
                    }
                ),
                content = {
                    FlowRow(
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        kamarUi.facility.forEach { facility ->
                            OutlineBackgroundInfoText(
                                text = facility,
                                colorBg = MaterialTheme.colorScheme.surface,
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface)
                            )
                        }
                    }
                }
            )
        }
        item { Spacer(Modifier.height(20.dp)) }
        item(key = "Status Kamar Card") {
            InfoSectionCard(
                icon = Icons.Filled.FiberManualRecord,
                title = stringResource(R.string.status),
                action = CardAction.None,
                content = {
                    Text(
                        text = kamarUi.status,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        }
        item { Spacer(Modifier.height(20.dp)) }
        item(key = "Button Delete Kamar Card") {
            ActionDangerButton(
                modifier = Modifier.fillMaxWidth(),
                height = 43.dp,
                onClick = { detailKamarActions(DetailKamarActions.ShowDeleteDialog) },
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
}

@Composable
private fun LoadingDetailKamar() {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 25.dp),
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(7) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(73.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .shimmerEffect()
            )
            if (it < 7) {
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}