package com.kosrvd.app.feature.management.presentation.ui.screen.notifikasi.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme
import com.kosrvd.app.feature.management.presentation.designsystem.utils.TypeNotification
import com.kosrvd.app.feature.management.presentation.ui.models.NotificationUi

@Composable
fun ItemNotificationCard(
    modifier: Modifier = Modifier,
    dataNotificationUi: NotificationUi,
    onItemClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
            .background(
                color = if (!dataNotificationUi.alreadyRead) MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f) else Color.Transparent,
            )
            .clickable(onClick = onItemClick)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = chooseIconByTypeNotif(typeNotif = dataNotificationUi.title),
                    contentDescription = "Icon Tipe Notif",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = dataNotificationUi.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = dataNotificationUi.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                verticalAlignment = Alignment.Top
            ) {
                Spacer(Modifier.width(34.dp))
                Text(
                    text = dataNotificationUi.content,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(18.dp))
                if (!dataNotificationUi.alreadyRead){
                    Badge(containerColor = MaterialTheme.colorScheme.error)
                }
            }
        }
        HorizontalDivider()
    }

}

@Composable
private fun chooseIconByTypeNotif(
    typeNotif: String
): Painter {
    return when(typeNotif){
        Constant.TITLE_PENGUMUMAN -> painterResource(R.drawable.ic_pengumuman)
        Constant.TITLE_PEMBAYARAN_TAGIHAN_DITOLAK -> painterResource(R.drawable.ic_bill_error)
        Constant.TITLE_TAGIHAN_BELUM_LUNAS -> painterResource(R.drawable.ic_bill_error)
        Constant.TITLE_TAGIHAN_PERLU_VERIFIKASI -> painterResource(R.drawable.ic_bill_tertiary)
        Constant.TITLE_TAGIHAN_LUNAS -> painterResource(R.drawable.ic_bill_primary)
        Constant.TITLE_LAPORAN_PERLU_KONFIRMASI -> painterResource(R.drawable.ic_report_menunggu_konfirmasi)
        Constant.TITLE_LAPORAN_DIPROSES -> painterResource(R.drawable.ic_report_process)
        Constant.TITLE_LAPORAN_SELESAI -> painterResource(R.drawable.ic_report_completion)
        else -> painterResource(R.drawable.ic_bill_error)
    }
}

@Preview(showBackground = true)
@Composable
private fun ItemNotificationCardPreview() {
    KosRvdAppTheme {
        ItemNotificationCard(
            modifier = Modifier,
            dataNotificationUi = NotificationUi(
                idNotifikasi = "",
                idAkun = "",
                idDetailReferensi = "",
                title = "Pembayaran Tagihan ditolak",
                content = "Pembayaran ditolak. Segera lakukan pembayaran tagihan kamar no 3. paling lambat 15 Oktober 2025, 23:59",
                date = "12 Oktober 2025, 07:00",
                alreadyRead = false,
                typeNotification = TypeNotification.TAGIHAN
            ),
            onItemClick = {}
        )   
    }
}