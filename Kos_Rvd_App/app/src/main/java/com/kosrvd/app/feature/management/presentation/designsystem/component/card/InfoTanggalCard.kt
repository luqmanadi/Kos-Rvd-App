package com.kosrvd.app.feature.management.presentation.designsystem.component.card

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme
import com.kosrvd.app.feature.management.presentation.designsystem.component.text.InfoContentColumnText

@Composable
fun InfoTanggalTagihanCard(
    modifier: Modifier = Modifier,
    dibuat: String?,
    uploadBukti: String?,
    jatuhTempo: String?,
    dibayar: String?,
) {
    val bottomPadding = if (uploadBukti == null) 20.dp else 0.dp
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = bottomPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            InfoContentColumnText(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.made),
                value = dibuat ?: "-",
                icon = ImageVector.vectorResource(R.drawable.calendar_add_on)
            )
            InfoContentColumnText(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.due_date),
                value = jatuhTempo ?: "-",
                icon = ImageVector.vectorResource(R.drawable.calendar_clock)
            )
        }
        if (uploadBukti != null){
            Spacer(Modifier.height(15.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                InfoContentColumnText(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.proof_upload),
                    value = uploadBukti,
                    icon = Icons.Filled.EditCalendar
                )
                if (dibayar!=null){
                    InfoContentColumnText(
                        modifier = Modifier.weight(1f),
                        title = stringResource(R.string.pay),
                        value = dibayar,
                        icon = Icons.Filled.EventAvailable
                    )
                }
            }
        }
    }
}

@Composable
fun InfoTanggalKeluhanCard(
    modifier: Modifier = Modifier,
    dibuat: String?,
    diProses: String?,
    selesai: String?,
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            InfoContentColumnText(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.made),
                value = dibuat ?: "-",
                icon = ImageVector.vectorResource(R.drawable.calendar_add_on)
            )
            InfoContentColumnText(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.processed),
                value = diProses ?: "-",
                icon = ImageVector.vectorResource(R.drawable.calendar_clock)
            )
        }
        Spacer(Modifier.height(15.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            InfoContentColumnText(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.finish),
                value = selesai ?: "-",
                icon = Icons.Filled.EventAvailable
            )
        }
    }
}

@Composable
fun InfoTanggalPemakaianParkiranMobilCard(
    modifier: Modifier = Modifier,
    mulaiSewa: String,
    selesaiSewa: String
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            InfoContentColumnText(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.rental_begin),
                value = mulaiSewa,
                icon = ImageVector.vectorResource(R.drawable.calendar_add_on)
            )
            InfoContentColumnText(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.rental_done),
                value = selesaiSewa,
                icon = Icons.Filled.EventAvailable
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InfoTanggalTagihanCardPreview() {
    KosRvdAppTheme {
        InfoTanggalTagihanCard(
            modifier = Modifier.padding(20.dp),
            dibuat = "24 Januari 2020",
            uploadBukti = "26 Januari 2020",
            jatuhTempo = "15 Januari 2025",
            dibayar = "76 Januari 2020"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun InfoTanggalKeluhanCardPreview() {
    KosRvdAppTheme {
        InfoTanggalKeluhanCard(
            modifier = Modifier.padding(20.dp),
            dibuat = "15 Januari 2020",
            diProses = null,
            selesai = null
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun InfoTanggalPemakaianParkiranMobilPreview() {
    KosRvdAppTheme {
        InfoTanggalPemakaianParkiranMobilCard(
            modifier = Modifier.padding(20.dp),
            mulaiSewa = "15 Januari 2025",
            selesaiSewa = "76 Januari 2020"
        )
    }
}