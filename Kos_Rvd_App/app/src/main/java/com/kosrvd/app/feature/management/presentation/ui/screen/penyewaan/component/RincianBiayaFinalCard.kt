package com.kosrvd.app.feature.management.presentation.ui.screen.penyewaan.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme
import com.kosrvd.app.feature.management.domain.model.AlatElektronik
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toRupiahFormat

@Composable
fun RincianBiayaFinalCard(
    modifier: Modifier = Modifier,
    biayaKamar: Long,
    biayaPemakaianParkir: Long?,
    listAlatElektronik: List<AlatElektronik>,
    totalBiaya: Long
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Payments,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = "Rincian Biaya",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(16.dp))

            // Sewa Kamar Bulanan
            BiayaRow(label = "Sewa Kamar Bulanan", value = biayaKamar)

            // Sewa Parkir Mobil (Conditional)
            biayaPemakaianParkir?.let {
                Spacer(Modifier.height(12.dp))
                BiayaRow(label = "Sewa Parkir Mobil Bulanan", value = it)
            }

            // Pemakaian Alat Elektronik (Conditional)
            if (listAlatElektronik.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Pemakaian Alat Elektronik",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                            .padding(top = 8.dp)
                    ) {
                        // Vertical Line indicator like in the image
                        Spacer(Modifier.width(16.dp))
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .fillMaxHeight()
                                .padding(vertical = 2.dp)
                                .background(MaterialTheme.colorScheme.outlineVariant)
                        )
                        
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 16.dp)
                        ) {
                            listAlatElektronik.forEachIndexed { index, alat ->
                                if (index > 0) Spacer(Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val label = if (alat.cost == 0L) "${alat.toolName} (Gratis)" else alat.toolName
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.outline,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = alat.cost.toRupiahFormat(),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            
            // Dashed line divider before total
            val dashLineColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
            ) {
                drawLine(
                    color = dashLineColor,
                    start = Offset(0f, center.y),
                    end = Offset(size.width, center.y),
                    strokeWidth = 2.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(
                        intervals = floatArrayOf(6.dp.toPx(), 4.dp.toPx()),
                        phase = 0f
                    )
                )
            }
            
            Spacer(Modifier.height(16.dp))

            // Total Tagihan
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Tagihan",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = buildAnnotatedString {
                        append(totalBiaya.toRupiahFormat())
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        ){
                            append(" /bulan")
                        }
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun BiayaRow(label: String, value: Long) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )
        Spacer(Modifier.weight(1f))
        Text(
            text = value.toRupiahFormat(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RincianBiayaFinalCardPreview() {
    KosRvdAppTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            RincianBiayaFinalCard(
                biayaKamar = 1800000L,
                biayaPemakaianParkir = 150000L,
                listAlatElektronik = listOf(
                    AlatElektronik("Magicom", 0L, ""),
                    AlatElektronik("Dispenser", 25000L, "")
                ),
                totalBiaya = 1975000L
            )
        }
    }
}
