package com.kosrvd.app.core.navigation.graph

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
import com.kosrvd.app.core.navigation.models.CustomNavTypes
import com.kosrvd.app.core.navigation.models.EditTypeKamar
import com.kosrvd.app.core.presentation.utils.CustomToastResultConfig
import com.kosrvd.app.core.presentation.utils.ObserveAsEvents
import com.kosrvd.app.core.presentation.utils.ObserveCustomToastResults
import com.kosrvd.app.core.presentation.utils.navigateBackWithSendKey
import com.kosrvd.app.feature.management.presentation.designsystem.utils.TypeEditKamar
import com.kosrvd.app.feature.management.presentation.ui.screen.kamar.buat_kamar.BuatKamarEvents
import com.kosrvd.app.feature.management.presentation.ui.screen.kamar.buat_kamar.BuatKamarScreen
import com.kosrvd.app.feature.management.presentation.ui.screen.kamar.buat_kamar.BuatKamarViewModel
import com.kosrvd.app.feature.management.presentation.ui.screen.kamar.detail_kamar.DetailKamarEvents
import com.kosrvd.app.feature.management.presentation.ui.screen.kamar.detail_kamar.DetailKamarScreen
import com.kosrvd.app.feature.management.presentation.ui.screen.kamar.detail_kamar.DetailKamarViewModel
import com.kosrvd.app.feature.management.presentation.ui.screen.kamar.edit_kamar.EditKamarEvents
import com.kosrvd.app.feature.management.presentation.ui.screen.kamar.edit_kamar.EditKamarScreen
import com.kosrvd.app.feature.management.presentation.ui.screen.kamar.edit_kamar.EditKamarViewModel
import com.kosrvd.app.feature.management.presentation.ui.screen.kamar.list_kamar.ListKamarEvents
import com.kosrvd.app.feature.management.presentation.ui.screen.kamar.list_kamar.ListKamarScreen
import com.kosrvd.app.feature.management.presentation.ui.screen.kamar.list_kamar.ListKamarViewModel
import kotlinx.coroutines.launch
import kotlin.reflect.typeOf

fun NavGraphBuilder.kamarGraph(
    navController: NavHostController
){
    navigation<NavigationGraph.KamarGraph>(
        startDestination = NavigationScreen.ListKamarScreen
    ){
        composable<NavigationScreen.ListKamarScreen> {
            val listKamarViewModel = hiltViewModel<ListKamarViewModel>()
            val listKamarUiState by listKamarViewModel.state.collectAsStateWithLifecycle()
            val customToastHostState = rememberCustomToastHostState()
            var colorShowBanner by remember {
                mutableStateOf(Color.Unspecified)
            }

            ObserveAsEvents(listKamarViewModel.events) { events ->
                when(events){
                    ListKamarEvents.NavigateAddKamar -> {
                        navController.navigate(NavigationScreen.BuatKamarScreen)
                    }
                    ListKamarEvents.NavigateBack -> {
                        navController.navigateUp()
                    }
                    is ListKamarEvents.NavigateToDetailKamar -> {
                        navController.navigate(NavigationScreen.DetailKamarScreen(events.idKamar))
                    }
                }
            }

            ObserveCustomToastResults(
                navBackStackEntry = it,
                customToasHostState = customToastHostState,
                CustomToastResultConfig(
                    key = Constant.ADD_KAMAR_KEY,
                    message = "KAMAR BERHASIL DIBUAT"
                ),
                CustomToastResultConfig(
                    key = Constant.DELETE_KAMAR_KEY,
                    message = "KAMAR BERHASIL DIHAPUS"
                )
            ){
                colorShowBanner = if (customToastHostState.currentMessage == "KAMAR BERHASIL DIBUAT") {
                    Color(0xFF006877)
                } else {
                    Color(0xFFBA1A1A)
                }
            }

            ListKamarScreen(
                listKamarUiState = listKamarUiState,
                listKamarActions = listKamarViewModel::onActions,
                customToastHostState = customToastHostState,
                colorToast = colorShowBanner
            )

        }
        composable<NavigationScreen.DetailKamarScreen> {navBackStackEntry ->
            val idDetailKamar = navBackStackEntry.toRoute<NavigationScreen.DetailKamarScreen>().idDetailKamar
            val detailKamarViewModel = hiltViewModel<DetailKamarViewModel>()
            // load Data
            LaunchedEffect(idDetailKamar) {
                detailKamarViewModel.loadKamarById(idDetailKamar)
            }

            val detailKamarUiState by detailKamarViewModel.state.collectAsStateWithLifecycle()
            val customToastHostState = rememberCustomToastHostState()
            val scope = rememberCoroutineScope()
            var colorShowBanner by remember {
                mutableStateOf(Color.Unspecified)
            }

            ObserveAsEvents(detailKamarViewModel.events){events ->
                when(events){
                    DetailKamarEvents.NavigateBack -> {
                        navController.navigateUp()
                    }
                    DetailKamarEvents.NavigateBackSuccessDeleteKamar -> {
                        navController.navigateBackWithSendKey(key = Constant.DELETE_KAMAR_KEY)
                    }
                    is DetailKamarEvents.NavigateToEditKamar -> {
                        navController.navigate(
                            NavigationScreen.EditKamarScreen(
                                idKamar = events.idKamar,
                                typeEditKamar = events.typeEditKamar,
                                editTypeKamar = events.editTypeKamar
                            )
                        )
                    }
                    is DetailKamarEvents.ShowSnackBarError -> {
                        scope.launch {
                            customToastHostState.showToast(events.message)
                            colorShowBanner = Color(0xFFBA1A1A)
                        }
                    }
                }
            }

            ObserveCustomToastResults(
                navBackStackEntry = navBackStackEntry,
                customToasHostState = customToastHostState,
                CustomToastResultConfig(
                    key = Constant.UPDATE_NOMOR_KAMAR_KEY,
                    message = "NOMOR KAMAR BERHASIL DIEDIT"
                ),
                CustomToastResultConfig(
                    key = Constant.UPDATE_UKURAN_KAMAR_KEY,
                    message = "UKURAN KAMAR BERHASIL DIEDIT"
                ),
                CustomToastResultConfig(
                    key = Constant.UPDATE_TARIF_KAPASITAS_KAMAR_KEY,
                    message = "TARIF DAN KAPASITAS KAMAR BERHASIL DIEDIT"
                ),
                CustomToastResultConfig(
                    key = Constant.UPDATE_PEMAKAIAN_ALAT_ELEKTRO_GRATIS_KEY,
                    message = "PEMAKAIAN ALAT GRATIS BERHASIL DIEDIT"
                ),
                CustomToastResultConfig(
                    key = Constant.UPDATE_FASILITAS_KAMAR_KEY,
                    message = "FASILITAS BERHASIL DIEDIT"
                )
            ){
                colorShowBanner = Color(0xFF006877)
            }

            DetailKamarScreen(
                idKamar = idDetailKamar,
                detailKamarUiState = detailKamarUiState,
                detailKamarActions = detailKamarViewModel::onActions,
                customToastHostState = customToastHostState,
                colorBgToast = colorShowBanner
            )
        }
        composable<NavigationScreen.EditKamarScreen>(
            typeMap = mapOf(typeOf<EditTypeKamar>() to CustomNavTypes.EditTypeKamarType)
        ) {
            val arguments = it.toRoute<NavigationScreen.EditKamarScreen>()
            val typeEditKamar = arguments.typeEditKamar
            val editTypeKamar = arguments.editTypeKamar

            val editKamarViewModel = hiltViewModel<EditKamarViewModel>()
            val editKamarUiState by editKamarViewModel.state.collectAsStateWithLifecycle()
            val customToastHostState = rememberCustomToastHostState()
            val scope = rememberCoroutineScope()

            ObserveAsEvents(editKamarViewModel.events) { events ->
                when(events){
                    EditKamarEvents.NavigateBack -> {
                        navController.navigateUp()
                    }
                    is EditKamarEvents.NavigateBackSuccessEditKamar -> {
                        when(events.typeEditKamar){
                            TypeEditKamar.EDIT_NOMOR_KAMAR -> {
                                navController.navigateBackWithSendKey(Constant.UPDATE_NOMOR_KAMAR_KEY)
                            }
                            TypeEditKamar.EDIT_UKURAN_KAMAR -> {
                                navController.navigateBackWithSendKey(Constant.UPDATE_UKURAN_KAMAR_KEY)
                            }
                            TypeEditKamar.EDIT_TARIF_KAMAR -> {
                                navController.navigateBackWithSendKey(Constant.UPDATE_TARIF_KAPASITAS_KAMAR_KEY)
                            }
                            TypeEditKamar.EDIT_LAYANAN_ALAT_ELEKTRONIK_GRATIS -> {
                                navController.navigateBackWithSendKey(Constant.UPDATE_PEMAKAIAN_ALAT_ELEKTRO_GRATIS_KEY)
                            }
                            TypeEditKamar.EDIT_FASILITAS_KAMAR -> {
                                navController.navigateBackWithSendKey(Constant.UPDATE_FASILITAS_KAMAR_KEY)
                            }
                        }
                    }
                    is EditKamarEvents.ShowSnackBarError -> {
                        scope.launch {
                            customToastHostState.showToast(events.message)
                        }
                    }
                }
            }

            EditKamarScreen(
                editKamarUiState = editKamarUiState,
                editKamarActions = editKamarViewModel::onActions,
                customToastHostState = customToastHostState,
                typeEditKamar = typeEditKamar,
                editTypeKamar = editTypeKamar
            )
        }
        composable<NavigationScreen.BuatKamarScreen> {
            val buatKamarViewModel = hiltViewModel<BuatKamarViewModel>()
            val buatKamarUiState by buatKamarViewModel.state.collectAsStateWithLifecycle()
            val customToastHostState = rememberCustomToastHostState()
            val scope = rememberCoroutineScope()

            ObserveAsEvents(buatKamarViewModel.events) { events ->
                when(events){
                    BuatKamarEvents.NavigateBack -> {
                        navController.navigateUp()
                    }
                    is BuatKamarEvents.NavigateBackSuccessBuatKamarBaru -> {
                        navController.navigateBackWithSendKey(Constant.ADD_KAMAR_KEY)
                    }
                    is BuatKamarEvents.ShowSnackBarError -> {
                        scope.launch {
                            customToastHostState.showToast(events.message)
                        }
                    }
                }
            }

            BuatKamarScreen(
                buatKamarUiState = buatKamarUiState,
                buatKamarActions = buatKamarViewModel::onActions,
                customToastHostState = customToastHostState
            )
        }
    }
}