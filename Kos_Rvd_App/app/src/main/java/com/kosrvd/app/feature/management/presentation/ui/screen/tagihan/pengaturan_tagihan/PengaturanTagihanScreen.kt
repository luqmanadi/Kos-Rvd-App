package com.kosrvd.app.feature.management.presentation.ui.screen.tagihan.pengaturan_tagihan

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.domain.utils.CustomToastHostState
import com.kosrvd.app.core.presentation.designsystem.component.appbar.TopBarCenterTitle
import com.kosrvd.app.core.presentation.designsystem.component.card.ErrorCard
import com.kosrvd.app.core.presentation.designsystem.component.text.CustomToastHost
import com.kosrvd.app.core.presentation.utils.shimmerEffect
import com.kosrvd.app.feature.management.presentation.ui.screen.tagihan.component.PengaturanTagihanCard

@Composable
fun PengaturanTagihanScreen(
    pengaturanTagihanUiState: PengaturanTagihanUiState,
    pengaturanTagihanActions: (PengaturanTagihanActions) -> Unit,
    customToastHostState: CustomToastHostState,
    colorToast: Color
) {
    Scaffold(
        topBar = {
            TopBarCenterTitle(
                modifier = Modifier.height(120.dp).padding(top = 10.dp),
                title = stringResource(R.string.pengaturan_tagihan),
                onBackClick = { pengaturanTagihanActions(PengaturanTagihanActions.NavigateBack) },
                isNeedBackIcon = true,
                fontWeight = FontWeight.Bold,
                containerColors = MaterialTheme.colorScheme.surfaceContainer
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ){
            when {
                pengaturanTagihanUiState.isLoading -> {
                    PengaturanTagihanLoading()
                }
                pengaturanTagihanUiState.errorMessage != null -> {
                    ErrorCard(
                        message = pengaturanTagihanUiState.errorMessage,
                        onRetry = { pengaturanTagihanActions(PengaturanTagihanActions.TryAgain) },
                    )
                }
                else -> {
                    PengaturanTagihanContent(
                        pengaturanTagihanUiState = pengaturanTagihanUiState,
                        pengaturanTagihanActions = pengaturanTagihanActions
                    )
                }
            }

            CustomToastHost(
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
    }
}

@Composable
private fun PengaturanTagihanContent(
    pengaturanTagihanUiState: PengaturanTagihanUiState,
    pengaturanTagihanActions: (PengaturanTagihanActions) -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        PengaturanTagihanCard(
            title = stringResource(R.string.title_pengaturan_tagihan_1),
            description = stringResource(R.string.description_pengaturan_tagihan_1),
            isActive = pengaturanTagihanUiState.useAutoReminder,
            enabled = pengaturanTagihanUiState.isSwitchUseAutoReminderEnabled,
            onClick = { state ->
                pengaturanTagihanActions(PengaturanTagihanActions.UpdateSwitchUseAutoReminder(state))
            }
        )
        PengaturanTagihanCard(
            title = stringResource(R.string.title_pengaturan_tagihan_2),
            description = stringResource(R.string.description_pengaturan_tagihan_2),
            isActive = pengaturanTagihanUiState.useGenerateOtomatis,
            enabled = pengaturanTagihanUiState.isSwitchUseGenerateOtomatisEnabled,
            onClick = { state ->
                pengaturanTagihanActions(PengaturanTagihanActions.UpdateSwitchUseGenerateOtomatis(state))
            }
        )
    }
}

@Composable
private fun PengaturanTagihanLoading() {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(40.dp)
                .clip(RoundedCornerShape(20.dp))
                .shimmerEffect()
        )
        Box(
            Modifier
                .fillMaxWidth()
                .height(40.dp)
                .clip(RoundedCornerShape(20.dp))
                .shimmerEffect()
        )
    }
}