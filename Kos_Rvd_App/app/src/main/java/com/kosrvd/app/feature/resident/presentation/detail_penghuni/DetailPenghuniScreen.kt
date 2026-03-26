package com.kosrvd.app.feature.resident.presentation.detail_penghuni

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.presentation.designsystem.component.appbar.TopBarLeftTitle
import com.kosrvd.app.core.presentation.designsystem.component.card.DataEmptyCard
import com.kosrvd.app.core.presentation.designsystem.component.card.ErrorCard
import com.kosrvd.app.core.presentation.designsystem.component.text.BackgroundInfoText
import com.kosrvd.app.core.presentation.utils.shimmerEffect
import com.kosrvd.app.core.presentation.designsystem.atom.image.LoadImage
import com.kosrvd.app.feature.resident.presentation.models.DetailPenghuniUi
import com.kosrvd.app.feature.resident.presentation.component.InfoPenghuniCard

@Composable
fun DetailPenghuniScreen(
    detailPenghuniUiState: DetailPenghuniUiState,
    detailPenghuniAction: (DetailPenghuniActions) -> Unit,
    idPenghuni: String
) {
    Scaffold(
        topBar = {
            TopBarLeftTitle(
                title = stringResource(R.string.detail_resident),
                onBackClick = { detailPenghuniAction(DetailPenghuniActions.NavigateBack) }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) {  innerPadding ->
        when {
            detailPenghuniUiState.isLoading -> {
                LoadDataDetailPenghuni(
                    modifier = Modifier.padding(innerPadding)
                )
            }
            detailPenghuniUiState.loadError != null -> {
                ErrorCard(
                    modifier = Modifier.padding(innerPadding),
                    message = detailPenghuniUiState.loadError,
                    onRetry = { detailPenghuniAction(DetailPenghuniActions.TryAgain(idPenghuni)) }
                )
            }
            else -> {
                MainContent(
                    modifier = Modifier.padding(innerPadding),
                    dataPenghuni = detailPenghuniUiState.penghuni
                )
            }
        }
    }
}

@Composable
fun LoadDataDetailPenghuni(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 40.dp),
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Box(
                Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .shimmerEffect()
            )
        }
        item { Spacer(Modifier.height(16.dp)) }
        item {
            Box(
                Modifier
                    .size(width = 180.dp, height = 24.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .shimmerEffect()
            )
        }
        item { Spacer(Modifier.height(5.dp)) }
        item {
            Box(
                Modifier
                    .size(width = 60.dp, height = 24.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .shimmerEffect()
            )
        }
        item { Spacer(Modifier.height(31.dp)) }
        item {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .shimmerEffect()
            )
        }
        item { Spacer(Modifier.height(15.dp)) }
        item {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .shimmerEffect()
            )
        }
        item { Spacer(Modifier.height(15.dp)) }
        item {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .shimmerEffect()
            )
        }
    }
}

@Composable
private fun MainContent(
    modifier: Modifier = Modifier,
    dataPenghuni: DetailPenghuniUi? = null
) {
    if (dataPenghuni == null){
        DataEmptyCard(
            modifier = modifier,
            title = "Data Penghuni Tidak Ditemukan",
            image = painterResource(R.drawable.ic_empty_data)
        )
    } else {
        DetailPenghuniContent(
            modifier = modifier,
            dataPenghuni = dataPenghuni
        )
    }
}

@Composable
private fun DetailPenghuniContent(
    modifier: Modifier = Modifier,
    dataPenghuni: DetailPenghuniUi
){
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { Spacer(Modifier.height(20.dp)) }
        item {
            LoadImage(
                url = dataPenghuni.photo,
                modifier = Modifier.size(120.dp),
                contentDescription = "Foto Penghuni",
                shape = CircleShape,
                errorImg = R.drawable.ic_fill_profile
            )
        }
        item { Spacer(Modifier.height(16.dp)) }
        item {
            Text(
                text = dataPenghuni.name,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
        }
        item { Spacer(Modifier.height(5.dp)) }
        item {
            BackgroundInfoText(
                text = dataPenghuni.status,
                colorBg = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        item { Spacer(Modifier.height(31.dp)) }
        item {
            InfoPenghuniCard(
                isBasicInformation = true,
                dataPenghuni = dataPenghuni,
                iconInfoCard = Icons.Default.Info,
                titleInfoCard = stringResource(R.string.basic_information)
            )
        }
        item { Spacer(Modifier.height(15.dp)) }
        item {
            InfoPenghuniCard(
                isContact = true,
                dataPenghuni = dataPenghuni,
                iconInfoCard = Icons.Default.Phone,
                titleInfoCard = stringResource(R.string.phone_number)
            )
        }
        item { Spacer(Modifier.height(15.dp)) }
        item {
            InfoPenghuniCard(
                isAddress = true,
                dataPenghuni = dataPenghuni,
                iconInfoCard = Icons.Default.LocationOn,
                titleInfoCard = stringResource(R.string.address)
            )
        }
    }
}