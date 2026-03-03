package com.kosrvd.app.feature.management.presentation.ui.screen.parkir_harian_mobil.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Garage
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.presentation.designsystem.component.text.IconTextInfo
import com.kosrvd.app.feature.management.presentation.designsystem.utils.ZonaParkirFormatter
import com.kosrvd.app.feature.management.presentation.ui.models.ListParkirHarianMobilUi

@Composable
fun ItemParkirHarianMobilCard(
    modifier: Modifier = Modifier,
    listParkirHarianMobilUi: ListParkirHarianMobilUi,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp).fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Filled.Garage,
                contentDescription = stringResource(R.string.title_usage_parking_daily),
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(8.dp)
            )
            Spacer(Modifier.width(15.dp))
            Column {
                Text(
                    text = listParkirHarianMobilUi.userName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(5.dp))
                IconTextInfo(
                    text = ZonaParkirFormatter.format(listParkirHarianMobilUi.zoneName),
                    icon = ImageVector.vectorResource(R.drawable.ic_zona_parkir),
                    sizeIcon = 10.dp,
                    colorIcon = MaterialTheme.colorScheme.secondary,
                    colorText = MaterialTheme.colorScheme.secondary,
                    spacing = 5.dp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(5.dp))
                IconTextInfo(
                    text = listParkirHarianMobilUi.numberPlate,
                    icon = Icons.Filled.DirectionsCar,
                    sizeIcon = 10.dp,
                    spacing = 5.dp,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(Modifier.weight(1f))
            Spacer(Modifier.width(10.dp))
            IconTextInfo(
                text = listParkirHarianMobilUi.status,
                icon = Icons.Filled.Circle,
                colorText = MaterialTheme.colorScheme.onSurface,
                colorIcon = when(listParkirHarianMobilUi.status) {
                    Constant.DIPESAN -> MaterialTheme.colorScheme.secondary
                    Constant.DIBATALKAN -> MaterialTheme.colorScheme.error
                    Constant.DIPAKAI -> MaterialTheme.colorScheme.tertiary
                    else -> MaterialTheme.colorScheme.primary
                },
                sizeIcon = 10.dp,
                textStyle = MaterialTheme.typography.bodyMedium,
                spacing = 5.dp
            )
            Spacer(Modifier.width(5.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "Next",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}