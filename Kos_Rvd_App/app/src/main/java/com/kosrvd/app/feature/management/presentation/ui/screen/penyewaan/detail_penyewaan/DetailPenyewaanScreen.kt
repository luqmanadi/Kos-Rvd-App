package com.kosrvd.app.feature.management.presentation.ui.screen.penyewaan.detail_penyewaan

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AirlineSeatFlat
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.ElectricalServices
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Garage
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.compose.ui.util.fastJoinToString
import com.kosrvd.app.R
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.domain.utils.CustomToastHostState
import com.kosrvd.app.core.presentation.designsystem.component.appbar.TopBarLeftTitle
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionDangerButton
import com.kosrvd.app.core.presentation.designsystem.component.card.ErrorCard
import com.kosrvd.app.core.presentation.designsystem.component.dialog.GeneralDialogConfirmationDanger
import com.kosrvd.app.core.presentation.designsystem.component.text.CustomToastHost
import com.kosrvd.app.core.presentation.utils.shimmerEffect
import com.kosrvd.app.feature.management.presentation.designsystem.component.card.InfoSectionCard
import com.kosrvd.app.feature.management.presentation.designsystem.utils.CardAction
import com.kosrvd.app.feature.management.presentation.designsystem.utils.ZonaParkirFormatter
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toDayMonthAndYear
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toRupiahFormat
import com.kosrvd.app.feature.management.presentation.ui.models.PenyewaUi

@Composable
fun DetailPenyewaanScreen(
    detailPenyewaanUiState: DetailPenyewaanUiState,
    detailPenyewaActions: (DetailPenyewaActions) -> Unit,
    customToastHostState: CustomToastHostState,
    colorToast: Color
) {
    Scaffold(
        topBar = {
            TopBarLeftTitle(
                title = stringResource(R.string.detail_rent),
                onBackClick = { detailPenyewaActions(DetailPenyewaActions.NavigateBack) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when {
                detailPenyewaanUiState.isLoading -> {
                    LoadingDetailPenyewa()
                }

                detailPenyewaanUiState.loadError != null -> {
                    ErrorCard(
                        message = detailPenyewaanUiState.loadError,
                        onRetry = { detailPenyewaActions(DetailPenyewaActions.TryAgain) }
                    )
                }

                detailPenyewaanUiState.penyewaUi != null -> {
                    DetailPenyewaMainContent(
                        penyewaUi = detailPenyewaanUiState.penyewaUi,
                        detailPenyewaActions = detailPenyewaActions
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

        if (detailPenyewaanUiState.showDialogEndRental) {
            GeneralDialogConfirmationDanger(
                onConfirm = {
                    detailPenyewaActions(DetailPenyewaActions.EndRental)
                },
                onDismiss = {
                    detailPenyewaActions(DetailPenyewaActions.DismissDialogEndRental)
                },
                title = stringResource(R.string.dialog_confirmation_end_the_lease),
                description = stringResource(R.string.description_dialog_confirmation_end_the_lease),
                isLoadingButton = detailPenyewaanUiState.isButtonLoadingEndRental,
                buttonCancelEnabled = !detailPenyewaanUiState.isButtonLoadingEndRental
            )
        }
    }
}

@Composable
private fun LoadingDetailPenyewa() {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 25.dp),
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(7) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(73.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .shimmerEffect()
            )
        }
    }
}

@Composable
private fun DetailPenyewaMainContent(
    penyewaUi: PenyewaUi,
    detailPenyewaActions: (DetailPenyewaActions) -> Unit
) {
    val sumTentant = penyewaUi.listResident.size
    val iconPeople = if (sumTentant > 1) Icons.Filled.Group else Icons.Filled.Person
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 25.dp),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item(key = "Info Penghuni Kamar Card") {
            InfoSectionCard(
                icon = iconPeople,
                title = stringResource(R.string.resident_room),
                action = CardAction.None,
                content = {
                    Text(
                        text = penyewaUi.listResident.map { it.name }
                            .fastJoinToString(separator = " & "),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        }
        item(key = "Info Nomor Kamar Card") {
            InfoSectionCard(
                icon = Icons.Filled.AirlineSeatFlat,
                title = stringResource(R.string.room_number_completed),
                action = CardAction.None,
                content = {
                    val cost = if (sumTentant > 1 &&
                        penyewaUi.infoKamar.currentRoomRentalCost.twoPersons != null
                    ) {
                        penyewaUi.infoKamar.currentRoomRentalCost.twoPersons
                    } else {
                        penyewaUi.infoKamar.currentRoomRentalCost.onePerson
                    }
                    Text(
                        text = "kamar No ${penyewaUi.infoKamar.numberRoom} - ${cost.toRupiahFormat()} ($sumTentant Orang)",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        }
        item(key = "Info Pemakaian Alat Elektronik Card") {
            val usageElectronic = penyewaUi.pemakaianAlatElektronikBulanan
            val action = if (penyewaUi.rentalStatus == Constant.ACTIVE) {
                CardAction.NavigationIconFooter {
                    detailPenyewaActions(DetailPenyewaActions.NavigateToEditPemakaianElektronik)
                }
            } else {
                CardAction.None
            }
            InfoSectionCard(
                icon = Icons.Filled.ElectricalServices,
                title = stringResource(R.string.monthly_electronic_device_use),
                action = action,
                content = {
                    if (usageElectronic.isNotEmpty()) {
                        usageElectronic.fastForEachIndexed { index, electronic ->
                            val isGratis = electronic.cost == 0L
                            val textFront =
                                if (isGratis) "${electronic.toolName} Gratis" else electronic.toolName
                            Text(
                                text = "${index + 1}. $textFront - ${electronic.cost.toRupiahFormat()}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    } else {
                        Text(
                            text = stringResource(R.string.not_usage_tools),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                footer = if (penyewaUi.rentalStatus == Constant.ACTIVE) {
                    {
                        if (usageElectronic.isEmpty()) {
                            Row(
                                modifier = Modifier,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Tambah Pemakaian",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(Modifier.width(10.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = "Detail",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        } else {
                            Row(
                                modifier = Modifier,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Edit Pemakaian",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(Modifier.width(10.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = "Detail",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                } else {
                    null
                }
            )
        }
        item(key = "Info Pemakaian Parkir Mobil Bulanan Card") {
            val usageParking = penyewaUi.pemakaianParkirMobilBulanan
            val contentText = if (usageParking != null) {
                buildString {
                    append(usageParking.carBrand)
                    append(" ")
                    append(usageParking.carName)
                    append(" ")
                    append("(${usageParking.numberPlate})")
                    append(" - ")
                    append(ZonaParkirFormatter.format(usageParking.zonaParkir.zoneName))
                    append(" - ")
                    append("${usageParking.zonaParkir.monthlyFee.toRupiahFormat()}/bulan")
                }
            } else {
                stringResource(R.string.no_parking_usage)
            }
            val action = if (penyewaUi.rentalStatus == Constant.ACTIVE) {
                CardAction.NavigationIconFooter {
                    detailPenyewaActions(DetailPenyewaActions.NavigateToEditPemakaianParkirMobil)
                }
            } else {
                CardAction.None
            }
            InfoSectionCard(
                icon = Icons.Filled.Garage,
                title = stringResource(R.string.monthly_parking_car_use),
                action = action,
                content = {
                    Text(
                        text = contentText,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                footer = if (penyewaUi.rentalStatus == Constant.ACTIVE) {
                    {
                        if (usageParking == null) {
                            Row(
                                modifier = Modifier,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Tambah Pemakaian",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(Modifier.width(10.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = "Detail",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        } else {
                            Row(
                                modifier = Modifier,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Edit Pemakaian",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(Modifier.width(10.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = "Detail",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                } else {
                    null
                }
            )
        }
        item(key = "Kapasitas Kamar Card") {
            InfoSectionCard(
                icon = iconPeople,
                title = stringResource(R.string.capacity_room),
                action = CardAction.None,
                content = {
                    val capacityRoom =
                        if (penyewaUi.infoKamar.currentRoomRentalCost.twoPersons != null) 2 else 1
                    Text(
                        text = "$capacityRoom Orang",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        }
        item(key = "Total Tagihan Card") {
            InfoSectionCard(
                icon = Icons.Filled.Paid,
                title = stringResource(R.string.total_bill),
                action = CardAction.None,
                content = {
                    Text(
                        text = "${penyewaUi.totalMonthlyBill}/bulan",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        }
        item(key = "Status Penyewa Card") {
            InfoSectionCard(
                icon = Icons.Filled.Circle,
                title = stringResource(R.string.status),
                action = CardAction.None,
                isRedBackgroundIcon = penyewaUi.rentalStatus == Constant.NON_ACTIVE,
                content = {
                    Text(
                        text = penyewaUi.rentalStatus,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        }
        item(key = "Tanggal Mulai Sewa Card") {
            InfoSectionCard(
                icon = Icons.Filled.CalendarMonth,
                title = stringResource(R.string.start_rental_date),
                action = CardAction.None,
                content = {
                    Text(
                        text = penyewaUi.rentalStartDate.toDayMonthAndYear(),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        }
        item(key = "Tanggal Selesai Sewa Card") {
            InfoSectionCard(
                icon = Icons.Filled.EventAvailable,
                title = stringResource(R.string.end_rental_date),
                action = CardAction.None,
                content = {
                    val tanggalAkhir = if (penyewaUi.rentalCompletionDate != null) {
                        penyewaUi.rentalCompletionDate.toDayMonthAndYear()
                    } else {
                        "-"
                    }
                    Text(
                        text = tanggalAkhir,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        }
        if (penyewaUi.rentalStatus == Constant.ACTIVE) {
            item(key = "Button Akhiri Sewa") {
                ActionDangerButton(
                    modifier = Modifier.fillMaxWidth(),
                    height = 43.dp,
                    onClick = { detailPenyewaActions(DetailPenyewaActions.ShowDialogEndRental) },
                    text = stringResource(R.string.end_the_lease),
                    shape = RoundedCornerShape(12.dp),
                )
            }
        }
    }
}