package com.kosrvd.app.feature.dashboard.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import com.kosrvd.app.R
import com.kosrvd.app.core.presentation.designsystem.component.text.StatusBackgroundText
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme
import com.kosrvd.app.core.presentation.utils.toDayMonthAndYear
import com.kosrvd.app.core.presentation.utils.toDayMonthShortAndYear
import com.kosrvd.app.core.presentation.utils.toNumberRoomFormat
import com.kosrvd.app.core.presentation.utils.toRupiahFormat
import com.kosrvd.app.feature.dashboard.presentation.models.LastBill

@Composable
fun InfoLastBillCard(
    modifier: Modifier = Modifier,
    numberRoom: String,
    lastBill: LastBill? = null,
    navigateToDetailBill: () -> Unit,
) {
    if (lastBill != null){
        Card (
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .clickable(
                    onClick = navigateToDetailBill
                )
            ,
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = stringResource(R.string.title_bill_new),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold

                    )
                    Spacer(modifier = Modifier.weight(1f))
                    StatusBackgroundText(
                        status = lastBill.paymentStatus,
                        textAlign = TextAlign.Right
                    )
                }
                Text(
                    text = numberRoom,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(5.dp))
                HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "Periode: ${lastBill.periodStart} - ${lastBill.periodEnd}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = lastBill.total,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Jatuh Tempo: ${lastBill.dueDate}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(10.dp))
                HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.look_detail),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = stringResource(R.string.look_detail),
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    } else {
        Card (
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = stringResource(R.string.title_bill_new),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold

                )
                Spacer(Modifier.height(15.dp))
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(20.dp).fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_tagihan_kosong),
                            contentDescription = stringResource(R.string.no_bill_kos)
                        )
                        Spacer(Modifier.height(15.dp))
                        Text(
                            text = stringResource(R.string.no_bill_kos),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.height(5.dp))
                        Text(
                            text = stringResource(R.string.description_no_bill_kos),
                            style = MaterialTheme.typography.bodySmall
                        )

                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InfoLastBillCardPreview() {
    KosRvdAppTheme {
        val dueDate = Timestamp.now()
        val amount : Long = 550000
        val numberRoom = 8
        val lastBill = LastBill(
            idTagihan = "ofjoejfeojfoejfoef",
            paymentStatus = "Menunggu Verifikasi",
            total = amount.toRupiahFormat(),
            dueDate = dueDate.toDayMonthAndYear(),
            periodEnd = dueDate.toDayMonthShortAndYear(),
            periodStart = dueDate.toDayMonthShortAndYear()
        )
        InfoLastBillCard(
            numberRoom = numberRoom.toNumberRoomFormat(),
            lastBill = lastBill,
            navigateToDetailBill = {},
            modifier = Modifier.padding(10.dp)
        )
    }
}