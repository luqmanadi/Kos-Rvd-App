package com.kosrvd.app.feature.parking.parkir_harian_mobil.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Garage
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.presentation.designsystem.molecul.text.InfoContentColumnText

@Composable
fun InfoKendaraanDanRIncianBiayaCard(
    modifier: Modifier = Modifier,
    namaMobil: String,
    merkMobil: String,
    platNomor: String,
    nominalSewaParkir: String
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
            Row {
                InfoContentColumnText(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.name_car),
                    value = namaMobil,
                    icon = Icons.Filled.Title
                )
                InfoContentColumnText(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.brand_car),
                    value = merkMobil,
                    icon = Icons.Filled.DirectionsCar
                )
            }
            Spacer(Modifier.height(15.dp))
            Row {
                InfoContentColumnText(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.number_police),
                    value = platNomor,
                    icon = Icons.Filled.Abc
                )
                InfoContentColumnText(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.parking_rent),
                    value = nominalSewaParkir,
                    icon = Icons.Filled.Garage
                )
            }
        }
    }
}