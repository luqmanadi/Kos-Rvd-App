package com.kosrvd.app.feature.management.presentation.ui.screen.parkir_harian_mobil.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionButton
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionOutlineButton
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme

@Composable
fun StepperControlBar(
    modifier: Modifier = Modifier,
    keyboardController: SoftwareKeyboardController? = null,
    currentStep: Int,
    totalSteps: Int,
    isNextEnabled: Boolean,
    isButtonSubmitLoading: Boolean,
    onBack: () -> Unit,
    onNext: () -> Unit,
    onSubmit: () -> Unit
) {
    val textButtonPrimary = if (currentStep < totalSteps) stringResource(R.string.next) else stringResource(R.string.add_wearer)
    val disableContainerColorButtonPrimary = if (currentStep < totalSteps) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f) else MaterialTheme.colorScheme.primary
    val disableContentColorButtonPrimary = if (currentStep < totalSteps) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onPrimary

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (currentStep > 1){
            ActionOutlineButton(
                modifier = Modifier.weight(1f),
                onClick =  onBack,
                enabled = !isButtonSubmitLoading,
                text = stringResource(R.string.back),
                shape = RoundedCornerShape(15.dp),
                height = 45.dp,
                borderStroke = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            )
        }
        ActionButton(
            modifier = Modifier.weight(1f),
            height = 45.dp,
            onClick = {
                if (currentStep < totalSteps){
                    onNext()
                    keyboardController?.hide()
                } else onSubmit()
                      },
            text = textButtonPrimary,
            shape = RoundedCornerShape(15.dp),
            isLoading = isButtonSubmitLoading,
            enabled = isNextEnabled,
            disableContainerColor = disableContainerColorButtonPrimary,
            disableContentColor = disableContentColorButtonPrimary
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StepperControlBarPreview() {
    KosRvdAppTheme {
        StepperControlBar(
            currentStep = 2,
            totalSteps = 4,
            isNextEnabled = true,
            isButtonSubmitLoading = false,
            onBack = {},
            onNext = {},
            onSubmit = {},
            keyboardController = LocalSoftwareKeyboardController.current
        )
    }
}