package com.kosrvd.app.core.presentation.designsystem.component.bottomnav

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kosrvd.app.presentation.navigation.NavigationScreen
import com.kosrvd.app.presentation.app_shell.MainScreenUiState

@Composable
fun BottomBar(
    mainNavController: NavHostController,
    mainScreenUiState: MainScreenUiState
) {
    val screens = listOf(
        BottomNavScreen.Dashboard,
        BottomNavScreen.Tagihan,
        BottomNavScreen.Keluhan,
        BottomNavScreen.Profile
    )
    val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = mainNavController,
                isHasNotification = mainScreenUiState.mainScreenUi.numberOfUnreadNotification > 0
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    isHasNotification: Boolean = false,
    screen: BottomNavScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {

    val isDashboardItem = screen.route is NavigationScreen.DashboardScreen
    NavigationBarItem(
        label = {
            Text(text = stringResource(screen.title))
        },
        icon = {
            BadgedBox(
                badge = {
                    if (isDashboardItem && isHasNotification){
                        Badge()
                    }
                }
            ) {
                Icon(
                    painter = painterResource(
                        id = if (currentDestination?.hierarchy?.any{it.hasRoute(screen.route::class) } == true) screen.selectedIcon else screen.unselectedIcon
                    ),
                    contentDescription = stringResource(id = screen.title)
                )
            }
        },
        selected = currentDestination?.hierarchy?.any{it.hasRoute(screen.route::class) } == true,
        onClick = {
            navController.navigate(screen.route) { // Navigasi pakai objek type-safe
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    )
}