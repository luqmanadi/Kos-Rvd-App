package com.kosrvd.app.feature.management.presentation.ui.screen.profile.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.kosrvd.app.R
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionButton
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionOutlineButton
import com.kosrvd.app.core.presentation.designsystem.component.textfield.PasswordTextField
import com.kosrvd.app.core.presentation.utils.UiText

@Composable
fun DialogInputOldPassword(
    textFieldState: TextFieldState,
    isError: Boolean,
    errorMessage: UiText?,
    shakeTrigger: Int,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = true
        )
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 15.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    imageVector = Icons.Filled.Password,
                    contentDescription = "Icon Password",
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(5.dp))
                Text(
                    text = stringResource(R.string.password_confirmations),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(Modifier.height(15.dp))
                Text(
                    text = stringResource(R.string.description_password_confirmation),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(Modifier.height(5.dp))
                PasswordTextField(
                    modifier = Modifier.fillMaxWidth(),
                    state = textFieldState,
                    isError = isError,
                    error = errorMessage,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Password,
                    ),
                    shakeTrigger = shakeTrigger,
                    label = R.string.enter_password
                )
                Spacer(Modifier.height(15.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ActionOutlineButton(
                        modifier = Modifier.weight(1f),
                        onClick =  onDismiss,
                        text = stringResource(R.string.cancel),
                        shape = RoundedCornerShape(12.dp),
                        height = 43.dp,
                        enabled = !isLoading,
                    )
                    ActionButton(
                        modifier = Modifier.weight(1f),
                        height = 43.dp,
                        onClick = onConfirm,
                        text = stringResource(R.string.confirmation),
                        shape = RoundedCornerShape(12.dp),
                        isLoading = isLoading
                    )
                }
            }
        }
    }
}