package com.kosrvd.app.core.navigation.graph

import android.content.Intent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.domain.utils.rememberCustomToastHostState
import com.kosrvd.app.core.navigation.NavigationGraph
import com.kosrvd.app.core.navigation.NavigationScreen
import com.kosrvd.app.core.navigation.NavigationScreen.DetailTagihan
import com.kosrvd.app.core.presentation.utils.ObserveAsEvents
import com.kosrvd.app.feature.management.presentation.ui.screen.dashboard.DashboardEvent
import com.kosrvd.app.feature.management.presentation.ui.screen.dashboard.DashboardScreen
import com.kosrvd.app.feature.management.presentation.ui.screen.dashboard.DashboardViewModel
import com.kosrvd.app.feature.management.presentation.ui.screen.keluhan.list_keluhan.ListKeluhanEvent
import com.kosrvd.app.feature.management.presentation.ui.screen.keluhan.list_keluhan.ListKeluhanScreen
import com.kosrvd.app.feature.management.presentation.ui.screen.keluhan.list_keluhan.ListKeluhanViewModel
import com.kosrvd.app.feature.management.presentation.ui.screen.profile.main_page.ProfileEvent
import com.kosrvd.app.feature.management.presentation.ui.screen.profile.main_page.ProfileScreen
import com.kosrvd.app.feature.management.presentation.ui.screen.profile.main_page.ProfileViewModel
import com.kosrvd.app.feature.management.presentation.ui.screen.tagihan.list_tagihan.ListTagihanEvent
import com.kosrvd.app.feature.management.presentation.ui.screen.tagihan.list_tagihan.ListTagihanScreen
import com.kosrvd.app.feature.management.presentation.ui.screen.tagihan.list_tagihan.ListTagihanViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainGraph(
    modifier: Modifier = Modifier,
    mainNavController: NavHostController,
    rootNavController: NavHostController
) {
    val context = LocalContext.current
    NavHost(
        navController = mainNavController,
        startDestination = NavigationScreen.DashboardScreen,
        modifier = modifier
    ){
        composable<NavigationScreen.DashboardScreen> {
            val dashboardViewModel = hiltViewModel<DashboardViewModel>()
            val dashboardUiState by dashboardViewModel.state.collectAsStateWithLifecycle()
            ObserveAsEvents(dashboardViewModel.events) { events ->
                when (events) {
                    is DashboardEvent.NavigateToListResident -> {
                        rootNavController.navigate(NavigationGraph.PenghuniGraph)
                    }
                    is DashboardEvent.NavigateToAnnouncement -> {
                        rootNavController.navigate(NavigationGraph.PengumumanGraph)
                    }
                    is DashboardEvent.NavigateToWhatsAppAdmin -> {
                        val url = "https://wa.me/${Constant.WHATSAPP_ADMIN}"
                        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                        context.startActivity(intent)
                    }
                    is DashboardEvent.NavigateToMapsKosRVD -> {
                        val url = Constant.MAPS_KOS_RVD
                        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                        context.startActivity(intent)
                    }
                    is DashboardEvent.NavigateToDetailBill -> {
                        rootNavController.navigate(DetailTagihan(events.idBill))
                    }
                    is DashboardEvent.NavigateToAkunPengguna -> {
                        rootNavController.navigate(NavigationGraph.AkunPenggunaGraph)
                    }
                    is DashboardEvent.NavigateToKamar -> {
                        rootNavController.navigate(NavigationGraph.KamarGraph)
                    }
                    is DashboardEvent.NavigateToPenyewaan -> {
                        rootNavController.navigate(NavigationGraph.PenyewaanGraph)
                    }
                    is DashboardEvent.NavigateToRiwayatParkirMobil -> {
                        rootNavController.navigate(NavigationGraph.ParkiranHarianMobilGraph)
                    }
                    is DashboardEvent.NavigateToZonaParkir -> {
                        rootNavController.navigate(NavigationGraph.ZonaParkirGraph)
                    }
                }
            }
            DashboardScreen(
                dashboardUiState = dashboardUiState,
                dashboardActions = dashboardViewModel::onActions
            )
        }

        composable<NavigationScreen.ListTagihanScreen> {
            val listTagihanViewModel = hiltViewModel<ListTagihanViewModel>()
            val listTagihanUiState by listTagihanViewModel.state.collectAsStateWithLifecycle()

            ObserveAsEvents(listTagihanViewModel.events) { event ->
                when(event){
                    is ListTagihanEvent.NavigateToDetailTagihan -> {
                        rootNavController.navigate(DetailTagihan(event.idTagihan))
                    }
                }
            }

            ListTagihanScreen(
                listTagihanUiState = listTagihanUiState,
                listTagihanAction = listTagihanViewModel::onActions
            )

        }

        composable<NavigationScreen.ProfileScreen> {
            val profileViewModel = hiltViewModel<ProfileViewModel>()
            val profileUiState by profileViewModel.state.collectAsStateWithLifecycle()

            val scope = rememberCoroutineScope()
            val customToastHostState = rememberCustomToastHostState()

            ObserveAsEvents(profileViewModel.events) { events ->
                when (events) {
                    is ProfileEvent.NavigateToDetailAkun -> {
                        rootNavController.navigate(NavigationScreen.DetailProfileScreen)
                    }
                    is ProfileEvent.NavigateToResetPassword -> {
                        rootNavController.navigate(NavigationScreen.ResetPasswordScreen)
                    }
                    is ProfileEvent.ShowErrorBanner -> {
                        scope.launch {
                            customToastHostState.showToast(events.message)
                        }
                    }
                }
            }

            ProfileScreen(
                profileUiState = profileUiState,
                profileActions = profileViewModel::onEvent,
                customToastHostState = customToastHostState
            )
        }

        composable<NavigationScreen.ListKeluhanScreen> {
            val listKeluhanViewModel = hiltViewModel<ListKeluhanViewModel>()
            val listKeluhanUiState by listKeluhanViewModel.state.collectAsStateWithLifecycle()
            ObserveAsEvents(listKeluhanViewModel.events) { event ->
                when (event) {
                    is ListKeluhanEvent.NavigateToDetailKeluhan -> {
                        rootNavController.navigate(NavigationScreen.DetailKeluhanScreen(event.idKeluhan))
                    }
                }
            }
            ListKeluhanScreen(
                listKeluhanAction = listKeluhanViewModel::onActions,
                listKeluhanUiState = listKeluhanUiState
            )
        }
    }
}