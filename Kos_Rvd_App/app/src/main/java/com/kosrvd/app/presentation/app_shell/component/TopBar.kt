package com.kosrvd.app.presentation.app_shell.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kosrvd.app.presentation.app_shell.MainScreenActions
import com.kosrvd.app.presentation.app_shell.MainScreenUiState

@Composable
fun TopBar(
    mainNavController: NavHostController,
    mainScreenUiState: MainScreenUiState,
    mainActions: (MainScreenActions) -> Unit
) {
    val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val allTopBarConfigs = remember {
        listOf(
            TopBarDestinationConfig.Dashboard(),
            TopBarDestinationConfig.Tagihan(),
            TopBarDestinationConfig.Keluhan(),
            TopBarDestinationConfig.Profile()
        )
    }

    val topBarConfig = allTopBarConfigs.firstOrNull { config->
        currentDestination?.hierarchy?.any { it.hasRoute(config.route::class) } == true
    }

    topBarConfig?.Content(
        mainScreenUiState = mainScreenUiState,
        mainActions = mainActions
    )
}