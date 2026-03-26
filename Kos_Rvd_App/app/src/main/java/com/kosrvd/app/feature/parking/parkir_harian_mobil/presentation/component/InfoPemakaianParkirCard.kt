package com.kosrvd.app.feature.parking.parkir_harian_mobil.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.ChangeCircle
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.presentation.designsystem.molecul.text.InfoContentColumnText

@Composable
fun InfoPemakaianParkirCard(
    modifier: Modifier = Modifier,
    idParkir: String,
    namaZona: String,
    namaPenyewa: String,
    catatan: String,
    statusPembayaran: String,
    statusParkir: String
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
        Column(
            Modifier.padding(20.dp)
        ) {
            InfoContentColumnText(
                title = stringResource(R.string.id_parking_car_daily),
                value = idParkir,
                icon = Icons.AutoMirrored.Filled.Article
            )
            Spacer(Modifier.height(15.dp))
            InfoContentColumnText(
                title = stringResource(R.string.name_zone),
                value = namaZona,
                icon = ImageVector.vectorResource(R.drawable.ic_zona_parkir)
            )
            Spacer(Modifier.height(15.dp))
            InfoContentColumnText(
                title = stringResource(R.string.name_tenant),
                value = namaPenyewa,
                icon = Icons.Filled.People
            )
            Spacer(Modifier.height(15.dp))
            InfoContentColumnText(
                title = stringResource(R.string.note),
                value = catatan,
                icon = Icons.AutoMirrored.Filled.Comment
            )
            Spacer(Modifier.height(15.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                if (statusParkir != Constant.DIBATALKAN) {
                    InfoContentColumnText(
                        title = stringResource(R.string.payment_status),
                        value = statusPembayaran,
                        isUseInfoStatus = true,
                        icon = Icons.Filled.ChangeCircle
                    )
                }
                InfoContentColumnText(
                    title = stringResource(R.string.status_parking),
                    value = statusParkir,
                    isUseInfoStatus = true,
                    icon = Icons.Filled.ChangeCircle
                )
            }
        }
    }
}