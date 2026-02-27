package com.kosrvd.app.feature.management.presentation.ui.screen.kamar.list_kamar

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
import com.kosrvd.app.feature.management.presentation.ui.models.ListKamarUi
import com.kosrvd.app.feature.management.presentation.ui.screen.kamar.component.ItemKamarCard

@Composable
fun ListKamarScreen(
    listKamarUiState: ListKamarUiState,
    listKamarActions: (ListKamarActions) -> Unit,
    customToastHostState: CustomToastHostState,
    colorToast: Color
) {
    Scaffold(
        topBar = {
            TopBarLeftTitle(
                title = stringResource(R.string.list_room),
                onBackClick = {  listKamarActions(ListKamarActions.NavigateBack) }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
        ) {
            Box(Modifier.weight(1f)
                .clipToBounds()){
                when {
                    listKamarUiState.isLoading -> {
                        ListKamarLoading()
                    }
                    listKamarUiState.loadError != null -> {
                        ErrorCard(
                            message = listKamarUiState.loadError,
                            onRetry = { listKamarActions(ListKamarActions.TryAgain) },
                        )
                    }
                    listKamarUiState.listKamarUi.isNotEmpty() -> {
                        ListKamarContent(
                            listKamarUi = listKamarUiState.listKamarUi,
                            listKamarActions = listKamarActions
                        )
                    }
                    else -> {
                        DataEmptyCard(
                            title = stringResource(R.string.list_room_empty),
                            image = painterResource(R.drawable.ic_empty_room),
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
                total = listKamarUiState.listKamarUi.size,
                create = { listKamarActions(ListKamarActions.NavigateAddKamar) },
                isNeedCreateButton = true,
                titleButton = stringResource(R.string.create_room),
                titleTotal = stringResource(R.string.room)
            )
        }
    }
}

@Composable
private fun ListKamarLoading() {
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
private fun ListKamarContent(
    listKamarUi: List<ListKamarUi>,
    listKamarActions: (ListKamarActions) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(listKamarUi){ kamar ->
            ItemKamarCard(
                listKamarUi = kamar,
                onClick = {
                    listKamarActions(ListKamarActions.NavigateToDetailKamar(kamar.idKamar))
                }
            )
        }
    }
}