package com.kosrvd.app.feature.billing.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastJoinToString
import com.google.firebase.Timestamp
import com.kosrvd.app.R
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme
import com.kosrvd.app.feature.billing.presentation.models.ListTagihanUi

@Composable
fun ItemTagihanCard(
    modifier: Modifier = Modifier,
    tagihanUi: ListTagihanUi,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 22.dp),
            verticalAlignment = Alignment.Top
        ) {
            Image(
                painter = painterResource(getStatusIcon(tagihanUi.paymentStatus)),
                contentDescription = "Icon Tagihan",
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.width(13.dp))
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = tagihanUi.numberRoom,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = tagihanUi.residentNameList.fastJoinToString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.MiddleEllipsis
                )
            }
            Spacer(Modifier.width(13.dp))
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = tagihanUi.billAmount,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(8.dp))
                StatusText(tagihanUi.paymentStatus)
            }
        }
    }
}

private fun getStatusIcon(status: String): Int {
    return when(status) {
        Constant.BELUM_LUNAS -> R.drawable.ic_bill_error
        Constant.MENUNGGU_VERIFIKASI -> R.drawable.ic_bill_tertiary
        else -> R.drawable.ic_bill_primary
    }
}

@Composable
private fun getStatusColor(status: String): Color {
    return when(status) {
        Constant.BELUM_LUNAS -> MaterialTheme.colorScheme.error
        Constant.MENUNGGU_VERIFIKASI -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.primary
    }
}

@Composable
private fun StatusText(status: String) {
    Text(
        text = status,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold,
        color = getStatusColor(status),
    )
}

@Preview(showBackground = true)
@Composable
private fun ItemTagihanCardPreview() {
    KosRvdAppTheme {
        val tagihanUi = ListTagihanUi(
            idTagihan = "",
            idPenyewa = "",
            numberRoom = "Kamar No 1",
            residentNameList = listOf("Muhammad Gibransyah", "Muhammad Fikri"),
            billAmount = "Rp 1.000.000",
            paymentStatus = "Lunas",
            dateCreated = Timestamp.now(),
            datePaidOff = Timestamp.now(),
            dateUploadProof = Timestamp.now()
        )
        ItemTagihanCard(
            tagihanUi = tagihanUi,
            onClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}