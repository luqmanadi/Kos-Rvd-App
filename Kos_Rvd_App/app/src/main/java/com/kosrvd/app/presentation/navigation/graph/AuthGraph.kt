package com.kosrvd.app.presentation.navigation.graph

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kosrvd.app.core.domain.utils.rememberCustomToastHostState
import com.kosrvd.app.presentation.navigation.NavigationGraph
import com.kosrvd.app.presentation.navigation.NavigationScreen
import com.kosrvd.app.core.presentation.utils.ObserveAsEvents
import com.kosrvd.app.core.presentation.utils.ShakeTargetLogIn
import com.kosrvd.app.feature.auth.presentation.forgot_password.ForgotPasswordEvent
import com.kosrvd.app.feature.auth.presentation.forgot_password.ForgotPasswordScreen
import com.kosrvd.app.feature.auth.presentation.forgot_password.ForgotPasswordViewModel
import com.kosrvd.app.feature.auth.presentation.login.LogInEvents
import com.kosrvd.app.feature.auth.presentation.login.LogInScreen
import com.kosrvd.app.feature.auth.presentation.login.LogInViewModel
import com.kosrvd.app.feature.auth.presentation.onboarding.OnBoardingScreen
import kotlinx.coroutines.launch

fun NavGraphBuilder.authGraph(
    navController: NavHostController
){
    navigation<NavigationGraph.AuthGraph>(
        startDestination = NavigationScreen.OnBoardingScreen
    ){
        composable<NavigationScreen.OnBoardingScreen> {
            OnBoardingScreen(
                onNavigateLogin = {navController.navigate(NavigationScreen.LoginScreen)}
            )
        }

        composable<NavigationScreen.LoginScreen> {
            val logInViewModel = hiltViewModel<LogInViewModel>()
            val logInUiState by logInViewModel.state.collectAsStateWithLifecycle()

            // Trigger Error Text Field
            var emailShakeTrigger by remember { mutableIntStateOf(0) }
            var passwordShakeTrigger by remember { mutableIntStateOf(0) }

            val scope = rememberCoroutineScope()
            val customToastHostState = rememberCustomToastHostState()

            ObserveAsEvents(logInViewModel.events) { events ->
                when(events){
                    is LogInEvents.NavigateUp -> {
                        navController.navigateUp()
                    }
                    is LogInEvents.NavigateToForgotPassword -> {
                        navController.navigate(NavigationScreen.ForgotPasswordScreen)
                    }
                    is LogInEvents.NavigateToManagementGraph -> {
                        navController.navigate(NavigationGraph.MainGraph) {
                            popUpTo(NavigationGraph.AuthGraph){
                                inclusive = true
                            }
                        }
                    }
                    is LogInEvents.ShowToast -> {
                        scope.launch {
                            customToastHostState.showToast(events.message)
                        }
                    }
                    is LogInEvents.ShakeTextField -> {
                        when (events.target) {
                            ShakeTargetLogIn.EMAIL -> {
                                emailShakeTrigger++
                            }
                            ShakeTargetLogIn.PASSWORD -> {
                                passwordShakeTrigger++
                            }
                            ShakeTargetLogIn.BOTH -> {
                                emailShakeTrigger++
                                passwordShakeTrigger++
                            }
                        }
                    }
                }
            }

            LogInScreen(
                logInUiState = logInUiState,
                logInActions = logInViewModel::onActions,
                emailShakeTrigger = emailShakeTrigger,
                passwordShakeTrigger = passwordShakeTrigger,
                customToastHostState = customToastHostState

            )
        }

        composable<NavigationScreen.ForgotPasswordScreen> {
            val forgotPasswordViewModel = hiltViewModel<ForgotPasswordViewModel>()
            val forgotPasswordUiState by forgotPasswordViewModel.state.collectAsStateWithLifecycle()
            val scope = rememberCoroutineScope()
            val customToastHostState = rememberCustomToastHostState()

            ObserveAsEvents(forgotPasswordViewModel.events) { events ->
                when(events){
                    is ForgotPasswordEvent.NavigateUp -> {
                        navController.navigateUp()
                    }
                    is ForgotPasswordEvent.ShowToast -> {
                        scope.launch {
                            customToastHostState.showToast(events.message)
                        }
                    }
                }
            }

            ForgotPasswordScreen(
                forgotPasswordUiState = forgotPasswordUiState,
                forgotPasswordAction = forgotPasswordViewModel::onActions,
                customToastHostState = customToastHostState
            )
        }
    }
}