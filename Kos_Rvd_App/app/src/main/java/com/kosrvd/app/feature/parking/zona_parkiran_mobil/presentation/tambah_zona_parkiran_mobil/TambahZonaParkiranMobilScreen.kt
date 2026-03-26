package com.kosrvd.app.feature.parking.zona_parkiran_mobil.presentation.tambah_zona_parkiran_mobil

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
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
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
import com.kosrvd.app.core.presentation.utils.RupiahFormatter
import com.kosrvd.app.core.presentation.utils.RupiahInputTransformation
import com.kosrvd.app.core.presentation.utils.ZonaParkirFormatter
import com.kosrvd.app.core.presentation.utils.ZonaParkirTransformation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun TambahZonaParkiranMobilScreen (
    tambahZonaParkiranMobilUiState: TambahZonaParkiranMobilUiState,
    tambahZonaParkiranMobilActions: (TambahZonaParkiranMobilActions) -> Unit,
    customToastHostState: CustomToastHostState
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val initialNamaZonaText = remember(tambahZonaParkiranMobilUiState.zoneName) {
        ZonaParkirFormatter.format(tambahZonaParkiranMobilUiState.zoneName)
    }
    val namaZonaState = rememberTextFieldState(initialNamaZonaText)
    LaunchedEffect(namaZonaState) {
        snapshotFlow { namaZonaState.text.toString() }.collectLatest {
            val cleanValue = ZonaParkirFormatter.parseToOriginal(it)
            tambahZonaParkiranMobilActions(TambahZonaParkiranMobilActions.UpdateNamaZona(cleanValue))
        }
    }

    val initialBiayaBulananText = remember(tambahZonaParkiranMobilUiState.biayaBulanan) {
        RupiahFormatter.format(tambahZonaParkiranMobilUiState.biayaBulanan)
    }
    val biayaBulananState = rememberTextFieldState(initialBiayaBulananText)
    LaunchedEffect(biayaBulananState) {
        snapshotFlow { biayaBulananState.text.toString() }.collectLatest {
            val cleanValue = RupiahFormatter.parseToRaw(it)
            tambahZonaParkiranMobilActions(TambahZonaParkiranMobilActions.UpdateBiayaBulanan(cleanValue))
        }
    }

    val initialBiayaHarianText = remember(tambahZonaParkiranMobilUiState.biayaHarian) {
        RupiahFormatter.format(tambahZonaParkiranMobilUiState.biayaHarian)
    }
    val biayaHarianState = rememberTextFieldState(initialBiayaHarianText)
    LaunchedEffect(biayaHarianState) {
        snapshotFlow { biayaHarianState.text.toString() }.collectLatest {
            val cleanValue = RupiahFormatter.parseToRaw(it)
            tambahZonaParkiranMobilActions(TambahZonaParkiranMobilActions.UpdateBiayaHarian(cleanValue))
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
        tambahZonaParkiranMobilUiState.shakeTriggerZoneNameError,
        tambahZonaParkiranMobilUiState.shakeTriggerBiayaBulananError,
        tambahZonaParkiranMobilUiState.shakeTriggerBiayaHarianError
    ) {

        if (tambahZonaParkiranMobilUiState.isZoneNameError) {
            delay(400)
            namaZonaRequester.bringIntoView()
            namaZonaFocus.requestFocus()
        }
        else if (tambahZonaParkiranMobilUiState.isBiayaBulananError) {
            delay(400)
            biayaBulananRequester.bringIntoView()
            biayaBulananFocus.requestFocus()
        }
        else if (tambahZonaParkiranMobilUiState.isBiayaHarianError) {
            delay(400)
            biayaHarianRequester.bringIntoView()
            biayaHarianFocus.requestFocus()
        }
    }

    Scaffold(
        topBar = {
            TopBarLeftTitle(
                title = stringResource(R.string.add_zone_parking),
                onBackClick = {
                    keyboardController?.hide()
                    tambahZonaParkiranMobilActions(TambahZonaParkiranMobilActions.NavigateBack)
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
        ){
            Column(
                Modifier.padding(horizontal = 16.dp, vertical = 20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = stringResource(R.string.add_zone_parking),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(5.dp))
                Text(
                    text = stringResource(R.string.description_add_zone_parking),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(15.dp))
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
                    isError = tambahZonaParkiranMobilUiState.isZoneNameError,
                    error = tambahZonaParkiranMobilUiState.zoneNameError?.asString() ?: "",
                    label = R.string.name_zone,
                    shakeTrigger = tambahZonaParkiranMobilUiState.shakeTriggerZoneNameError,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Text
                    ),
                    lineLimits = TextFieldLineLimits.SingleLine,
                    focusRequester = namaZonaFocus,
                    inputTransformation = ZonaParkirTransformation
                )
                Spacer(Modifier.height(20.dp))
                Text(
                    text = stringResource(R.string.rent_bill),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(15.dp))
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
                    isError = tambahZonaParkiranMobilUiState.isBiayaBulananError,
                    error = tambahZonaParkiranMobilUiState.biayaBulananError?.asString() ?: "",
                    label = R.string.rent_bill_per_month,
                    shakeTrigger = tambahZonaParkiranMobilUiState.shakeTriggerBiayaBulananError,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Number,
                    ),
                    lineLimits = TextFieldLineLimits.SingleLine,
                    focusRequester = biayaBulananFocus,
                    inputTransformation = RupiahInputTransformation
                )
                Spacer(Modifier.height(15.dp))
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
                    isError = tambahZonaParkiranMobilUiState.isBiayaHarianError,
                    error = tambahZonaParkiranMobilUiState.biayaHarianError?.asString() ?: "",
                    label = R.string.rent_bill_per_daily,
                    shakeTrigger = tambahZonaParkiranMobilUiState.shakeTriggerBiayaHarianError,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Number,
                    ),
                    lineLimits = TextFieldLineLimits.SingleLine,
                    focusRequester = biayaHarianFocus,
                    inputTransformation = RupiahInputTransformation
                )
                Spacer(Modifier.weight(1f))
                Spacer(Modifier.height(15.dp))
                ActionButton(
                    modifier = Modifier.fillMaxWidth(),
                    height = 43.dp,
                    onClick = {
                        keyboardController?.hide()
                        tambahZonaParkiranMobilActions(TambahZonaParkiranMobilActions.TambahZonaParkiranMobil)
                    },
                    text = stringResource(R.string.add_zone),
                    shape = RoundedCornerShape(12.dp),
                    isLoading = tambahZonaParkiranMobilUiState.isButtonLoading,
                    enabled = !tambahZonaParkiranMobilUiState.isButtonLoading
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