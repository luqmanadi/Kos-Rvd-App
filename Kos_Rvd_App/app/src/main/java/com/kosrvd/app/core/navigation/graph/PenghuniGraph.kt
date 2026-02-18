package com.kosrvd.app.core.navigation.graph

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.kosrvd.app.core.navigation.NavigationGraph
import com.kosrvd.app.core.navigation.NavigationScreen
import com.kosrvd.app.core.presentation.utils.ObserveAsEvents
import com.kosrvd.app.feature.management.presentation.ui.screen.penghuni.detail_penghuni.DetailPenghuniEvent
import com.kosrvd.app.feature.management.presentation.ui.screen.penghuni.detail_penghuni.DetailPenghuniScreen
import com.kosrvd.app.feature.management.presentation.ui.screen.penghuni.detail_penghuni.DetailPenghuniViewModel
import com.kosrvd.app.feature.management.presentation.ui.screen.penghuni.list_penghuni.ListPenghuniEvent
import com.kosrvd.app.feature.management.presentation.ui.screen.penghuni.list_penghuni.ListPenghuniScreen
import com.kosrvd.app.feature.management.presentation.ui.screen.penghuni.list_penghuni.ListPenghuniViewModel

fun NavGraphBuilder.penghuniGraph(
    navController: NavHostController
){
    navigation<NavigationGraph.PenghuniGraph>(
        startDestination = NavigationScreen.ListPenghuniScreen
    ){
        composable<NavigationScreen.ListPenghuniScreen> {
            val listPenghuniViewModel = hiltViewModel<ListPenghuniViewModel>()
            val listPenghuniUiState by listPenghuniViewModel.state.collectAsStateWithLifecycle()
            ObserveAsEvents(listPenghuniViewModel.events) { events ->
                when (events) {
                    is ListPenghuniEvent.NavigateToDetailPenghuni -> {
                        navController.navigate(NavigationScreen.DetailPenghuniScreen(events.idPenghuni))
                    }
                    is ListPenghuniEvent.NavigateBack -> {
                        navController.navigateUp()
                    }
                }
            }
            ListPenghuniScreen(
                listPenghuniUiState = listPenghuniUiState,
                listPenghuniAction = listPenghuniViewModel::onActions
            )
        }

        composable<NavigationScreen.DetailPenghuniScreen> {navBackStackEntry ->
            // get id penghuni
            val idPenghuni = navBackStackEntry.toRoute<NavigationScreen.DetailPenghuniScreen>().penghuniId

            val detailPenghuniViewModel = hiltViewModel<DetailPenghuniViewModel>()
            val detailPenghuniUiState by detailPenghuniViewModel.state.collectAsStateWithLifecycle()

            // observe event
            ObserveAsEvents(detailPenghuniViewModel.events){ events ->
                when(events){
                    is DetailPenghuniEvent.NavigateBack -> {
                        navController.navigateUp()
                    }
                }
            }

            LaunchedEffect(idPenghuni) {
                // load data penghuni
                detailPenghuniViewModel.loadDetailPenghuni(idPenghuni)
            }

            DetailPenghuniScreen(
                detailPenghuniUiState = detailPenghuniUiState,
                detailPenghuniAction = detailPenghuniViewModel::onActions,
                idPenghuni = idPenghuni
            )
        }
    }
}