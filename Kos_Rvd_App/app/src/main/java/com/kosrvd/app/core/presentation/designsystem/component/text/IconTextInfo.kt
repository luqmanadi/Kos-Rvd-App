package com.kosrvd.app.core.presentation.designsystem.component.text

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun IconTextInfo(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    colorText: Color = MaterialTheme.colorScheme.primary,
    colorIcon: Color = MaterialTheme.colorScheme.primary,
    sizeIcon: Dp = 24.dp,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    spacing: Dp = 10.dp,
    fontWeight: FontWeight = FontWeight.Normal
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(sizeIcon),
            imageVector = icon,
            contentDescription = text,
            tint = colorIcon
        )
        Spacer(modifier = Modifier.width(spacing))
        Text(
            text = text,
            style = textStyle,
            color = colorText,
            fontWeight = fontWeight
        )
    }
}

@Composable
fun IconTextInfo(
    modifier: Modifier = Modifier,
    text: String,
    icon: Painter,
    colorText: Color = MaterialTheme.colorScheme.primary,
    sizeIcon: Dp = 24.dp,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    spacing: Dp = 10.dp,
    fontWeight: FontWeight = FontWeight.Normal

) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(sizeIcon),
            painter = icon,
            contentDescription = text,
            alignment = Alignment.Center
        )
        Spacer(modifier = Modifier.width(spacing))
        Text(
            text = text,
            style = textStyle,
            color = colorText,
            fontWeight = fontWeight
        )
    }
}