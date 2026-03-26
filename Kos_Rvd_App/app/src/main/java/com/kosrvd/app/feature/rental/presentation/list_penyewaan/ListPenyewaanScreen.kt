package com.kosrvd.app.feature.rental.presentation.list_penyewaan

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
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
import com.kosrvd.app.core.presentation.designsystem.organism.card.InfoTotalAndCreateSection
import com.kosrvd.app.feature.rental.presentation.models.ListPenyewaanUi
import com.kosrvd.app.feature.rental.presentation.component.ItemPenyewaCard

@Composable
fun ListPenyewaanScreen(
    listPenyewaanUiState: ListPenyewaanUiState,
    listPenyewaanActions: (ListPenyewaanActions) -> Unit,
    customToastHostState: CustomToastHostState,
) {
    Scaffold(
        topBar = {
            TopBarLeftTitle(
                title = stringResource(R.string.list_rental),
                onBackClick = { listPenyewaanActions(ListPenyewaanActions.NavigateBack) }
            )
        }
    ) { innerPadding->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Box(Modifier
                .weight(1f)
                .clipToBounds()){
                when {
                    listPenyewaanUiState.isLoading -> {
                        ListPenyewaanLoading()
                    }
                    listPenyewaanUiState.loadError != null -> {
                        ErrorCard(
                            message = listPenyewaanUiState.loadError,
                            onRetry = { listPenyewaanActions(ListPenyewaanActions.TryAgain) },
                        )
                    }
                    listPenyewaanUiState.listPenyewaanUi.isNotEmpty() -> {
                        ListPenyewaanContent(
                            listPenyewaanUi = listPenyewaanUiState.listPenyewaanUi,
                            listPenyewaanActions = listPenyewaanActions
                        )
                    }
                    else -> {
                        DataEmptyCard(
                            title = stringResource(R.string.no_history_data_rental),
                            image = painterResource(R.drawable.ic_penyewaan),
                        )
                    }
                }
                CustomToastHost(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    hostState = customToastHostState,
                    color = MaterialTheme.colorScheme.error,
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
                total = listPenyewaanUiState.listPenyewaanUi.size,
                create = { listPenyewaanActions(ListPenyewaanActions.NavigateToCreatePenyewaan) },
                isNeedCreateButton = true,
                titleButton = stringResource(R.string.add_rental),
                titleTotal = stringResource(R.string.tenant)
            )
        }
    }
}

@Composable
private fun ListPenyewaanLoading() {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(10){
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .shimmerEffect()
            )
        }
    }
}

@Composable
private fun ListPenyewaanContent(
    listPenyewaanUi: List<ListPenyewaanUi>,
    listPenyewaanActions: (ListPenyewaanActions) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(listPenyewaanUi){ penyewa ->
            ItemPenyewaCard(
                listPenyewaanUi = penyewa,
                onClick = {
                    listPenyewaanActions(ListPenyewaanActions.NavigateToDetailPenyewaan(penyewa.idPenyewa))
                }
            )
        }
    }
}