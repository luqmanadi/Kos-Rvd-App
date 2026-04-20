package com.kosrvd.app.feature.complaint.presentation.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ChangeCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DoorFront
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme
import com.kosrvd.app.core.presentation.designsystem.molecul.text.InfoContentRowText
import com.kosrvd.app.core.presentation.utils.toNumber

@Composable
fun InformasiLaporanKeluhanCard(
    modifier: Modifier = Modifier,
    idKeluhan: String,
    statusLaporan: String,
    nomorKamar: String,
    namaPelapor: String,
    judulLaporan: String,
    deskripsiLaporan: String
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
        InfoContentRowText(
            modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp),
            title = stringResource(R.string.id_complain),
            value = idKeluhan,
            icon = Icons.AutoMirrored.Filled.Article
        )
        Spacer(Modifier.height(15.dp))
        InfoContentRowText(
            modifier = Modifier.padding(horizontal = 20.dp),
            title = stringResource(R.string.status),
            value = statusLaporan,
            isKeluhanStatusInfo = true,
            icon = Icons.Filled.ChangeCircle
        )
        Spacer(Modifier.height(15.dp))
        InfoContentRowText(
            modifier = Modifier.padding(horizontal = 20.dp),
            title = stringResource(R.string.room),
            value = nomorKamar.toNumber(),
            icon = Icons.Filled.DoorFront
        )
        Spacer(Modifier.height(15.dp))
        InfoContentRowText(
            modifier = Modifier.padding(horizontal = 20.dp),
            title = stringResource(R.string.reporter_name),
            value = namaPelapor,
            icon = Icons.Filled.AccountBox
        )
        Spacer(Modifier.height(15.dp))
        InfoContentRowText(
            modifier = Modifier.padding(horizontal = 20.dp),
            title = stringResource(R.string.title),
            value = judulLaporan,
            icon = Icons.Filled.Title
        )
        Spacer(Modifier.height(15.dp))
        InfoContentRowText(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
            title = stringResource(R.string.description),
            value = deskripsiLaporan,
            icon = Icons.Filled.Description
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun InformasiLaporanKeluhanCardPreview() {
    KosRvdAppTheme {
        InformasiLaporanKeluhanCard(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 15.dp),
            idKeluhan = "wodkwodkowkdowkdowkd",
            statusLaporan = "Selesai",
            nomorKamar = "10",
            namaPelapor = "Adi Adi Adi",
            judulLaporan = "Bocor Parah ",
            deskripsiLaporan = "ejfiejfiejfijwofwrnv iwijwrf ipwjefiwjf"
        )
    }
}