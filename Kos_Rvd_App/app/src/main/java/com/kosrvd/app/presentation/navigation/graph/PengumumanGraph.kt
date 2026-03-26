package com.kosrvd.app.presentation.navigation.graph

import android.util.Log
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
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.domain.utils.rememberCustomToastHostState
import com.kosrvd.app.presentation.navigation.NavigationGraph
import com.kosrvd.app.presentation.navigation.NavigationScreen
import com.kosrvd.app.core.presentation.utils.CustomToastResultConfig
import com.kosrvd.app.core.presentation.utils.ObserveAsEvents
import com.kosrvd.app.core.presentation.utils.ObserveCustomToastResults
import com.kosrvd.app.core.presentation.utils.navigateBackWithSendKey
import com.kosrvd.app.feature.announcement.presentation.buat_pengumuman.BuatPengumumanEvents
import com.kosrvd.app.feature.announcement.presentation.buat_pengumuman.BuatPengumumanScreen
import com.kosrvd.app.feature.announcement.presentation.buat_pengumuman.BuatPengumumanViewModel
import com.kosrvd.app.feature.announcement.presentation.list_pengumuman.PengumumanEvents
import com.kosrvd.app.feature.announcement.presentation.list_pengumuman.PengumumanScreen
import com.kosrvd.app.feature.announcement.presentation.list_pengumuman.PengumumanViewModel
import kotlinx.coroutines.launch

fun NavGraphBuilder.pengumumanGraph(
    navController: NavHostController
){
    navigation<NavigationGraph.PengumumanGraph>(
        startDestination = NavigationScreen.PengumumanScreen
    ){
        composable<NavigationScreen.PengumumanScreen> {
            val pengumumanViewModel = hiltViewModel<PengumumanViewModel>()
            val pengumumanUiState by pengumumanViewModel.state.collectAsStateWithLifecycle()
            val scope = rememberCoroutineScope()
            val customToastHostState = rememberCustomToastHostState()
            var colorShowBanner by remember {
                mutableStateOf(Color.Unspecified)
            }

            ObserveAsEvents(pengumumanViewModel.events) { events ->
                when (events){
                    is PengumumanEvents.NavigateBack -> {
                        navController.navigateUp()
                    }
                    is PengumumanEvents.NavigateCreatePengumuman -> {
                        navController.navigate(NavigationScreen.BuatPengumumanScreen)
                    }
                    is PengumumanEvents.ShowSnackBar -> {
                        scope.launch {
                            if (events.isError){
                                colorShowBanner = Color(0xFFBA1A1A)
                                customToastHostState.showToast(events.message)
                            } else {
                                colorShowBanner = Color(0xFF006877)
                                Log.d("TAG", "pengumumanGraph: ${events.message}")
                                customToastHostState.showToast(events.message)
                            }
                        }
                    }
                }
            }

            ObserveCustomToastResults(
                navBackStackEntry = it,
                customToasHostState = customToastHostState,
                CustomToastResultConfig(
                    key = Constant.CREATE_PENGUMUMAN_KEY,
                    message = "Pengumuman berhasil dibuat."
                )
            ){
                colorShowBanner = Color(0xFF006877)
            }

            PengumumanScreen(
                pengumumamUiState = pengumumanUiState,
                pengumumanActions = pengumumanViewModel::onActions,
                customToastHostState = customToastHostState,
                colorShowBanner = colorShowBanner
            )
        }

        composable<NavigationScreen.BuatPengumumanScreen> {
            val buatPengumumanViewModel = hiltViewModel<BuatPengumumanViewModel>()
            val buatPengumumanUiState by buatPengumumanViewModel.state.collectAsStateWithLifecycle()
            val scope = rememberCoroutineScope()
            val customToastHostState = rememberCustomToastHostState()

            ObserveAsEvents(buatPengumumanViewModel.events){events ->
                when(events){
                    is BuatPengumumanEvents.NavigateBack -> {
                        navController.navigateUp()
                    }
                    BuatPengumumanEvents.NavigateBackToSendSuccessCreatePengumuman -> {
                        navController.navigateBackWithSendKey(Constant.CREATE_PENGUMUMAN_KEY)
                    }
                    is BuatPengumumanEvents.ShowSnackBarError -> {
                        scope.launch {
                            customToastHostState.showToast(events.message)
                        }
                    }
                }
            }

            BuatPengumumanScreen(
                buatPengumumanUiState = buatPengumumanUiState,
                buatPengumumanActions = buatPengumumanViewModel::onActions,
                customToastHostState = customToastHostState
            )
        }
    }
}