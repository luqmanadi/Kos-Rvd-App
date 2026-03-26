package com.kosrvd.app.feature.billing.presentation.list_tagihan

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
import com.kosrvd.app.feature.billing.presentation.components.ItemTagihanCard
import com.kosrvd.app.feature.billing.presentation.models.TagihanGroupUi

@Composable
fun ListTagihanScreen(
    listTagihanAction: (ListTagihanActions) -> Unit,
    listTagihanUiState: ListTagihanUiState
) {
    val listLabel = listOf(
        stringResource(R.string.not_yet_paid_off),
        stringResource(R.string.waiting_for_verification),
        stringResource(R.string.paid_off)
    )

    val pagerState = rememberPagerState(
        initialPage = listTagihanUiState.selectedTab
    ) { listLabel.size }

    // Sync pager dengan viewmodel
    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            listTagihanAction(ListTagihanActions.ChooseTab(pagerState.currentPage))
        }
    }

    LaunchedEffect(listTagihanUiState.selectedTab) {
        if (pagerState.currentPage != listTagihanUiState.selectedTab) {
            pagerState.animateScrollToPage(listTagihanUiState.selectedTab)
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        PrimaryTabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth()
        ) {
            listLabel.forEachIndexed { index, label ->
                Tab(
                    selected = index == pagerState.currentPage,
                    onClick = {
                        listTagihanAction(ListTagihanActions.ChooseTab(index))
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
            BillContent(
                page = page,
                listTagihanUiState = listTagihanUiState,
                listTagihanAction = listTagihanAction
            )
        }
    }
}

@Composable
private fun BillContent(
    page: Int,
    listTagihanUiState: ListTagihanUiState,
    listTagihanAction: (ListTagihanActions) -> Unit
) {
    // Get specific tab state based on page index
    val tabState = when (page) {
        0 -> listTagihanUiState.listTagihanBelumLunasUi
        1 -> listTagihanUiState.listTagihanMenungguVerifikasiUi
        2 -> listTagihanUiState.listTagihanLunasUi
        else -> TabTagihanUi()
    }

    TabContent(
        tabState = tabState,
        selectedTab = page,
        listTagihanAction = listTagihanAction
    )
}

@Composable
private fun TabContent(
    tabState: TabTagihanUi,
    selectedTab: Int,
    listTagihanAction: (ListTagihanActions) -> Unit
) {
    when {
        tabState.isLoading -> {
            LoadingListTagihan()
        }

        tabState.loadError != null -> {
            ErrorCard(
                message = tabState.loadError,
                onRetry = { listTagihanAction(ListTagihanActions.TryAgain) }
            )
        }

        tabState.listTagihan.isNotEmpty() -> {
            TagihanContent(
                listTagihan = tabState.listTagihan,
                onTagihanClick = { idTagihan ->
                    listTagihanAction(ListTagihanActions.NavigateToDetailTagihan(idTagihan))
                }
            )
        }

        else -> {
            DataEmptyCard(
                title = when (selectedTab) {
                    0 -> stringResource(R.string.empty_bill_not_pay_off)
                    1 -> stringResource(R.string.empty_verified_bill)
                    2 -> stringResource(R.string.empty_paid_off_bill)
                    else -> stringResource(R.string.empty_bill_not_pay_off)
                },
                image = when (selectedTab) {
                    0 -> painterResource(R.drawable.ic_tagihan_belum_lunas_custom)
                    1 -> painterResource(R.drawable.ic_empty_wait_verification)
                    2 -> painterResource(R.drawable.ic_empty_data)
                    else -> painterResource(R.drawable.ic_no_internet)
                }
            )
        }
    }
}

@Composable
private fun TagihanContent(
    onTagihanClick: (String) -> Unit,
    listTagihan: List<TagihanGroupUi>
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        listTagihan.forEach { (dateTitleGroup, _, items) ->
            item {
                Text(
                    text = dateTitleGroup,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            items(items) { tagihanItem ->
                ItemTagihanCard(
                    tagihanUi = tagihanItem,
                    onClick = { onTagihanClick(tagihanItem.idTagihan) }
                )
            }
        }
    }
}

@Composable
private fun LoadingListTagihan(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(5) {
            Column {
                Box(
                    Modifier
                        .size(100.dp, 25.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .shimmerEffect()
                )
                Spacer(Modifier.height(20.dp))
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(20.dp))
                        .shimmerEffect()
                )
            }
        }
    }
}