package com.kosrvd.app.core.presentation.designsystem.component.radio_button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalParking
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme
import com.kosrvd.app.feature.management.domain.model.ZonaParkiran
import com.kosrvd.app.feature.management.presentation.designsystem.utils.ZonaParkirFormatter

@Composable
fun CustomRadioButtonV2(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    subtitle: String,
    selected: Boolean,
    enabled: Boolean = true,
    onClick: () -> Unit
) {

    val color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
    val containerColor = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.05f) else Color.Transparent

    Surface(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, color),
        color = containerColor
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Container Ikon (Kotak dengan sudut melengkung)
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color),
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

            // Konten Teks (Judul dan Subjudul)
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            RadioButton(
                selected = selected,
                colors = RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colorScheme.primary,
                    unselectedColor = MaterialTheme.colorScheme.outlineVariant
                ),
                onClick = null
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomRadioButtonV2Preview() {
    KosRvdAppTheme {
        val listZonaParkiran = listOf(
            ZonaParkiran(
                monthlyFee = 100000,
                idZonaParkir = "feojfoejf",
                zoneName = "A",
                dailyCosts = 2000,
                status = "Kosong",
            ),
            ZonaParkiran(
                monthlyFee = 100000,
                idZonaParkir = "feojfoejf",
                zoneName = "B",
                dailyCosts = 2000,
                status = "Kosong",
            )
        )

        val (selectedOption, onOptionSelected) = remember { mutableStateOf(listZonaParkiran[0]) }

        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            listZonaParkiran.forEach { zonaParkiran ->
                CustomRadioButtonV2(
                    icon = Icons.Filled.LocalParking,
                    title = ZonaParkirFormatter.format(zonaParkiran.zoneName),
                    subtitle = "1 Slot Mobil",
                    selected = zonaParkiran == selectedOption,
                    onClick = {
                        onOptionSelected(zonaParkiran)
                    }
                )
            }
        }
    }
}