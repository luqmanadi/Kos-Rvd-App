package com.kosrvd.app

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.WindowMetrics
import androidx.window.layout.WindowMetricsCalculator
import com.kosrvd.app.core.navigation.graph.RootNavGraph
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val mainViewModel by viewModels<MainViewModel>()

    private lateinit var navController: NavHostController


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition {
            mainViewModel.state.value.isCheckingAuth
        }
        val restoreState = savedInstanceState?.getBundle("nav_state")

        super.onCreate(savedInstanceState)

        lockOrientationForSmallScreens()

        // CEK INTENT Notifi SAAT APLIKASI MATI LALU DIBUKA
        intent.extras.let { bundle ->
            val idNotifikasi = bundle?.getString("idNotifikasi")
            val typeNotification = bundle?.getString("typeNotification")
            val idDetailReferensi = bundle?.getString("idDetailReferensi")
            mainViewModel.handleDeepLink(typeNotification, idDetailReferensi, idNotifikasi)
        }
        enableEdgeToEdge()
        setContent {
            val mainUiState by mainViewModel.state.collectAsStateWithLifecycle()
            navController = rememberNavController()
            navController.restoreState(restoreState)
            KosRvdAppTheme {
                RootNavGraph(
                    navController = navController,
                    isAuthenticationRequired = mainUiState.isAuthenticated,
                    mainViewModel = mainViewModel
                )
            }
        }
    }

    // CEK INTENT Notif SAAT APLIKASI SEDANG BERJALAN DI BACKGROUND
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.extras.let { bundle ->
            val idNotifikasi = bundle?.getString("idNotifikasi")
            val typeNotification = bundle?.getString("typeNotification")
            val idDetailReferensi = bundle?.getString("idDetailReferensi")
            mainViewModel.handleDeepLink(typeNotification, idDetailReferensi, idNotifikasi)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (::navController.isInitialized) {
            outState.putBundle("nav_state", navController.saveState())
        }
    }

    private fun lockOrientationForSmallScreens(){
        val metrics = WindowMetricsCalculator.getOrCreate().computeMaximumWindowMetrics(this)
        val width = metrics.bounds.width()
        val height = metrics.bounds.height()
        val density = resources.displayMetrics.density

        // konversi pixel to dp
        val widthDp = width / density
        val heightDp = height / density

        val isCompact = widthDp < 600f || heightDp < 480f
        requestedOrientation = if (isCompact) {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            ActivityInfo.SCREEN_ORIENTATION_FULL_USER
        }

    }
}