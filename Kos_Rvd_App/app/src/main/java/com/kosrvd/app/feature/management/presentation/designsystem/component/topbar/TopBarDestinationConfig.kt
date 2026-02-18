package com.kosrvd.app.feature.management.presentation.designsystem.component.topbar

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.kosrvd.app.R
import com.kosrvd.app.core.navigation.NavigationScreen
import com.kosrvd.app.core.presentation.designsystem.component.appbar.TopBarCenterTitle
import com.kosrvd.app.core.presentation.designsystem.component.appbar.TopBarLogoAction
import com.kosrvd.app.core.presentation.designsystem.component.appbar.TopBarLogoActionShimmer
import com.kosrvd.app.core.presentation.designsystem.component.appbar.TopBarLogoLeftTextCenter
import com.kosrvd.app.feature.management.presentation.designsystem.component.bottomnav.BottomNavScreen
import com.kosrvd.app.feature.management.presentation.ui.MainScreenActions
import com.kosrvd.app.feature.management.presentation.ui.MainScreenUiState

sealed interface TopBarDestinationConfig {
    val route: NavigationScreen

    @Composable
    fun Content(modifier: Modifier = Modifier, mainScreenUiState: MainScreenUiState, mainActions: (MainScreenActions) -> Unit)

    data class Dashboard(
        override val route: NavigationScreen = BottomNavScreen.Dashboard.route
    ): TopBarDestinationConfig {
        @Composable
        override fun Content(
            modifier: Modifier,
            mainScreenUiState: MainScreenUiState,
            mainActions: (MainScreenActions) -> Unit
        ) {
            when {
                mainScreenUiState.isLoadingTopBarDashboard -> {
                    TopBarLogoActionShimmer()
                }

                else -> {
                    TopBarLogoAction(
                        name = mainScreenUiState.mainScreenUi.name,
                        navigateTo = { mainActions(MainScreenActions.NavigateToNotificationScreen) },
                        numberOfUnreadNotification = mainScreenUiState.mainScreenUi.numberOfUnreadNotification
                    )
                }
            }
        }
    }

    data class Tagihan(
        override val route: NavigationScreen = BottomNavScreen.Tagihan.route
    ): TopBarDestinationConfig {
        @Composable
        override fun Content(
            modifier: Modifier,
            mainScreenUiState: MainScreenUiState,
            mainActions: (MainScreenActions) -> Unit
        ) {
            TopBarLogoLeftTextCenter(
                title = stringResource(R.string.bill_kos),
                fontWeight = FontWeight.Bold,
                containerColors = MaterialTheme.colorScheme.surfaceContainer
            )
        }
    }

    data class Keluhan(
        override val route: NavigationScreen = BottomNavScreen.Keluhan.route
    ): TopBarDestinationConfig{
        @Composable
        override fun Content(
            modifier: Modifier,
            mainScreenUiState: MainScreenUiState,
            mainActions: (MainScreenActions) -> Unit
        ) {
            TopBarLogoLeftTextCenter(
                title = stringResource(R.string.complain_kos),
                fontWeight = FontWeight.Bold,
                containerColors = MaterialTheme.colorScheme.surfaceContainer
            )
        }
    }

    data class Profile(
        override val route: NavigationScreen = BottomNavScreen.Profile.route
    ): TopBarDestinationConfig{
        @Composable
        override fun Content(
            modifier: Modifier,
            mainScreenUiState: MainScreenUiState,
            mainActions: (MainScreenActions) -> Unit
        ) {
            TopBarCenterTitle(
                title = stringResource(R.string.profile),
                isNeedBackIcon = false,
                fontWeight = FontWeight.Bold
            )
        }
    }


}