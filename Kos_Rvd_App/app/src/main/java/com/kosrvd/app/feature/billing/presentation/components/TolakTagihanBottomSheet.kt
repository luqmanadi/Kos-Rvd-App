package com.kosrvd.app.feature.billing.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionDangerButton
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionOutlineButton
import com.kosrvd.app.core.presentation.designsystem.component.textfield.GeneralTextField
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TolakTagihanBottomSheet(
    modifier: Modifier = Modifier,
    isButtonSendLoading: Boolean,
    onDismiss: () -> Unit,
    onSend: () -> Unit,
    isAlasanPenolakanError: Boolean,
    alasanPenolakanError: String,
    alasanPenolakanState: TextFieldState,
    alasanPenolakanShakeTrigger: Int,
) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
        sheetState = sheetState
    ) {
        Text(
            text = stringResource(R.string.title_reject_payment),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp),
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(15.dp))
        Text(
            text = stringResource(R.string.description_reject_payment),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(Modifier.height(15.dp))
        GeneralTextField(
            modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
            state = alasanPenolakanState,
            isError = isAlasanPenolakanError,
            error = alasanPenolakanError,
            label = R.string.fill_reason_reject,
            shakeTrigger = alasanPenolakanShakeTrigger,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text,
            ),
            lineLimits = TextFieldLineLimits.SingleLine
        )
        Spacer(Modifier.height(15.dp))
        Row(
            Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ActionOutlineButton(
                modifier = Modifier.weight(1f),
                onClick = onDismiss,
                text = stringResource(R.string.cancel),
                shape = RoundedCornerShape(12.dp),
                height = 43.dp,
                enabled = !isButtonSendLoading,
            )
            ActionDangerButton(
                modifier = Modifier.weight(1f),
                height = 43.dp,
                onClick = onSend,
                text = stringResource(R.string.send),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                isLoading = isButtonSendLoading,
                enabled = !isButtonSendLoading,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.Send,
                        contentDescription = null
                    )
                }
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun TolakTagihanBottomSheetPreview() {
    KosRvdAppTheme {
        TolakTagihanBottomSheet(
            modifier = Modifier,
            isButtonSendLoading = false,
            onDismiss = { },
            onSend = { },
            isAlasanPenolakanError = false,
            alasanPenolakanError = "",
            alasanPenolakanState = rememberTextFieldState(),
            alasanPenolakanShakeTrigger = 0
        )
    }
}