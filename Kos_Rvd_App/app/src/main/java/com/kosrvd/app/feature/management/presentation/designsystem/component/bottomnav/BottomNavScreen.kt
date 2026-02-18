package com.kosrvd.app.feature.management.presentation.designsystem.component.bottomnav

import com.kosrvd.app.R
import com.kosrvd.app.core.navigation.NavigationScreen
import kotlinx.serialization.Serializable

@Serializable
sealed class BottomNavScreen (
    val route: NavigationScreen,
    val title: Int,
    val selectedIcon: Int,
    val unselectedIcon: Int
){
    @Serializable
    data object Dashboard: BottomNavScreen(
        route = NavigationScreen.DashboardScreen,
        title = R.string.dashboard,
        selectedIcon = R.drawable.ic_fill_dashboard,
        unselectedIcon = R.drawable.ic_no_fill_dashboard
    )

    @Serializable
    data object Tagihan: BottomNavScreen(
        route = NavigationScreen.ListTagihanScreen,
        title = R.string.bill,
        selectedIcon = R.drawable.ic_fill_tagihan,
        unselectedIcon = R.drawable.ic_no_fill_tagihan
    )

    @Serializable
    data object Keluhan: BottomNavScreen(
        route = NavigationScreen.ListKeluhanScreen,
        title = R.string.complain,
        selectedIcon = R.drawable.ic_fill_keluhan,
        unselectedIcon = R.drawable.ic_no_fill_keluhan
    )

    @Serializable
    object Profile: BottomNavScreen(
        route = NavigationScreen.ProfileScreen,
        title = R.string.profile,
        selectedIcon = R.drawable.ic_fill_profile,
        unselectedIcon = R.drawable.ic_no_fill_profile
    )
}