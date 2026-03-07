package com.kosrvd.app.core.navigation.graph

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.domain.utils.rememberCustomToastHostState
import com.kosrvd.app.core.navigation.NavigationGraph
import com.kosrvd.app.core.navigation.NavigationScreen
import com.kosrvd.app.core.presentation.utils.CustomToastResultConfig
import com.kosrvd.app.core.presentation.utils.ObserveAsEvents
import com.kosrvd.app.core.presentation.utils.ObserveCustomToastResults
import com.kosrvd.app.core.presentation.utils.navigateBackWithSendKey
import com.kosrvd.app.feature.management.presentation.ui.screen.penyewaan.detail_penyewaan.DetailPenyewaEvents
import com.kosrvd.app.feature.management.presentation.ui.screen.penyewaan.detail_penyewaan.DetailPenyewaanScreen
import com.kosrvd.app.feature.management.presentation.ui.screen.penyewaan.detail_penyewaan.DetailPenyewaanViewModel
import com.kosrvd.app.feature.management.presentation.ui.screen.penyewaan.list_penyewaan.ListPenyewaanEvents
import com.kosrvd.app.feature.management.presentation.ui.screen.penyewaan.list_penyewaan.ListPenyewaanScreen
import com.kosrvd.app.feature.management.presentation.ui.screen.penyewaan.list_penyewaan.ListPenyewaanViewModel
import kotlinx.coroutines.launch

fun NavGraphBuilder.penyewaanGraph(
    navController: NavHostController
){
    navigation<NavigationGraph.PenyewaanGraph>(
        startDestination = NavigationScreen.ListPenyewaanScreen
    ){
        composable<NavigationScreen.ListPenyewaanScreen> {
            val listPenyewaanViewModel = hiltViewModel<ListPenyewaanViewModel>()
            val listPenyewaanUiState by listPenyewaanViewModel.state.collectAsStateWithLifecycle()
            val customToastHostState = rememberCustomToastHostState()

            ObserveAsEvents(listPenyewaanViewModel.events) { events ->
                when(events){
                    ListPenyewaanEvents.NavigateBack -> {
                        navController.navigateUp()
                    }
                    ListPenyewaanEvents.NavigateToCreatePenyewaan -> {
                        navController.navigate(NavigationScreen.BuatPenyewaanScreen)
                    }
                    is ListPenyewaanEvents.NavigateToDetailPenyewaan -> {
                        navController.navigate(NavigationScreen.DetailPenyewaanScreen(events.idPenyewa))
                    }
                }
            }

            ObserveCustomToastResults(
                navBackStackEntry = it,
                customToasHostState = customToastHostState,
                CustomToastResultConfig(
                    key = Constant.NON_ACTIVE_RENTAL_KEY,
                    message = "BERHASIL MENONAKTIFKAN PENYEWA"
                )
            )

            ListPenyewaanScreen(
                listPenyewaanUiState = listPenyewaanUiState,
                listPenyewaanActions = listPenyewaanViewModel::onActions,
                customToastHostState = customToastHostState
            )

        }
        composable<NavigationScreen.DetailPenyewaanScreen> {navBackStackEntry ->
            val detailPenyewaanViewModel = hiltViewModel<DetailPenyewaanViewModel>()
            val detailPenyewaanUiState by detailPenyewaanViewModel.state.collectAsStateWithLifecycle()
            val customToastHostState = rememberCustomToastHostState()
            val scope = rememberCoroutineScope()
            var colorShowBanner by remember {
                mutableStateOf(Color.Unspecified)
            }

            ObserveAsEvents(detailPenyewaanViewModel.events) { events ->
                when(events){
                    DetailPenyewaEvents.NavigateBack -> {
                        navController.navigateUp()
                    }
                    DetailPenyewaEvents.NavigateBackToSendEndedRental -> {
                        navController.navigateBackWithSendKey(Constant.NON_ACTIVE_RENTAL_KEY)
                    }
                    is DetailPenyewaEvents.ShowSnackBarError -> {
                        scope.launch {
                            colorShowBanner = Color(0xFFBA1A1A)
                            customToastHostState.showToast(events.message)
                        }
                    }
                }
            }

            ObserveCustomToastResults(
                navBackStackEntry = navBackStackEntry,
                customToasHostState = customToastHostState,
                CustomToastResultConfig(
                    key = Constant.UPDATE_PEMAKAIAN_PARKIR_MOBIL_BULANAN_KEY,
                    message = "BERHASIL UPDATE PEMAKAIAN PARKIR MOBIL BULANAN"
                ),
                CustomToastResultConfig(
                    key = Constant.UPDATE_PEMAKAIAN_ALAT_ELEKTRONIK_KEY,
                    message = "BERHASIL UPDATE PEMAKAIAN ALAT ELEKTRONIK"
                )
            ){
                colorShowBanner = Color(0xFF006877)
            }

            DetailPenyewaanScreen(
                detailPenyewaanUiState = detailPenyewaanUiState,
                detailPenyewaActions = detailPenyewaanViewModel::onActions,
                customToastHostState = customToastHostState,
                colorToast = colorShowBanner
            )
        }
        composable<NavigationScreen.BuatPenyewaanScreen> {
            // TODO: Kerjakan Bagian Buat Penyewaan ini
        }
        composable<NavigationScreen.EditPemakaianAlatEleketronikScreen> { navBackStackEntry ->
            val arguments = navBackStackEntry.toRoute<NavigationScreen.EditPemakaianAlatEleketronikScreen>()
            // TODO: Kerjakan Bagian Edit atau Tambah Pemakaian alat elektronik ini
        }
        composable<NavigationScreen.EditPemakaianParkirMobilBulananScreen> { navBackStackEntry ->
            val arguments = navBackStackEntry.toRoute<NavigationScreen.EditPemakaianParkirMobilBulananScreen>()
            // TODO: Kerjakan Bagian Edit atau tambah pemakaian parkir mobil ini
        }
    }
}