package com.kosrvd.app.feature.management.presentation.ui.screen.zona_parkiran_mobil.edit_zona_parkiran_mobil

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirlineSeatFlat
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.domain.utils.CustomToastHostState
import com.kosrvd.app.core.presentation.designsystem.component.appbar.TopBarLeftTitle
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionButton
import com.kosrvd.app.core.presentation.designsystem.component.text.CustomToastHost
import com.kosrvd.app.core.presentation.designsystem.component.textfield.GeneralTextField
import com.kosrvd.app.feature.management.presentation.designsystem.component.card.InfoSectionCard
import com.kosrvd.app.feature.management.presentation.designsystem.utils.CardAction
import com.kosrvd.app.feature.management.presentation.designsystem.utils.RupiahFormatter
import com.kosrvd.app.feature.management.presentation.designsystem.utils.RupiahInputTransformation
import com.kosrvd.app.feature.management.presentation.designsystem.utils.TypeEditZonaParkir
import com.kosrvd.app.feature.management.presentation.designsystem.utils.ZonaParkirFormatter
import com.kosrvd.app.feature.management.presentation.designsystem.utils.ZonaParkirTransformation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun EditZonaParkiranMobilScreen(
    editZonaParkiranMobilUiState: EditZonaParkiranMobilUiState,
    editZonaParkiranMobilActions: (EditZonaParkiranMobilActions) -> Unit,
    customToastHostState: CustomToastHostState
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    val buttonEnabled =
        when(editZonaParkiranMobilUiState.typeEditZonaParkir){
            TypeEditZonaParkir.EDIT_BIAYA_BULANAN -> {
                editZonaParkiranMobilUiState.biayaBulanan.isNotBlank() && editZonaParkiranMobilUiState.biayaBulanan != editZonaParkiranMobilUiState.oldBiayaBulanan
            }
            TypeEditZonaParkir.EDIT_BIAYA_HARIAN -> {
                editZonaParkiranMobilUiState.biayaHarian.isNotBlank() && editZonaParkiranMobilUiState.biayaHarian != editZonaParkiranMobilUiState.oldBiayaHarian
            }
            TypeEditZonaParkir.EDIT_NAMA_ZONA -> {
                editZonaParkiranMobilUiState.zoneName.isNotBlank() && editZonaParkiranMobilUiState.zoneName != editZonaParkiranMobilUiState.oldZoneName
            }
            null -> {
                false
            }
        }

    val titleTopBar =
        when(editZonaParkiranMobilUiState.typeEditZonaParkir){
            TypeEditZonaParkir.EDIT_BIAYA_BULANAN -> stringResource(R.string.edit_zone_name)
            TypeEditZonaParkir.EDIT_BIAYA_HARIAN -> stringResource(R.string.edit_daily_bill)
            TypeEditZonaParkir.EDIT_NAMA_ZONA -> stringResource(R.string.edit_monthly_bill)
            null -> "Type Edit Zona Kosong"
        }

    val initialNamaZonaText = remember(editZonaParkiranMobilUiState.zoneName) {
        ZonaParkirFormatter.format(editZonaParkiranMobilUiState.zoneName)
    }
    val namaZonaState = rememberTextFieldState(initialNamaZonaText)
    LaunchedEffect(namaZonaState) {
        snapshotFlow { namaZonaState.text.toString() }.collectLatest {
            val cleanValue = ZonaParkirFormatter.parseToOriginal(it)
            editZonaParkiranMobilActions(EditZonaParkiranMobilActions.UpdateNamaZona(cleanValue))
        }
    }

    val initialBiayaBulananText = remember(editZonaParkiranMobilUiState.biayaBulanan) {
        RupiahFormatter.format(editZonaParkiranMobilUiState.biayaBulanan)
    }
    val biayaBulananState = rememberTextFieldState(initialBiayaBulananText)
    LaunchedEffect(biayaBulananState) {
        snapshotFlow { biayaBulananState.text.toString() }.collectLatest {
            val cleanValue = RupiahFormatter.parseToRaw(it)
            editZonaParkiranMobilActions(EditZonaParkiranMobilActions.UpdateBiayaBulanan(cleanValue))
        }
    }

    val initialBiayaHarianText = remember(editZonaParkiranMobilUiState.biayaHarian) {
        RupiahFormatter.format(editZonaParkiranMobilUiState.biayaHarian)
    }
    val biayaHarianState = rememberTextFieldState(initialBiayaHarianText)
    LaunchedEffect(biayaHarianState) {
        snapshotFlow { biayaHarianState.text.toString() }.collectLatest {
            val cleanValue = RupiahFormatter.parseToRaw(it)
            editZonaParkiranMobilActions(EditZonaParkiranMobilActions.UpdateBiayaHarian(cleanValue))
        }
    }

    val coroutineScope = rememberCoroutineScope()

    val namaZonaRequester = remember { BringIntoViewRequester() }
    val namaZonaFocus = remember { FocusRequester() }

    val biayaBulananRequester = remember { BringIntoViewRequester() }
    val biayaBulananFocus = remember { FocusRequester() }

    val biayaHarianRequester = remember { BringIntoViewRequester() }
    val biayaHarianFocus = remember { FocusRequester() }

    LaunchedEffect(
        editZonaParkiranMobilUiState.shakeTriggerZoneNameError,
        editZonaParkiranMobilUiState.shakeTriggerBiayaBulananError,
        editZonaParkiranMobilUiState.shakeTriggerBiayaHarianError
    ) {

        if (editZonaParkiranMobilUiState.isZoneNameError) {
            delay(400)
            namaZonaRequester.bringIntoView()
            namaZonaFocus.requestFocus()
        }
        else if (editZonaParkiranMobilUiState.isBiayaBulananError) {
            delay(400)
            biayaBulananRequester.bringIntoView()
            biayaBulananFocus.requestFocus()
        }
        else if (editZonaParkiranMobilUiState.isBiayaHarianError) {
            delay(400)
            biayaHarianRequester.bringIntoView()
            biayaHarianFocus.requestFocus()
        }
    }

    Scaffold(
        topBar = {
            TopBarLeftTitle(
                title = titleTopBar,
                onBackClick = { editZonaParkiranMobilActions(EditZonaParkiranMobilActions.NavigateBack)}
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .imePadding()
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                InfoSectionCard(
                    icon = Icons.Filled.AirlineSeatFlat,
                    title = stringResource(R.string.room_number_completed),
                    action = CardAction.None,
                    content = {
                        Text(
                            text = ZonaParkirFormatter.parseToOriginal(editZonaParkiranMobilUiState.oldZoneName),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                )
                Spacer(Modifier.height(20.dp))
                when(editZonaParkiranMobilUiState.typeEditZonaParkir) {
                    TypeEditZonaParkir.EDIT_BIAYA_BULANAN -> {
                        EditBiayaBulananParkiranMobilContent(
                            biayaBulananState = biayaBulananState,
                            editZonaParkiranMobilUiState = editZonaParkiranMobilUiState,
                            biayaBulananRequester = biayaBulananRequester,
                            biayaBulananFocus = biayaBulananFocus,
                            coroutineScope = coroutineScope
                        )
                    }
                    TypeEditZonaParkir.EDIT_BIAYA_HARIAN -> {
                        EditBiayaHarianParkiranMobilContent(
                            biayaHarianState = biayaHarianState,
                            editZonaParkiranMobilUiState = editZonaParkiranMobilUiState,
                            biayaHarianRequester = biayaHarianRequester,
                            biayaHarianFocus = biayaHarianFocus,
                            coroutineScope = coroutineScope
                        )
                    }
                    TypeEditZonaParkir.EDIT_NAMA_ZONA -> {
                        EditNamaZonaParkiranMobilContent(
                            namaZonaState = namaZonaState,
                            editZonaParkiranMobilUiState = editZonaParkiranMobilUiState,
                            namaZonaRequester = namaZonaRequester,
                            namaZonaFocus = namaZonaFocus,
                            coroutineScope = coroutineScope
                        )
                    }
                    null -> { Text(text = "Type Edit Zona Kosong") }
                }
                Spacer(Modifier.weight(1f))
                Spacer(Modifier.height(15.dp))
                ActionButton(
                    modifier = Modifier.fillMaxWidth(),
                    height = 43.dp,
                    onClick = {
                        keyboardController?.hide()
                        editZonaParkiranMobilActions(EditZonaParkiranMobilActions.SaveEditZonaParkiranMobil)
                    },
                    text = stringResource(R.string.save),
                    shape = RoundedCornerShape(12.dp),
                    isLoading = editZonaParkiranMobilUiState.isButtonLoading,
                    enabled = buttonEnabled && !editZonaParkiranMobilUiState.isButtonLoading,
                    disableContainerColor = if (editZonaParkiranMobilUiState.isButtonLoading) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                    disableContentColor = if (editZonaParkiranMobilUiState.isButtonLoading) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                )
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
    }
}

@Composable
private fun EditNamaZonaParkiranMobilContent(
    namaZonaState: TextFieldState,
    editZonaParkiranMobilUiState: EditZonaParkiranMobilUiState,
    namaZonaRequester : BringIntoViewRequester,
    namaZonaFocus : FocusRequester,
    coroutineScope : CoroutineScope

) {
    Text(
        text = stringResource(R.string.enter_new_zone_name),
        style = MaterialTheme.typography.bodyMedium,
    )
    Spacer(Modifier.height(20.dp))
    GeneralTextField(
        modifier = Modifier.fillMaxWidth()
            .bringIntoViewRequester(namaZonaRequester)
            .onFocusEvent { focusState ->
                if (focusState.isFocused) {
                    coroutineScope.launch {
                        delay(500)
                        namaZonaRequester.bringIntoView()
                    }
                }
            },
        state = namaZonaState,
        isError = editZonaParkiranMobilUiState.isZoneNameError,
        error = editZonaParkiranMobilUiState.zoneNameError?.asString() ?: "",
        label = R.string.name_zone,
        shakeTrigger = editZonaParkiranMobilUiState.shakeTriggerZoneNameError,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text,
        ),
        lineLimits = TextFieldLineLimits.SingleLine,
        focusRequester = namaZonaFocus,
        inputTransformation = ZonaParkirTransformation
    )
}


@Composable
private fun EditBiayaBulananParkiranMobilContent(
    biayaBulananState: TextFieldState,
    editZonaParkiranMobilUiState: EditZonaParkiranMobilUiState,
    biayaBulananRequester : BringIntoViewRequester,
    biayaBulananFocus : FocusRequester,
    coroutineScope : CoroutineScope

) {
    Text(
        text = stringResource(R.string.enter_new_monthly_bill),
        style = MaterialTheme.typography.bodyMedium,
    )
    Spacer(Modifier.height(20.dp))
    GeneralTextField(
        modifier = Modifier.fillMaxWidth()
            .bringIntoViewRequester(biayaBulananRequester)
            .onFocusEvent { focusState ->
                if (focusState.isFocused) {
                    coroutineScope.launch {
                        delay(500)
                        biayaBulananRequester.bringIntoView()
                    }
                }
            },
        state = biayaBulananState,
        isError = editZonaParkiranMobilUiState.isBiayaBulananError,
        error = editZonaParkiranMobilUiState.biayaBulananError?.asString() ?: "",
        label = R.string.bill_month,
        shakeTrigger = editZonaParkiranMobilUiState.shakeTriggerBiayaBulananError,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number,
        ),
        lineLimits = TextFieldLineLimits.SingleLine,
        focusRequester = biayaBulananFocus,
        inputTransformation = RupiahInputTransformation
    )
}

@Composable
private fun EditBiayaHarianParkiranMobilContent(
    biayaHarianState: TextFieldState,
    editZonaParkiranMobilUiState: EditZonaParkiranMobilUiState,
    biayaHarianRequester : BringIntoViewRequester,
    biayaHarianFocus : FocusRequester,
    coroutineScope : CoroutineScope

) {
    Text(
        text = stringResource(R.string.enter_new_daily_bill),
        style = MaterialTheme.typography.bodyMedium,
    )
    Spacer(Modifier.height(20.dp))
    GeneralTextField(
        modifier = Modifier.fillMaxWidth()
            .bringIntoViewRequester(biayaHarianRequester)
            .onFocusEvent { focusState ->
                if (focusState.isFocused) {
                    coroutineScope.launch {
                        delay(500)
                        biayaHarianRequester.bringIntoView()
                    }
                }
            },
        state = biayaHarianState,
        isError = editZonaParkiranMobilUiState.isBiayaHarianError,
        error = editZonaParkiranMobilUiState.biayaHarianError?.asString() ?: "",
        label = R.string.bill_daily,
        shakeTrigger = editZonaParkiranMobilUiState.shakeTriggerBiayaHarianError,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number,
        ),
        lineLimits = TextFieldLineLimits.SingleLine,
        focusRequester = biayaHarianFocus,
        inputTransformation = RupiahInputTransformation
    )
}