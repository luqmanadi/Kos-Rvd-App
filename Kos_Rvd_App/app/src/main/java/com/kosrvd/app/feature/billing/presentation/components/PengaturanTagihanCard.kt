package com.kosrvd.app.feature.billing.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme

@Composable
fun PengaturanTagihanCard(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    isActive: Boolean,
    enabled: Boolean = true,
    onClick: (Boolean) -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 18.sp
                    )
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 22.sp,
                        letterSpacing = 0.2.sp
                    )
                )
            }
            Switch(
                enabled = enabled,
                checked = isActive,
                onCheckedChange = onClick,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PengaturanTagihanCardPreview() {
    KosRvdAppTheme {
        var state1 by remember { mutableStateOf(false) }
        var state2 by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PengaturanTagihanCard(
                title = "Pengingat Pembayaran Otomatis",
                description = "Kirim pengingat otomatis setiap jam 09:00 WIB untuk tagihan yang belum lunas.",
                isActive = state1,
                onClick = { state ->
                    state1 = state
                }
            )
            PengaturanTagihanCard(
                title = "Generate Tagihan Otomatis",
                description = "Buat tagihan secara otomatis setiap tanggal 1 bulan baru untuk seluruh penghuni aktif.",
                isActive = state2,
                onClick = { state ->
                    state2 = state
                }
            )
        }
    }
}