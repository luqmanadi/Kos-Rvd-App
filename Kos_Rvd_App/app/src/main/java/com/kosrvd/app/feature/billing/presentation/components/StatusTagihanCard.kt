package com.kosrvd.app.feature.billing.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.PendingActions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme

@Composable
fun StatusTagihanCard(
    modifier: Modifier = Modifier,
    alasanPenolakan: String?,
    statusTagihan: String
) {
    val isRejected = alasanPenolakan != null && statusTagihan == Constant.BELUM_LUNAS

    val config = when {
        isRejected -> {
            StatusConfig(
                stringResource(R.string.payment_rejected),
                alasanPenolakan,
                Icons.Outlined.Error,
                MaterialTheme.colorScheme.errorContainer,
                MaterialTheme.colorScheme.onErrorContainer
            )
        }
        statusTagihan == Constant.MENUNGGU_VERIFIKASI -> {
            StatusConfig(
                stringResource(R.string.waiting_for_verification),
                stringResource(R.string.description_payment_success),
                Icons.Outlined.PendingActions,
                MaterialTheme.colorScheme.tertiaryContainer, // tertiaryFixed placeholder
                MaterialTheme.colorScheme.onTertiaryContainer // onTertiaryFixed placeholder
            )
        }
        statusTagihan == Constant.LUNAS -> {
            StatusConfig(
                stringResource(R.string.pard_off_v_2),
                stringResource(R.string.description_payment_bill_or_verification_success),
                Icons.Outlined.CheckCircle,
                MaterialTheme.colorScheme.secondaryContainer,
                MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
        else -> null
    }

    if (config == null) return

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = config.containerColor),
        border = BorderStroke(
            width = 1.dp,
            color = config.contentColor.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = config.icon,
                contentDescription = null,
                tint = config.contentColor,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = config.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = config.contentColor
                )
                Spacer(Modifier.height(5.dp))
                Text(
                    text = config.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = config.contentColor.copy(alpha = 0.8f)
                )
            }
        }
    }
}

private data class StatusConfig(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val containerColor: Color,
    val contentColor: Color
)

@Preview(showBackground = true)
@Composable
private fun StatusTagihanCardPreview() {
    KosRvdAppTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatusTagihanCard(
                statusTagihan = Constant.MENUNGGU_VERIFIKASI,
                alasanPenolakan = null
            )
            StatusTagihanCard(
                statusTagihan = Constant.BELUM_LUNAS,
                alasanPenolakan = "Bukti transfer yang diunggah buram dan nominal tidak terbaca dengan jelas. Silakan unggah ulang bukti yang valid."
            )
            StatusTagihanCard(
                statusTagihan = Constant.LUNAS,
                alasanPenolakan = null
            )
            StatusTagihanCard(
                statusTagihan = Constant.BELUM_LUNAS,
                alasanPenolakan = null
            )
        }
    }
}