package com.kosrvd.app.feature.management.presentation.ui.screen.tagihan.detail_tagihan

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.google.firebase.Timestamp
import com.kosrvd.app.R
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.domain.utils.CustomToastHostState
import com.kosrvd.app.core.domain.utils.Role
import com.kosrvd.app.core.presentation.designsystem.component.appbar.TopBarCenterTitle
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionButton
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionDangerButton
import com.kosrvd.app.core.presentation.designsystem.component.card.ErrorCard
import com.kosrvd.app.core.presentation.designsystem.component.dialog.GeneralDialogConfirmationDanger
import com.kosrvd.app.core.presentation.designsystem.component.text.CustomToastHost
import com.kosrvd.app.core.presentation.designsystem.component.text.RequiredLabelText
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme
import com.kosrvd.app.core.presentation.utils.shimmerEffect
import com.kosrvd.app.feature.management.presentation.designsystem.component.card.BuktiFotoCard
import com.kosrvd.app.feature.management.presentation.designsystem.component.card.InfoTanggalTagihanCard
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toDayMonthAndYear
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toNumber
import com.kosrvd.app.feature.management.presentation.ui.models.DetailTagihanUi
import com.kosrvd.app.feature.management.presentation.ui.screen.tagihan.component.InformasiTagihanCard
import com.kosrvd.app.feature.management.presentation.ui.screen.tagihan.component.MetodePembayaranCardV1
import com.kosrvd.app.feature.management.presentation.ui.screen.tagihan.component.MetodePembayaranCardV2
import com.kosrvd.app.feature.management.presentation.ui.screen.tagihan.component.RejectionPaymentCard
import com.kosrvd.app.feature.management.presentation.ui.screen.tagihan.component.RincianBiayaTagihanCard
import com.kosrvd.app.feature.management.presentation.ui.screen.tagihan.component.TolakTagihanBottomSheet
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DetailTagihanScreen(
    idTagihan: String,
    customToastHostState: CustomToastHostState,
    detailTagihanUiState: DetailTagihanUiState,
    detailTagihanActions: (DetailTagihanActions) -> Unit,
) {
    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            TopBarCenterTitle(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.detail_bill),
                onBackClick = { detailTagihanActions(DetailTagihanActions.NavigateBack) },
                isNeedBackIcon = true,
                fontWeight = FontWeight.Bold,
                isActionIcon = detailTagihanUiState.role == Role.ADMIN,
                actionIcon = {
                    IconButton(
                        onClick = {detailTagihanActions(DetailTagihanActions.ShowHapusDialog)}
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
        when {
            detailTagihanUiState.isLoading -> {
                LoadingTagihanContent(Modifier.padding(innerPadding))
            }

            detailTagihanUiState.loadError != null -> {
                ErrorCard(
                    modifier = Modifier.padding(innerPadding),
                    message = detailTagihanUiState.loadError,
                    onRetry = { detailTagihanActions(DetailTagihanActions.TryAgain(idTagihan)) }
                )
            }

            detailTagihanUiState.detailTagihanUi != null -> {
                TagihanMainContent(
                    modifier = Modifier.padding(innerPadding),
                    customToastHostState = customToastHostState,
                    detailTagihanUiState = detailTagihanUiState,
                    detailTagihanActions = detailTagihanActions,
                    detailTagihanUi = detailTagihanUiState.detailTagihanUi,
                    listState = listState
                )
            }
        }
    }
}

@Composable
private fun TagihanMainContent(
    modifier: Modifier = Modifier,
    listState: LazyListState,
    customToastHostState: CustomToastHostState,
    detailTagihanUi: DetailTagihanUi,
    detailTagihanUiState: DetailTagihanUiState,
    detailTagihanActions: (DetailTagihanActions) -> Unit
) {
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { contetUri ->
        if (contetUri != null) {
            detailTagihanActions(DetailTagihanActions.UpdateProofOfPayment(contetUri))
        }
    }

    val alasanPenolakanState = rememberTextFieldState(detailTagihanUiState.alasanPenolakan)
    LaunchedEffect(alasanPenolakanState) {
        snapshotFlow { alasanPenolakanState.text.toString() }.collectLatest {
            detailTagihanActions(DetailTagihanActions.UpdateAlasanPenolakan(it))
        }
    }

    Box(modifier.fillMaxSize()){
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 25.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item(key = "Informasi Tagihan Header") {
                Text(
                    text = stringResource(R.string.bill_information),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            item { Spacer(Modifier.height(15.dp)) }
            item(key = "Informasi Tagihan Card") {
                InformasiTagihanCard(
                    modifier = Modifier.fillMaxWidth(),
                    idTagihanOrIdPenyewaan = detailTagihanUi.idTagihan,
                    numberRoom = detailTagihanUi.numberRoom?.toNumber() ?: "",
                    month = detailTagihanUi.billingMonth,
                    residentNameList = detailTagihanUi.residentNameList,
                    paymentStatus = detailTagihanUi.paymentStatus
                )
            }
            item { Spacer(Modifier.height(20.dp)) }
            item(key = "Rincian Biaya Header") {
                Text(
                    text = stringResource(R.string.cost_breakdown),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            item { Spacer(Modifier.height(15.dp)) }
            item(key = "Rincian Biaya Card") {
                RincianBiayaTagihanCard(
                    modifier = Modifier.fillMaxWidth(),
                    adminFees = detailTagihanUi.adminFees,
                    highPowerElectronicEquipmentUsageCosts = detailTagihanUi.highPowerElectronicEquipmentUsageCostsMonthly,
                    roomRentalFee = detailTagihanUi.roomRentalFee,
                    carParkingRentalFee = detailTagihanUi.carParkingRentalFeeMonthly
                )
            }
            item { Spacer(Modifier.height(20.dp)) }
            item(key = "Metode Pembayaran Transfer Header") {
                Text(
                    text = stringResource(R.string.payment_method),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            item { Spacer(Modifier.height(15.dp)) }
            if (detailTagihanUi.paymentStatus == Constant.BELUM_LUNAS) {
                item(key = "Metode Pembayaran Transfer Card V1") {
                    MetodePembayaranCardV1(
                        modifier = Modifier.fillMaxWidth(),
                        dueDate = detailTagihanUi.dueDate,
                        jumlahTransfer = detailTagihanUi.billAmount
                    )
                }
            } else {
                item(key = "Metode Pembayaran Transfer Card V2") {
                    MetodePembayaranCardV2(
                        modifier = Modifier.fillMaxWidth(),
                        jumlahTransfer = detailTagihanUi.billAmount
                    )
                }
            }
            item { Spacer(Modifier.height(20.dp)) }
            item(key = "Bukti Pembayaran Header") {
                Text(
                    text = stringResource(R.string.proof_of_payment),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            item { Spacer(Modifier.height(15.dp)) }
            if (detailTagihanUi.rejectionStatement != null && detailTagihanUi.paymentStatus == Constant.BELUM_LUNAS) {
                item(key = "Bukti Pembayaran Sebelumnya Header") {
                    Text(
                        text = stringResource(R.string.previous_proof_of_payment),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                item { Spacer(Modifier.height(15.dp)) }
                item(key = "Alasan Penolakan Card") {
                    RejectionPaymentCard(
                        modifier = Modifier.fillMaxWidth(),
                        dateUploadProofOfPayment = detailTagihanUi.dateUploadProof ?: Timestamp.now()
                            .toDayMonthAndYear(),
                        response = detailTagihanUi.rejectionStatement
                    )
                }
                item { Spacer(Modifier.height(15.dp)) }
                item(key = "Bukti Pembayaran Sebelumnya Card") {
                    BuktiFotoCard(
                        modifier = Modifier.fillMaxWidth(),
                        previewOnly = true,
                        title = "",
                        description = "",
                        imageUri = (detailTagihanUi.proofOfPayment ?: "").toUri(),
                        isCanChooseImage = false,
                        isBuktiLaporan = false,
                        onPreviewImage = {
                            detailTagihanActions(
                                DetailTagihanActions.NavigateToPreviewImage(
                                    detailTagihanUi.proofOfPayment?.toUri()?: Uri.EMPTY
                                )
                            )
                        }
                    )
                }
                item { Spacer(Modifier.height(15.dp)) }
                item(key = "Bukti Pembayaran Sekarang Header") {
                    RequiredLabelText(labelText = stringResource(R.string.proof_of_payment_now))
                }
                item { Spacer(Modifier.height(15.dp)) }
            }

            item(key = "Bukti Pembayaran Card") {
                val previewOnly =
                    detailTagihanUi.paymentStatus == Constant.MENUNGGU_VERIFIKASI || detailTagihanUi.paymentStatus == Constant.LUNAS
                val uriImage =
                    if (detailTagihanUi.proofOfPayment != null && (detailTagihanUi.paymentStatus == Constant.MENUNGGU_VERIFIKASI || detailTagihanUi.paymentStatus == Constant.LUNAS)) detailTagihanUi.proofOfPayment.toUri() else detailTagihanUiState.proofOfPayment
                BuktiFotoCard(
                    modifier = Modifier.fillMaxWidth(),
                    previewOnly = previewOnly,
                    title = stringResource(R.string.title_no_proof_of_payment),
                    description = stringResource(R.string.description_no_proof_of_payment),
                    imageUri = uriImage,
                    isCanChooseImage = !previewOnly,
                    isBuktiLaporan = false,
                    onPreviewImage = {
                        detailTagihanActions(
                            DetailTagihanActions
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
            item(key = "Tanggal Header") {
                Text(
                    text = stringResource(R.string.date),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            item { Spacer(Modifier.height(15.dp)) }
            item(key = "Tanggal Card") {
                InfoTanggalTagihanCard(
                    modifier = Modifier.fillMaxWidth(),
                    dibuat = detailTagihanUi.dateCreated,
                    uploadBukti = if (detailTagihanUi.paymentStatus != Constant.BELUM_LUNAS) detailTagihanUi.dateUploadProof else null,
                    jatuhTempo = detailTagihanUi.dueDate.toDayMonthAndYear(),
                    dibayar = detailTagihanUi.datePaidOff
                )
            }
            if (detailTagihanUi.paymentStatus == Constant.BELUM_LUNAS) {
                item { Spacer(Modifier.height(20.dp)) }
                item(key = "Tombol Bayar atau Kirim Bukti Pembayaran Tagihan") {
                    val textButton = if (detailTagihanUiState.role != Role.PENGHUNI) stringResource(R.string.send) else stringResource(R.string.pay)
                    ActionButton(
                        modifier = Modifier.fillMaxWidth(),
                        height = 43.dp,
                        onClick = { detailTagihanActions(DetailTagihanActions.BayarOrKirimTagihan) },
                        text = textButton,
                        shape = RoundedCornerShape(12.dp),
                        isLoading = detailTagihanUiState.isButtonKirimOrBayarLoading,
                        enabled = !detailTagihanUiState.isButtonKirimOrBayarLoading
                    )
                }
            }
            if (detailTagihanUiState.role == Role.ADMIN && detailTagihanUi.paymentStatus == Constant.MENUNGGU_VERIFIKASI) {
                item { Spacer(Modifier.height(20.dp)) }
                item(key = "Tombol Verifikasi atau Tolak Pembayaran Tagihan") {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        ActionDangerButton(
                            modifier = Modifier.weight(1f),
                            height = 43.dp,
                            onClick = { detailTagihanActions(DetailTagihanActions.ShowTolakDialog) },
                            text = stringResource(R.string.reject),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ActionButton(
                            modifier = Modifier.weight(1f),
                            height = 43.dp,
                            onClick = { detailTagihanActions(DetailTagihanActions.VerifikasiPembayaranTagihan) },
                            text = stringResource(R.string.verification),
                            shape = RoundedCornerShape(12.dp),
                            isLoading = detailTagihanUiState.isButtonVerifikasiLoading,
                            enabled = !detailTagihanUiState.isButtonVerifikasiLoading
                        )
                    }
                }
            }
        }

        CustomToastHost(
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

    if (detailTagihanUiState.isTolakBottomSheetVisible){
        TolakTagihanBottomSheet(
            modifier = modifier,
            isButtonSendLoading = detailTagihanUiState.isButtonKirimTolakLoading,
            onDismiss = {detailTagihanActions(DetailTagihanActions.DismissTolakDialog)},
            onSend = {detailTagihanActions(DetailTagihanActions.KirimTolakPembayaranTagihan)},
            isAlasanPenolakanError = detailTagihanUiState.isAlasanPenolakanError,
            alasanPenolakanError = detailTagihanUiState.alasanPenolakanError?.asString()?: "",
            alasanPenolakanState = alasanPenolakanState,
            alasanPenolakanShakeTrigger = detailTagihanUiState.alasanPenolakanShakeTrigger
        )
    }

    if (detailTagihanUiState.isHapusDialogVisible){
        GeneralDialogConfirmationDanger(
            onConfirm = {
                detailTagihanActions(DetailTagihanActions.HapusTagihan)
            },
            onDismiss = {
                detailTagihanActions(DetailTagihanActions.DismissHapusDialog)
            },
            title = stringResource(R.string.title_dialog_confirmation_delete_bill),
            description = stringResource(R.string.description_dialog_confirmation_delete_bill),
            isLoadingButton = detailTagihanUiState.isButtonHapusLoading
        )
    }
}

@Composable
private fun LoadingTagihanContent(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 25.dp),
        modifier = modifier.fillMaxSize()
    ) {
        item {
            Box(
                Modifier
                    .size(150.dp, 25.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .shimmerEffect()
            )
        }
        item { Spacer(Modifier.height(15.dp)) }
        item {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(391.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .shimmerEffect()
            )
        }
        item { Spacer(Modifier.height(20.dp)) }
        item {
            Box(
                Modifier
                    .size(111.dp, 25.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .shimmerEffect()
            )
        }
        item { Spacer(Modifier.height(15.dp)) }
        item {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(310.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .shimmerEffect()
            )
        }
        item { Spacer(Modifier.height(20.dp)) }
        item {
            Box(
                Modifier
                    .size(240.dp, 25.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .shimmerEffect()
            )
        }
        item { Spacer(Modifier.height(15.dp)) }
        item {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(310.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .shimmerEffect()
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun DetailTagihanScreenPreview() {
    val detailTagihanUi = DetailTagihanUi(
        adminFees = true,
        billAmount = 100299,
        billingMonth = "November 2025",
        carParkingRentalFeeMonthly = "Rp 0",
        dateCreated = Timestamp.now().toDayMonthAndYear(),
        datePaidOff = null,
        dateUploadProof = Timestamp.now().toDayMonthAndYear(),
        dueDate = Timestamp.now(),
        highPowerElectronicEquipmentUsageCostsMonthly = emptyList(),
        idPenyewa = "eifiejf02390293",
        idTagihan = "kfekfje23029303",
        numberRoom = 1,
        paymentStatus = Constant.MENUNGGU_VERIFIKASI,
        proofOfPayment = null,
        rejectionStatement = null,
        residentAccountIdList = listOf("eifiejfiefjief", "wijwijiwjriwjr"),
        residentNameList = listOf("Ndiman", "Januar"),
        roomRentalFee = "Rp 100.299",
        verificationDate = null
    )
    val detailTagihanUiState = DetailTagihanUiState(
        isLoading = false,
        detailTagihanUi = detailTagihanUi,
        loadError = null,
        isButtonKirimOrBayarLoading = false,
        isButtonHapusLoading = false,
        isButtonVerifikasiLoading = false,
        isButtonKirimTolakLoading = false,
        isHapusDialogVisible = false,
        isTolakBottomSheetVisible = false,
        alasanPenolakan = "",
        isAlasanPenolakanError = false,
        alasanPenolakanError = null,
        alasanPenolakanShakeTrigger = 0,
        proofOfPayment = Uri.EMPTY,
        role = Role.ADMIN
    )
    KosRvdAppTheme {
        DetailTagihanScreen(
            detailTagihanUiState = detailTagihanUiState,
            detailTagihanActions = {},
            idTagihan = detailTagihanUi.idTagihan,
            customToastHostState = CustomToastHostState()
        )
    }
}