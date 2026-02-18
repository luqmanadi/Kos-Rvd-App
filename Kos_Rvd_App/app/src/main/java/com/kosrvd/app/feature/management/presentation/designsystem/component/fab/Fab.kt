package com.kosrvd.app.feature.management.presentation.designsystem.component.fab

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kosrvd.app.feature.management.presentation.ui.MainScreenActions
import com.kosrvd.app.feature.management.presentation.ui.MainScreenUiState

@Composable
fun Fab(
    mainNavController: NavHostController,
    mainScreenActions: (MainScreenActions) -> Unit,
    mainScreenUiState: MainScreenUiState
) {
    val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val allFabConfigs = remember {
        listOf(
            FabDestinationConfig.BuatTagihanAdmin(),
            FabDestinationConfig.BuatKeluhanPenghuni()
        )
    }
    val userRole = mainScreenUiState.userRole

    val fabConfig = allFabConfigs.firstOrNull { config ->
        currentDestination?.hierarchy?.any { it.hasRoute(config.route::class) } == true && userRole == config.requiredRole
    }

    fabConfig?.FabContent(
        mainActions = mainScreenActions
    )
}