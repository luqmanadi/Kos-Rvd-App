package com.kosrvd.app.core.presentation.designsystem.component.line

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DottedHorizontalLine(
    modifier: Modifier = Modifier,
    strokeWidthDp: Dp = 1.dp,
    dashLengthDp: Dp = 5.dp,
    gapLengthDP: Dp = 5.dp,
    strokeColor: Color = MaterialTheme.colorScheme.outline
) {
    Spacer(modifier = modifier
        .fillMaxWidth()
        .height(1.dp)
        .drawBehind {
            val strokeWidth = strokeWidthDp.toPx()
            val y = size.height - strokeWidth / 2
            val dashLength = dashLengthDp.toPx()
            val gapLength = gapLengthDP.toPx()

            drawLine(
                color = strokeColor,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = strokeWidth,
                pathEffect = PathEffect.dashPathEffect(
                    intervals = floatArrayOf(dashLength, gapLength),
                    phase = 0f
                )
            )
        }
    )
}