package com.kosrvd.app.feature.complaint.presentation.detail_keluhan

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.kosrvd.app.R
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.domain.utils.CustomToastHostState
import com.kosrvd.app.core.domain.utils.Role
import com.kosrvd.app.core.presentation.designsystem.component.appbar.TopBarCenterTitle
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionButton
import com.kosrvd.app.core.presentation.designsystem.component.card.ErrorCard
import com.kosrvd.app.core.presentation.designsystem.component.dialog.GeneralDialogConfirmationDanger
import com.kosrvd.app.core.presentation.designsystem.component.text.CustomToastHost
import com.kosrvd.app.core.presentation.designsystem.component.text.RequiredLabelText
import com.kosrvd.app.core.presentation.designsystem.component.textfield.GeneralTextField
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme
import com.kosrvd.app.core.presentation.utils.shimmerEffect
import com.kosrvd.app.core.presentation.designsystem.organism.card.BuktiFotoCard
import com.kosrvd.app.core.presentation.designsystem.organism.card.InfoTanggalKeluhanCard
import com.kosrvd.app.feature.complaint.presentation.models.DetailKeluhanUi
import com.kosrvd.app.feature.complaint.presentation.component.InformasiLaporanKeluhanCard
import com.kosrvd.app.feature.complaint.presentation.component.TanggapanKeluhanCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun DetailKeluhanScreen(
    idKeluhan: String,
    detailKeluhanUiState: DetailKeluhanUiState,
    detailKeluhanActions: (DetailKeluhanActions) -> Unit,
    customToastHostState: CustomToastHostState
) {
    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            TopBarCenterTitle(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.detail_complain),
                onBackClick = { detailKeluhanActions(DetailKeluhanActions.NavigateBack) },
                isNeedBackIcon = true,
                fontWeight = FontWeight.Bold,
                isActionIcon = true,
                actionIcon = {
                    IconButton(
                        onClick = { detailKeluhanActions(DetailKeluhanActions.OpenDialog) }
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
            detailKeluhanUiState.isLoading -> {
                LoadingDetailContent(Modifier.padding(innerPadding))
            }
            detailKeluhanUiState.loadError != null -> {
                ErrorCard(
                    modifier = Modifier.padding(innerPadding),
                    message = detailKeluhanUiState.loadError,
                    onRetry = { detailKeluhanActions(DetailKeluhanActions.TryAgain(idKeluhan)) }
                )
            }
            detailKeluhanUiState.detailKeluhanUi != null -> {
                DetailKeluhanContent(
                    modifier = Modifier.padding(innerPadding),
                    listState = listState,
                    detailKeluhanUiState = detailKeluhanUiState,
                    detailKeluhanActions = detailKeluhanActions,
                    customToastHostState = customToastHostState,
                    detailKeluhanUi = detailKeluhanUiState.detailKeluhanUi
                )
            }
        }
    }

}

@Composable
private fun LoadingDetailContent(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 25.dp),
        modifier = modifier.fillMaxSize().imePadding()
    ) {
        item {
            Box(
                Modifier
                    .size(200.dp, 25.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .shimmerEffect()
            )
        }
        item { Spacer(Modifier.height(15.dp)) }
        item {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .shimmerEffect()
            )
        }
        item { Spacer(Modifier.height(20.dp)) }
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
                    .height(48.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .shimmerEffect()
            )
        }
        item { Spacer(Modifier.height(20.dp)) }
        item {
            Box(
                Modifier
                    .size(250.dp, 25.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .shimmerEffect()
            )
        }
        item { Spacer(Modifier.height(15.dp)) }
        item {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .shimmerEffect()
            )
        }
    }
}

@Composable
private fun DetailKeluhanContent(
    modifier: Modifier = Modifier,
    listState : LazyListState,
    detailKeluhanUi: DetailKeluhanUi,
    detailKeluhanUiState: DetailKeluhanUiState,
    detailKeluhanActions: (DetailKeluhanActions) -> Unit,
    customToastHostState: CustomToastHostState
) {
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { contetUri ->
        if (contetUri != null) {
            detailKeluhanActions(DetailKeluhanActions.SetResponseImage(contetUri))
        }
    }

    val responseState = rememberTextFieldState(detailKeluhanUiState.response)
    LaunchedEffect(responseState) {
        snapshotFlow { responseState.text.toString() }.collectLatest {
            detailKeluhanActions(DetailKeluhanActions.SetResponse(it))
        }
    }

    val coroutineScope = rememberCoroutineScope()

    val tanggapanRequester = remember { BringIntoViewRequester() }
    val tanggapanFocus = remember { FocusRequester() }

    LaunchedEffect(
        detailKeluhanUiState.shakeTriggerResponseError
    ) {
        if (detailKeluhanUiState.isResponseError){
            listState.animateScrollToItem(8)
            delay(400)
            tanggapanRequester.bringIntoView()
            tanggapanFocus.requestFocus()
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ){
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 25.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item(key = "Informasi Laporan Keluhan Header") {
                Text(
                    text = stringResource(R.string.information_report_complain),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            item { Spacer(Modifier.height(15.dp)) }
            item(key = "Informasi Laporan Keluhan Card") {
                InformasiLaporanKeluhanCard(
                    modifier = Modifier.fillMaxWidth(),
                    idKeluhan = detailKeluhanUi.idKeluhan,
                    statusLaporan = detailKeluhanUi.complaintStatus,
                    nomorKamar = detailKeluhanUi.numberRoom,
                    namaPelapor = detailKeluhanUi.reporterName,
                    judulLaporan = detailKeluhanUi.title,
                    deskripsiLaporan = detailKeluhanUi.description,
                )
            }
            item { Spacer(Modifier.height(20.dp)) }
            item(key = "Bukti Foto Laporan Header") {
                Text(
                    text = stringResource(R.string.proof_of_complain),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            item { Spacer(Modifier.height(15.dp)) }
            item(key = "Bukti Foto Laporan Card") {
                BuktiFotoCard(
                    modifier = Modifier.fillMaxWidth(),
                    previewOnly = true,
                    title = stringResource(R.string.no_photo_report),
                    description = stringResource(R.string.description_no_photo_report),
                    imageUri = detailKeluhanUi.photoComplaint?.toUri() ?: Uri.EMPTY,
                    isCanChooseImage = false,
                    isBuktiLaporan = true,
                    onPreviewImage = {
                        detailKeluhanActions(
                            DetailKeluhanActions
                                .NavigateToPreviewImage(
                                    detailKeluhanUi.photoComplaint?.toUri() ?: Uri.EMPTY
                                )
                        )
                    },
                )
            }

            if (detailKeluhanUi.complaintStatus == Constant.SEDANG_DIPROSES && detailKeluhanUiState.role == Role.ADMIN){
                item { Spacer(Modifier.height(20.dp)) }
                item(key = "Tanggapan Laporan Keluhan Header") {
                    RequiredLabelText(
                        labelText = stringResource(R.string.response_complain_report),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                item { Spacer(Modifier.height(10.dp)) }
                item(key = "Tanggapan Laporan Keluhan Text Field") {
                    GeneralTextField(
                        modifier = Modifier.fillMaxWidth()
                            .bringIntoViewRequester(tanggapanRequester)
                            .onFocusEvent { focusState ->
                                if (focusState.isFocused) {
                                    coroutineScope.launch {
                                        delay(500)
                                        tanggapanRequester.bringIntoView()
                                    }
                                }
                            },
                        state = responseState,
                        isError = detailKeluhanUiState.isResponseError,
                        error = detailKeluhanUiState.responseError?.asString() ?: "",
                        label = R.string.response_report,
                        shakeTrigger = detailKeluhanUiState.shakeTriggerResponseError,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Text,
                        ),
                        lineLimits = TextFieldLineLimits.MultiLine(3, 5)
                    )
                }
                item { Spacer(Modifier.height(10.dp)) }
                item(key = "Bukti Foto Tanggapan Header") {
                    Text(
                        text = stringResource(R.string.proof_of_photo_response_optional),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                item { Spacer(Modifier.height(15.dp)) }
                item(key = "Bukti Foto Tanggapan Card") {
                    BuktiFotoCard(
                        modifier = Modifier.fillMaxWidth(),
                        previewOnly = false,
                        title = stringResource(R.string.no_photo_response_report_complain),
                        description = stringResource(R.string.description_no_photo_response_report_complain),
                        imageUri = detailKeluhanUiState.responseImage,
                        isCanChooseImage = true,
                        isBuktiLaporan = false,
                        onPreviewImage = {
                            detailKeluhanActions(
                                DetailKeluhanActions
                                    .NavigateToPreviewImage(
                                        detailKeluhanUiState.responseImage
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
            }

            if (detailKeluhanUi.complaintStatus == Constant.SELESAI){
                item { Spacer(Modifier.height(20.dp)) }
                item(key = "Bukti Foto Tanggapan Header") {
                    Text(
                        text = stringResource(R.string.proof_of_photo_response),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                item { Spacer(Modifier.height(15.dp)) }
                item(key = "Tanggapan Card") {
                    TanggapanKeluhanCard(
                        modifier = Modifier.fillMaxWidth(),
                        text = detailKeluhanUi.response ?: ""
                    )
                }
                item { Spacer(Modifier.height(15.dp)) }
                item(key = "Bukti Foto Tanggapan Card") {
                    BuktiFotoCard(
                        modifier = Modifier.fillMaxWidth(),
                        previewOnly = true,
                        title = stringResource(R.string.no_photo_response_report_complain),
                        description = stringResource(R.string.description_no_photo_response_report_complain),
                        imageUri = detailKeluhanUi.photoResponse?.toUri() ?: Uri.EMPTY,
                        isCanChooseImage = false,
                        isBuktiLaporan = false,
                        onPreviewImage = {
                            detailKeluhanActions(
                                DetailKeluhanActions
                                    .NavigateToPreviewImage(
                                        detailKeluhanUi.photoResponse?.toUri() ?: Uri.EMPTY
                                    )
                            )
                        }
                    )
                }
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
                InfoTanggalKeluhanCard(
                    modifier = Modifier.fillMaxWidth(),
                    dibuat = detailKeluhanUi.reportDate,
                    diProses = detailKeluhanUi.processDate,
                    selesai = detailKeluhanUi.completionDate
                )
            }
            if (detailKeluhanUi.complaintStatus == Constant.MENUNGGU_KONFIRMASI && detailKeluhanUiState.role == Role.ADMIN){
                item { Spacer(Modifier.height(20.dp)) }
                item(key = "Tombol Mulai Proses Laporan Keluhan") {
                    ActionButton(
                        modifier = Modifier.fillMaxWidth(),
                        height = 43.dp,
                        onClick = { detailKeluhanActions(DetailKeluhanActions.ProsesKeluhan) },
                        text = stringResource(R.string.start_process),
                        shape = RoundedCornerShape(12.dp),
                        isLoading = detailKeluhanUiState.isButtonProsesLoading,
                        enabled = !detailKeluhanUiState.isButtonProsesLoading
                    )
                }
            }
            if (detailKeluhanUi.complaintStatus == Constant.SEDANG_DIPROSES && detailKeluhanUiState.role == Role.ADMIN){
                item { Spacer(Modifier.height(20.dp)) }
                item(key = "Tombol Tandai Selesai Laporan Keluhan") {
                    ActionButton(
                        modifier = Modifier.fillMaxWidth(),
                        height = 43.dp,
                        onClick = { detailKeluhanActions(DetailKeluhanActions.SelesaiKeluhan) },
                        text = stringResource(R.string.mark_as_complete),
                        shape = RoundedCornerShape(12.dp),
                        isLoading = detailKeluhanUiState.isButtonSelesaiLoading,
                        enabled = !detailKeluhanUiState.isButtonSelesaiLoading
                    )
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

    if (detailKeluhanUiState.isShowDialogDeleteVisible){
        GeneralDialogConfirmationDanger(
            onConfirm = {
                detailKeluhanActions(DetailKeluhanActions.DeleteKeluhan)
            },
            onDismiss = {
                detailKeluhanActions(DetailKeluhanActions.DismissDialog)
            },
            title = stringResource(R.string.title_dialog_confirmation_delete_report),
            description = stringResource(R.string.description_dialog_confirmation_delete_report),
            isLoadingButton = detailKeluhanUiState.isButtonDeleteLoading,
            buttonCancelEnabled = !detailKeluhanUiState.isButtonDeleteLoading
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun DetailKeluhanScreenPreview() {
    KosRvdAppTheme {
        val detailKeluhanUi = DetailKeluhanUi(
            complaintStatus = Constant.SEDANG_DIPROSES,
            completionDate = null,
            description = "egokogkrogkorgk",
            idKeluhan = "eofjeo03094",
            idAkun = "jfieji20029320",
            numberRoom = "1",
            photoComplaint = "323",
            photoResponse = null,
            processDate = "18 Nov 2020",
            reportDate = "18 Nov 2020",
            reporterName = "gorkgo",
            response = null,
            title = "eofekoeok"
        )
        DetailKeluhanScreen(
            idKeluhan = "gorkgo",
            detailKeluhanUiState = DetailKeluhanUiState(
                isLoading = false,
                detailKeluhanUi = detailKeluhanUi,
                loadError = null,
                role = Role.ADMIN,
            ),
            detailKeluhanActions = {  },
            customToastHostState = CustomToastHostState()
        )
    }
}
