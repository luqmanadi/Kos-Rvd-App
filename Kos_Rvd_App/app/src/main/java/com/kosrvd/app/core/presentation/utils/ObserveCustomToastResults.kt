package com.kosrvd.app.core.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavBackStackEntry
import com.kosrvd.app.core.domain.utils.CustomToastHostState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge

data class CustomToastResultConfig(
    val key: String,
    val message: String
)

@Composable
fun ObserveCustomToastResults(
    navBackStackEntry: NavBackStackEntry?,
    customToasHostState: CustomToastHostState,
    vararg customToastConfigs: CustomToastResultConfig,
    durationMillis: Long = 3000L,
    content: @Composable () -> Unit = {}
) {
    if (navBackStackEntry == null) return

    LaunchedEffect(navBackStackEntry, customToastConfigs) {
        val flows: List<Flow<CustomToastResultConfig>> = customToastConfigs.map { config ->
            navBackStackEntry.savedStateHandle
                .getStateFlow(key = config.key, initialValue = false)
                .filter { isTriggered -> isTriggered }
                .map { config }
        }
        merge(*flows.toTypedArray())
            .collect { resultConfig ->
                customToasHostState.showToast(resultConfig.message, durationMillis = durationMillis)
                navBackStackEntry.savedStateHandle[resultConfig.key] = false
            }
    }

    content()
}