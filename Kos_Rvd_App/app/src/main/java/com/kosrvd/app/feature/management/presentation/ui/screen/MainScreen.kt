package com.kosrvd.app.feature.management.presentation.ui.screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kosrvd.app.DeepLinkDestination
import com.kosrvd.app.MainViewModel
import com.kosrvd.app.core.navigation.NavigationGraph
import com.kosrvd.app.core.navigation.NavigationScreen
import com.kosrvd.app.core.navigation.graph.MainGraph
import com.kosrvd.app.core.presentation.utils.ObserveAsEvents
import com.kosrvd.app.feature.management.presentation.designsystem.component.bottomnav.BottomBar
import com.kosrvd.app.feature.management.presentation.designsystem.component.fab.Fab
import com.kosrvd.app.feature.management.presentation.designsystem.component.snackbar.CustomSnackbarVisuals
import com.kosrvd.app.feature.management.presentation.designsystem.component.snackbar.SnackbarType
import com.kosrvd.app.feature.management.presentation.designsystem.component.topbar.TopBar
import com.kosrvd.app.feature.management.presentation.ui.MainScreenEvent
import com.kosrvd.app.feature.management.presentation.ui.MainScreenViewModel

@Composable
fun MainScreen(
    rootNavController: NavHostController,
    mainViewModel: MainViewModel,
    snackbarHostState: SnackbarHostState
) {
    val mainScreenViewModel = hiltViewModel<MainScreenViewModel>()
    val mainScreenUiState by mainScreenViewModel.state.collectAsStateWithLifecycle()

    val pendingDeepLink by mainViewModel.pendingDeepLink.collectAsStateWithLifecycle()

    val mainNavController = rememberNavController()

    ObserveAsEvents(mainScreenViewModel.events) { event ->
        when(event){
            is MainScreenEvent.NavigateToAuthGraph -> {
                rootNavController.navigate(NavigationGraph.AuthGraph){
                    popUpTo(NavigationGraph.MainGraph){
                        inclusive = true
                    }
                }
            }
            is MainScreenEvent.NavigateToNotificationScreen -> {
                rootNavController.navigate(NavigationScreen.NotificationScreen)
            }
            MainScreenEvent.NavigateToCreateKeluhan -> {
                rootNavController.navigate(NavigationScreen.BuatKeluhanScreen)
            }
            MainScreenEvent.NavigateToCreateTagihan -> {
                rootNavController.navigate(NavigationScreen.BuatTagihanScreen)
            }
        }
    }

    LaunchedEffect(pendingDeepLink) {
        pendingDeepLink?.let { destination ->
            when (destination) {
                is DeepLinkDestination.DetailTagihan -> {
                    // Reset ke Dashboard dulu biar back stack rapi (Opsional)
                    mainNavController.navigate(NavigationScreen.DashboardScreen) {
                        popUpTo(mainNavController.graph.findStartDestination().id) { saveState = true }
                    }
                    // Dorong ke Detail
                    rootNavController.navigate(NavigationScreen.DetailTagihan(destination.idTagihan))
                }
                is DeepLinkDestination.DetailKeluhan -> {
                    mainNavController.navigate(NavigationScreen.DashboardScreen) {
                        popUpTo(mainNavController.graph.findStartDestination().id) { saveState = true }
                    }
                    rootNavController.navigate(NavigationScreen.DetailKeluhanScreen(destination.idKeluhan))
                }
                DeepLinkDestination.Pengumuman -> {
                    rootNavController.navigate(NavigationGraph.PengumumanGraph)
                }
            }
            mainViewModel.onDeepLinkConsumed()
        }
    }

    Scaffold(
        bottomBar = {
            BottomBar(mainNavController, mainScreenUiState)
        },
        topBar = {
            TopBar(
                mainNavController = mainNavController,
                mainScreenUiState = mainScreenUiState,
                mainActions = mainScreenViewModel::onActions
            )
        },
        floatingActionButton = {
            Fab(
                mainNavController = mainNavController,
                mainScreenActions = mainScreenViewModel::onActions,
                mainScreenUiState = mainScreenUiState
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            ){ data ->
                val visuals = data.visuals as? CustomSnackbarVisuals
                val type = visuals?.type ?: SnackbarType.SUCCESS

                Snackbar(
                    shape = RoundedCornerShape(12.dp),
                    containerColor = type.containerColor,
                    contentColor = type.contentColor,
                ){
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = type.icon,
                            contentDescription = null
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = visuals?.message ?: "Info Error",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        MainGraph(
            modifier = Modifier.padding(innerPadding),
            mainNavController = mainNavController,
            rootNavController = rootNavController
        )
    }
}