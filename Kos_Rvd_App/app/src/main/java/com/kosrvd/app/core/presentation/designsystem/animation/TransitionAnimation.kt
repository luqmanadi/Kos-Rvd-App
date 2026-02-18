package com.kosrvd.app.core.presentation.designsystem.animation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut

fun scaleIntoContainer(
    direction: ScaleTransitionDirection = ScaleTransitionDirection.INWARDS,
    initialScale: Float = if (direction == ScaleTransitionDirection.OUTWARDS) 1.1f else 0.9f
): EnterTransition {
    return scaleIn(
        animationSpec = tween(300, delayMillis = 100),
        initialScale = initialScale
    ) + fadeIn()
}

fun scaleOutOfContainer(
    direction: ScaleTransitionDirection = ScaleTransitionDirection.OUTWARDS,
    targetScale: Float = if (direction == ScaleTransitionDirection.INWARDS) 1.1f else 0.9f
): ExitTransition {
    return scaleOut(
        animationSpec = tween(
            durationMillis = 300,
            delayMillis = 100
        ), targetScale = targetScale
    ) + fadeOut()
}

fun fadeIntoContainer(): EnterTransition {
    return fadeIn(
        animationSpec = tween(300, delayMillis = 100))
}

fun fadeOutOfContainer(): ExitTransition {
    return fadeOut(
        animationSpec = tween(
            durationMillis = 300,
            delayMillis = 100
        ))
}

enum class ScaleTransitionDirection{
    OUTWARDS, INWARDS
}