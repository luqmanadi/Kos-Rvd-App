package com.kosrvd.app.core.presentation.designsystem.component.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

@Composable
fun RequiredLabelText(
    labelText: String,
    style: TextStyle = MaterialTheme.typography.bodySmall,
    fontWeight: FontWeight = FontWeight.Normal
) {
    Text(
        text = buildAnnotatedString {

            // Gunakan teks dari parameter
            append(labelText)
            append(" ")

            // Beri gaya merah pada bintang
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold
                )
            ) {
                append("*")
            }
        },
        style = style,
        fontWeight = fontWeight
    )
}