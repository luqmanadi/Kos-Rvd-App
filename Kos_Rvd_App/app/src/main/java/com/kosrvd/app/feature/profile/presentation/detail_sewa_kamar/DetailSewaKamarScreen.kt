package com.kosrvd.app.feature.profile.presentation.detail_sewa_kamar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ElectricalServices
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.util.fastJoinToString
import com.kosrvd.app.R
import com.kosrvd.app.core.presentation.designsystem.component.appbar.TopBarLeftTitle
import com.kosrvd.app.core.presentation.designsystem.component.card.ErrorCard
import com.kosrvd.app.core.presentation.utils.shimmerEffect
import com.kosrvd.app.core.presentation.designsystem.organism.card.InfoSectionCard
import com.kosrvd.app.core.presentation.utils.CardAction
import com.kosrvd.app.core.presentation.utils.toRupiahFormat
import com.kosrvd.app.feature.profile.presentation.models.DetailSewaKamarUi

@Composable
fun DetailSewaKamarScreen(
    idPenyewa: String,
    detailSewaKamarUiState: DetailSewaKamarUiState,
    detailsSewaKamarActions: (DetailSewaKamarActions) -> Unit
) {
    Scaffold(
        topBar = {
            TopBarLeftTitle(
                title = stringResource(R.string.rent),
                onBackClick = { detailsSewaKamarActions(DetailSewaKamarActions.NavigateBack) }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        when {
            detailSewaKamarUiState.isLoading -> {
                LoadingDetailSewaKamar(Modifier.padding(innerPadding))
            }

            detailSewaKamarUiState.error != null -> {
                ErrorCard(
                    Modifier.padding(innerPadding),
                    message = detailSewaKamarUiState.error,
                    onRetry = { detailsSewaKamarActions(DetailSewaKamarActions.TryAgain(idPenyewa)) }
                )
            }

            detailSewaKamarUiState.detailSewaKamarUi != null -> {
                DetailSewaKamarContent(
                    modifier = Modifier.padding(innerPadding),
                    detailSewaKamarUi = detailSewaKamarUiState.detailSewaKamarUi
                )
            }
        }
    }
}

@Composable
private fun LoadingDetailSewaKamar(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(8) {
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
private fun DetailSewaKamarContent(
    modifier: Modifier = Modifier,
    detailSewaKamarUi: DetailSewaKamarUi
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item(key = "Nomor Kamar Info Card") {
            InfoSectionCard(
                icon = Icons.Filled.AirlineSeatFlat,
                title = stringResource(R.string.room_number_completed),
                action = CardAction.None,
                content = {
                    Text(
                        text = detailSewaKamarUi.numberRoom,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        }
        item { Spacer(Modifier.height(20.dp)) }
        item(key = "Penghuni Kamar Info Card") {
            InfoSectionCard(
                icon = Icons.Filled.Person,
                title = stringResource(R.string.resident_room),
                action = CardAction.None,
                content = {
                    Text(
                        text = detailSewaKamarUi.listResident.fastJoinToString(separator = " & "),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        }
        item { Spacer(Modifier.height(20.dp)) }
        item(key = "Harga Sewa Kamar Info Card") {
            InfoSectionCard(
                icon = Icons.Filled.Paid,
                title = stringResource(R.string.cost_rental),
                action = CardAction.None,
                content = {
                    Text(
                        text = buildString {
                            append(detailSewaKamarUi.currentRoomRentalCost)
                            append("/bulan")
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        }
        item { Spacer(Modifier.height(20.dp)) }
        item(key = "Pemakaian Parkiran Mobil Info Card") {
            InfoSectionCard(
                icon = Icons.Filled.DirectionsCar,
                title = stringResource(R.string.parking_usage),
                action = CardAction.None,
                content = {
                    if (detailSewaKamarUi.pemakaianParkirMobil != null){
                        Text(
                            text = buildString {
                                append("Zona ${detailSewaKamarUi.pemakaianParkirMobil.zonaParkir.zoneName.capitalize(Locale.current)}")
                                append(" - ")
                                append(detailSewaKamarUi.pemakaianParkirMobil.zonaParkir.monthlyFee.toRupiahFormat())
                                append("/bulan")
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.no_parking_usage),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                }
            )
        }
        item { Spacer(Modifier.height(20.dp)) }
        item(key = "Pemakaian Alat Elektronik Daya Tinggi Info Card") {
            InfoSectionCard(
                icon = Icons.Filled.ElectricalServices,
                title = stringResource(R.string.use_of_electronic_devices),
                action = CardAction.None,
                content = {
                    if (detailSewaKamarUi.pemakaianAlatElektronik.isNotEmpty()){
                        detailSewaKamarUi.pemakaianAlatElektronik.forEachIndexed { index, pemakaianAlatElektronik ->
                            Text(
                                text = buildString {
                                    append("${index + 1}. ")
                                    append(pemakaianAlatElektronik.toolName)
                                    append(" - ")
                                    append(pemakaianAlatElektronik.cost.toRupiahFormat())
                                    append("/bulan")
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    } else {
                        Text(
                            text = stringResource(R.string.no_data_use_tools),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                }
            )
        }
        item { Spacer(Modifier.height(20.dp)) }
        item(key = "Total Tagihan Card") {
            InfoSectionCard(
                icon = ImageVector.vectorResource(R.drawable.rupiah_black),
                title = stringResource(R.string.total_bill),
                action = CardAction.None,
                content = {
                    Text(
                        text = buildString {
                            append(detailSewaKamarUi.totalMonthlyBill)
                            append("/bulan")
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        }
        item { Spacer(Modifier.height(20.dp)) }
        item(key = "Tanggal Mulai Sewa Info Card") {
            InfoSectionCard(
                icon = Icons.Filled.CalendarMonth,
                title = stringResource(R.string.start_rental_date),
                action = CardAction.None,
                content = {
                    Text(
                        text = detailSewaKamarUi.rentalStartDate,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        }
        item { Spacer(Modifier.height(20.dp)) }
        item(key = "Status Sewa Card") {
            InfoSectionCard(
                icon = Icons.Filled.Info,
                title = stringResource(R.string.rental_status),
                action = CardAction.None,
                content = {
                    Text(
                        text = detailSewaKamarUi.rentalStatus.capitalize(Locale.current),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        }
    }
}