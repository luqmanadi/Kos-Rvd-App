package com.kosrvd.app.core.presentation.utils

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavBackStackEntry
import com.kosrvd.app.feature.management.presentation.designsystem.component.snackbar.SnackbarType
import com.kosrvd.app.feature.management.presentation.designsystem.component.snackbar.showCustomSnackbar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge

data class SnackbarResultConfig(
    val key: String,
    val message: String,
    val type: SnackbarType = SnackbarType.ERROR // Anda bisa sesuaikan default-nya
)

@Composable
fun ObserveSnackbarResults(
    navBackStackEntry: NavBackStackEntry?,
    snackbarHostState: SnackbarHostState,
    vararg snackbarConfigs: SnackbarResultConfig
) {
    if (navBackStackEntry == null) return

    LaunchedEffect(navBackStackEntry, snackbarConfigs) {
        val flows: List<Flow<SnackbarResultConfig>> = snackbarConfigs.map { config ->
            navBackStackEntry.savedStateHandle
                .getStateFlow(key = config.key, initialValue = false)
                .filter { isTriggered -> isTriggered }
                .map { config }
        }

        merge(*flows.toTypedArray())
            .collect { resultConfig ->
                snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showCustomSnackbar(
                    message = resultConfig.message,
                    type = resultConfig.type
                )
                navBackStackEntry.savedStateHandle[resultConfig.key] = false
            }
    }
}