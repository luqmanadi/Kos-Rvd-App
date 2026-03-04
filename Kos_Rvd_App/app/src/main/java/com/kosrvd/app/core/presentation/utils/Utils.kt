package com.kosrvd.app.core.presentation.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.navigation.NavHostController
import com.google.firebase.Timestamp
import com.kosrvd.app.BuildConfig
import com.kosrvd.app.core.di.laptopIp
import com.kosrvd.app.feature.management.domain.model.AlatElektronik
import com.kosrvd.app.feature.management.presentation.designsystem.utils.INDONESIAN_LOCALE
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

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

fun convertMillisToDate(millis: Long): String {
    val pattern = "dd MMM yyyy"
    val formatter = DateTimeFormatter.ofPattern(pattern, INDONESIAN_LOCALE)
    return formatter.format(Date(millis).toInstant())
}

fun convertMillisToTimeStamp(millis: Long): Timestamp {
    return Timestamp(Date(millis))
}

fun getMaxEndDateMillisUTC(startMillis: Long): Long {
    // Wajib set ke UTC agar sejajar dengan output DateRangePicker Compose
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
        timeInMillis = startMillis
        add(Calendar.MONTH, 1)         // Langkah 1: Tambah tepat 1 bulan (misal 16 Feb -> 16 Mar)
        add(Calendar.DAY_OF_MONTH, -1) // Langkah 2: Mundur 1 hari (16 Mar -> 15 Mar)
    }
    return calendar.timeInMillis
}

fun Modifier.customShadow(
    color: Color = Color.Black,
    blur: Dp = 0.dp,
    spread: Dp = 0.dp
) = drawBehind {
    drawIntoCanvas { canvas ->
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.color = Color.Transparent.toArgb()
        
        frameworkPaint.setShadowLayer(
            blur.toPx(),
            0f, 0f, // x dan y offset = 0
            color.toArgb()
        )

        val spreadPx = spread.toPx()
        canvas.drawRoundRect(
            left = -spreadPx,
            top = -spreadPx,
            right = size.width + spreadPx,
            bottom = size.height + spreadPx,
            radiusX = (size.width / 2) + spreadPx,
            radiusY = (size.height / 2) + spreadPx,
            paint = paint
        )
    }
}

fun calculateTotalBill (
    hargaSewaKamar: Long,
    hargaSewaParkirMobil: Long?,
    hargaPemakaianElektronik: List<AlatElektronik>
): Long {
    return hargaSewaKamar + (hargaSewaParkirMobil ?: 0) + hargaPemakaianElektronik.sumOf { it.cost }
}

data class PeriodInfo(
    val maxEndDateMillisUTC: Long,
    val totalDaysInPeriod: Int
)

fun calculateMaxEndPeriodInfo(startMillisUTC: Long): PeriodInfo {
    // Picker menggunakan UTC, jadi kita baca sebagai LocalDate UTC agar tanggalnya tidak geser
    val startDate = Instant.ofEpochMilli(startMillisUTC).atZone(ZoneId.of("UTC")).toLocalDate()
    val day = startDate.dayOfMonth

    val startPeriodDate: LocalDate
    val endPeriodDate: LocalDate

    // Logika: Jika tanggal mulai >= 16, periode sampai tgl 15 bulan DEPAN.
    // Jika tanggal mulai <= 15, periode sampai tgl 15 bulan INI.
    if (day >= 16) {
        startPeriodDate = startDate.withDayOfMonth(16)
        endPeriodDate = startDate.plusMonths(1).withDayOfMonth(15)
    } else {
        startPeriodDate = startDate.minusMonths(1).withDayOfMonth(16)
        endPeriodDate = startDate.withDayOfMonth(15)
    }

    val maxEndDateMillisUTC = endPeriodDate.atStartOfDay(ZoneId.of("UTC")).toInstant().toEpochMilli()

    // Hitung total hari (inklusif, makanya ditambah 1)
    val totalDaysInPeriod = ChronoUnit.DAYS.between(startPeriodDate, endPeriodDate).toInt() + 1

    return PeriodInfo(maxEndDateMillisUTC, totalDaysInPeriod)
}