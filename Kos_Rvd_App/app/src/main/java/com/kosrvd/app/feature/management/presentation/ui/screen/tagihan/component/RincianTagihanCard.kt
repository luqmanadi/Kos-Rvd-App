package com.kosrvd.app.feature.management.presentation.ui.screen.tagihan.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.DoorFront
import androidx.compose.material.icons.filled.ElectricalServices
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Garage
import androidx.compose.material.icons.filled.KingBed
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme
import com.kosrvd.app.feature.management.domain.model.AlatElektronik
import com.kosrvd.app.feature.management.domain.model.Diskon
import com.kosrvd.app.feature.management.presentation.designsystem.component.text.InfoContentColumnText
import com.kosrvd.app.feature.management.presentation.designsystem.component.text.InfoContentColumnTextForAlatElektronik
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toRupiahFormat

@Composable
fun RincianBiayaTagihanCard(
    modifier: Modifier = Modifier,
    adminFees: Boolean,
    highPowerElectronicEquipmentUsageCosts: List<AlatElektronik>,
    roomRentalFee: String,
    carParkingRentalFee: String
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 5.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = MaterialTheme.colorScheme.primary,
                spotColor = MaterialTheme.colorScheme.primary,
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            Modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp)
        ) {
            InfoContentColumnText(
                title = stringResource(R.string.room_rental),
                value = roomRentalFee,
                icon = Icons.Filled.DoorFront,
                modifier = Modifier.weight(1f)
            )
            InfoContentColumnText(
                title = stringResource(R.string.car_parking_rental),
                value = carParkingRentalFee,
                icon = Icons.Filled.Garage,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(Modifier.height(15.dp))
        if (adminFees){
            InfoContentColumnText(
                title = stringResource(R.string.admin_fees),
                value = stringResource(R.string.nominal_admin_fees),
                icon = ImageVector.vectorResource(R.drawable.ic_jumlah_bayar),
                modifier = Modifier.padding(start = 20.dp)
            )
            Spacer(Modifier.height(15.dp))
        }
        InfoContentColumnTextForAlatElektronik(
            modifier = Modifier.padding(start = 20.dp, bottom = 20.dp),
            title = stringResource(R.string.use_of_electronic_devices),
            value = highPowerElectronicEquipmentUsageCosts,
            icon = Icons.Filled.ElectricalServices
        )
    }
}

@Composable
fun RincianBiayaCard(
    modifier: Modifier = Modifier,
    biayaSewaKamar: Long,
    biayaAdmin: Boolean,
    diskon: Diskon?,
    biayaSewaParkir: Long?,
    pemakaianElektronik: List<AlatElektronik>,
    totalTagihan: Long
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Sewa Kamar
                RincianItem(
                    icon = Icons.Filled.KingBed,
                    title = stringResource(R.string.room_rental),
                    subtitle = stringResource(R.string.biaya_bulanan_reguler),
                    amount = biayaSewaKamar.toRupiahFormat()
                )

                // Sewa Parkir
                if (biayaSewaParkir != null && biayaSewaParkir > 0) {
                    Spacer(modifier = Modifier.height(12.dp))
                    RincianItem(
                        icon = Icons.Filled.Garage,
                        title = stringResource(R.string.car_parking_rental),
                        amount = biayaSewaParkir.toRupiahFormat()
                    )
                }

                // Biaya Admin
                if (biayaAdmin) {
                    Spacer(modifier = Modifier.height(12.dp))
                    RincianItem(
                        icon = Icons.Filled.AdminPanelSettings,
                        title = stringResource(R.string.admin_fees_short),
                        amount = stringResource(R.string.nominal_admin_fees)
                    )
                }

                // Pemakaian Elektronik
                if (pemakaianElektronik.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    ElectronicUsageSection(
                        electronics = pemakaianElektronik
                    )
                }

                // Potongan
                if (diskon != null) {
                    Spacer(modifier = Modifier.height(24.dp))
                    PotonganSection(diskon = diskon)
                }
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f)
            )

            // Total footer
            TotalFooter(total = totalTagihan)
        }
    }
}

@Composable
private fun RincianItem(
    icon: Any,
    title: String,
    subtitle: String? = null,
    amount: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconBox(icon = icon)
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Normal
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
        Text(
            text = amount,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun IconBox(icon: Any) {
    Surface(
        modifier = Modifier.size(40.dp),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
    ) {
        Box(contentAlignment = Alignment.Center) {
            when (icon) {
                is ImageVector -> Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(20.dp)
                )
                is Int -> Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun ElectronicUsageSection(
    electronics: List<AlatElektronik>
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconBox(icon = Icons.Filled.FlashOn)
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stringResource(R.string.use_of_electronic_devices),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Normal
            )
        }

        Row(
            modifier = Modifier
                .padding(start = 20.dp, top = 8.dp)
                .height(IntrinsicSize.Min)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(2.dp)
                    .background(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(1.dp)
                    )
            )
            
            Column(modifier = Modifier.padding(start = 32.dp)) {
                electronics.forEach { electronic ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val toolName = if (electronic.cost == 0L) {
                            "${electronic.toolName} (Gratis)"
                        } else {
                            electronic.toolName
                        }
                        Text(
                            text = toolName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = electronic.cost.toRupiahFormat(),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PotonganSection(diskon: Diskon) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Percent,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.potongan),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = diskon.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "- ${diskon.price.toRupiahFormat()}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "${diskon.percent}%",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun TotalFooter(total: Long) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.total_bill),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.outline,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = total.toRupiahFormat(),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RincianBiayaTagihanCardPreview() {
    KosRvdAppTheme {
        RincianBiayaTagihanCard(
            adminFees = true,
            highPowerElectronicEquipmentUsageCosts = listOf(
                AlatElektronik("Magicom", 25000, "User"),
                AlatElektronik("Dispenser", 25000, "User")
            ),
            roomRentalFee = "Rp 595.000",
            carParkingRentalFee = "Rp 100.000"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RincianBiayaCardPreview() {
    KosRvdAppTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            RincianBiayaCard(
                biayaSewaKamar = 595000,
                biayaAdmin = true,
                diskon = Diskon(10, 79500, "Promo Kamar Baru"),
                biayaSewaParkir = 100000,
                pemakaianElektronik = listOf(
                    AlatElektronik("Magicom", 0, "User"),
                    AlatElektronik("Dispenser", 25000, "User")
                ),
                totalTagihan = 715500
            )
        }
    }
}
