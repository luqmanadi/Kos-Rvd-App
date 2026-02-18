package com.kosrvd.app.core.presentation.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.ContextWrapper
import android.os.Build
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabPosition
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.navigation.NavHostController
import com.kosrvd.app.BuildConfig
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.di.laptopIp

/**
 * Modifier kustom untuk menganimasikan efek "shake" (getar) pada Composable.
 * * @param trigger Angka integer yang akan memicu animasi saat nilainya berubah.
 * @param intensity Kekuatan getaran (seberapa jauh pergeserannya dalam Dp).
 * @param durationMillis Durasi total animasi dalam milidetik.
 */
fun Modifier.shake(
    trigger: Int,
    intensity: Float = 10f, // Kekuatan getaran
): Modifier = composed {
    // Kita butuh Animatable untuk mengontrol nilai offset
    val offset = remember { Animatable(0f) }
    var lastShakeTrigger by rememberSaveable { mutableIntStateOf(trigger) }

    // Kita gunakan LaunchedEffect yang memantau 'trigger'
    LaunchedEffect(trigger) {
        // Hanya jalankan jika trigger BUKAN 0 (state awal)
        if (trigger > 0 && trigger != lastShakeTrigger) {
            lastShakeTrigger = trigger
            // Animasikan!
            offset.animateTo(
                targetValue = 0f, // Kembali ke 0 di akhir
                animationSpec = keyframes {
                    durationMillis = 500
                    // Tentukan keyframe (nilai target pada waktu)
                    // (targetValue at timeMillis)
                    -10f at 100 // Kiri
                    10f at 200 // Kanan
                    -10f at 300 // Kiri
                    10f at 400 // Kanan
                    0f  at 500 // Tengah
                }
            )
        }
    }

    // Terapkan offset horizontal berdasarkan nilai Animatable
    this.offset(x = offset.value.dp)
}

enum class ShakeTargetLogIn {
    EMAIL,
    PASSWORD,
    BOTH
}

enum class ShakeTargetBuatLaporanKeluhan {
    TITLE,
    DESCRIPTION,
    BOTH
}


fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition()
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        )
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                MaterialTheme.colorScheme.surfaceVariant,
                MaterialTheme.colorScheme.outlineVariant,
                MaterialTheme.colorScheme.surfaceVariant
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    ).onGloballyPositioned{
        size = it.size
    }
}

fun textCopyThenPost(context: Context, textCopied:String) {
    val clipboardManager = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
    // When setting the clipboard text.
    clipboardManager.setPrimaryClip(ClipData.newPlainText   ("", textCopied))
    // Only show a toast for Android 12 and lower.
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2)
        Toast.makeText(context, "Berhasil disalin", Toast.LENGTH_SHORT).show()
}

fun Modifier.pagerTabIndicatorOffset(
    pagerState: PagerState,
    tabPositions: List<TabPosition>,
    pageIndexMapping: (Int) -> Int = { it },
): Modifier = layout { measurable, constraints ->
    if (tabPositions.isEmpty()) {
        // Jika tidak ada tab, letakkan di posisi 0
        layout(constraints.maxWidth, 0) {}
    } else {
        val currentPage = minOf(tabPositions.lastIndex, pageIndexMapping(pagerState.currentPage))
        val currentTab = tabPositions[currentPage]
        val offset = pagerState.currentPageOffsetFraction
        val nextPage = minOf(tabPositions.lastIndex, currentPage + 1)
        val nextTab = tabPositions.getOrNull(nextPage) ?: currentTab

        // Hitung lebar indikator saat ini (interpolasi antara tab sekarang & next)
        val indicatorWidth = lerp(currentTab.width, nextTab.width, offset).roundToPx()

        // Hitung posisi X (start) saat ini
        val indicatorOffset = lerp(currentTab.left, nextTab.left, offset).roundToPx()

        val placeable = measurable.measure(
            Constraints(
                minWidth = indicatorWidth,
                maxWidth = indicatorWidth,
                minHeight = 0,
                maxHeight = constraints.maxHeight
            )
        )

        layout(constraints.maxWidth, maxOf(placeable.height, constraints.minHeight)) {
            placeable.place(indicatorOffset, 0)
        }
    }
}

fun Modifier.dashedBorder(
    color: Color,
    shape: Shape,
    strokeWidth: Dp = 2.dp,
    dashLength: Dp = 4.dp,
    gapLength: Dp = 4.dp,
    cap: StrokeCap = StrokeCap.Round
) = dashedBorder(SolidColor(color), shape, strokeWidth, dashLength, gapLength, cap)

fun Modifier.dashedBorder(
    brush: Brush,
    shape: Shape,
    strokeWidth: Dp = 2.dp,
    dashLength: Dp = 4.dp,
    gapLength: Dp = 4.dp,
    cap: StrokeCap = StrokeCap.Round
) = this.drawWithContent {
    val outline = shape.createOutline(size, layoutDirection, density = this)
    val dashedStroke = Stroke(
        cap = cap,
        width = strokeWidth.toPx(),
        pathEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(dashLength.toPx(), gapLength.toPx())
        )
    )

    // Draw the content
    drawContent()

    // Draw the border
    drawOutline(
        outline = outline,
        style = dashedStroke,
        brush = brush
    )
}

fun getDisplayUrl(url: String?): String {
    if (url == null) return ""

    // 1. Jika dari Galeri (URI), langsung return
    if (url.startsWith("content://") || url.startsWith("file://") || url.startsWith("android.resource://")) {
        return url
    }

    // 2. Jika dari Firebase (mengandung 127.0.0.1 atau localhost)
    return if (BuildConfig.DEBUG) {
        url.replace("127.0.0.1", laptopIp)
            .replace("localhost", laptopIp)
    } else {
        // Di Production, link sudah otomatis https://firebasestorage...
        url
    }
}


fun NavHostController.navigateBackWithSendKey(key: String) {
    this.previousBackStackEntry
        ?.savedStateHandle
        ?.set(key, true)
    this.popBackStack()
}