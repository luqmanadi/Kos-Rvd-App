package com.kosrvd.app.feature.management.presentation.ui.screen.tagihan.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.kosrvd.app.R
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme
import com.kosrvd.app.core.presentation.utils.getMaxEndDateMillisUTC
import com.kosrvd.app.feature.management.presentation.designsystem.utils.INDONESIAN_LOCALE
import com.kosrvd.app.feature.management.presentation.designsystem.utils.UTC_ZONE_ID
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toDayMonthShortAndYear

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerModalPeriodTagihan(
    initialSelectedStartDateMillis: Long? = null,
    initialSelectedEndDateMillis: Long? = null,
    onDissmiss: () -> Unit,
    onDateSelected: (startMillis: Long, endMillis: Long, periodString: String) -> Unit
) {
    val configuration = LocalConfiguration.current
    val localizedConfiguration = remember(configuration) {
        Configuration(configuration).apply {
            setLocale(INDONESIAN_LOCALE)
        }
    }

    CompositionLocalProvider(LocalConfiguration provides localizedConfiguration) {
        val dateRangePickerState = rememberDateRangePickerState(
            initialSelectedStartDateMillis = initialSelectedStartDateMillis,
            initialSelectedEndDateMillis = initialSelectedEndDateMillis
        )

        val startMillis = dateRangePickerState.selectedStartDateMillis
        val endMillis = dateRangePickerState.selectedEndDateMillis

        val maxEndDateMillis = remember(startMillis) {
            startMillis?.let { getMaxEndDateMillisUTC(it) }
        }

        val maxDaysCount = remember(startMillis, maxEndDateMillis) {
            if (startMillis != null && maxEndDateMillis != null) {
                ((maxEndDateMillis - startMillis) / (1000 * 60 * 60 * 24)).toInt() + 1
            } else {
                31
            }
        }

        val isRangeTooLong = if (endMillis != null && maxEndDateMillis != null) {
            endMillis > maxEndDateMillis
        } else {
            false
        }

        DatePickerDialog(
            onDismissRequest = onDissmiss,
            confirmButton = {
                TextButton(
                    onClick = {
                        if (startMillis != null && endMillis != null && !isRangeTooLong) {
                            val periodString = "${startMillis.toDayMonthShortAndYear(UTC_ZONE_ID)} - ${endMillis.toDayMonthShortAndYear(UTC_ZONE_ID)}"
                            onDateSelected(startMillis, endMillis, periodString)
                        }
                    },
                    enabled = startMillis != null && endMillis != null && !isRangeTooLong,
                    shape = RoundedCornerShape(15.dp)
                ) {
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
        ) {
            DateRangePicker(
                state = dateRangePickerState,
                modifier = Modifier.height(400.dp),
                title = {
                    Column(modifier = Modifier.padding(start = 24.dp, end = 12.dp, top = 16.dp, bottom = 10.dp)) {
                        Text(
                            text = stringResource(R.string.choose_period_bill),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        val warningText = if (startMillis != null && isRangeTooLong) {
                            "Rentang melebihi batas! (Maksimal $maxDaysCount hari)"
                        } else if (startMillis != null && endMillis == null && maxEndDateMillis != null) {
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
}

@Preview(showSystemUi = true)
@Composable
private fun DateRangePickerModalPeriodTagihanPreview() {
    KosRvdAppTheme {
        Box(Modifier.fillMaxSize()) {
            DateRangePickerModalPeriodTagihan(
                onDissmiss = {},
                onDateSelected = { _, _, _ -> }
            )
        }
    }
}