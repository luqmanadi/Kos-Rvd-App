package com.kosrvd.app.feature.management.presentation.ui.screen.profile.reset_password

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.domain.utils.CustomToastHostState
import com.kosrvd.app.core.presentation.designsystem.component.appbar.TopBarLeftTitle
import com.kosrvd.app.core.presentation.designsystem.component.text.CustomToastHost
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme
import com.kosrvd.app.feature.management.presentation.designsystem.component.loading.DialogCircularLoading

@Composable
fun ResetPasswordScreen(
    resetPasswordUiState: ResetPasswordUiState,
    resetPasswordActions: (ResetPasswordActions) -> Unit,
    customToastHostState: CustomToastHostState,
) {
    Scaffold(
        topBar = {
            TopBarLeftTitle(
                title = stringResource(R.string.choose_reset_password),
                onBackClick = { resetPasswordActions(ResetPasswordActions.NavigateBack) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ){
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp)
            ) {
                item(key = "Kirim Email Reset Password Card") {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 5.dp,
                                shape = RoundedCornerShape(20.dp),
                                ambientColor = MaterialTheme.colorScheme.primary,
                                spotColor = MaterialTheme.colorScheme.primary,
                            ),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        onClick = { resetPasswordActions(ResetPasswordActions.SendEmailReset)}
                    ) {
                        Row(
                            Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Email,
                                contentDescription = stringResource(R.string.send_email_reset_password),
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier
                                    .background(
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .padding(8.dp)
                            )
                            Spacer(Modifier.width(16.dp))
                            Text(
                                text = stringResource(R.string.send_email_reset_password),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                            )
                            Spacer(Modifier.weight(1f))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "Detail",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }

                    }
                }
                item { Spacer(Modifier.height(15.dp)) }
                item(key = "Reset Password Card") {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 5.dp,
                                shape = RoundedCornerShape(20.dp),
                                ambientColor = MaterialTheme.colorScheme.primary,
                                spotColor = MaterialTheme.colorScheme.primary,
                            ),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        onClick = { resetPasswordActions(ResetPasswordActions.NavigateToEditDataProfileChangePassword)}
                    ) {
                        Row(
                            Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Password,
                                contentDescription = stringResource(R.string.change_password),
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier
                                    .background(
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .padding(8.dp)
                            )
                            Spacer(Modifier.width(16.dp))
                            Text(
                                text = stringResource(R.string.change_password),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                            )
                            Spacer(Modifier.weight(1f))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "Detail",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }

                    }
                }
            }

            CustomToastHost(
                hostState = customToastHostState,
                color = MaterialTheme.colorScheme.primary,
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

            if (resetPasswordUiState.isShowLoading){
                DialogCircularLoading()
            }
        }
    }
}



@Preview(showSystemUi = true)
@Composable
private fun ResetPasswordScreenPreview() {
    KosRvdAppTheme {
        ResetPasswordScreen(
            resetPasswordUiState = ResetPasswordUiState(
                false
            ),
            resetPasswordActions = {},
            customToastHostState = CustomToastHostState()
            )
    }
}

