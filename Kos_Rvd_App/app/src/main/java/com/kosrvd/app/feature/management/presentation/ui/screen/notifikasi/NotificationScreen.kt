package com.kosrvd.app.feature.management.presentation.ui.screen.notifikasi

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.presentation.designsystem.component.appbar.TopBarLeftTitle
import com.kosrvd.app.core.presentation.designsystem.component.card.DataEmptyCard
import com.kosrvd.app.core.presentation.designsystem.component.card.ErrorCard
import com.kosrvd.app.core.presentation.utils.shimmerEffect
import com.kosrvd.app.feature.management.presentation.ui.models.NotificationUi
import com.kosrvd.app.feature.management.presentation.ui.screen.notifikasi.component.ItemNotificationCard

@Composable
fun NotificationScreen(
    notificationUiState: NotificationUiState,
    notificationActions: (NotificationActions) -> Unit,
) {
    Scaffold(
        topBar = {
            TopBarLeftTitle(
                title = stringResource(R.string.notification),
                onBackClick = { notificationActions(NotificationActions.NavigateBack) }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        when{
            notificationUiState.isLoading -> {
                LoadingNotificationScreen(modifier = Modifier.padding(innerPadding))
            }
            notificationUiState.loadError != null -> {
                ErrorCard(
                    modifier = Modifier.padding(innerPadding),
                    message = notificationUiState.loadError,
                    onRetry = { notificationActions(NotificationActions.TryAgain) }
                )
            }
            notificationUiState.listNotification.isNotEmpty() -> {
                NotificationMainContent(
                    modifier = Modifier.padding(innerPadding),
                    listNotification = notificationUiState.listNotification,
                    onItemClick = { notification ->
                        notificationActions(NotificationActions.NavigateToDetailNotification(notification))
                    }
                )
            }
            else -> {
                DataEmptyCard(
                    modifier = Modifier.padding(innerPadding),
                    title = stringResource(R.string.no_data_notif),
                    image = painterResource(R.drawable.ic_empty_data_notification)
                )
            }
        }
    }
}

@Composable
private fun NotificationMainContent(
    modifier: Modifier,
    listNotification: List<NotificationUi>,
    onItemClick: (notification: NotificationUi) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(listNotification){ dataNotif ->
            ItemNotificationCard(
                modifier = Modifier,
                dataNotificationUi = dataNotif,
                onItemClick = {
                    onItemClick(dataNotif)
                }
            )
        }
    }
}

@Composable
private fun LoadingNotificationScreen(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
    ) {
        items(10){
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(102.dp)
                .shimmerEffect())
        }
    }
}