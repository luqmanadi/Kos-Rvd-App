package com.kosrvd.app.feature.auth.presentation.ui.screen.login

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.domain.utils.CustomToastHostState
import com.kosrvd.app.core.presentation.designsystem.component.appbar.TopBarLogo
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionButton
import com.kosrvd.app.core.presentation.designsystem.component.text.CustomToastHost
import com.kosrvd.app.core.presentation.designsystem.component.textfield.GeneralTextField
import com.kosrvd.app.core.presentation.designsystem.component.textfield.PasswordTextField
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun LogInScreen(
    logInUiState: LogInUiState,
    logInActions: (LogInActions) -> Unit,
    emailShakeTrigger: Int, // <-- Parameter baru
    passwordShakeTrigger: Int, // <-- Parameter baru
    customToastHostState: CustomToastHostState,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val emailState = rememberTextFieldState(logInUiState.email)
    LaunchedEffect(emailState) {
        snapshotFlow { emailState.text.toString() }.collectLatest {
            logInActions(LogInActions.UpdateEmail(it))
        }
    }

    val passwordState = rememberTextFieldState(logInUiState.password)
    LaunchedEffect(passwordState){
        snapshotFlow { passwordState.text.toString() }.collectLatest {
            logInActions(LogInActions.UpdatePassword(it))
        }
    }
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    val emailRequester = remember { BringIntoViewRequester() }
    val emailFocus = remember { FocusRequester() }

    val passwordRequester = remember { BringIntoViewRequester() }
    val passwordFocus = remember { FocusRequester() }

    LaunchedEffect(
        emailShakeTrigger,
        passwordShakeTrigger
    ) {
        if (logInUiState.isEmailError) {
            delay(400)
            emailRequester.bringIntoView()
            emailFocus.requestFocus()
        }
        else if (logInUiState.isPasswordError) {
            delay(400)
            passwordRequester.bringIntoView()
            passwordFocus.requestFocus()
        }
    }

    Scaffold(
        topBar = {
            TopBarLogo(
                navigateUp = { logInActions(LogInActions.NavigateUp) }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding).fillMaxSize()
        ){
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState)
                    .imePadding()
            ) {
                Spacer(modifier = Modifier.height(35.dp))
                Text(
                    text = stringResource(R.string.enter),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = stringResource(R.string.description_enter),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(25.dp))
                GeneralTextField(
                    state = emailState,
                    isError = logInUiState.isEmailError,
                    error = logInUiState.emailError?.asString() ?: "",
                    label = R.string.email,
                    shakeTrigger = emailShakeTrigger,
                    leadingIcon = {
                        Icon(imageVector = Icons.Filled.Email, contentDescription = stringResource(R.string.email))
                    },
                    lineLimits = TextFieldLineLimits.SingleLine,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    focusRequester = emailFocus,
                    modifier = Modifier.fillMaxWidth()
                        .bringIntoViewRequester(emailRequester)
                        .onFocusEvent { focusState ->
                            if (focusState.isFocused) {
                                coroutineScope.launch {
                                    delay(500)
                                    emailRequester.bringIntoView()
                                }
                            }
                        }
                )
                Spacer(modifier = Modifier.height(5.dp))
                PasswordTextField(
                    state = passwordState,
                    error = logInUiState.passwordError,
                    isError = logInUiState.isPasswordError,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    shakeTrigger = passwordShakeTrigger,
                    focusRequester = passwordFocus,
                    modifier = Modifier.fillMaxWidth()
                        .bringIntoViewRequester(passwordRequester)
                        .onFocusEvent { focusState ->
                            if (focusState.isFocused) {
                                coroutineScope.launch {
                                    delay(500)
                                    passwordRequester.bringIntoView()
                                }
                            }
                        },
                    label = R.string.password
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = stringResource(R.string.forgot_password),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable(onClick = {logInActions(LogInActions.NavigateToForgotPassword)}),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(30.dp))
                ActionButton(
                    onClick = {
                        logInActions(LogInActions.LogIn)
                        keyboardController?.hide()
                    },
                    text = stringResource(R.string.enter),
                    isLoading = logInUiState.isButtonLoading,
                    enabled = !logInUiState.isButtonLoading,
                    shape = RoundedCornerShape(10.dp),
                    height = 45.dp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            CustomToastHost(
                hostState = customToastHostState,
                color = MaterialTheme.colorScheme.error,
                enter = slideInVertically(
                    // Enters by sliding in from offset -fullHeight to 0.
                    initialOffsetY = { fullHeight -> -fullHeight },
                    animationSpec = tween(durationMillis = 150, easing = LinearOutSlowInEasing)
                ),
                exit = slideOutVertically(
                    // Exits by sliding out from offset 0 to -fullHeight.
                    targetOffsetY = { fullHeight -> -fullHeight },
                    animationSpec = tween(durationMillis = 250, easing = LinearOutSlowInEasing)
                )
            )
        }
    }
}