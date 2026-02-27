package com.kosrvd.app.feature.management.presentation.ui.screen.parkir_harian_mobil.list_parkir_harian_mobil

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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
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
import com.kosrvd.app.feature.management.presentation.ui.models.ListParkirHarianMobilUi
import com.kosrvd.app.feature.management.presentation.ui.screen.parkir_harian_mobil.component.ItemParkirHarianMobilCard

@Composable
fun ListParkirHarianMobilScreen(
    listParkirHarianMobilUiState: ListParkirHarianMobilUiState,
    listParkirHarianMobilActions: (ListParkirHarianMobilActions) -> Unit,
    customToastHostState: CustomToastHostState,
    colorToast: Color
) {
    Scaffold(
        topBar = {
            TopBarLeftTitle(
                title = stringResource(R.string.title_usage_parking_daily),
                onBackClick = { listParkirHarianMobilActions(ListParkirHarianMobilActions.NavigateBack) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            Box(Modifier
                .weight(1f)
                .clipToBounds()){
                when {
                    listParkirHarianMobilUiState.isLoading -> {
                        ListParkirHarianLoading()
                    }
                    listParkirHarianMobilUiState.loadError != null -> {
                        ErrorCard(
                            message = listParkirHarianMobilUiState.loadError,
                            onRetry = { listParkirHarianMobilActions(ListParkirHarianMobilActions.TryAgain) },
                        )
                    }
                    listParkirHarianMobilUiState.listParkirHarianMobil.isNotEmpty() -> {
                        ListParkirHarianContent(
                            listParkirHarianMobil = listParkirHarianMobilUiState.listParkirHarianMobil,
                            listParkirHarianMobilActions = listParkirHarianMobilActions
                        )
                    }
                    else -> {
                        DataEmptyCard(
                            title = stringResource(R.string.no_data_using_parking_car_per_daily),
                            image = painterResource(R.drawable.ic_zone_parking_empty),
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
                total = listParkirHarianMobilUiState.listParkirHarianMobil.size,
                create = { listParkirHarianMobilActions(ListParkirHarianMobilActions.NavigateToTambahParkirHarianMobil) },
                isNeedCreateButton = true,
                titleButton = stringResource(R.string.add_wearer),
                titleTotal = stringResource(R.string.wearer)
            )
        }
    }
}

@Composable
private fun ListParkirHarianLoading() {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(10){
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(79.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .shimmerEffect()
            )
        }
    }
}

@Composable
private fun ListParkirHarianContent(
    listParkirHarianMobil: List<ListParkirHarianMobilUi>,
    listParkirHarianMobilActions: (ListParkirHarianMobilActions) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(listParkirHarianMobil){ parkirHarianMobil ->
            ItemParkirHarianMobilCard(
                modifier = Modifier.fillMaxWidth(),
                listParkirHarianMobilUi = parkirHarianMobil,
                onClick = {
                    listParkirHarianMobilActions(
                        ListParkirHarianMobilActions.NavigateToDetailParkirHarianMobil(parkirHarianMobil.idParkirHarianMobil)
                    )
                }
            )
        }
    }
}