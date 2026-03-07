package com.kosrvd.app.feature.management.presentation.ui.screen.keluhan.buat_laporan_keluhan

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.domain.utils.CustomToastHostState
import com.kosrvd.app.core.presentation.designsystem.component.appbar.TopBarCenterTitle
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionButton
import com.kosrvd.app.core.presentation.designsystem.component.text.CustomToastHost
import com.kosrvd.app.core.presentation.designsystem.component.text.IconTextInfo
import com.kosrvd.app.core.presentation.designsystem.component.textfield.GeneralTextField
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme
import com.kosrvd.app.feature.management.presentation.designsystem.component.card.BuktiFotoCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun BuatLaporanKeluhanScreen(
    buatLaporanKeluhanUiState: BuatLaporanKeluhanUiState,
    buatLaporanKeluhanActions: (BuatLaporanKeluhanActions) -> Unit,
    titleShakeTrigger: Int,
    descriptionShakeTrigger: Int,
    customToastHostState: CustomToastHostState,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            TopBarCenterTitle(
                title = stringResource(R.string.make_a_complain_report),
                onBackClick = {
                    keyboardController?.hide()
                    buatLaporanKeluhanActions(BuatLaporanKeluhanActions.NavigateUp)
                              },
                containerColors = MaterialTheme.colorScheme.surfaceContainer,
                fontWeight = FontWeight.Bold
            )
        }
    ) { innerPadding ->
        MainContent(
            buatLaporanKeluhanUiState = buatLaporanKeluhanUiState,
            buatLaporanKeluhanActions = buatLaporanKeluhanActions,
            modifier = Modifier.padding(innerPadding),
            titleShakeTrigger = titleShakeTrigger,
            descriptionShakeTrigger = descriptionShakeTrigger,
            customToastHostState = customToastHostState,
            keyboardController = keyboardController
        )
    }
}

@Composable
private fun MainContent (
    buatLaporanKeluhanUiState: BuatLaporanKeluhanUiState,
    buatLaporanKeluhanActions: (BuatLaporanKeluhanActions) -> Unit,
    modifier: Modifier = Modifier,
    titleShakeTrigger: Int,
    descriptionShakeTrigger: Int,
    customToastHostState: CustomToastHostState,
    keyboardController: SoftwareKeyboardController?
) {
    val scrollState = rememberScrollState()
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { contetUri ->
        if (contetUri != null){
            buatLaporanKeluhanActions(BuatLaporanKeluhanActions.UpdateImage(contetUri))
        }
    }

    val titleState = rememberTextFieldState(buatLaporanKeluhanUiState.title)
    LaunchedEffect(titleState) {
        snapshotFlow { titleState.text.toString() }.collectLatest {
            buatLaporanKeluhanActions(BuatLaporanKeluhanActions.UpdateTitle(it))
        }
    }

    val descriptionState = rememberTextFieldState(buatLaporanKeluhanUiState.description)
    LaunchedEffect(descriptionState){
        snapshotFlow { descriptionState.text.toString() }.collectLatest {
            buatLaporanKeluhanActions(BuatLaporanKeluhanActions.UpdateDescription(it))
        }
    }

    val coroutineScope = rememberCoroutineScope()

    val titleRequester = remember { BringIntoViewRequester() }
    val titleFocus = remember { FocusRequester() }

    val descriptionRequester = remember { BringIntoViewRequester() }
    val descriptionFocus = remember { FocusRequester() }

    LaunchedEffect(
        titleShakeTrigger,
        descriptionShakeTrigger
    ) {
        if (buatLaporanKeluhanUiState.isTitleError && buatLaporanKeluhanUiState.isDescriptionError) {
            delay(400)
            titleRequester.bringIntoView()
            titleFocus.requestFocus()
        }
        else if (buatLaporanKeluhanUiState.isTitleError) {
            delay(400)
            titleRequester.bringIntoView()
            titleFocus.requestFocus()
        }
        else if (buatLaporanKeluhanUiState.isDescriptionError){
            delay(400)
            descriptionRequester.bringIntoView()
            descriptionFocus.requestFocus()
        }
    }

    Box(modifier = modifier.fillMaxSize()){
        Column(
            Modifier.fillMaxSize().imePadding()
        ){
            Column(
                modifier = Modifier.weight(1f).verticalScroll(scrollState).padding(horizontal = 16.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                IconTextInfo(
                    modifier = Modifier,
                    text = stringResource(R.string.make_a_complain_report),
                    icon = painterResource(R.drawable.ic_pengumuman),
                    colorText = MaterialTheme.colorScheme.onSurface,
                    sizeIcon = 28.dp,
                    textStyle = MaterialTheme.typography.titleMedium,
                    spacing = 15.dp,
                    fontWeight = FontWeight.Bold
                )
                GeneralTextField(
                    state = titleState,
                    isError = buatLaporanKeluhanUiState.isTitleError,
                    error = buatLaporanKeluhanUiState.titleError?.asString()?: "",
                    label = R.string.title,
                    modifier = Modifier.fillMaxWidth()
                        .bringIntoViewRequester(titleRequester)
                        .onFocusEvent { focusState ->
                            if (focusState.isFocused) {
                                coroutineScope.launch {
                                    delay(500)
                                    titleRequester.bringIntoView()
                                }
                            }
                        },
                    shakeTrigger = titleShakeTrigger,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Text,
                    ),
                    lineLimits = TextFieldLineLimits.SingleLine,
                    focusRequester = titleFocus
                )
                GeneralTextField(
                    state = descriptionState,
                    isError = buatLaporanKeluhanUiState.isDescriptionError,
                    error = buatLaporanKeluhanUiState.descriptionError?.asString()?: "",
                    label = R.string.description,
                    modifier = Modifier.fillMaxWidth()
                        .bringIntoViewRequester(descriptionRequester)
                        .onFocusEvent { focusState ->
                            if (focusState.isFocused) {
                                coroutineScope.launch {
                                    delay(500)
                                    descriptionRequester.bringIntoView()
                                }
                            }
                        },
                    shakeTrigger = descriptionShakeTrigger,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Default,
                        keyboardType = KeyboardType.Text,
                    ),
                    lineLimits = TextFieldLineLimits.MultiLine(
                        minHeightInLines = 3,
                        maxHeightInLines = 5
                    ),
                    focusRequester = descriptionFocus
                )
                Text(
                    text = stringResource(R.string.proof_of_complain_optional),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                BuktiFotoCard(
                    modifier = Modifier.fillMaxWidth(),
                    previewOnly = false,
                    title = stringResource(R.string.no_photo_report),
                    description = stringResource(R.string.description_no_photo_report),
                    imageUri = buatLaporanKeluhanUiState.photoReport,
                    isCanChooseImage = true,
                    isBuktiLaporan = false,
                    onChooseImage = {
                        photoPicker.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    onPreviewImage = {
                        buatLaporanKeluhanActions(BuatLaporanKeluhanActions.OnPreviewImage(buatLaporanKeluhanUiState.photoReport))
                    }
                )
            }
            Column {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                )
                ActionButton(
                    text = stringResource(R.string.make_a_complain),
                    onClick = {
                        keyboardController?.hide()
                        buatLaporanKeluhanActions(BuatLaporanKeluhanActions.BuatLaporanKeluhan)
                              },
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    enabled = !buatLaporanKeluhanUiState.isButtonLoading,
                    isLoading = buatLaporanKeluhanUiState.isButtonLoading,
                    shape = RoundedCornerShape(12.dp)
                )
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
}


@Preview(showSystemUi = true)
@Composable
private fun BuatLaporanKeluhanScreenPreview() {
    KosRvdAppTheme {
        BuatLaporanKeluhanScreen(
            buatLaporanKeluhanUiState = BuatLaporanKeluhanUiState(),
            buatLaporanKeluhanActions = {},
            titleShakeTrigger = 0,
            descriptionShakeTrigger = 0,
            customToastHostState = CustomToastHostState()
        )
    }
}