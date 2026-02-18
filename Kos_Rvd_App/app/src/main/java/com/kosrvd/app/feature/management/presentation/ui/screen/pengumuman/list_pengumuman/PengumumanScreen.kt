package com.kosrvd.app.feature.management.presentation.ui.screen.pengumuman.list_pengumuman

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.domain.utils.CustomToastHostState
import com.kosrvd.app.core.domain.utils.Role
import com.kosrvd.app.core.presentation.designsystem.component.appbar.TopBarLeftTitle
import com.kosrvd.app.core.presentation.designsystem.component.card.DataEmptyCard
import com.kosrvd.app.core.presentation.designsystem.component.card.ErrorCard
import com.kosrvd.app.core.presentation.designsystem.component.dialog.GeneralDialogConfirmationDanger
import com.kosrvd.app.core.presentation.designsystem.component.text.CustomToastHost
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme
import com.kosrvd.app.core.presentation.utils.shimmerEffect
import com.kosrvd.app.feature.management.presentation.ui.models.PengumumanUi
import com.kosrvd.app.feature.management.presentation.ui.screen.pengumuman.component.ItemPengumumanCard

@Composable
fun PengumumanScreen(
    pengumumamUiState: PengumumanUiState,
    pengumumanActions: (PengumumanActions) -> Unit,
    customToastHostState: CustomToastHostState,
    colorShowBanner: Color
) {
    Scaffold(
        topBar = {
            TopBarLeftTitle(
                title = stringResource(R.string.announcement),
                onBackClick = {pengumumanActions(PengumumanActions.NavigateBack)}
            )
        },
        floatingActionButtonPosition = FabPosition.EndOverlay,
        floatingActionButton = {
            if (pengumumamUiState.role == Role.ADMIN){
                SmallFloatingActionButton(
                    modifier = Modifier.padding(end = 8.dp, bottom = 32.dp),
                    onClick = { pengumumanActions(PengumumanActions.NavigateCreatePengumuman) },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Buat Pengumuman",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        },
    ) { innerPadding ->
        when{
            pengumumamUiState.isLoading -> {
                LoadingPengumuman(modifier = Modifier.padding(innerPadding))
            }
            pengumumamUiState.loadError != null -> {
                ErrorCard(
                    modifier = Modifier.padding(innerPadding),
                    message = pengumumamUiState.loadError,
                    onRetry = { pengumumanActions(PengumumanActions.TryAgain) }
                )
            }
            pengumumamUiState.listPengumuman.isNotEmpty() -> {
                PengumumanContent(
                    listPengumuman = pengumumamUiState.listPengumuman,
                    modifier = Modifier.padding(innerPadding),
                    pengumumanActions = pengumumanActions,
                    pengumumanUiState = pengumumamUiState,
                    customToastHostState = customToastHostState,
                    colorShowBanner = colorShowBanner
                )
            }
            else -> {
                DataEmptyCard(
                    modifier = Modifier.padding(innerPadding),
                    title = stringResource(R.string.no_data_announcement),
                    image = painterResource(R.drawable.ic_empty_data_pengumuman)
                )
            }
        }
    }
}

@Composable
private fun PengumumanContent(
    pengumumanUiState: PengumumanUiState,
    pengumumanActions: (PengumumanActions) -> Unit,
    listPengumuman: List<PengumumanUi>,
    modifier: Modifier,
    customToastHostState: CustomToastHostState,
    colorShowBanner: Color
) {
    Box(
        modifier = modifier.fillMaxSize()
    ){
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            items(listPengumuman){data ->
                ItemPengumumanCard(
                    modifier = Modifier.fillMaxWidth(),
                    dataPengumumanUi = data,
                    onDeleted = {
                        pengumumanActions(PengumumanActions.ShowDeleteDialog(data.idPengumuman))
                    },
                    role = pengumumanUiState.role
                )
            }
        }

        CustomToastHost(
            hostState = customToastHostState,
            color = colorShowBanner,
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

    if (pengumumanUiState.isDeleteDialogVisible) {
        GeneralDialogConfirmationDanger(
            onConfirm = {
                pengumumanActions(PengumumanActions.DeletePengumuman)
            },
            onDismiss = {
                pengumumanActions(PengumumanActions.DismissDeleteDialog)
            },
            title = stringResource(R.string.title_dialog_confirmation_delete_announcement),
            description = stringResource(R.string.description_dialog_confirmation_delete_announcement),
            isLoadingButton = pengumumanUiState.buttonLoading,
            buttonCancelEnabled = pengumumanUiState.buttonCancelEnabled
        )
    }
}

@Composable
private fun LoadingPengumuman(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(5){
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .shimmerEffect()
            )
        }
    }
}

@Preview
@Composable
private fun PengumumanScreenPreview() {
    KosRvdAppTheme {
        PengumumanScreen(
            pengumumamUiState = PengumumanUiState(
                isLoading = false,
                loadError = "Error Ngab",
                listPengumuman = emptyList()
            ),
            pengumumanActions = {},
            customToastHostState = CustomToastHostState(),
            colorShowBanner = MaterialTheme.colorScheme.primary
        )
    }
}