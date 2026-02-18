package com.kosrvd.app.core.presentation.designsystem.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ActionOutlinePrimaryButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    shape: Shape = ButtonDefaults.shape,
    height: Dp = ButtonDefaults.MinHeight,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    strokeWidth: Dp = 2.dp,
    leadingIcon: @Composable (() -> Unit)? = null,
    fontWeight: FontWeight = FontWeight.Normal
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .height(height),
        shape = shape,
        enabled = enabled && !isLoading,
        border = BorderStroke(strokeWidth, MaterialTheme.colorScheme.primary),
        contentPadding = contentPadding,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.primary,
            containerColor = Color.Transparent
        )
    ) {
        if (isLoading){
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 2.dp
            )
        }else{
            Row {
                leadingIcon?.let {
                    it()
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
            Text(
                text = text,
                textAlign = TextAlign.Center,
                fontWeight = fontWeight
            )
        }
    }

}