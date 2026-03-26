package com.kosrvd.app.feature.billing.presentation.components

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme
import com.kosrvd.app.core.domain.models.AlatElektronik
import com.kosrvd.app.core.presentation.utils.toRupiahFormat
import com.kosrvd.app.feature.billing.domain.model.Diskon
import com.kosrvd.app.feature.billing.domain.model.ProrataDetail

@Composable
fun RincianBiayaCard(
    modifier: Modifier = Modifier,
    biayaSewaKamar: Long,
    biayaAdmin: Boolean,
    diskon: Diskon?,
    biayaSewaParkir: Long?,
    pemakaianElektronik: List<AlatElektronik>,
    totalTagihan: Long,
    sumDayPeriodeBill: Int,
    prorataDetail: ProrataDetail?
) {
    val isProrata = prorataDetail != null

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
                    subtitle = if (!isProrata) stringResource(R.string.biaya_bulanan_reguler) else null,
                    amount = if (isProrata) prorataDetail.proSewaKamar.toRupiahFormat() else biayaSewaKamar.toRupiahFormat(),
                    originalAmount = if (isProrata) biayaSewaKamar.toRupiahFormat() else null,
                    prorataDays = if (isProrata) sumDayPeriodeBill else null
                )

                // Sewa Parkir
                if (biayaSewaParkir != null && biayaSewaParkir > 0) {
                    Spacer(modifier = Modifier.height(16.dp))
                    RincianItem(
                        icon = Icons.Filled.Garage,
                        title = stringResource(R.string.car_parking_rental),
                        amount = if (isProrata) prorataDetail.proSewaParkir?.toRupiahFormat()
                            ?: "" else biayaSewaParkir.toRupiahFormat(),
                        originalAmount = if (isProrata) biayaSewaParkir.toRupiahFormat() else null,
                        prorataDays = if (isProrata) sumDayPeriodeBill else null
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
                    Spacer(modifier = Modifier.height(16.dp))
                    ElectronicUsageSection(
                        electronics = pemakaianElektronik,
                        isProrata = isProrata,
                        prorataDays = sumDayPeriodeBill,
                        prorataPrices = prorataDetail?.proSewaElektronik
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
    amount: String,
    originalAmount: String? = null, // Parameter untuk harga coret
    prorataDays: Int? = null        // Parameter untuk memunculkan badge
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
            if (prorataDays != null) {
                // Badge Prorata
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = "Prorata ($prorataDays hari)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            } else if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
        // Bagian Harga (Coret dan Final)
        Column(horizontalAlignment = Alignment.End) {
            if (originalAmount != null) {
                Text(
                    text = originalAmount,
                    style = MaterialTheme.typography.bodySmall.copy(
                        textDecoration = TextDecoration.LineThrough
                    ),
                    color = MaterialTheme.colorScheme.outline
                )
            }
            Text(
                text = amount,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun IconBox(icon: Any) {
    Surface(
        modifier = Modifier.size(40.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
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
    electronics: List<AlatElektronik>,
    isProrata: Boolean,
    prorataDays: Int,
    prorataPrices: List<Long>?
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconBox(icon = Icons.Filled.FlashOn)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = stringResource(R.string.use_of_electronic_devices),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                if (isProrata) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            text = "Prorata ($prorataDays hari)",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(
                                horizontal = 8.dp,
                                vertical = 2.dp
                            )
                        )
                    }
                }
            }
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
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(1.dp)
                    )
            )

            Column(modifier = Modifier.padding(start = 32.dp)) {
                electronics.forEachIndexed { index, electronic ->
                    val isGratis = electronic.cost == 0L
                    val finalPrice = if (isProrata && !isGratis) prorataPrices?.getOrNull(index)
                        ?.toRupiahFormat() ?: "" else electronic.cost.toRupiahFormat()
                    val originalPrice =
                        if (isProrata && !isGratis) electronic.cost.toRupiahFormat() else null
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isGratis) "${electronic.toolName} (Gratis)" else electronic.toolName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Column(horizontalAlignment = Alignment.End) {
                            if (originalPrice != null) {
                                Text(
                                    text = originalPrice,
                                    style = MaterialTheme.typography.bodySmall.copy(textDecoration = TextDecoration.LineThrough),
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                            Text(
                                text = finalPrice,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
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
        shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
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
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
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
private fun RincianBiayaCardPreview() {
    KosRvdAppTheme {
        val prorataDetail = ProrataDetail(
            proSewaKamar = 350000,
            proSewaParkir = 50000,
            proSewaElektronik = listOf(0, 12000, 0)
        )
        Box(modifier = Modifier.padding(16.dp)) {
            RincianBiayaCard(
                biayaSewaKamar = 595000,
                biayaAdmin = true,
                diskon = Diskon(10, 79500, "Promo Kamar Baru"),
                biayaSewaParkir = 100000,
                pemakaianElektronik = listOf(
                    AlatElektronik("Magicom", 0, "User"),
                    AlatElektronik("Dispenser", 25000, "User"),
                    AlatElektronik("AC", 0, "User")
                ),
                totalTagihan = 715500,
                sumDayPeriodeBill = 15,
                prorataDetail = prorataDetail
            )
        }
    }
}