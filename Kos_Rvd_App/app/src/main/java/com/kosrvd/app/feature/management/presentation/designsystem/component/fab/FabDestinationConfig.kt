package com.kosrvd.app.feature.management.presentation.designsystem.component.fab

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.material3.ToggleFloatingActionButtonDefaults.animateIcon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.kosrvd.app.core.domain.utils.Role
import com.kosrvd.app.core.navigation.NavigationScreen
import com.kosrvd.app.feature.management.presentation.ui.MainScreenActions

sealed interface FabDestinationConfig {
    val route: NavigationScreen
    val requiredRole: Role

    @Composable
    fun FabContent(modifier: Modifier = Modifier, mainActions: (MainScreenActions) -> Unit, listState: LazyListState = rememberLazyListState())

    data class BuatTagihanAdmin(
        override val route: NavigationScreen = NavigationScreen.ListTagihanScreen,
        override val requiredRole: Role = Role.ADMIN,

    ): FabDestinationConfig{
        @OptIn(ExperimentalMaterial3ExpressiveApi::class)
        @Composable
        override fun FabContent(
            modifier: Modifier,
            mainActions: (MainScreenActions) -> Unit,
            listState: LazyListState
        ) {
            var expanded by rememberSaveable  { mutableStateOf(false) }
            val items = listOf(
                FabItem(
                    text = "Pengaturan",
                    icon = Icons.Filled.Settings,
                    onClick = {
                        mainActions(MainScreenActions.NavigateToPengaturanScreen)
                        expanded = false
                    }
                ),
                FabItem(
                    text = "Buat Tagihan",
                    icon = Icons.Filled.PostAdd,
                    onClick = {
                        mainActions(MainScreenActions.NavigateToCreateTagihan)
                        expanded = false
                    }
                )
            )

            BackHandler(expanded) { expanded = false }

            FloatingActionButtonMenu(
                expanded = expanded,
                button = {
                    ToggleFloatingActionButton(
                        checked = expanded,
                        onCheckedChange = { expanded = it }
                    ){
                        val imageVector by remember {
                            derivedStateOf {
                                if (checkedProgress > 0.5f) Icons.Filled.Close else Icons.Filled.Add
                            }
                        }
                        Icon(
                            painter = rememberVectorPainter(imageVector),
                            contentDescription = null,
                            modifier = Modifier.animateIcon({ checkedProgress })
                        )
                    }
                },
                modifier = modifier
            ) {
                items.forEach { item ->
                    FloatingActionButtonMenuItem(
                        onClick = item.onClick,
                        text = {
                            Text(text = item.text)
                        },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.text
                            )
                        }
                    )
                }
            }
        }
    }

    data class BuatKeluhanPenghuni(
        override val route: NavigationScreen = NavigationScreen.ListKeluhanScreen,
        override val requiredRole: Role = Role.PENGHUNI,
    ): FabDestinationConfig{
        @Composable
        override fun FabContent(
            modifier: Modifier,
            mainActions: (MainScreenActions) -> Unit,
            listState: LazyListState
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

data class FabItem(
    val text: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)