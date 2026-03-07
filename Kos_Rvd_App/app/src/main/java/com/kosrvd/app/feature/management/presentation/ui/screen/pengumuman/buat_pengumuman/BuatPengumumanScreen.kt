package com.kosrvd.app.feature.management.presentation.ui.screen.pengumuman.buat_pengumuman

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.domain.utils.CustomToastHostState
import com.kosrvd.app.core.presentation.designsystem.component.appbar.TopBarLeftTitle
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionButton
import com.kosrvd.app.core.presentation.designsystem.component.text.CustomToastHost
import com.kosrvd.app.core.presentation.designsystem.component.text.IconTextInfo
import com.kosrvd.app.core.presentation.designsystem.component.textfield.GeneralTextField
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun BuatPengumumanScreen(
    buatPengumumanUiState: BuatPengumumanUiState,
    buatPengumumanActions: (BuatPengumumanActions) -> Unit,
    customToastHostState: CustomToastHostState,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Scaffold(
        topBar = {
            TopBarLeftTitle(
                title = stringResource(R.string.create_announcement),
                onBackClick = {
                    keyboardController?.hide()
                    buatPengumumanActions(BuatPengumumanActions.NavigateBack)
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize())
        {

            BuatPengumumanContent(
                buatPengumumanUiState = buatPengumumanUiState,
                buatPengumumanActions = buatPengumumanActions,
                keyboardController = keyboardController
            )

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
}

@Composable
private fun BuatPengumumanContent(
    buatPengumumanUiState: BuatPengumumanUiState,
    buatPengumumanActions: (BuatPengumumanActions) -> Unit,
    keyboardController:  SoftwareKeyboardController? = null
) {
    val titleState = rememberTextFieldState(buatPengumumanUiState.title)
    LaunchedEffect(titleState) {
        snapshotFlow { titleState.text.toString() }.collectLatest {
            buatPengumumanActions(BuatPengumumanActions.UpdateTitle(it))
        }
    }
    val descriptionState = rememberTextFieldState(buatPengumumanUiState.description)
    LaunchedEffect(descriptionState) {
        snapshotFlow { descriptionState.text.toString() }.collectLatest {
            buatPengumumanActions(BuatPengumumanActions.UpdateDescription(it))
        }
    }
    val listState = rememberLazyListState()

    val coroutineScope = rememberCoroutineScope()

    val titleRequester = remember { BringIntoViewRequester() }
    val titleFocus = remember { FocusRequester() }

    val descriptionRequester = remember { BringIntoViewRequester() }
    val descriptionFocus = remember { FocusRequester() }

    LaunchedEffect(
        buatPengumumanUiState.shakeTargetTitle,
        buatPengumumanUiState.shakeTargetDescription,
    ) {
        if (buatPengumumanUiState.isTitleError) {
            listState.animateScrollToItem(2)
            delay(400)
            titleRequester.bringIntoView()
            titleFocus.requestFocus()
        }
        else if (buatPengumumanUiState.isDescriptionError) {
            listState.animateScrollToItem(4)
            delay(400)
            descriptionRequester.bringIntoView()
            descriptionFocus.requestFocus()
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize().imePadding(),
        contentPadding = PaddingValues(vertical = 20.dp, horizontal = 16.dp)
    ) {
        item(key = "Buat Pengumuman Header") {
            IconTextInfo(
                text = stringResource(R.string.make_a_new_announcement),
                icon = painterResource(R.drawable.ic_pengumuman),
                sizeIcon = 28.dp,
                textStyle = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                colorText = MaterialTheme.colorScheme.onSurface,
                spacing = 15.dp
            )
        }
        item { Spacer(Modifier.height(10.dp)) }
        item(key = "Isi Judul TextField") {
            GeneralTextField(
                state = titleState,
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
                isError = buatPengumumanUiState.isTitleError,
                error = buatPengumumanUiState.titleError?.asString() ?: "",
                shakeTrigger = buatPengumumanUiState.shakeTargetTitle,
                keyboardOptions = KeyboardOptions.Default.copy(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text,
                ),
                lineLimits = TextFieldLineLimits.SingleLine,
                focusRequester = titleFocus
            )
        }
        item { Spacer(Modifier.height(10.dp)) }
        item(key = "Isi Deskripsi TextField") {
            GeneralTextField(
                state = descriptionState,
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
                isError = buatPengumumanUiState.isDescriptionError,
                error = buatPengumumanUiState.descriptionError?.asString() ?: "",
                shakeTrigger = buatPengumumanUiState.shakeTargetDescription,
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
        }
        item { Spacer(Modifier.height(30.dp)) }
        item(key = "Button Buat Pengumuman") {
            ActionButton(
                text = stringResource(R.string.make),
                onClick = {
                    keyboardController?.hide()
                    buatPengumumanActions(BuatPengumumanActions.CreatePengumuman)
                          },
                modifier = Modifier.fillMaxWidth(),
                enabled = !buatPengumumanUiState.isButtonLoading,
                isLoading = buatPengumumanUiState.isButtonLoading,
                shape = RoundedCornerShape(12.dp),
                height = 43.dp
            )
        }
    }
}