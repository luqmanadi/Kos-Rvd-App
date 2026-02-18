package com.kosrvd.app.core.presentation.designsystem.component.text

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kosrvd.app.core.domain.utils.CustomToastHostState

@Composable
private fun ErrorTextBanner(
    text: String,
    modifier: Modifier = Modifier,
    color: Color,
    fontWeight: FontWeight
) {
    Surface(
        modifier = modifier
            .fillMaxWidth(),
        color = color,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = text,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = fontWeight,
                textAlign = TextAlign.Start,
            )
        }
    }

}

@Composable
fun CustomToastHost(
    hostState: CustomToastHostState,
    modifier: Modifier = Modifier,
    color: Color,
    fontWeight: FontWeight = FontWeight.Normal,
    enter: EnterTransition = fadeIn(),
    exit: ExitTransition = fadeOut(),
) {
    val currentMessage = hostState.currentMessage

    val isVisible = hostState.isVisible

    AnimatedVisibility(
        visible = isVisible,
        enter = enter,
        exit = exit,
        modifier = modifier
    ) {
        ErrorTextBanner(
            text = currentMessage,
            color = color,
            fontWeight = fontWeight
        )

    }
}