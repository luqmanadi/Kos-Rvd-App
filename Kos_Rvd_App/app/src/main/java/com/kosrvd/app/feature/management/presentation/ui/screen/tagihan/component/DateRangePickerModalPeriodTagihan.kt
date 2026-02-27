package com.kosrvd.app.feature.management.presentation.ui.screen.tagihan.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.kosrvd.app.R
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme
import com.kosrvd.app.core.presentation.utils.getMaxEndDateMillisUTC
import com.kosrvd.app.feature.management.presentation.designsystem.utils.UTC_ZONE_ID
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toDayMonthShortAndYear

@Composable
fun DateRangePickerModalPeriodTagihan(
    onDissmiss: () -> Unit,
    onDateSelected: (Long, Long) -> Unit
) {
    val dateRangePickerState = rememberDateRangePickerState()

    val startMillis = dateRangePickerState.selectedStartDateMillis
    val endMillis = dateRangePickerState.selectedEndDateMillis

    // 1. Hitung batas maksimal tanggal akhir secara dinamis (1 Bulan Siklus) di UTC
    val maxEndDateMillis = remember(startMillis) {
        startMillis?.let { getMaxEndDateMillisUTC(it) }
    }

    // 2. Hitung jumlah hari dinamis (untuk info di UI)
    val maxDaysCount = remember(startMillis, maxEndDateMillis) {
        if (startMillis != null && maxEndDateMillis != null) {
            // +1 karena inklusif (hari pertama dihitung)
            ((maxEndDateMillis - startMillis) / (1000 * 60 * 60 * 24)).toInt() + 1
        } else {
            31 // Fallback teks default sebelum user memilih
        }
    }

    // 3. Validasi: Apakah End Date melebihi batas 1 bulan siklus?
    val isRangeTooLong = if (endMillis != null && maxEndDateMillis != null) {
        endMillis > maxEndDateMillis
    } else {
        false
    }

//    val configuration = Configuration(LocalConfiguration.current).apply {
//        setLocale(INDONESIAN_LOCALE)
//    }
//
//    CompositionLocalProvider(LocalConfiguration provides configuration) {
//
//    }

    DatePickerDialog(
        onDismissRequest = onDissmiss,
        confirmButton = {
            TextButton(
                onClick = {
                    if (startMillis != null && endMillis != null && !isRangeTooLong) {
                        onDateSelected(startMillis, endMillis)
                    }
                },
                enabled = startMillis != null && endMillis != null && !isRangeTooLong,
                shape = RoundedCornerShape(15.dp)
            ){
                Text(text = stringResource(R.string.save))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDissmiss
            ) {
                Text(text = stringResource(R.string.cancel))
            }
        },
        shape = RoundedCornerShape(24.dp),
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ){
        DateRangePicker(
            state = dateRangePickerState,
            modifier = Modifier
                .weight(1f),
            title = {
                Column(modifier = Modifier.padding(start = 24.dp, end = 12.dp, top = 16.dp, bottom = 10.dp)) {
                    Text(
                        text = stringResource(R.string.choose_period_bill),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    // UI Teks Dinamis yang Pintar
                    val warningText = if (startMillis != null && isRangeTooLong) {
                        "Rentang melebihi batas! (Maksimal $maxDaysCount hari)"
                    } else if (startMillis != null && endMillis == null && maxEndDateMillis != null) {
                        // Gunakan parameter UTC_ZONE_ID agar sinkron dengan picker
                        val maxDateStr = maxEndDateMillis.toDayMonthShortAndYear(UTC_ZONE_ID)
                        "Maksimal s.d $maxDateStr ($maxDaysCount hari)"
                    } else {
                        "Maksimal 1 bulan siklus penagihan"
                    }

                    Text(
                        text = warningText,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isRangeTooLong) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            headline = {
                // Tampilan Tanggal Besar di atas Kalender
                val displayText = if (startMillis != null && endMillis != null) {
                    val startStr = startMillis.toDayMonthShortAndYear(UTC_ZONE_ID)
                    val endStr = endMillis.toDayMonthShortAndYear(UTC_ZONE_ID)
                    "$startStr - $endStr"
                } else if (startMillis != null) {
                    val startStr = startMillis.toDayMonthShortAndYear(UTC_ZONE_ID)
                    "$startStr - Periode Akhir"
                } else {
                    "Pilih Periode"
                }

                Text(
                    text = displayText,
                    modifier = Modifier.padding(start = 24.dp, end = 12.dp, bottom = 12.dp),
                    style = MaterialTheme.typography.headlineSmall,
                    color = if (isRangeTooLong) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                )
            },
            showModeToggle = false,
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun DateRangePickerModalPeriodTagihanPreview() {
    KosRvdAppTheme {
        Box(Modifier.fillMaxSize()){
            DateRangePickerModalPeriodTagihan(
                onDissmiss = {},
                onDateSelected = { _, _ -> }
            )
        }
    }
}