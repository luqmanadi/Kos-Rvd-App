package com.kosrvd.app.feature.resident.presentation.list_penghuni

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.domain.utils.Role
import com.kosrvd.app.core.presentation.designsystem.component.appbar.TopBarLeftTitle
import com.kosrvd.app.core.presentation.designsystem.component.card.DataEmptyCard
import com.kosrvd.app.core.presentation.designsystem.component.card.ErrorCard
import com.kosrvd.app.core.presentation.utils.shimmerEffect
import com.kosrvd.app.feature.resident.presentation.component.ItemPenghuniCard
import com.kosrvd.app.feature.resident.presentation.models.ListResidentUi

@Composable
fun ListPenghuniScreen(
    listPenghuniUiState: ListPenghuniUiState,
    listPenghuniAction: (ListPenghuniActions) -> Unit
) {
    Scaffold(
        topBar = {
            TopBarLeftTitle(
                title = stringResource(R.string.list_residents),
                onBackClick = { listPenghuniAction(ListPenghuniActions.NavigateBack) }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        when{
            listPenghuniUiState.isLoading -> {
                LoadListPenghuni(
                    modifier = Modifier.padding(innerPadding)
                )
            }
            listPenghuniUiState.loadError != null -> {
                ErrorCard(
                    modifier = Modifier.padding(innerPadding),
                    message = listPenghuniUiState.loadError,
                    onRetry = { listPenghuniAction(ListPenghuniActions.TryAgain) }
                )
            }
            else -> {
                MainContent(
                    modifier = Modifier.padding(innerPadding),
                    listPenghuni = listPenghuniUiState.listPenghuni,
                    listPenghuniAction = listPenghuniAction,
                    role = listPenghuniUiState.role
                )
            }
        }
    }
}

@Composable
fun LoadListPenghuni(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(10){
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(105.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .shimmerEffect()
            )
        }
    }
}

@Composable
private fun MainContent(
    modifier: Modifier = Modifier,
    listPenghuni : List<ListResidentUi>,
    listPenghuniAction: (ListPenghuniActions) -> Unit,
    role: Role
){
    if (listPenghuni.isEmpty()){
        DataEmptyCard(
            modifier = modifier,
            title = if (role == Role.ADMIN) stringResource(R.string.no_data_residents_kos) else stringResource(R.string.no_data_resident_except_you),
            image = painterResource(R.drawable.ic_empty_data)
        )
    }else{
        ListContent(
            modifier = modifier,
            listPenghuni = listPenghuni,
            listPenghuniAction = listPenghuniAction
        )
    }
}

@Composable
private fun ListContent(
    modifier: Modifier = Modifier,
    listPenghuni : List<ListResidentUi>,
    listPenghuniAction: (ListPenghuniActions) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(listPenghuni){ penghuni ->
            ItemPenghuniCard(
                listResidentUi = penghuni,
                onClick = {
                    listPenghuniAction(ListPenghuniActions.NavigateToDetailPenghuni(penghuni.idPenghuni))
                }
            )
        }
    }
}