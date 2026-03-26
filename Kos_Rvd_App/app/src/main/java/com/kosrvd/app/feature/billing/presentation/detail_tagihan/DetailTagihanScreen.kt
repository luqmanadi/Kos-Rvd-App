package com.kosrvd.app.feature.billing.presentation.detail_tagihan

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionOutlineButton
import com.kosrvd.app.core.presentation.designsystem.component.card.ErrorCard
import com.kosrvd.app.core.presentation.designsystem.component.dialog.GeneralDialogConfirmationDanger
import com.kosrvd.app.core.presentation.designsystem.component.text.CustomToastHost
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme
import com.kosrvd.app.core.presentation.utils.shimmerEffect
import com.kosrvd.app.core.domain.models.AlatElektronik
import com.kosrvd.app.core.presentation.designsystem.organism.card.UnggahBuktiPembayaranCard
import com.kosrvd.app.core.presentation.utils.toDayMonthAndYear
import com.kosrvd.app.core.presentation.utils.toFullIndonesianDateTime
import com.kosrvd.app.feature.billing.presentation.components.BuktiPembayaranTagihanCard
import com.kosrvd.app.feature.billing.presentation.components.InfoDetailTagihanCard
import com.kosrvd.app.feature.billing.presentation.components.MetodePembayaranCardV3
import com.kosrvd.app.feature.billing.presentation.components.MetodePembayaranCardV4
import com.kosrvd.app.feature.billing.presentation.components.RincianBiayaCard
import com.kosrvd.app.feature.billing.presentation.components.StatusTagihanCard
import com.kosrvd.app.feature.billing.presentation.components.TolakTagihanBottomSheet
import com.kosrvd.app.feature.billing.presentation.models.DetailTagihanUi
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DetailTagihanScreen(
    idTagihan: String,
    customToastHostState: CustomToastHostState,
    detailTagihanUiState: DetailTagihanUiState,
    detailTagihanActions: (DetailTagihanActions) -> Unit,
    uploadProofOfPaymentRequester: BringIntoViewRequester
) {

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
                        onClick = { detailTagihanActions(DetailTagihanActions.ShowHapusDialog) }
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
                    uploadProofOfPaymentRequester = uploadProofOfPaymentRequester
                )
            }
        }
    }
}

@Composable
private fun TagihanMainContent(
    modifier: Modifier = Modifier,
    customToastHostState: CustomToastHostState,
    detailTagihanUi: DetailTagihanUi,
    detailTagihanUiState: DetailTagihanUiState,
    detailTagihanActions: (DetailTagihanActions) -> Unit,
    uploadProofOfPaymentRequester: BringIntoViewRequester
) {
    val state = rememberScrollState()

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

    Box(modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .weight(1f)
                    .verticalScroll(state)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Status Tagihan Card
                StatusTagihanCard(
                    alasanPenolakan = detailTagihanUi.rejectionStatement,
                    statusTagihan = detailTagihanUi.paymentStatus
                )

                // Bukti Pembayaran
                if (detailTagihanUi.proofOfPayment != null && detailTagihanUi.dateUploadProof != null) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.proof_of_payment),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                        BuktiPembayaranTagihanCard(
                            proofOfPayment = detailTagihanUi.proofOfPayment,
                            dateUploadProof = detailTagihanUi.dateUploadProof,
                            onClick = {
                                detailTagihanActions(
                                    DetailTagihanActions.NavigateToPreviewImage(
                                        detailTagihanUi.proofOfPayment.toUri()
                                    )
                                )
                            }
                        )
                    }
                }

                // Informasi Tagihan
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.bill_information),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    InfoDetailTagihanCard(
                        detailTagihanUi = detailTagihanUi
                    )
                }

                // Rincian Biaya
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.cost_breakdown),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )

                    RincianBiayaCard(
                        biayaSewaKamar = detailTagihanUi.roomRentalFee,
                        biayaAdmin = detailTagihanUi.adminFees,
                        diskon = detailTagihanUi.diskon,
                        biayaSewaParkir = detailTagihanUi.carParkingRentalFeeMonthly,
                        pemakaianElektronik = detailTagihanUi.highPowerElectronicEquipmentUsageCostsMonthly,
                        totalTagihan = detailTagihanUi.billAmount,
                        sumDayPeriodeBill = detailTagihanUi.sumDayPeriodeBill,
                        prorataDetail = detailTagihanUi.prorataDetail
                    )
                }

                // Metode Pembayaran
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.payment_method),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    if (detailTagihanUi.paymentStatus == Constant.BELUM_LUNAS) {
                        MetodePembayaranCardV3(
                            jumlahTransfer = detailTagihanUi.billAmount,
                            dueDate = detailTagihanUi.dueDate
                        )
                    } else {
                        MetodePembayaranCardV4(totalTransfer = detailTagihanUi.billAmount)
                    }
                }

                // Unggah Bukti Pembayaran
                if (detailTagihanUi.paymentStatus == Constant.BELUM_LUNAS) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        val title = if (detailTagihanUi.rejectionStatement != null) {
                            stringResource(R.string.new_upload_proof)
                        } else {
                            stringResource(R.string.upload_proof)
                        }
                        Text(
                            text = title,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                        UnggahBuktiPembayaranCard(
                            modifier = Modifier.bringIntoViewRequester(
                                uploadProofOfPaymentRequester
                            ),
                            paymentStatus = detailTagihanUi.paymentStatus,
                            uploadProofOfPayment = detailTagihanUiState.proofOfPayment,
                            rejectionStatement = detailTagihanUi.rejectionStatement,
                            onClickPreview = {
                                detailTagihanActions(
                                    DetailTagihanActions.NavigateToPreviewImage(
                                        detailTagihanUiState.proofOfPayment
                                    )
                                )
                            },
                            onClickChooseImage = {
                                photoPicker.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            }
                        )
                    }
                }
            }
            if (detailTagihanUi.paymentStatus == Constant.BELUM_LUNAS) {
                Column {
                    val textButton = when {
                        detailTagihanUi.rejectionStatement != null -> stringResource(R.string.re_confirmation_payment)
                        else -> stringResource(R.string.confirmation_payment)
                    }
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                    )
                    ActionButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        height = 45.dp,
                        onClick = { detailTagihanActions(DetailTagihanActions.BayarOrKirimTagihan) },
                        text = textButton,
                        shape = RoundedCornerShape(15.dp),
                        isLoading = detailTagihanUiState.isButtonKirimOrBayarLoading,
                        enabled = !detailTagihanUiState.isButtonKirimOrBayarLoading,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = textButton
                            )
                        }
                    )

                }
            }
            if (detailTagihanUiState.role == Role.ADMIN && detailTagihanUi.paymentStatus == Constant.MENUNGGU_VERIFIKASI) {
                Column {
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        ActionOutlineButton(
                            modifier = Modifier.weight(1f),
                            onClick = { detailTagihanActions(DetailTagihanActions.ShowTolakDialog) },
                            text = stringResource(R.string.reject),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(15.dp),
                            height = 45.dp,
                            borderStroke = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                            colorsButton = ButtonDefaults.outlinedButtonColors(
                                disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(
                                    alpha = 0.12f
                                ),
                                disabledContentColor = MaterialTheme.colorScheme.onSurface,
                                contentColor = MaterialTheme.colorScheme.error,
                                containerColor = Color.Transparent
                            ),
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.ic_reject),
                                    contentDescription = stringResource(R.string.reject)
                                )
                            }
                        )
                        ActionButton(
                            modifier = Modifier.weight(1f),
                            height = 45.dp,
                            onClick = { detailTagihanActions(DetailTagihanActions.VerifikasiPembayaranTagihan) },
                            text = stringResource(R.string.verification),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(15.dp),
                            isLoading = detailTagihanUiState.isButtonVerifikasiLoading,
                            enabled = !detailTagihanUiState.isButtonVerifikasiLoading,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = stringResource(R.string.verification)
                                )
                            }
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

    if (detailTagihanUiState.isTolakBottomSheetVisible) {
        TolakTagihanBottomSheet(
            modifier = modifier,
            isButtonSendLoading = detailTagihanUiState.isButtonKirimTolakLoading,
            onDismiss = { detailTagihanActions(DetailTagihanActions.DismissTolakDialog) },
            onSend = { detailTagihanActions(DetailTagihanActions.KirimTolakPembayaranTagihan) },
            isAlasanPenolakanError = detailTagihanUiState.isAlasanPenolakanError,
            alasanPenolakanError = detailTagihanUiState.alasanPenolakanError?.asString() ?: "",
            alasanPenolakanState = alasanPenolakanState,
            alasanPenolakanShakeTrigger = detailTagihanUiState.alasanPenolakanShakeTrigger
        )
    }

    if (detailTagihanUiState.isHapusDialogVisible) {
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
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(3) {
            Box(
                Modifier
                    .size(150.dp, 25.dp)
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(10.dp))
                    .shimmerEffect()
            )
            Spacer(Modifier.height(15.dp))
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(20.dp))
                    .shimmerEffect()
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun DetailTagihanScreenPreview() {
    val uploadProofOfPaymentRequester = remember { BringIntoViewRequester() }
    val detailTagihanUi = DetailTagihanUi(
        adminFees = true,
        billAmount = 100299,
        periodStart = Timestamp.now(),
        periodEnd = Timestamp.now(),
        carParkingRentalFeeMonthly = 0,
        dateCreated = Timestamp.now().toDayMonthAndYear(),
        datePaidOff = null,
        dateUploadProof = Timestamp.now().toFullIndonesianDateTime(),
        dueDate = Timestamp.now(),
        highPowerElectronicEquipmentUsageCostsMonthly = listOf(
            AlatElektronik("Setrika", 10000, ""),
            AlatElektronik("Setrika", 10000, ""),
        ),
        idPenyewa = "eifiejf02390293",
        idTagihan = "kfekfje23029303",
        numberRoom = 1,
        paymentStatus = Constant.BELUM_LUNAS,
        proofOfPayment = null,
        rejectionStatement = null,
        residentAccountIdList = listOf("eifiejfiefjief", "wijwijiwjriwjr"),
        residentNameList = listOf("Ndiman", "Januar"),
        roomRentalFee = 500000,
        verificationDate = null,
        diskon = null,
        sumDayPeriodeBill = 30,
        prorataDetail = null
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
            customToastHostState = CustomToastHostState(),
            uploadProofOfPaymentRequester = uploadProofOfPaymentRequester
        )
    }
}