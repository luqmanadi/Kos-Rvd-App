package com.kosrvd.app.presentation.navigation.graph

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
import com.kosrvd.app.presentation.navigation.NavigationGraph
import com.kosrvd.app.presentation.navigation.NavigationScreen
import com.kosrvd.app.core.presentation.utils.CustomToastResultConfig
import com.kosrvd.app.core.presentation.utils.ObserveAsEvents
import com.kosrvd.app.core.presentation.utils.ObserveCustomToastResults
import com.kosrvd.app.core.presentation.utils.navigateBackWithSendKey
import com.kosrvd.app.feature.parking.zona_parkiran_mobil.domain.utils.TypeEditZonaParkir
import com.kosrvd.app.feature.parking.zona_parkiran_mobil.presentation.detail_zona_parkiran_mobil.DetailZonaParkiranMobilEvents
import com.kosrvd.app.feature.parking.zona_parkiran_mobil.presentation.detail_zona_parkiran_mobil.DetailZonaParkiranMobilScreen
import com.kosrvd.app.feature.parking.zona_parkiran_mobil.presentation.detail_zona_parkiran_mobil.DetailZonaParkiranMobilViewModel
import com.kosrvd.app.feature.parking.zona_parkiran_mobil.presentation.edit_zona_parkiran_mobil.EditZonaParkiranMobilEvents
import com.kosrvd.app.feature.parking.zona_parkiran_mobil.presentation.edit_zona_parkiran_mobil.EditZonaParkiranMobilScreen
import com.kosrvd.app.feature.parking.zona_parkiran_mobil.presentation.edit_zona_parkiran_mobil.EditZonaParkiranMobilViewModel
import com.kosrvd.app.feature.parking.zona_parkiran_mobil.presentation.list_zona_parkiran_mobil.ListZonaParkiranMobilEvents
import com.kosrvd.app.feature.parking.zona_parkiran_mobil.presentation.list_zona_parkiran_mobil.ListZonaParkiranMobilScreen
import com.kosrvd.app.feature.parking.zona_parkiran_mobil.presentation.list_zona_parkiran_mobil.ListZonaParkiranMobilViewModel
import com.kosrvd.app.feature.parking.zona_parkiran_mobil.presentation.tambah_zona_parkiran_mobil.TambahZonaParkiranMobilEvents
import com.kosrvd.app.feature.parking.zona_parkiran_mobil.presentation.tambah_zona_parkiran_mobil.TambahZonaParkiranMobilScreen
import com.kosrvd.app.feature.parking.zona_parkiran_mobil.presentation.tambah_zona_parkiran_mobil.TambahZonaParkiranMobilViewModel
import kotlinx.coroutines.launch

fun NavGraphBuilder.zonaParkirGraph(
    navController: NavHostController
){
    navigation<NavigationGraph.ZonaParkirGraph>(
        startDestination = NavigationScreen.ListZonaParkirScreen
    ){
        composable<NavigationScreen.ListZonaParkirScreen> {
            val listZonaParkiranMobilViewModel = hiltViewModel<ListZonaParkiranMobilViewModel>()
            val listZonaParkiranMobilUiState by listZonaParkiranMobilViewModel.state.collectAsStateWithLifecycle()
            val customToastHostState = rememberCustomToastHostState()
            var colorShowBanner by remember {
                mutableStateOf(Color.Unspecified)
            }

            ObserveAsEvents(listZonaParkiranMobilViewModel.events) { events ->
                when(events){
                    ListZonaParkiranMobilEvents.NavigateBack -> {
                        navController.navigateUp()
                    }
                    ListZonaParkiranMobilEvents.NavigateToCreateZonaParkiranMobil -> {
                        navController.navigate(NavigationScreen.TambahZonaParkirScreen)
                    }
                    is ListZonaParkiranMobilEvents.NavigateToDetailZonaParkiranMobil -> {
                        navController.navigate(NavigationScreen.DetailZonaParkirScreen(events.idZonaParkir))
                    }
                    is ListZonaParkiranMobilEvents.NavigateToPreviewImageScreen -> {
                        val safeUrl = Uri.encode(events.imageUrl)
                        navController.navigate(NavigationScreen.PreviewImageScreen(safeUrl))
                    }
                }
            }

            ObserveCustomToastResults(
                navBackStackEntry = it,
                customToasHostState = customToastHostState,
                CustomToastResultConfig(
                    key = Constant.ADD_ZONA_PARKIR_KEY,
                    message = "ZONA PARKIR BERHASIL DITAMBAHKAN"
                ),
                CustomToastResultConfig(
                    key = Constant.DELETE_ZONA_PARKIR_KEY,
                    message = "ZONA PARKIR BERHASIL DIHAPUS"
                )
            ){
                colorShowBanner = if (customToastHostState.currentMessage == "ZONA PARKIR BERHASIL DITAMBAHKAN") {
                    Color(0xFF006877)
                } else {
                    Color(0xFFBA1A1A)
                }
            }


            ListZonaParkiranMobilScreen(
                listZonaParkiranMobilUiState = listZonaParkiranMobilUiState,
                listZonaParkiranMobilActions = listZonaParkiranMobilViewModel::onActions,
                customToastHostState = customToastHostState,
                colorToast = colorShowBanner
            )
        }
        composable<NavigationScreen.DetailZonaParkirScreen> { navBackStackEntry ->
            val detailZonaParkiranMobilViewModel = hiltViewModel<DetailZonaParkiranMobilViewModel>()
            val detailZonaParkiranMobilUiState by detailZonaParkiranMobilViewModel.state.collectAsStateWithLifecycle()
            val customToastHostState = rememberCustomToastHostState()
            val scope = rememberCoroutineScope()
            var colorShowBanner by remember {
                mutableStateOf(Color.Unspecified)
            }

            ObserveAsEvents(detailZonaParkiranMobilViewModel.events) { events ->
                when(events){
                    DetailZonaParkiranMobilEvents.NavigateBack -> {
                        navController.navigateUp()
                    }
                    DetailZonaParkiranMobilEvents.NavigateBackToSendMessageDeleteZonaParkir -> {
                        navController.navigateBackWithSendKey(Constant.DELETE_ZONA_PARKIR_KEY)
                    }
                    is DetailZonaParkiranMobilEvents.NavigateToEditEditZonaParkiranMobil -> {
                        navController.navigate(
                            NavigationScreen.EditZonaParkirScreen(
                                idZonaParkir = events.idZonaParkir,
                                typeEditZonaParkir = events.typeEditZonaParkir,
                                biayaBulanan = events.biayaBulanan,
                                biayaHarian = events.biayaHarian,
                                namaZona = events.namaZona
                            ))
                    }
                    is DetailZonaParkiranMobilEvents.ShowSnackBarError -> {
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
                    key = Constant.UPDATE_NAMA_ZONA_KEY,
                    message = "NAMA ZONA PARKIR BERHASIL DIEDIT"
                ),
                CustomToastResultConfig(
                    key = Constant.UPDATE_BIAYA_BULANAN_KEY,
                    message = "BIAYA BULANAN BERHASIL DIEDIT"
                ),
                CustomToastResultConfig(
                    key = Constant.UPDATE_BIAYA_HARIAN_KEY,
                    message = "BIAYA HARIAN BERHASIL DIEDIT"
                )
            ){
                colorShowBanner = Color(0xFF006877)
            }

            DetailZonaParkiranMobilScreen(
                detailZonaParkiranMobilUiState = detailZonaParkiranMobilUiState,
                detailZonaParkiranMobilActions = detailZonaParkiranMobilViewModel::onActions,
                customToastHostState = customToastHostState,
                colorToast = colorShowBanner
            )
        }
        composable<NavigationScreen.TambahZonaParkirScreen> {
            val tambahZonaParkiranMobilViewModel = hiltViewModel<TambahZonaParkiranMobilViewModel>()
            val tambahZonaParkiranMobilUiState by tambahZonaParkiranMobilViewModel.state.collectAsStateWithLifecycle()
            val customToastHostState = rememberCustomToastHostState()
            val scope = rememberCoroutineScope()

            ObserveAsEvents(tambahZonaParkiranMobilViewModel.events) { events ->
                when(events){
                    TambahZonaParkiranMobilEvents.NavigateBack -> {
                        navController.navigateUp()
                    }
                    TambahZonaParkiranMobilEvents.NavigateBackSendSuccessTambahZonaParkiranMobil -> {
                        navController.navigateBackWithSendKey(Constant.ADD_ZONA_PARKIR_KEY)
                    }
                    is TambahZonaParkiranMobilEvents.ShowSnackBarError -> {
                        scope.launch {
                            customToastHostState.showToast(events.message)
                        }
                    }
                }
            }

            TambahZonaParkiranMobilScreen(
                tambahZonaParkiranMobilUiState = tambahZonaParkiranMobilUiState,
                tambahZonaParkiranMobilActions = tambahZonaParkiranMobilViewModel::onActions,
                customToastHostState = customToastHostState
            )
        }
        composable<NavigationScreen.EditZonaParkirScreen> {
            val editZonaParkiranMobilViewModel = hiltViewModel<EditZonaParkiranMobilViewModel>()
            val editZonaParkiranMobilUiState by editZonaParkiranMobilViewModel.state.collectAsStateWithLifecycle()
            val customToastHostState = rememberCustomToastHostState()
            val scope = rememberCoroutineScope()

            ObserveAsEvents(editZonaParkiranMobilViewModel.events) { events ->
                when(events){
                    EditZonaParkiranMobilEvents.NavigateBack -> {
                        navController.navigateUp()
                    }
                    is EditZonaParkiranMobilEvents.NavigateBackSuccessEditZonaParkiranMobil -> {
                        when(events.typeEditZonaParkiranMobil){
                            TypeEditZonaParkir.EDIT_BIAYA_BULANAN -> {
                                navController.navigateBackWithSendKey(Constant.UPDATE_BIAYA_BULANAN_KEY)
                            }
                            TypeEditZonaParkir.EDIT_BIAYA_HARIAN -> {
                                navController.navigateBackWithSendKey(Constant.UPDATE_BIAYA_HARIAN_KEY)
                            }
                            TypeEditZonaParkir.EDIT_NAMA_ZONA -> {
                                navController.navigateBackWithSendKey(Constant.UPDATE_NAMA_ZONA_KEY)
                            }
                        }
                    }
                    is EditZonaParkiranMobilEvents.ShowSnackBarError -> {
                        scope.launch {
                            customToastHostState.showToast(events.message)
                        }
                    }
                }
            }

            EditZonaParkiranMobilScreen(
                editZonaParkiranMobilUiState = editZonaParkiranMobilUiState,
                editZonaParkiranMobilActions = editZonaParkiranMobilViewModel::onActions,
                customToastHostState = customToastHostState
            )
        }
    }
}