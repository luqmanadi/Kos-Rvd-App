package com.kosrvd.app.feature.management.presentation.ui.screen.parkir_harian_mobil.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocalParking
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kosrvd.app.R
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme
import com.kosrvd.app.feature.management.domain.model.ZonaParkiran
import com.kosrvd.app.feature.management.presentation.designsystem.utils.UTC_ZONE_ID
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toDayMonthShortAndYear
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toRupiahFormat
import com.kosrvd.app.feature.management.presentation.ui.screen.parkir_harian_mobil.tambah_parkir_harian_mobil.TambahParkirHarianMobilUiState

@Composable
fun KonfirmasiDataTambahParkirHarianMobilCard(
    modifier: Modifier = Modifier,
    tambahParkirHarianMobilUiState: TambahParkirHarianMobilUiState
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface // Warna latar belakang kartu sesuai gambar
        ),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(25.dp)
        ) {
            // Tanggal Pemakaian
            val startDateStr = tambahParkirHarianMobilUiState.startDate?.toDayMonthShortAndYear() ?: "-"
            val endDateStr = tambahParkirHarianMobilUiState.completionDate?.toDayMonthShortAndYear() ?: "-"
            val dateRangeText = buildAnnotatedString {
                append("$startDateStr - $endDateStr ")
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Normal)) {
                    append("(${tambahParkirHarianMobilUiState.sumDayBooking} Hari)")
                }
            }
            ConfirmationItem(
                icon = Icons.Default.DateRange,
                label = stringResource(R.string.date_usage),
                value = dateRangeText.text, // Menggunakan text sederhana untuk kesederhanaan komponen item
                annotatedValue = dateRangeText,
                isLast = false
            )

            // Zona Parkir
            ConfirmationItem(
                icon = Icons.Default.LocalParking,
                label = stringResource(R.string.zone_parking),
                value = "${tambahParkirHarianMobilUiState.selectedZoneParking?.zoneName ?: "-"} - 1 Slot Mobil",
                isLast = false
            )

            // Kendaraan
            ConfirmationItem(
                icon = Icons.Default.DirectionsCar,
                label = stringResource(R.string.transportation),
                value = "",
                annotatedValue = buildAnnotatedString {
                    append("${tambahParkirHarianMobilUiState.carBrand} ${tambahParkirHarianMobilUiState.carName} ")
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Normal)) {
                        append("(${tambahParkirHarianMobilUiState.numberPlate})")
                    }
                },
                isLast = false
            )

            // Penyewa
            ConfirmationItem(
                icon = Icons.Filled.Person,
                label = stringResource(R.string.tenant),
                value = tambahParkirHarianMobilUiState.userName,
                isLast = false
            )

            // Catatan
            ConfirmationItem(
                icon = Icons.AutoMirrored.Filled.Comment,
                label = stringResource(R.string.note),
                value = tambahParkirHarianMobilUiState.notes.ifEmpty { "-" },
                isLast = false
            )

            // Total Biaya
            ConfirmationItem(
                icon = Icons.Filled.Paid,
                label = stringResource(R.string.total_cost),
                value = tambahParkirHarianMobilUiState.totalCost.toRupiahFormat(),
                isLast = true
            )
        }
    }
}

@Composable
private fun ConfirmationItem(
    icon: ImageVector,
    label: String,
    value: String,
    annotatedValue: CharSequence? = null,
    isLast: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        // Icon Container
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.primary), // Teal gelap sesuai gambar
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(5.dp))
            if (annotatedValue is AnnotatedString) {
                Text(
                    text = annotatedValue,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            } else {
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            if (!isLast) {
                Spacer(modifier = Modifier.height(6.dp))
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun KonfirmasiDataTambahParkirHarianMobilCardPreview() {
    KosRvdAppTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            KonfirmasiDataTambahParkirHarianMobilCard(
                tambahParkirHarianMobilUiState = TambahParkirHarianMobilUiState(
                    startDate = 1755216000000L, // 15 Aug 2025
                    completionDate = 1755648000000L, // 20 Aug 2025
                    sumDayBooking = 6,
                    selectedZoneParking = ZonaParkiran(
                        idZonaParkir = "1",
                        zoneName = "Zona A",
                        monthlyFee = 100000,
                        dailyCosts = 1000,
                        status = "Available"
                    ),
                    carBrand = "Honda",
                    carName = "Jazz",
                    numberPlate = "B 651 AB",
                    userName = "Budi Santoso",
                    notes = "Mobilnya bagus ya",
                    totalCost = 6000
                )
            )
        }
    }
}