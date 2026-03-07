package com.kosrvd.app.feature.management.presentation.ui.screen.penyewaan.component

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
import androidx.compose.material.icons.filled.AirlineSeatFlat
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastJoinToString
import com.kosrvd.app.R
import com.kosrvd.app.core.presentation.designsystem.component.text.IconTextInfo
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toNumberRoomFormat
import com.kosrvd.app.feature.management.presentation.ui.models.ListPenyewaanUi

@Composable
fun ItemPenyewaCard(
    modifier: Modifier = Modifier,
    listPenyewaanUi: ListPenyewaanUi,
    onClick: () -> Unit
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
                imageVector = if (listPenyewaanUi.listNamePenghuni.size>1) Icons.Filled.Group else Icons.Filled.Person,
                contentDescription = stringResource(R.string.list_rental),
                tint = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(8.dp)
            )
            Spacer(Modifier.width(15.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = listPenyewaanUi.listNamePenghuni.fastJoinToString(separator = " & "),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(5.dp))
                IconTextInfo(
                    text = listPenyewaanUi.numberRoom.toNumberRoomFormat(),
                    icon = Icons.Filled.AirlineSeatFlat,
                    colorText = MaterialTheme.colorScheme.secondary,
                    colorIcon = MaterialTheme.colorScheme.secondary,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    sizeIcon = 24.dp,
                    spacing = 5.dp
                )
            }
            Spacer(Modifier.width(10.dp))
            IconTextInfo(
                text = listPenyewaanUi.rentalStatus,
                icon = Icons.Filled.Circle,
                colorText = MaterialTheme.colorScheme.onSurface,
                colorIcon = if (listPenyewaanUi.rentalStatus.capitalize(Locale.current) == "Aktif") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                sizeIcon = 10.dp,
                textStyle = MaterialTheme.typography.bodyMedium,
                spacing = 5.dp
            )
            Spacer(Modifier.width(10.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "Next",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}