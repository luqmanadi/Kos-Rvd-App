package com.kosrvd.app.core.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.kosrvd.app.core.navigation.NavigationGraph
import com.kosrvd.app.core.navigation.NavigationScreen
import com.kosrvd.app.core.navigation.models.CustomNavTypes
import com.kosrvd.app.core.navigation.models.EditPindahKamarType
import com.kosrvd.app.core.navigation.models.TambahPenghuniType
import kotlin.reflect.typeOf

fun NavGraphBuilder.penyewaanGraph(
    navController: NavHostController
){
    navigation<NavigationGraph.PenyewaanGraph>(
        startDestination = NavigationScreen.ListPenyewaanScreen
    ){
        composable<NavigationScreen.ListPenyewaanScreen> {
            // TODO: Kerjakan Bagian List Penyewaan ini
        }
        composable<NavigationScreen.DetailPenyewaanScreen> {navBackStackEntry ->
            val idPenyewaan = navBackStackEntry.toRoute<NavigationScreen.DetailPenyewaanScreen>().idPenyewa
            // TODO: Kerjakan Bagian Detail Penyewaan ini
        }
        composable<NavigationScreen.BuatPenyewaanScreen> {
            // TODO: Kerjakan Bagian Buat Penyewaan ini
        }
        composable<NavigationScreen.PindahKamarScreen>(
            typeMap = mapOf(typeOf<EditPindahKamarType>() to CustomNavTypes.editPindahKamarType)
        ) {
            // TODO: Kerjakan Bagian Pindah Kamar ini
        }
        composable<NavigationScreen.TambahPenguniScreen>(
            typeMap = mapOf(typeOf<TambahPenghuniType>() to CustomNavTypes.tambahPenghuniType)
        ) {
            // TODO: Kerjakan Bagian Tambah Penghuni ini
        }
        composable<NavigationScreen.EditAtauTambahPemakaianAlatEleketronikScreen> {navBackStackEntry ->
            val arguments = navBackStackEntry.toRoute<NavigationScreen.EditAtauTambahPemakaianAlatEleketronikScreen>()
            // TODO: Kerjakan Bagian Edit atau Tambah Pemakaian alat elektronik ini
        }
        composable<NavigationScreen.EditAtauTambahPemakaianParkirMobilScreen> { navBackStackEntry ->
            val arguments = navBackStackEntry.toRoute<NavigationScreen.EditAtauTambahPemakaianParkirMobilScreen>()
            // TODO: Kerjakan Bagian Edit atau tambah pemakaian parkir mobil ini
        }
    }
}