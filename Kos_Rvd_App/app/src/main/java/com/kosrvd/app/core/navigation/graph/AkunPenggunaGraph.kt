package com.kosrvd.app.core.navigation.graph

import android.net.Uri
import androidx.compose.runtime.LaunchedEffect
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
import com.kosrvd.app.core.navigation.NavigationScreen.PreviewImageScreen
import com.kosrvd.app.core.presentation.utils.CustomToastResultConfig
import com.kosrvd.app.core.presentation.utils.ObserveAsEvents
import com.kosrvd.app.core.presentation.utils.ObserveCustomToastResults
import com.kosrvd.app.core.presentation.utils.navigateBackWithSendKey
import com.kosrvd.app.feature.management.presentation.ui.screen.akun.buat_akun.BuatAkunEvents
import com.kosrvd.app.feature.management.presentation.ui.screen.akun.buat_akun.BuatAkunScreen
import com.kosrvd.app.feature.management.presentation.ui.screen.akun.buat_akun.BuatAkunViewModel
import com.kosrvd.app.feature.management.presentation.ui.screen.akun.detail_akun.DetailAkunEvents
import com.kosrvd.app.feature.management.presentation.ui.screen.akun.detail_akun.DetailAkunScreen
import com.kosrvd.app.feature.management.presentation.ui.screen.akun.detail_akun.DetailAkunViewModel
import com.kosrvd.app.feature.management.presentation.ui.screen.akun.list_akun.ListAkunEvents
import com.kosrvd.app.feature.management.presentation.ui.screen.akun.list_akun.ListAkunScreen
import com.kosrvd.app.feature.management.presentation.ui.screen.akun.list_akun.ListAkunViewModel
import kotlinx.coroutines.launch

fun NavGraphBuilder.akunPenggunaGraph(
    navController: NavHostController
){
    navigation<NavigationGraph.AkunPenggunaGraph>(
        startDestination = NavigationScreen.ListAkunPenggunaScreen
    ){
        composable<NavigationScreen.ListAkunPenggunaScreen> {
            val listAkunViewModel = hiltViewModel<ListAkunViewModel>()
            val listAkunUiState by listAkunViewModel.state.collectAsStateWithLifecycle()
            val customToastHostState = rememberCustomToastHostState()
            var colorShowBanner by remember {
                mutableStateOf(Color.Unspecified)
            }

            ObserveAsEvents(listAkunViewModel.events) { events ->
                when(events){
                    ListAkunEvents.NavigateBack -> {
                        navController.navigateUp()
                    }
                    ListAkunEvents.NavigateToCreateAkun -> {
                        navController.navigate(NavigationScreen.BuatAkunPenggunaScreen)
                    }
                    is ListAkunEvents.NavigateToDetailAkun -> {
                        navController.navigate(NavigationScreen.DetailAkunPenggunaScreen(events.idAkun))
                    }
                }
            }

            ObserveCustomToastResults(
                navBackStackEntry = it,
                customToasHostState = customToastHostState,
                CustomToastResultConfig(
                    key = Constant.ACCOUNT_CREATION_KEY,
                    message = "PEMBUATAN AKUN BERHASIL"
                ),
                CustomToastResultConfig(
                    key = Constant.NON_ACTIVE_ACCOUNT_KEY,
                    message = "NON-AKTIF AKUN BERHASIL"
                ),
                CustomToastResultConfig(
                    key = Constant.ACCOUNT_ACTIVATION_KEY,
                    message = "AKUN BERHASIL DIAKTIFKAN"
                )
            ){
                colorShowBanner = if (customToastHostState.currentMessage == "PEMBUATAN AKUN BERHASIL" || customToastHostState.currentMessage == "AKUN BERHASIL DIAKTIFKAN") {
                    Color(0xFF006877)
                } else {
                    Color(0xFFBA1A1A)
                }
            }

            ListAkunScreen(
                listAkunUiState = listAkunUiState,
                listAkunActions = listAkunViewModel::onActions,
                customToastHostState = customToastHostState,
                colorToast = colorShowBanner
            )
        }
        composable<NavigationScreen.DetailAkunPenggunaScreen> {navBackStackEntry ->
            val idAkun = navBackStackEntry.toRoute<NavigationScreen.DetailAkunPenggunaScreen>().idAkun

            val detailAkunViewModel = hiltViewModel<DetailAkunViewModel>()

            LaunchedEffect(idAkun) {
                detailAkunViewModel.loadDetailAkun(idAkun)
            }

            val detailAkunUiState by detailAkunViewModel.state.collectAsStateWithLifecycle()
            val customToastHostState = rememberCustomToastHostState()
            val scope = rememberCoroutineScope()

            ObserveAsEvents(detailAkunViewModel.events) { events ->
                when(events){
                    DetailAkunEvents.NavigateBack -> {
                        navController.navigateUp()
                    }
                    is DetailAkunEvents.NavigateToPreviewImage -> {
                        val safeUrl = Uri.encode(events.imageUrl.toString())
                        navController.navigate(PreviewImageScreen(safeUrl))
                    }
                    DetailAkunEvents.NavigateBackToSendNonActivateAccountSnackBar -> {
                        navController.navigateBackWithSendKey(Constant.NON_ACTIVE_ACCOUNT_KEY)
                    }
                    is DetailAkunEvents.ShowBannerError -> {
                        scope.launch {
                            customToastHostState.showToast(events.message)
                        }
                    }
                    DetailAkunEvents.NavigateBackToSendActivateAccountSnackBar -> {
                        navController.navigateBackWithSendKey(Constant.ACCOUNT_ACTIVATION_KEY)
                    }
                }
            }

            DetailAkunScreen(
                detailAkunUiState = detailAkunUiState,
                detailAkunActions = detailAkunViewModel::onActions,
                customToastHostState = customToastHostState,
                idAkun = idAkun
            )
        }
        composable<NavigationScreen.BuatAkunPenggunaScreen> {
            val buatAkunViewModel = hiltViewModel<BuatAkunViewModel>()
            val buatAkunUiState by buatAkunViewModel.state.collectAsStateWithLifecycle()
            val customToastHostState = rememberCustomToastHostState()
            val scope = rememberCoroutineScope()

            ObserveAsEvents(buatAkunViewModel.events){
                when(it){
                    BuatAkunEvents.NavigateBack -> {
                        navController.navigateUp()
                    }
                    BuatAkunEvents.NavigateBackToSendCreateAkunSnackBar -> {
                        navController.navigateBackWithSendKey(Constant.ACCOUNT_CREATION_KEY)
                    }
                    is BuatAkunEvents.NavigateToPreviewImage -> {
                        val safeUrl = Uri.encode(it.imageUri.toString())
                        navController.navigate(PreviewImageScreen(safeUrl))
                    }
                    is BuatAkunEvents.ShowSnackBarError -> {
                        scope.launch {
                            customToastHostState.showToast(it.message)
                        }
                    }
                }
            }

            BuatAkunScreen(
                buatAkunUiState = buatAkunUiState,
                buatAkunActions = buatAkunViewModel::onActions,
                customToastHostState = customToastHostState
            )
        }
    }
}