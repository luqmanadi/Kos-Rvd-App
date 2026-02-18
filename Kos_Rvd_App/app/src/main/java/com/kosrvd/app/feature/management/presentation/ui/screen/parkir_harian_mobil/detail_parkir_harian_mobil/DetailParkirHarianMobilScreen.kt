package com.kosrvd.app.feature.management.presentation.ui.screen.parkir_harian_mobil.detail_parkir_harian_mobil

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.kosrvd.app.R
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.domain.utils.CustomToastHostState
import com.kosrvd.app.core.presentation.designsystem.component.appbar.TopBarCenterTitle
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionButton
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionDangerButton
import com.kosrvd.app.core.presentation.designsystem.component.card.ErrorCard
import com.kosrvd.app.core.presentation.designsystem.component.dialog.GeneralDialogConfirmationDanger
import com.kosrvd.app.core.presentation.designsystem.component.text.CustomToastHost
import com.kosrvd.app.core.presentation.utils.shimmerEffect
import com.kosrvd.app.feature.management.presentation.designsystem.component.card.BuktiFotoCard
import com.kosrvd.app.feature.management.presentation.designsystem.component.card.InfoTanggalPemakaianParkiranMobilCard
import com.kosrvd.app.feature.management.presentation.designsystem.utils.ZonaParkirFormatter
import com.kosrvd.app.feature.management.presentation.ui.models.DetailParkirHarianMobilUi
import com.kosrvd.app.feature.management.presentation.ui.screen.parkir_harian_mobil.component.InfoKendaraanDanRIncianBiayaCard
import com.kosrvd.app.feature.management.presentation.ui.screen.parkir_harian_mobil.component.InfoPemakaianParkirCard

@Composable
fun DetailParkirHarianMobilScreen(
    detailParkirHarianMobilUiState: DetailParkirHarianMobilUiState,
    detailParkirHarianMobilActions: (DetailParkirHarianMobilActions) -> Unit,
    customToastHostState: CustomToastHostState
) {
    Scaffold(
        topBar = {
            TopBarCenterTitle(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.detail_usage_daily_parking),
                onBackClick = { detailParkirHarianMobilActions(DetailParkirHarianMobilActions.NavigateBack) },
                isNeedBackIcon = true,
                fontWeight = FontWeight.Bold,
                isActionIcon = true,
                actionIcon = {
                    IconButton(
                        onClick = { detailParkirHarianMobilActions(DetailParkirHarianMobilActions.ShowHapusDialog) }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = stringResource(R.string.delete)
                        )
                    }
                },
                containerColors = MaterialTheme.colorScheme.surfaceContainer
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ){
            when{
                detailParkirHarianMobilUiState.isLoading -> {
                    LoadingDetailParkirHarianMobil()
                }
                detailParkirHarianMobilUiState.loadError != null -> {
                    ErrorCard(
                        message = detailParkirHarianMobilUiState.loadError,
                        onRetry = { detailParkirHarianMobilActions(DetailParkirHarianMobilActions.TryAgain) }
                    )
                }
                detailParkirHarianMobilUiState.dataDetailParkirHarianMobil != null -> {
                    DetailParkirHarianMobilContent(
                        detailParkirHarianMobilUi = detailParkirHarianMobilUiState.dataDetailParkirHarianMobil,
                        detailParkirHarianMobilUiState = detailParkirHarianMobilUiState,
                        detailParkirHarianMobilActions = detailParkirHarianMobilActions
                    )
                }
            }
            CustomToastHost(
                modifier = Modifier.align(Alignment.TopCenter),
                hostState = customToastHostState,
                color = MaterialTheme.colorScheme.error,
                enter = slideInVertically(
                    // Enters by sliding in from offset -fullHeight to 0.
                    initialOffsetY = { fullHeight -> -fullHeight },
                    animationSpec = tween(durationMillis = 150, easing = LinearOutSlowInEasing)
                ),
                exit = slideOutVertically(
                    // Exits by sliding out from offset 0 to -fullHeight.
                    targetOffsetY = { fullHeight -> -fullHeight },
                    animationSpec = tween(durationMillis = 250, easing = LinearOutSlowInEasing)
                )
            )
        }

        if (detailParkirHarianMobilUiState.isHapusDialogVisible){
            GeneralDialogConfirmationDanger(
                onConfirm = {
                    detailParkirHarianMobilActions(DetailParkirHarianMobilActions.DeleteParkirHarianMobil)
                },
                onDismiss = {
                    detailParkirHarianMobilActions(DetailParkirHarianMobilActions.DismissHapusDialog)
                },
                title = stringResource(R.string.title_dialog_confirmation_delete_usage_parking_daily),
                description = stringResource(R.string.description_dialog_confirmation_delete_usage_parking_daily),
                isLoadingButton = detailParkirHarianMobilUiState.isButtonHapusLoading,
                buttonCancelEnabled = !detailParkirHarianMobilUiState.isButtonHapusLoading
            )
        }

        if (detailParkirHarianMobilUiState.isCancelledDialogVisible){
            GeneralDialogConfirmationDanger(
                onConfirm = {
                    detailParkirHarianMobilActions(DetailParkirHarianMobilActions.CancelParkirHarianMobil)
                },
                onDismiss = {
                    detailParkirHarianMobilActions(DetailParkirHarianMobilActions.DismissCancelledDialog)
                },
                title = stringResource(R.string.title_dialog_confirmation_lease_end_usage_parking_daily),
                description = stringResource(R.string.description_dialog_confirmation_lease_end_usage_parking_daily),
                isLoadingButton = detailParkirHarianMobilUiState.isButtonCancelledLoading,
                buttonCancelEnabled = !detailParkirHarianMobilUiState.isButtonCancelledLoading
            )
        }
    }
}

@Composable
private fun LoadingDetailParkirHarianMobil() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Box(
            Modifier
                .size(150.dp, 25.dp)
                .clip(RoundedCornerShape(10.dp))
                .shimmerEffect()
        )
        Box(
            Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(20.dp))
                .shimmerEffect()
        )
        Box(
            Modifier
                .size(150.dp, 25.dp)
                .clip(RoundedCornerShape(10.dp))
                .shimmerEffect()
        )
        Box(
            Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(20.dp))
                .shimmerEffect()
        )
        Box(
            Modifier
                .size(150.dp, 25.dp)
                .clip(RoundedCornerShape(10.dp))
                .shimmerEffect()
        )
        Box(
            Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(20.dp))
                .shimmerEffect()
        )
    }
}

@Composable
private fun DetailParkirHarianMobilContent(
    detailParkirHarianMobilUi: DetailParkirHarianMobilUi,
    detailParkirHarianMobilUiState: DetailParkirHarianMobilUiState,
    detailParkirHarianMobilActions: (DetailParkirHarianMobilActions) -> Unit
) {
    val listState = rememberLazyListState()

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { contetUri ->
        if (contetUri != null) {
            detailParkirHarianMobilActions(DetailParkirHarianMobilActions.UpdateProofOfPayment(contetUri))
        }
    }

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item(key = "Informasi Pemakaian Parkir Header") {
            Text(
                text = stringResource(R.string.information_usage_parking_daily),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
        item { Spacer(Modifier.height(15.dp)) }
        item(key = "Informasi Pemakaian Parkir Card") {
            InfoPemakaianParkirCard(
                modifier = Modifier.fillMaxWidth(),
                idParkir = detailParkirHarianMobilUi.idParkirHarianMobil,
                namaZona = ZonaParkirFormatter.format(detailParkirHarianMobilUi.zoneName),
                namaPenyewa = detailParkirHarianMobilUi.userName,
                catatan = detailParkirHarianMobilUi.notes,
                statusPembayaran = detailParkirHarianMobilUi.paymentStatus,
                statusParkir = detailParkirHarianMobilUi.statusParkir
            )
        }
        item { Spacer(Modifier.height(20.dp)) }
        item(key = "Informasi Kendaraan dan Rincian Biaya Header") {
            Text(
                text = stringResource(R.string.information_transportation_and_bill),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
        item { Spacer(Modifier.height(15.dp)) }
        item(key = "Informasi Kendaraan dan Rincian Biaya Card") {
            InfoKendaraanDanRIncianBiayaCard(
                modifier = Modifier.fillMaxWidth(),
                namaMobil = detailParkirHarianMobilUi.carName,
                merkMobil = detailParkirHarianMobilUi.carBrand,
                platNomor = detailParkirHarianMobilUi.numberPlate,
                nominalSewaParkir = detailParkirHarianMobilUi.totalCost
            )
        }
        item { Spacer(Modifier.height(20.dp)) }
        if (!detailParkirHarianMobilUi.isCancelled){
            item(key = "Bukti Pembayaran Header") {
                Text(
                    text = stringResource(R.string.proof_of_payment),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            item { Spacer(Modifier.height(15.dp)) }
            item(key = "Bukti Pembayaran Card") {
                val previewOnly =
                    detailParkirHarianMobilUi.proofOfPayment != null && detailParkirHarianMobilUi.paymentStatus == Constant.LUNAS
                val uriImage =
                    if (detailParkirHarianMobilUi.proofOfPayment != null && detailParkirHarianMobilUi.paymentStatus == Constant.LUNAS) detailParkirHarianMobilUi.proofOfPayment.toUri() else detailParkirHarianMobilUiState.proofOfPayment
                BuktiFotoCard(
                    modifier = Modifier.fillMaxWidth(),
                    previewOnly = previewOnly,
                    title = stringResource(R.string.title_no_proof_of_payment),
                    description = stringResource(R.string.upload_proof_of_payment_parking_rental),
                    imageUri = uriImage,
                    isCanChooseImage = !previewOnly,
                    isBuktiLaporan = false,
                    onPreviewImage = {
                        detailParkirHarianMobilActions(
                            DetailParkirHarianMobilActions
                                .NavigateToPreviewImage(
                                    uriImage
                                )
                        )
                    },
                    onChooseImage = {
                        photoPicker.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                )
            }
            item { Spacer(Modifier.height(20.dp)) }
        }
        item(key = "Tanggal Header") {
            Text(
                text = stringResource(R.string.date),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
        item { Spacer(Modifier.height(15.dp)) }
        item(key = "Tanggal Card") {
            InfoTanggalPemakaianParkiranMobilCard(
                modifier = Modifier.fillMaxWidth(),
                mulaiSewa = detailParkirHarianMobilUi.startDate,
                selesaiSewa = detailParkirHarianMobilUi.completionDate
            )
        }
        if (detailParkirHarianMobilUi.paymentStatus == Constant.BELUM_LUNAS){
            item { Spacer(Modifier.height(20.dp)) }
            item(key = "Tombol Upload Bukti Pembayaran") {
                ActionButton(
                    modifier = Modifier.fillMaxWidth(),
                    height = 43.dp,
                    onClick = { detailParkirHarianMobilActions(DetailParkirHarianMobilActions.UploadProofOfPayment) },
                    text = stringResource(R.string.proof_upload),
                    shape = RoundedCornerShape(12.dp),
                    isLoading = detailParkirHarianMobilUiState.isButtonUploadLoading,
                    enabled = !detailParkirHarianMobilUiState.isButtonUploadLoading
                )
            }
            item { Spacer(Modifier.height(15.dp)) }
            item(key = "Tombol Batalkan Pemakaian") {
                ActionDangerButton(
                    modifier = Modifier.fillMaxWidth(),
                    height = 43.dp,
                    onClick = { detailParkirHarianMobilActions(DetailParkirHarianMobilActions.ShowCancelledDialog) },
                    text = stringResource(R.string.cancel_usage),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
    }
}