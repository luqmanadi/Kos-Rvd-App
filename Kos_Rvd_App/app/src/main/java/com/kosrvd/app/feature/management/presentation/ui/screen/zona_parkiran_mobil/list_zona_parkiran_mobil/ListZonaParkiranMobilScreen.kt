package com.kosrvd.app.feature.management.presentation.ui.screen.zona_parkiran_mobil.list_zona_parkiran_mobil

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.domain.utils.CustomToastHostState
import com.kosrvd.app.core.presentation.designsystem.component.appbar.TopBarLeftTitle
import com.kosrvd.app.core.presentation.designsystem.component.card.DataEmptyCard
import com.kosrvd.app.core.presentation.designsystem.component.card.ErrorCard
import com.kosrvd.app.core.presentation.designsystem.component.text.CustomToastHost
import com.kosrvd.app.core.presentation.utils.shimmerEffect
import com.kosrvd.app.feature.management.presentation.designsystem.component.card.InfoTotalAndCreateSection
import com.kosrvd.app.feature.management.presentation.designsystem.utils.ZonaParkirFormatter
import com.kosrvd.app.feature.management.presentation.ui.models.ListZonaParkiranMobilUi
import com.kosrvd.app.feature.management.presentation.ui.screen.zona_parkiran_mobil.component.ItemZonaParkirCard

@Composable
fun ListZonaParkiranMobilScreen(
    listZonaParkiranMobilUiState: ListZonaParkiranMobilUiState,
    listZonaParkiranMobilActions: (ListZonaParkiranMobilActions) -> Unit,
    customToastHostState: CustomToastHostState,
    colorToast: Color
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopBarLeftTitle(
                title = stringResource(R.string.list_zone_parking_car),
                actionsRow = {
                    IconButton(
                        onClick = {
                            listZonaParkiranMobilActions(ListZonaParkiranMobilActions.NavigateToPreviewZonaParkiranMobil(context.packageName))
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = "Gambar Zona Parkir"
                        )
                    }
                },
                onBackClick = {
                    listZonaParkiranMobilActions(ListZonaParkiranMobilActions.NavigateBack)
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            Box(Modifier
                .weight(1f)
                .clipToBounds()){
                when{
                    listZonaParkiranMobilUiState.isLoading -> {
                        LoadListParkiranMobil()
                    }
                    listZonaParkiranMobilUiState.loadError != null -> {
                        ErrorCard(
                            message = listZonaParkiranMobilUiState.loadError,
                            onRetry = { listZonaParkiranMobilActions(ListZonaParkiranMobilActions.TryAgain) }
                        )
                    }
                    listZonaParkiranMobilUiState.listZonaParkiranMobil.isNotEmpty() -> {
                        ListZonaParkiranMobilContent(
                            listZonaParkiranMobil = listZonaParkiranMobilUiState.listZonaParkiranMobil,
                            listZonaParkiranMobilActions = listZonaParkiranMobilActions
                        )
                    }
                    else -> {
                        DataEmptyCard(
                            title = stringResource(R.string.no_data_parking_zone),
                            image = painterResource(R.drawable.ic_zone_parking_empty)
                        )
                    }
                }

                CustomToastHost(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    hostState = customToastHostState,
                    color = colorToast,
                    enter = slideInVertically(
                        // Enters by sliding in from offset fullHeight to 0.
                        initialOffsetY = { fullHeight -> fullHeight },
                        animationSpec = tween(durationMillis = 150, easing = LinearOutSlowInEasing)
                    ),
                    exit = slideOutVertically(
                        // Exits by sliding out from offset 0 to fullHeight.
                        targetOffsetY = { fullHeight -> fullHeight },
                        animationSpec = tween(durationMillis = 250, easing = LinearOutSlowInEasing)
                    )
                )
            }

            InfoTotalAndCreateSection(
                modifier = Modifier.fillMaxWidth(),
                total = listZonaParkiranMobilUiState.listZonaParkiranMobil.size,
                create = { listZonaParkiranMobilActions(ListZonaParkiranMobilActions.NavigateToCreateZonaParkiranMobil) },
                isNeedCreateButton = true,
                titleButton = stringResource(R.string.add_zone),
                titleTotal = stringResource(R.string.zone)
            )
        }
    }
}

@Composable
private fun LoadListParkiranMobil() {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(10){
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
private fun ListZonaParkiranMobilContent(
    listZonaParkiranMobil : List<ListZonaParkiranMobilUi>,
    listZonaParkiranMobilActions: (ListZonaParkiranMobilActions) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(listZonaParkiranMobil){ zona ->
            ItemZonaParkirCard(
                onClick = {
                    listZonaParkiranMobilActions(ListZonaParkiranMobilActions.NavigateToDetailZonaParkiranMobil(zona.idZonaParkir))
                },
                nameZone = ZonaParkirFormatter.format(zona.zoneName),
                zoneStatus = zona.status
            )
        }
    }
}