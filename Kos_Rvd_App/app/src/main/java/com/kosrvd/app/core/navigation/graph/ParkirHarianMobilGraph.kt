package com.kosrvd.app.core.navigation.graph

import android.net.Uri
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
import com.kosrvd.app.core.navigation.NavigationGraph
import com.kosrvd.app.core.navigation.NavigationScreen
import com.kosrvd.app.core.presentation.utils.CustomToastResultConfig
import com.kosrvd.app.core.presentation.utils.ObserveAsEvents
import com.kosrvd.app.core.presentation.utils.ObserveCustomToastResults
import com.kosrvd.app.core.presentation.utils.navigateBackWithSendKey
import com.kosrvd.app.feature.management.presentation.ui.screen.parkir_harian_mobil.detail_parkir_harian_mobil.DetailParkirHarianMobilEvents
import com.kosrvd.app.feature.management.presentation.ui.screen.parkir_harian_mobil.detail_parkir_harian_mobil.DetailParkirHarianMobilScreen
import com.kosrvd.app.feature.management.presentation.ui.screen.parkir_harian_mobil.detail_parkir_harian_mobil.DetailParkirHarianMobilViewModel
import com.kosrvd.app.feature.management.presentation.ui.screen.parkir_harian_mobil.list_parkir_harian_mobil.ListParkirHarianMobilEvents
import com.kosrvd.app.feature.management.presentation.ui.screen.parkir_harian_mobil.list_parkir_harian_mobil.ListParkirHarianMobilScreen
import com.kosrvd.app.feature.management.presentation.ui.screen.parkir_harian_mobil.list_parkir_harian_mobil.ListParkirHarianMobilViewModel
import kotlinx.coroutines.launch

fun NavGraphBuilder.parkirHarianMobilGraph(
    navController: NavHostController
){
    navigation<NavigationGraph.ParkiranHarianMobilGraph>(
        startDestination = NavigationScreen.ListParkirHarianMobilScreen
    ){
        composable<NavigationScreen.ListParkirHarianMobilScreen> {
            val listParkirHarianMobilViewModel = hiltViewModel<ListParkirHarianMobilViewModel>()
            val listParkirHarianMobilUiState by listParkirHarianMobilViewModel.state.collectAsStateWithLifecycle()
            val customToastHostState = rememberCustomToastHostState()
            var colorShowBanner by remember {
                mutableStateOf(Color.Unspecified)
            }

            ObserveAsEvents(listParkirHarianMobilViewModel.events) { events ->
                when(events){
                    ListParkirHarianMobilEvents.NavigateBack -> {
                        navController.navigateUp()
                    }
                    is ListParkirHarianMobilEvents.NavigateToDetailParkirHarianMobil -> {
                        navController.navigate(NavigationScreen.DetailParkirHarianMobilScreen(events.idParkirHarianMobil))
                    }
                    ListParkirHarianMobilEvents.NavigateToTambahParkirHarianMobil -> {
                        navController.navigate(NavigationScreen.TambahParkirHarianMobilScreen)
                    }
                }
            }

            ObserveCustomToastResults(
                navBackStackEntry = it,
                customToasHostState = customToastHostState,
                CustomToastResultConfig(
                    key = Constant.ADD_PARKIR_HARIAN_KEY,
                    message = "BERHASIL TAMBAH PEMAKAI PARKIR"
                ),
                CustomToastResultConfig(
                    key = Constant.REMOVE_PARKIR_HARIAN_KEY,
                    message = "BERHASIL HAPUS PEMAKAI PARKIR"
                ),
                CustomToastResultConfig(
                    key = Constant.SUCCESS_UPLOAD_PROOF_OF_PAYMENT_PARKIR_HARIAN_KEY,
                    message = "BERHASIL UPLOAD PEMBAYARAN"
                ),
                CustomToastResultConfig(
                    key = Constant.CANCEL_PARKIR_HARIAN_KEY,
                    message = "PEMAKAIAN PARKIR DIBATALKAN"
                )
            ){
                colorShowBanner = if (customToastHostState.currentMessage == "BERHASIL HAPUS PEMAKAI PARKIR" || customToastHostState.currentMessage == "PEMAKAIAN PARKIR DIBATALKAN") {
                    Color(0xFFBA1A1A)
                } else {
                    Color(0xFF006877)
                }
            }

            ListParkirHarianMobilScreen(
                listParkirHarianMobilUiState = listParkirHarianMobilUiState,
                listParkirHarianMobilActions = listParkirHarianMobilViewModel::onActions,
                customToastHostState = customToastHostState,
                colorToast = colorShowBanner
            )
        }
        composable<NavigationScreen.DetailParkirHarianMobilScreen> {
            val detailParkirHarianMobilViewModel = hiltViewModel<DetailParkirHarianMobilViewModel>()
            val detailParkirHarianMobilUiState by detailParkirHarianMobilViewModel.state.collectAsStateWithLifecycle()
            val customToastHostState = rememberCustomToastHostState()
            val scope = rememberCoroutineScope()

            ObserveAsEvents(detailParkirHarianMobilViewModel.events) { events ->
                when(events){
                    DetailParkirHarianMobilEvents.NavigateBack -> {
                        navController.navigateUp()
                    }
                    DetailParkirHarianMobilEvents.NavigateBackSuccessCancel -> {
                        navController.navigateBackWithSendKey(Constant.CANCEL_PARKIR_HARIAN_KEY)
                    }
                    DetailParkirHarianMobilEvents.NavigateBackSuccessDelete -> {
                        navController.navigateBackWithSendKey(Constant.REMOVE_PARKIR_HARIAN_KEY)
                    }
                    DetailParkirHarianMobilEvents.NavigateBackSuccessUploadProofOfPayment -> {
                        navController.navigateBackWithSendKey(Constant.SUCCESS_UPLOAD_PROOF_OF_PAYMENT_PARKIR_HARIAN_KEY)
                    }
                    is DetailParkirHarianMobilEvents.NavigateToPreviewImage -> {
                        val safeUrl = Uri.encode(events.imageUri.toString())
                        navController.navigate(NavigationScreen.PreviewImageScreen(safeUrl))
                    }
                    is DetailParkirHarianMobilEvents.ShowSnackBarError -> {
                        scope.launch {
                            customToastHostState.showToast(events.message)
                        }
                    }
                }
            }

            DetailParkirHarianMobilScreen(
                detailParkirHarianMobilUiState = detailParkirHarianMobilUiState,
                detailParkirHarianMobilActions = detailParkirHarianMobilViewModel::onActions,
                customToastHostState = customToastHostState
            )
        }
        composable<NavigationScreen.TambahParkirHarianMobilScreen> {
            // TODO: Kerjakan Bagian Tambah Pemakaian Parkir Mobil Harian ini
        }
    }
}