package com.kosrvd.app.feature.rental.presentation.edit_pemakaian_elektronik

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ElectricalServices
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.domain.utils.CustomToastHostState
import com.kosrvd.app.core.presentation.designsystem.component.appbar.TopBarLeftTitle
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionButton
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionOutlineButton
import com.kosrvd.app.core.presentation.designsystem.component.text.CustomToastHost
import com.kosrvd.app.core.presentation.designsystem.component.textfield.GeneralTextField
import com.kosrvd.app.core.presentation.designsystem.organism.card.InfoItemCard
import com.kosrvd.app.core.presentation.designsystem.organism.card.InfoSectionCard
import com.kosrvd.app.core.presentation.utils.CardAction
import com.kosrvd.app.core.presentation.utils.RupiahFormatter
import com.kosrvd.app.core.presentation.utils.RupiahInputTransformation
import com.kosrvd.app.core.presentation.utils.toRupiahFormat
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun EditPemakaianElektronikScreen(
    editPemakaianElektronikUiState: EditPemakaianElektronikUiState,
    editPemakaianElektronikActions: (EditPemakaianElektronikActions) -> Unit,
    customToastHostState: CustomToastHostState
) {
    val titleTopBar = if (editPemakaianElektronikUiState.oldListAlatElektronik.isNotEmpty()) {
        stringResource(R.string.edit_usage_tools_electronic)
    } else {
        stringResource(R.string.add_usage_tools_electronic)
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            TopBarLeftTitle(
                title = titleTopBar,
                onBackClick = {
                    keyboardController?.hide()
                    editPemakaianElektronikActions(EditPemakaianElektronikActions.NavigateBack)
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
            Box(Modifier
                .weight(1f)
                .clipToBounds()
            ){
                EditPemakaianElektronikMainContent(
                    editPemakaianElektronikUiState = editPemakaianElektronikUiState,
                    editPemakaianElektronikActions = editPemakaianElektronikActions
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ActionButton(
                    modifier = Modifier.fillMaxWidth(),
                    height = 45.dp,
                    onClick = {
                        keyboardController?.hide()
                        editPemakaianElektronikActions(EditPemakaianElektronikActions.SaveEditPemakaianElektronik)
                              },
                    text = stringResource(R.string.save),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(15.dp),
                    isLoading = editPemakaianElektronikUiState.isButtonLoading,
                    enabled = editPemakaianElektronikUiState.isButtonSubmitEnabled && !editPemakaianElektronikUiState.isButtonLoading,
                    disableContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                    disableContentColor = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun EditPemakaianElektronikMainContent(
    modifier: Modifier = Modifier,
    editPemakaianElektronikUiState: EditPemakaianElektronikUiState,
    editPemakaianElektronikActions: (EditPemakaianElektronikActions) -> Unit,
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val nameElectronicRequester = remember { BringIntoViewRequester() }
    val nameElectronicFocus = remember { FocusRequester() }
    val priceElectronicRequester = remember { BringIntoViewRequester() }

    val nameElectronicState = rememberTextFieldState(editPemakaianElektronikUiState.namaAlatElektronik)
    LaunchedEffect(nameElectronicState) {
        snapshotFlow { nameElectronicState.text.toString() }.collectLatest {
            editPemakaianElektronikActions(EditPemakaianElektronikActions.OnNamaAlatElektronikChange(it))
        }
    }

    val initialPriceElectronicText = rememberSaveable(editPemakaianElektronikUiState.priceAlatElektronik) {
        RupiahFormatter.format(editPemakaianElektronikUiState.priceAlatElektronik)
    }
    val priceElectronicState = rememberTextFieldState(initialPriceElectronicText)
    LaunchedEffect(priceElectronicState) {
        snapshotFlow { priceElectronicState.text.toString() }.collectLatest {
            val cleanValue = RupiahFormatter.parseToRaw(it)
            editPemakaianElektronikActions(EditPemakaianElektronikActions.OnPriceAlatElektronikChange(cleanValue))
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(vertical = 16.dp, horizontal = 20.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = stringResource(R.string.title_edit_usage_tool_electronic),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                GeneralTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .bringIntoViewRequester(nameElectronicRequester)
                        .onFocusEvent { focusState ->
                            if (focusState.isFocused) {
                                coroutineScope.launch {
                                    delay(500)
                                    nameElectronicRequester.bringIntoView()
                                }
                            }
                        },
                    state = nameElectronicState,
                    isError = false,
                    error = "",
                    label = R.string.enter_name_tools,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Words
                    ),
                    lineLimits = TextFieldLineLimits.SingleLine,
                    focusRequester = nameElectronicFocus
                )
                GeneralTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .bringIntoViewRequester(priceElectronicRequester)
                        .onFocusEvent { focusState ->
                            if (focusState.isFocused) {
                                coroutineScope.launch {
                                    delay(500)
                                    priceElectronicRequester.bringIntoView()
                                }
                            }
                        },
                    state = priceElectronicState,
                    isError = false,
                    error = "",
                    label = R.string.enter_price_tools,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Number,
                    ),
                    lineLimits = TextFieldLineLimits.SingleLine,
                    inputTransformation = RupiahInputTransformation
                )
                ActionOutlineButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .bringIntoViewRequester(nameElectronicRequester),
                    onClick = {
                        editPemakaianElektronikActions(EditPemakaianElektronikActions.AddAlatElektronik)
                        nameElectronicFocus.requestFocus()
                        nameElectronicState.clearText()
                        priceElectronicState.clearText()
                    },
                    text = stringResource(R.string.add_tool),
                    shape = RoundedCornerShape(12.dp),
                    height = 43.dp,
                    enabled = editPemakaianElektronikUiState.isButtonAddAlatElektronikEnabled,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = null,
                        )
                    }
                )
            }
        }
        Spacer(Modifier.height(20.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Text(
                text = "DAFTAR ALAT",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                if (editPemakaianElektronikUiState.listAlatElektronik.isNotEmpty()){
                    editPemakaianElektronikUiState.listAlatElektronik.forEachIndexed { index, alatElektronik ->
                        InfoSectionCard(
                            modifier = Modifier.fillMaxWidth(),
                            icon = Icons.Filled.ElectricalServices,
                            title = stringResource(R.string.name_tools_price),
                            action = if (alatElektronik.origin != Constant.ORIGIN_KAMAR_DEFAULT) {
                                CardAction.Delete {
                                    editPemakaianElektronikActions(EditPemakaianElektronikActions.RemoveAlatElektronik(index))
                                }
                            } else { CardAction.None },
                            content = {
                                val textBody = if (alatElektronik.cost == 0L) {
                                    "${alatElektronik.toolName} (Gratis) - ${0L.toRupiahFormat()}"
                                } else "${alatElektronik.toolName} - ${alatElektronik.cost.toRupiahFormat()}"
                                Text(
                                    text = textBody,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        )
                    }
                } else {
                    InfoItemCard(
                        title = stringResource(R.string.no_data_use_tools),
                        isShowIconDelete = false
                    )
                }
            }
        }
    }
}