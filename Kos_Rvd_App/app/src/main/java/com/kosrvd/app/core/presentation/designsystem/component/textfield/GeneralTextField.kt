package com.kosrvd.app.core.presentation.designsystem.component.textfield

import androidx.annotation.StringRes
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kosrvd.app.core.presentation.utils.shake

@Composable
fun GeneralTextField(
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester? = null,
    state: TextFieldState,
    enabled: Boolean = true,
    isError: Boolean,
    error: String,
    @StringRes label: Int,
    shakeTrigger: Int = 0,
    inputTransformation: InputTransformation? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.Default,
) {

    OutlinedTextField(
        state = state,
        modifier = modifier
            .shake(trigger = shakeTrigger)
            .then(
                if (focusRequester != null) {
                    Modifier.focusRequester(focusRequester)
                } else {
                    Modifier
                }
            ),
        isError = isError,
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        inputTransformation = inputTransformation,
        leadingIcon = leadingIcon,
        label = {
            Text(text = stringResource(label))
        },
        keyboardOptions = keyboardOptions,
        lineLimits = lineLimits,
        supportingText = {
            if (isError) {
                Text(
                    text = error
                )
            }
        },
    )
}