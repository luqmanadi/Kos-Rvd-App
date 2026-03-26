package com.kosrvd.app.feature.complaint.presentation.list_keluhan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.presentation.designsystem.component.card.DataEmptyCard
import com.kosrvd.app.core.presentation.designsystem.component.card.ErrorCard
import com.kosrvd.app.core.presentation.utils.shimmerEffect
import com.kosrvd.app.feature.complaint.presentation.models.KeluhanGroupUi
import com.kosrvd.app.feature.complaint.presentation.component.ItemKeluhanCard

@Composable
fun ListKeluhanScreen(
    listKeluhanAction: (ListKeluhanActions) -> Unit,
    listKeluhanUiState: ListKeluhanUiState
) {
    val listLabel = listOf(
        stringResource(R.string.confirmation),
        stringResource(R.string.process),
        stringResource(R.string.finish)
    )

    val pagerState = rememberPagerState(
        initialPage = listKeluhanUiState.selectedTab
    ) { listLabel.size }

    // Sync pager dengan viewmodel
    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            listKeluhanAction(ListKeluhanActions.ChooseTab(pagerState.currentPage))
        }
    }

    LaunchedEffect(listKeluhanUiState.selectedTab) {
        if (pagerState.currentPage != listKeluhanUiState.selectedTab) {
            pagerState.animateScrollToPage(listKeluhanUiState.selectedTab)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        PrimaryTabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = Modifier.fillMaxWidth()
        ) {
            listLabel.forEachIndexed { index, label ->
                Tab(
                    selected = index == pagerState.currentPage,
                    onClick = {
                        listKeluhanAction(ListKeluhanActions.ChooseTab(index))
                    },
                    text = {
                        Text(
                            text = label,
                            color = if (index == pagerState.currentPage)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = if (index == pagerState.currentPage) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            ComplaintContent(
                page = page,
                listKeluhanUiState = listKeluhanUiState,
                listKeluhanAction = listKeluhanAction
            )
        }
    }
}

@Composable
private fun ComplaintContent(
    page: Int,
    listKeluhanUiState: ListKeluhanUiState,
    listKeluhanAction: (ListKeluhanActions) -> Unit
) {

    // Get specific tab state based on page index
    val tabState = when (page) {
        0 -> listKeluhanUiState.listKeluhanMenungguKonfirmasi
        1 -> listKeluhanUiState.listKeluhanSedangDiproses
        2 -> listKeluhanUiState.listKeluhanSelesai
        else -> TabKeluhanUi()
    }

    TabContent(
        tabState = tabState,
        selectedTab = page,
        listKeluhanAction = listKeluhanAction
    )
}

@Composable
private fun TabContent(
    tabState: TabKeluhanUi,
    selectedTab: Int,
    listKeluhanAction: (ListKeluhanActions) -> Unit
) {
    when {
        tabState.isLoading -> {
            LoadingListKeluhan()
        }

        tabState.loadError != null -> {
            ErrorCard(
                message = tabState.loadError,
                onRetry = { listKeluhanAction(ListKeluhanActions.TryAgain) }
            )
        }

        tabState.listKeluhan.isNotEmpty() -> {
            KeluhanContent(
                listKeluhan = tabState.listKeluhan,
                onKeluhanClick = { idTagihan ->
                    listKeluhanAction(ListKeluhanActions.NavigateToDetailKeluhan(idTagihan))
                }
            )
        }

        else -> {
            DataEmptyCard(
                image = when (selectedTab) {
                    0 -> painterResource(R.drawable.ic_complaint_waiting_confirmation)
                    1 -> painterResource(R.drawable.ic_complaint_process)
                    2 -> painterResource(R.drawable.ic_complaint_completion)
                    else -> painterResource(R.drawable.ic_complaint_waiting_confirmation)
                } ,
                title = when (selectedTab) {
                    0 -> stringResource(R.string.no_report_complain)
                    1 -> stringResource(R.string.no_report_processed)
                    2 -> stringResource(R.string.no_report_finished)
                    else -> stringResource(R.string.no_report_complain)
                },
            )
        }
    }
}

@Composable
private fun KeluhanContent(
    listKeluhan: List<KeluhanGroupUi>,
    onKeluhanClick: (String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        listKeluhan.forEach { (dateTitleGroup, _, items) ->
            item {
                Text(
                    text = dateTitleGroup,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            items(items) { keluhanUi ->
                ItemKeluhanCard(
                    keluhanUi = keluhanUi,
                    onClick = { onKeluhanClick(keluhanUi.idKeluhan) }
                )
            }
        }
    }
}

@Composable
private fun LoadingListKeluhan(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(3){
            Column{
                Box(
                    Modifier
                        .size(130.dp, 25.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .shimmerEffect()
                )
                Spacer(Modifier.height(15.dp))
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .shimmerEffect()
                )
                Spacer(Modifier.height(15.dp))
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .shimmerEffect()
                )
            }
        }
    }
}