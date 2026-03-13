package com.kosrvd.app.feature.management.presentation.ui.screen.penyewaan.edit_pemakaian_parkir_mobil

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Garage
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
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
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionDangerButton
import com.kosrvd.app.core.presentation.designsystem.component.text.CustomToastHost
import com.kosrvd.app.core.presentation.designsystem.component.textfield.GeneralTextField
import com.kosrvd.app.feature.management.presentation.designsystem.component.card.InfoSectionCard
import com.kosrvd.app.feature.management.presentation.designsystem.component.dropdown.DropDownCustomV1
import com.kosrvd.app.feature.management.presentation.designsystem.utils.CardAction
import com.kosrvd.app.feature.management.presentation.designsystem.utils.ZonaParkirFormatter
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toRupiahFormat
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun EditPemakaianParkirMobilScreen(
    editPemakaianParkirMobilUiState: EditPemakaianParkirMobilUiState,
    editPemakaianParkirMobilActions: (EditPemakaianParkirMobilActions) -> Unit,
    customToastHostState: CustomToastHostState
) {
    val titleTopBar = if (editPemakaianParkirMobilUiState.oldSelectedZoneParking != null) {
        stringResource(R.string.edit_usage_parking_car)
    } else {
        stringResource(R.string.add_usage_parking_card)
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            TopBarLeftTitle(
                title = titleTopBar,
                onBackClick = {
                    keyboardController?.hide()
                    editPemakaianParkirMobilActions(EditPemakaianParkirMobilActions.NavigateBack)
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .imePadding(),
        ) {
            Box(
                Modifier
                    .weight(1f)
                    .clipToBounds()
            ) {
                EditPemakaianParkirMobilMainContent(
                    editPemakaianParkirMobilUiState = editPemakaianParkirMobilUiState,
                    editPemakaianParkirMobilActions = editPemakaianParkirMobilActions
                )
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
            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
            TowButtonRow(
                keyboardController = keyboardController,
                editPemakaianParkirMobilUiState = editPemakaianParkirMobilUiState,
                editPemakaianParkirMobilActions = editPemakaianParkirMobilActions
            )
        }
    }
}

@Composable
private fun EditPemakaianParkirMobilMainContent(
    modifier: Modifier = Modifier,
    editPemakaianParkirMobilUiState: EditPemakaianParkirMobilUiState,
    editPemakaianParkirMobilActions: (EditPemakaianParkirMobilActions) -> Unit,
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val carNameRequester = remember { BringIntoViewRequester() }
    val carBrandRequester = remember { BringIntoViewRequester() }
    val numberPlateRequester = remember { BringIntoViewRequester() }
    val notesRequester = remember { BringIntoViewRequester() }

    val textTitleZoneParking = if (editPemakaianParkirMobilUiState.oldSelectedZoneParking != null) {
        stringResource(R.string.title_if_have_rental_parking)
    } else stringResource(R.string.title_if_no_have_rental_parking)

    val carNameState = rememberTextFieldState(editPemakaianParkirMobilUiState.carName)
    LaunchedEffect(carNameState) {
        snapshotFlow { carNameState.text.toString() }.collectLatest {
            editPemakaianParkirMobilActions(EditPemakaianParkirMobilActions.OnCarNameChange(it))
        }
    }

    val carBrandState = rememberTextFieldState(editPemakaianParkirMobilUiState.carBrand)
    LaunchedEffect(carBrandState) {
        snapshotFlow { carBrandState.text.toString() }.collectLatest {
            editPemakaianParkirMobilActions(EditPemakaianParkirMobilActions.OnCarBrandChange(it))
        }
    }

    val numberPlateState = rememberTextFieldState(editPemakaianParkirMobilUiState.numberPlate)
    LaunchedEffect(numberPlateState) {
        snapshotFlow { numberPlateState.text.toString() }.collectLatest {
            editPemakaianParkirMobilActions(EditPemakaianParkirMobilActions.OnNumberPlateChange(it))
        }
    }

    val notesState = rememberTextFieldState(editPemakaianParkirMobilUiState.notes)
    LaunchedEffect(notesState) {
        snapshotFlow { notesState.text.toString() }.collectLatest {
            editPemakaianParkirMobilActions(EditPemakaianParkirMobilActions.OnNotesChange(it))
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(vertical = 16.dp, horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        if (editPemakaianParkirMobilUiState.oldSelectedZoneParking != null) {
            Column(
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Text(
                    text = stringResource(R.string.title_parking_zone_usage),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                InfoSectionCard(
                    icon = Icons.Filled.Garage,
                    title = stringResource(R.string.parking_zone_price),
                    action = CardAction.None,
                    content = {
                        val nameZone =
                            ZonaParkirFormatter.format(editPemakaianParkirMobilUiState.oldSelectedZoneParking.zoneName)
                        val priceZone =
                            editPemakaianParkirMobilUiState.oldSelectedZoneParking.monthlyFee.toRupiahFormat()
                        val textBody = "$nameZone - $priceZone"

                        Text(
                            text = textBody,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Text(
                text = textTitleZoneParking,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                InfoSectionCard(
                    icon = Icons.Filled.Garage,
                    title = stringResource(R.string.parking_zone_price),
                    action = CardAction.None,
                    content = {
                        val textBody = if (editPemakaianParkirMobilUiState.newSelectedZoneParking != null){
                            val nameZone = ZonaParkirFormatter.format(editPemakaianParkirMobilUiState.newSelectedZoneParking.zoneName)
                            val priceZone = editPemakaianParkirMobilUiState.newSelectedZoneParking.monthlyFee.toRupiahFormat()
                            "$nameZone - $priceZone"
                        } else {
                            "Belum Memilih Zona Parkir Mobil"
                        }

                        Text(
                            text = textBody,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                )
                DropDownCustomV1(
                    items = editPemakaianParkirMobilUiState.listZonaParkir,
                    selectedItem = editPemakaianParkirMobilUiState.newSelectedZoneParking,
                    onItemSelected = { data ->
                        editPemakaianParkirMobilActions(EditPemakaianParkirMobilActions.OnSelectedZoneParkingChange(data))
                    },
                    itemToString = { data ->
                        if (data!= null){
                            ZonaParkirFormatter.format(data.zoneName)
                        } else {
                            "Belum Memilih Zona Parkir (Kosong)"
                        }
                    },
                    textNoData = R.string.no_data_parking_empty
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = stringResource(R.string.fill_data_transportation),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                GeneralTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .bringIntoViewRequester(carNameRequester)
                        .onFocusEvent { focusState ->
                            if (focusState.isFocused) {
                                coroutineScope.launch {
                                    delay(500)
                                    carNameRequester.bringIntoView()
                                }
                            }
                        },
                    state = carNameState,
                    isError = editPemakaianParkirMobilUiState.isCarNameError,
                    error = editPemakaianParkirMobilUiState.carNameError?.asString() ?: "",
                    label = R.string.name_car,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Words
                    ),
                    lineLimits = TextFieldLineLimits.SingleLine
                )
                GeneralTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .bringIntoViewRequester(carBrandRequester)
                        .onFocusEvent { focusState ->
                            if (focusState.isFocused) {
                                coroutineScope.launch {
                                    delay(500)
                                    carBrandRequester.bringIntoView()
                                }
                            }
                        },
                    state = carBrandState,
                    isError = editPemakaianParkirMobilUiState.isCarBrandError,
                    error = editPemakaianParkirMobilUiState.carBrandError?.asString() ?: "",
                    label = R.string.brand_car,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Words
                    ),
                    lineLimits = TextFieldLineLimits.SingleLine
                )
                GeneralTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .bringIntoViewRequester(numberPlateRequester)
                        .onFocusEvent { focusState ->
                            if (focusState.isFocused) {
                                coroutineScope.launch {
                                    delay(500)
                                    numberPlateRequester.bringIntoView()
                                }
                            }
                        },
                    state = numberPlateState,
                    isError = editPemakaianParkirMobilUiState.isNumberPlateError,
                    error = editPemakaianParkirMobilUiState.numberPlateError?.asString() ?: "",
                    label = R.string.number_police,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Words
                    ),
                    lineLimits = TextFieldLineLimits.SingleLine
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = stringResource(R.string.note_optional),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            GeneralTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .bringIntoViewRequester(notesRequester)
                    .onFocusEvent { focusState ->
                        if (focusState.isFocused) {
                            coroutineScope.launch {
                                delay(500)
                                notesRequester.bringIntoView()
                            }
                        }
                    },
                state = notesState,
                isError = false,
                error = "",
                label = R.string.fill_note,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Words
                ),
                lineLimits = TextFieldLineLimits.SingleLine
            )
        }
    }
}

@Composable
private fun TowButtonRow(
    modifier: Modifier = Modifier,
    keyboardController: SoftwareKeyboardController?,
    editPemakaianParkirMobilUiState: EditPemakaianParkirMobilUiState,
    editPemakaianParkirMobilActions: (EditPemakaianParkirMobilActions) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (editPemakaianParkirMobilUiState.oldSelectedZoneParking != null) {
            ActionDangerButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    keyboardController?.hide()
                    editPemakaianParkirMobilActions(EditPemakaianParkirMobilActions.EndPemakaianParkirMobil)
                },
                enabled = !editPemakaianParkirMobilUiState.isButtonEndRentalLoading || !editPemakaianParkirMobilUiState.isButtonSubmitLoading,
                isLoading = editPemakaianParkirMobilUiState.isButtonEndRentalLoading,
                text = stringResource(R.string.end_the_lease),
                shape = RoundedCornerShape(15.dp),
                height = 45.dp
            )
        }
        ActionButton(
            modifier = Modifier.weight(1f),
            height = 45.dp,
            onClick = {
                keyboardController?.hide()
                editPemakaianParkirMobilActions(EditPemakaianParkirMobilActions.SaveEditPemakaianParkirMobil)
            },
            text = stringResource(R.string.save),
            shape = RoundedCornerShape(15.dp),
            isLoading = editPemakaianParkirMobilUiState.isButtonSubmitLoading,
            enabled = editPemakaianParkirMobilUiState.isButtonSubmitEnabled && !editPemakaianParkirMobilUiState.isButtonSubmitLoading && !editPemakaianParkirMobilUiState.isButtonEndRentalLoading,
            disableContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
            disableContentColor = MaterialTheme.colorScheme.onSurface
        )
    }
}