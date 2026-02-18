package com.kosrvd.app.core.presentation.designsystem.component.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DoubleTextBgInfo(
    modifier: Modifier = Modifier,
    text1: String,
    text2: String,
    bgText: Color = MaterialTheme.colorScheme.surfaceVariant,
    colorText: Color = MaterialTheme.colorScheme.onSurface,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    fontWeight: FontWeight = FontWeight.Normal,
) {
    Row(
        modifier = modifier
            .background(color = bgText, shape = RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text1,
            style = style,
            color = colorText,
            fontWeight = fontWeight,
            modifier = Modifier
        )
        Spacer(modifier = Modifier.width(35.dp))
        Text(
            text = text2,
            style = style,
            color = colorText,
            fontWeight = fontWeight,
            modifier = Modifier
        )
    }
}