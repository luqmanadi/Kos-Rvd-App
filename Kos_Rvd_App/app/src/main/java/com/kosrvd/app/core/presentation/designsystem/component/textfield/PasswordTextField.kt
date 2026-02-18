package com.kosrvd.app.core.presentation.designsystem.component.textfield

import androidx.annotation.StringRes
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedSecureTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.presentation.utils.UiText
import com.kosrvd.app.core.presentation.utils.shake

@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester? = null,
    state: TextFieldState,
    error: UiText? = null,
    isError : Boolean = false,
    shakeTrigger: Int = 0,
    @StringRes label: Int,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {

    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    val image = if (passwordVisible) {
        Icons.Filled.VisibilityOff
    } else {
        Icons.Filled.Visibility
    }
    val description = if (passwordVisible) "Sembunyikan password" else "Tampilkan password"

    OutlinedSecureTextField(
        state = state,
        modifier = modifier.shake(trigger = shakeTrigger)
            .then(
                if (focusRequester != null) {
                    Modifier.focusRequester(focusRequester)
                } else {
                    Modifier
                }
            ),
        isError = isError,
        shape = RoundedCornerShape(12.dp),
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = stringResource(R.string.password)
            )
        },
        textObfuscationMode =
            if (passwordVisible) {
                TextObfuscationMode.Visible
            } else {
                TextObfuscationMode.RevealLastTyped
            },
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, contentDescription = description)
            }
        },
        label = {
            Text(text = stringResource(label))
        },
        supportingText = {
            if(isError) {
                Text(
                    text = error!!.asString()
                )
            }
        },
        keyboardOptions = keyboardOptions
    )
}