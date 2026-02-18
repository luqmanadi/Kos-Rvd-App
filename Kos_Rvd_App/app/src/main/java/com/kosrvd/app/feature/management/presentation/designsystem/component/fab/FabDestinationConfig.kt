package com.kosrvd.app.feature.management.presentation.designsystem.component.fab

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kosrvd.app.core.domain.utils.Role
import com.kosrvd.app.core.navigation.NavigationScreen
import com.kosrvd.app.feature.management.presentation.ui.MainScreenActions

sealed interface FabDestinationConfig {
    val route: NavigationScreen
    val requiredRole: Role

    @Composable
    fun FabContent(modifier: Modifier = Modifier, mainActions: (MainScreenActions) -> Unit)

    data class BuatTagihanAdmin(
        override val route: NavigationScreen = NavigationScreen.ListTagihanScreen,
        override val requiredRole: Role = Role.ADMIN
    ): FabDestinationConfig{
        @Composable
        override fun FabContent(
            modifier: Modifier,
            mainActions: (MainScreenActions) -> Unit
        ) {
            SmallFloatingActionButton(
                modifier = modifier,
                onClick = { mainActions(MainScreenActions.NavigateToCreateTagihan) },
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Buat Tagihan",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }

    data class BuatKeluhanPenghuni(
        override val route: NavigationScreen = NavigationScreen.ListKeluhanScreen,
        override val requiredRole: Role = Role.PENGHUNI
    ): FabDestinationConfig{
        @Composable
        override fun FabContent(
            modifier: Modifier,
            mainActions: (MainScreenActions) -> Unit
        ) {
            SmallFloatingActionButton(
                modifier = modifier,
                onClick = { mainActions(MainScreenActions.NavigateToCreateKeluhan) },
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Buat Keluhan",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}