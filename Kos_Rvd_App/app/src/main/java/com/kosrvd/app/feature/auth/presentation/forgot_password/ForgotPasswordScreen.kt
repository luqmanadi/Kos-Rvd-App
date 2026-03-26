package com.kosrvd.app.feature.auth.presentation.forgot_password

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusEvent
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun ForgotPasswordScreen(
    forgotPasswordUiState: ForgotPasswordUiState,
    forgotPasswordAction: (ForgotPasswordAction) -> Unit,
    customToastHostState: CustomToastHostState,
) {
    val emailState = rememberTextFieldState(forgotPasswordUiState.email)
    LaunchedEffect(emailState) {
        snapshotFlow { emailState.text.toString() }.collectLatest {
            forgotPasswordAction(ForgotPasswordAction.OnUpdateEmail(it))
        }
    }
    val scrollState = rememberScrollState()

    val coroutineScope = rememberCoroutineScope()

    val emailRequester = remember { BringIntoViewRequester() }
    val emailFocus = remember { FocusRequester() }

    LaunchedEffect(
        forgotPasswordUiState.emailShakeTrigger
    ) {
        if (forgotPasswordUiState.isEmailError) {
            delay(400)
            emailRequester.bringIntoView()
            emailFocus.requestFocus()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopBarLogo(
                navigateUp = { forgotPasswordAction(ForgotPasswordAction.NavigateUp) }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ){
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.height(35.dp))
                Text(
                    text = stringResource(R.string.forgot_password),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = stringResource(R.string.description_forgot_password),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(modifier = Modifier.height(25.dp))
                GeneralTextField(
                    state = emailState,
                    isError = forgotPasswordUiState.isEmailError,
                    error = forgotPasswordUiState.emailError?.asString() ?: "",
                    label = R.string.email,
                    leadingIcon = {
                        Icon(imageVector = Icons.Filled.Email, contentDescription = stringResource(R.string.email))
                    },
                    shakeTrigger = forgotPasswordUiState.emailShakeTrigger,
                    lineLimits = TextFieldLineLimits.SingleLine,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done
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
                Spacer(modifier = Modifier.height(15.dp))
                ActionButton(
                    onClick = { forgotPasswordAction(ForgotPasswordAction.ForgotPassword) },
                    text = stringResource(R.string.send),
                    isLoading = forgotPasswordUiState.isButtonLoading,
                    enabled = !forgotPasswordUiState.isButtonLoading,
                    shape = RoundedCornerShape(10.dp),
                    height = 45.dp,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            CustomToastHost(
                hostState = customToastHostState,
                color = if (customToastHostState.currentMessage == "Email Reset Password Berhasil Dikirim")
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.error,
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
