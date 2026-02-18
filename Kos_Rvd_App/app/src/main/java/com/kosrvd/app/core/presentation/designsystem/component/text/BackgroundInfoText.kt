package com.kosrvd.app.core.presentation.designsystem.component.text

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kosrvd.app.core.data.constant.Constant

@Composable
fun BackgroundInfoText(
    modifier: Modifier = Modifier,
    text: String,
    colorBg: Color = MaterialTheme.colorScheme.surfaceVariant,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    fontWeight: FontWeight = FontWeight.Normal,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    textColor: Color = Color.Unspecified
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = colorBg,
        modifier = modifier
    ) {
        Text(
            text = text,
            style = style,
            color = textColor,
            fontWeight = fontWeight,
            modifier = Modifier
                .padding(vertical = 6.dp, horizontal = 10.dp),
            maxLines = maxLines,
            overflow = overflow
        )
    }

}

@Composable
fun OutlineBackgroundInfoText(
    modifier: Modifier = Modifier,
    text: String,
    colorBg: Color = MaterialTheme.colorScheme.surfaceVariant,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    fontWeight: FontWeight = FontWeight.Normal,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    border: BorderStroke? = null,
    textColor: Color = Color.Unspecified
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = colorBg,
        border = border,
        modifier = modifier
    ) {
        Text(
            text = text,
            style = style,
            color = textColor,
            fontWeight = fontWeight,
            modifier = Modifier
                .padding(7.dp),
            maxLines = maxLines,
            overflow = overflow
        )
    }
}

@Composable
fun StatusBackgroundText (
    status: String,
    modifier: Modifier = Modifier
) {
    when (status) {
        Constant.MENUNGGU_VERIFIKASI -> {
            BackgroundInfoText(
                text = status,
                modifier = modifier,
                colorBg = MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Constant.BELUM_LUNAS -> {
            BackgroundInfoText(
                text = status,
                modifier = modifier,
                colorBg = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Constant.DIPESAN -> {
            BackgroundInfoText(
                text = status,
                modifier = modifier,
                colorBg = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Constant.DIBATALKAN -> {
            BackgroundInfoText(
                text = status,
                modifier = modifier,
                colorBg = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Constant.LUNAS -> {
           BackgroundInfoText(
                text = status,
                modifier = modifier,
                colorBg = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Constant.DIPAKAI -> {
            BackgroundInfoText(
                text = status,
                modifier = modifier,
                colorBg = MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Constant.MENUNGGU_KONFIRMASI -> {
          BackgroundInfoText(
                text = status,
                modifier = modifier,
                colorBg = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Constant.SEDANG_DIPROSES -> {
            BackgroundInfoText(
                text = status,
                modifier = modifier,
                colorBg = MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Constant.SELESAI -> {
            BackgroundInfoText(
                text = status,
                modifier = modifier,
                colorBg = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodySmall
            )
        }
        else -> {
            BackgroundInfoText(
                text = "ono lek rapodo",
                modifier = modifier,
                colorBg = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}