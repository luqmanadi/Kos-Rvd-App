package com.kosrvd.app.feature.management.presentation.ui.screen.parkir_harian_mobil.component

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme
import com.kosrvd.app.feature.management.presentation.designsystem.utils.INDONESIAN_LOCALE
import com.kosrvd.app.feature.management.presentation.designsystem.utils.UTC_ZONE_ID
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toDayMonthShortAndYear
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toEndOfDay
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toStartOfDay
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerParkirHarianMobilCard(
    modifier: Modifier = Modifier,
    startDate: Long? = null,
    completionDate: Long? = null,
    onSelectedDate: (Long?, Long?) -> Unit
) {
    // Set Locale ke Indonesia agar nama hari dan bulan otomatis berubah
    val configuration = LocalConfiguration.current
    val localizedConfiguration = remember(configuration) {
        Configuration(configuration).apply {
            setLocale(INDONESIAN_LOCALE)
        }
    }

    CompositionLocalProvider(LocalConfiguration provides localizedConfiguration) {
        val initialStart = remember(startDate) {
            startDate?.let {
                Instant.ofEpochMilli(it)
                    .atZone(ZoneId.of("Asia/Jakarta"))
                    .toLocalDate()
                    .atStartOfDay(ZoneId.of("UTC"))
                    .toInstant()
                    .toEpochMilli()
            }
        }

        val initialEnd = remember(completionDate) {
            completionDate?.let {
                Instant.ofEpochMilli(it)
                    .atZone(ZoneId.of("Asia/Jakarta"))
                    .toLocalDate()
                    .atStartOfDay(ZoneId.of("UTC"))
                    .toInstant()
                    .toEpochMilli()
            }
        }

        val dateRangePickerState = rememberDateRangePickerState(
            initialSelectedStartDateMillis = initialStart,
            initialSelectedEndDateMillis = initialEnd
        )

        // Callback saat tanggal berubah
        LaunchedEffect(dateRangePickerState.selectedStartDateMillis, dateRangePickerState.selectedEndDateMillis) {
            val newStart = dateRangePickerState.selectedStartDateMillis
            val newEnd = dateRangePickerState.selectedEndDateMillis
            // Hanya update jika data tidak null dan berbeda dengan data awal (opsional)
            if (newStart != null && newEnd != null) {
                onSelectedDate(newStart.toStartOfDay(), newEnd.toEndOfDay())
            }
        }

        Card(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(45.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(0.2f)),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Header: "Pilih Tanggal Pemakaian"
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 45.dp, topEnd = 45.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)) // Teal sangat muda
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Pilih Tanggal Pemakaian",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer // Teal tua
                        )
                    )
                }
                HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                // Body: DateRangePicker
                DateRangePicker(
                    state = dateRangePickerState,
                    modifier = Modifier.fillMaxWidth().height(340.dp),
                    title = null, // Kita pakai header custom di atas
                    headline = null, // Kita pakai footer custom di bawah
                    showModeToggle = false,
                    colors = DatePickerDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        selectedDayContainerColor = MaterialTheme.colorScheme.primary, // Teal utama
                        selectedDayContentColor = MaterialTheme.colorScheme.surface,
                        dayInSelectionRangeContainerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f), // Range highlight
                        dayInSelectionRangeContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        todayDateBorderColor = MaterialTheme.colorScheme.primary,
                        todayContentColor = MaterialTheme.colorScheme.primary
                    )
                )

                // Footer: Menampilkan rentang tanggal yang dipilih
                val startMillis = dateRangePickerState.selectedStartDateMillis
                val endMillis = dateRangePickerState.selectedEndDateMillis

                val dateRangeText = if (startMillis != null && endMillis != null) {
                    "${startMillis.toDayMonthShortAndYear()} - ${endMillis.toDayMonthShortAndYear()}"
                } else if (startMillis != null) {
                    "${startMillis.toDayMonthShortAndYear()} - ..."
                } else {
                    "Pilih Rentang Tanggal"
                }

                HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = dateRangeText,
                    modifier = Modifier.padding(bottom = 16.dp, top = 8.dp),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DateRangePickerParkirHarianMobilCardPreview() {
    KosRvdAppTheme {
        Box(modifier = Modifier.padding(20.dp).fillMaxSize()) {
            DateRangePickerParkirHarianMobilCard(
                modifier = Modifier.align(Alignment.Center),
                onSelectedDate = { _, _ -> }
            )
        }
    }
}