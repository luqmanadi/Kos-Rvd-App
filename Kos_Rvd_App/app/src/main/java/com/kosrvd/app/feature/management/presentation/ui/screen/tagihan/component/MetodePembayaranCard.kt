package com.kosrvd.app.feature.management.presentation.ui.screen.tagihan.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import com.kosrvd.app.R
import com.kosrvd.app.core.presentation.designsystem.component.line.DottedHorizontalLine
import com.kosrvd.app.core.presentation.designsystem.component.text.BackgroundInfoText
import com.kosrvd.app.core.presentation.designsystem.component.text.CopyableNumberField
import com.kosrvd.app.core.presentation.designsystem.component.text.PaymentDeadlineText
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toRupiahFormat

@Composable
fun MetodePembayaranCardV1(
    modifier: Modifier = Modifier,
    dueDate: Timestamp,
    jumlahTransfer: Long
) {
    val context = LocalContext.current
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
        PaymentDeadlineText(
            deadline = dueDate,
            modifier = Modifier.padding(start = 20.dp, top = 15.dp)
        )
        Spacer(Modifier.height(15.dp))
        DottedHorizontalLine(Modifier)
        Spacer(Modifier.height(15.dp))
        AccountBankInfo(modifier = Modifier.padding(start = 20.dp))
        Spacer(Modifier.height(20.dp))
        CopyableNumberField(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp),
            number = stringResource(R.string.number_account_bank),
            textToCopy = stringResource(R.string.number_account_bank),
            context = context
        )
        Spacer(Modifier.height(25.dp))
        Text(
            text = stringResource(R.string.sum_transfer),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 20.dp)
        )
        Spacer(Modifier.height(15.dp))
        CopyableNumberField(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
            number = jumlahTransfer.toRupiahFormat(),
            textToCopy = jumlahTransfer.toString(),
            context = context
        )
    }
}

@Composable
fun AccountBankInfo(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(55.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ){
            Image(
                painter = painterResource(R.drawable.ic_bank_mandiri),
                contentDescription = stringResource(R.string.name_bank)
            )
        }
        Spacer(Modifier.width(15.dp))
        Column{
            Text(
                text = stringResource(R.string.name_bank),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(5.dp))
            Text(
                text = stringResource(R.string.name_mother_kos),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun MetodePembayaranCardV2(
    modifier: Modifier = Modifier,
    jumlahTransfer: Long
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
        AccountBankInfo(modifier = Modifier.padding(start = 20.dp, top = 20.dp))
        Spacer(Modifier.height(15.dp))
        DottedHorizontalLine()
        Spacer(Modifier.height(15.dp))
        Row(
            Modifier.padding(start = 20.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.account_number),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.weight(1f))
            BackgroundInfoText(
                text = stringResource(R.string.number_account_bank),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium,
                textColor = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(Modifier.height(15.dp))
        Row(
            Modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.sum_transfer),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.weight(1f))
            BackgroundInfoText(
                text = jumlahTransfer.toRupiahFormat(),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium,
                textColor = MaterialTheme.colorScheme.onSurface
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun MetodePembayaranCardV1Preview() {
    KosRvdAppTheme {
        MetodePembayaranCardV1(
            modifier = Modifier.padding(10.dp),
            dueDate = Timestamp.now(),
            jumlahTransfer = 1000000
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MetodePembayaranCardV2Preview() {
    KosRvdAppTheme {
        MetodePembayaranCardV2(
            modifier = Modifier.padding(10.dp),
            jumlahTransfer = 1000000
        )
    }
}