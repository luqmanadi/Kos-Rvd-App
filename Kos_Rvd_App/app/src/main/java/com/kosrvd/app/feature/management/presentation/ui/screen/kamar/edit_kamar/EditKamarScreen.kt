package com.kosrvd.app.feature.management.presentation.ui.screen.kamar.edit_kamar

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AirlineSeatFlat
import androidx.compose.material.icons.filled.ElectricalServices
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
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
import com.kosrvd.app.core.navigation.models.EditTypeKamar
import com.kosrvd.app.core.presentation.designsystem.component.appbar.TopBarLeftTitle
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionButton
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionOutlineButton
import com.kosrvd.app.core.presentation.designsystem.component.radio_button.CustomRadioButtonV1
import com.kosrvd.app.core.presentation.designsystem.component.text.CustomToastHost
import com.kosrvd.app.core.presentation.designsystem.component.textfield.GeneralTextField
import com.kosrvd.app.core.presentation.utils.shimmerEffect
import com.kosrvd.app.feature.management.domain.model.AlatElektronik
import com.kosrvd.app.feature.management.presentation.designsystem.component.card.InfoItemCard
import com.kosrvd.app.feature.management.presentation.designsystem.component.card.InfoSectionCard
import com.kosrvd.app.feature.management.presentation.designsystem.component.dropdown.DropDownCustomV1
import com.kosrvd.app.feature.management.presentation.designsystem.utils.CardAction
import com.kosrvd.app.feature.management.presentation.designsystem.utils.RupiahFormatter
import com.kosrvd.app.feature.management.presentation.designsystem.utils.RupiahInputTransformation
import com.kosrvd.app.feature.management.presentation.designsystem.utils.TypeEditKamar
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toRupiahFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun EditKamarScreen(
    editKamarUiState: EditKamarUiState,
    editKamarActions: (EditKamarActions) -> Unit,
    customToastHostState: CustomToastHostState,
    typeEditKamar: TypeEditKamar,
    editTypeKamar: EditTypeKamar
) {
    val titleTopBar = when(typeEditKamar){
        TypeEditKamar.EDIT_NOMOR_KAMAR -> stringResource(R.string.edit_number_room)
        TypeEditKamar.EDIT_UKURAN_KAMAR -> stringResource(R.string.edit_size_room)
        TypeEditKamar.EDIT_TARIF_KAMAR -> stringResource(R.string.edit_tarif_and_capacity_room)
        TypeEditKamar.EDIT_LAYANAN_ALAT_ELEKTRONIK_GRATIS -> stringResource(R.string.edit_pemakaian_alat_elektronik_gratis)
        TypeEditKamar.EDIT_FASILITAS_KAMAR -> stringResource(R.string.edit_facility_room)
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    val oldNumberRoom = editTypeKamar.nomorKamar.toString()
    val oldUkuranKamar = editTypeKamar.ukuranKamar ?: ""
    val oldTarifKamarSatuOrang = editTypeKamar.tarifKamar.onePerson.toString()
    val oldTarifKamarDuaOrang = editTypeKamar.tarifKamar.twoPersons.toString()
    val layananAlatElektronikGratis = editTypeKamar.layananElektronikKamar?.map { alatElektronikSerialize ->
        AlatElektronik(
            toolName = alatElektronikSerialize.toolName,
            cost = alatElektronikSerialize.cost,
            origin = alatElektronikSerialize.origin
        )
    } ?: emptyList()
    val fasilitasKamar = editTypeKamar.fasilitasKamar

    val enableButton = when(typeEditKamar){
        TypeEditKamar.EDIT_NOMOR_KAMAR -> {
            oldNumberRoom != editKamarUiState.numberRoom
        }
        TypeEditKamar.EDIT_UKURAN_KAMAR -> {
            oldUkuranKamar != editKamarUiState.ukuranKamar
        }
        TypeEditKamar.EDIT_TARIF_KAMAR -> {
            when {
                editKamarUiState.jumlahOrang == 2 -> {
                    oldTarifKamarDuaOrang != editKamarUiState.tarifDuaOrang || oldTarifKamarSatuOrang != editKamarUiState.tarifSatuOrang
                }
                editKamarUiState.jumlahOrang != editKamarUiState.oldJumlahOrang -> true
                else -> {
                    oldTarifKamarSatuOrang != editKamarUiState.tarifSatuOrang
                }
            }
        }
        TypeEditKamar.EDIT_LAYANAN_ALAT_ELEKTRONIK_GRATIS -> {
            layananAlatElektronikGratis != editKamarUiState.layananElektronikGratisKamar
        }
        TypeEditKamar.EDIT_FASILITAS_KAMAR -> {
            fasilitasKamar != editKamarUiState.fasilitasKamar
        }
    }

    val numberRoomState = rememberTextFieldState(editKamarUiState.numberRoom)
    LaunchedEffect(numberRoomState) {
        snapshotFlow { numberRoomState.text.toString() }.collectLatest {
            editKamarActions(EditKamarActions.UpdateNumberRoom(it))
        }
    }

    val initialTarifSatuOrangText = remember(editKamarUiState.tarifSatuOrang) {
        RupiahFormatter.format(editKamarUiState.tarifSatuOrang)
    }
    Log.d("TAG", "EditKamarScreen: $initialTarifSatuOrangText")
    val tarifSatuOrangState = rememberTextFieldState(initialTarifSatuOrangText)
    LaunchedEffect(tarifSatuOrangState) {
        snapshotFlow { tarifSatuOrangState.text.toString() }.collectLatest {
            val cleanValue = RupiahFormatter.parseToRaw(it)
            editKamarActions(EditKamarActions.UpdateTarifSatuOrang(cleanValue))
        }
    }

    val initialTarifDuaOrangText = remember(editKamarUiState.tarifDuaOrang) {
        RupiahFormatter.format(editKamarUiState.tarifDuaOrang ?: "")
    }
    val tarifDuaOrangState = rememberTextFieldState(initialTarifDuaOrangText)
    LaunchedEffect(tarifDuaOrangState) {
        snapshotFlow { tarifDuaOrangState.text.toString() }.collectLatest {
            val cleanValue = RupiahFormatter.parseToRaw(it)
            editKamarActions(EditKamarActions.UpdateTarifDuaOrang(cleanValue))
        }
    }

    val namaFasilitasState = rememberTextFieldState(editKamarUiState.namaFasilitas)
    LaunchedEffect(namaFasilitasState) {
        snapshotFlow { namaFasilitasState.text.toString() }.collectLatest {
            editKamarActions(EditKamarActions.UpdateNamaFasilitas(it))
        }
    }

    val namaAlatElektronikState = rememberTextFieldState(editKamarUiState.namaAlatElektronik)
    LaunchedEffect(namaAlatElektronikState) {
        snapshotFlow { namaAlatElektronikState.text.toString() }.collectLatest {
            editKamarActions(EditKamarActions.UpdateNamaAlatElektronik(it))
        }
    }

    val coroutineScope = rememberCoroutineScope()

    val numberRoomRequester = remember { BringIntoViewRequester() }
    val numberRoomFocus = remember { FocusRequester() }

    val tarifSatuRequester = remember { BringIntoViewRequester() }
    val tarifSatuFocus = remember { FocusRequester() }

    val tarifDuaRequester = remember { BringIntoViewRequester() }
    val tarifDuaFocus = remember { FocusRequester() }

    val ukuranKamarRequester = remember { BringIntoViewRequester() }
    val fasilitasKamarRequester = remember { BringIntoViewRequester() }
    val pemakaianAlatElektronikKamarRequester = remember { BringIntoViewRequester() }

    LaunchedEffect(
        editKamarUiState.shakeTriggerNomorKamarError,
        editKamarUiState.shakeTriggerTarifSatuOrangError,
        editKamarUiState.shakeTriggerTarifDuaOrangError
    ) {

        if (editKamarUiState.isnumberRoomError) {
            delay(400)
            numberRoomRequester.bringIntoView()
            numberRoomFocus.requestFocus()
        }
        else if (editKamarUiState.isTarifSatuOrangError) {
            delay(400)
            tarifSatuRequester.bringIntoView()
            tarifSatuFocus.requestFocus()
        }
        else if (editKamarUiState.isTarifDuaOrangError && editKamarUiState.jumlahOrang == 2) {
            delay(400)
            tarifDuaRequester.bringIntoView()
            tarifDuaFocus.requestFocus()
        }
    }

    Scaffold(
        topBar = {
            TopBarLeftTitle(
                title = titleTopBar
            ) {
                editKamarActions(EditKamarActions.NavigateBack)
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
        ){
            Column(
                Modifier
                    .padding(vertical = 20.dp, horizontal = 16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                InfoSectionCard(
                    icon = Icons.Filled.AirlineSeatFlat,
                    title = stringResource(R.string.room_number_completed),
                    action = CardAction.None,
                    content = {
                        Text(
                            text = editKamarUiState.nomorKamarConstant,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                )
                Spacer(Modifier.height(20.dp))
                when(typeEditKamar){
                    TypeEditKamar.EDIT_NOMOR_KAMAR -> EditNomorKamarContent(
                        numberRoomState = numberRoomState,
                        editKamarUiState = editKamarUiState,
                        nomorKamarRequester = numberRoomRequester,
                        nomorKamarFocus = numberRoomFocus,
                        coroutineScope = coroutineScope,
                    )
                    TypeEditKamar.EDIT_UKURAN_KAMAR -> EditUkuranKamarContent(
                        editKamarUiState = editKamarUiState,
                        editKamarActions = editKamarActions,
                        editUkuranKamarRequester = ukuranKamarRequester,
                        coroutineScope = coroutineScope
                    )
                    TypeEditKamar.EDIT_TARIF_KAMAR -> {
                        if (editKamarUiState.isLoadingTarifKamar){
                            LoadingTarifDanKapasitasKamar()
                        } else {
                            EditTarifKamarContent(
                                editKamarUiState = editKamarUiState,
                                editKamarActions = editKamarActions,
                                tarifSatuOrangState = tarifSatuOrangState,
                                tarifDuaOrangState = tarifDuaOrangState,
                                tarifSatuOrangRequester = tarifSatuRequester,
                                tarifSatuOrangFocus = tarifSatuFocus,
                                tarifDuaOrangRequester = tarifDuaRequester,
                                tarifDuaOrangFocus = tarifDuaFocus,
                                coroutineScope = coroutineScope,
                            )
                        }
                    }
                    TypeEditKamar.EDIT_LAYANAN_ALAT_ELEKTRONIK_GRATIS -> EditLayananAlatElektronikGratisContent(
                        editKamarUiState = editKamarUiState,
                        editKamarActions = editKamarActions,
                        namaAlatElektronikState = namaAlatElektronikState,
                        editPemakaianAlatElektronikRequester = pemakaianAlatElektronikKamarRequester,
                        coroutineScope = coroutineScope,
                    )
                    TypeEditKamar.EDIT_FASILITAS_KAMAR -> EditFasilitasKamarContent(
                        editKamarUiState = editKamarUiState,
                        editKamarActions = editKamarActions,
                        namaFasilitasState = namaFasilitasState,
                        fasilitasKamarRequester = fasilitasKamarRequester,
                        coroutineScope = coroutineScope,
                    )
                }
                Spacer(Modifier.weight(1f))
                Spacer(Modifier.height(15.dp))
                ActionButton(
                    modifier = Modifier.fillMaxWidth(),
                    height = 43.dp,
                    onClick = {
                        keyboardController?.hide()
                        editKamarActions(EditKamarActions.SaveEditKamar)
                              },
                    text = stringResource(R.string.save),
                    shape = RoundedCornerShape(12.dp),
                    isLoading = editKamarUiState.isButtonLoading,
                    enabled = enableButton && !editKamarUiState.isButtonLoading,
                    disableContainerColor = if (editKamarUiState.isButtonLoading) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                    disableContentColor = if (editKamarUiState.isButtonLoading) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
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
fun LoadingTarifDanKapasitasKamar() {
    Column{
        Box(
            Modifier
                .fillMaxWidth()
                .height(30.dp)
                .clip(RoundedCornerShape(20.dp))
                .shimmerEffect()
        )
        Spacer(Modifier.height(20.dp))
        Box(
            Modifier
                .fillMaxWidth()
                .height(70.dp)
                .clip(RoundedCornerShape(20.dp))
                .shimmerEffect()
        )
        Spacer(Modifier.height(30.dp))
        Box(
            Modifier
                .fillMaxWidth()
                .height(20.dp)
                .clip(RoundedCornerShape(20.dp))
                .shimmerEffect()
        )
        Spacer(Modifier.height(20.dp))
        Box(
            Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(20.dp))
                .shimmerEffect()
        )
    }
}

@Composable
private fun EditFasilitasKamarContent(
    editKamarUiState: EditKamarUiState,
    editKamarActions: (EditKamarActions) -> Unit,
    namaFasilitasState: TextFieldState,
    fasilitasKamarRequester : BringIntoViewRequester,
    coroutineScope : CoroutineScope
) {
    Text(
        text = stringResource(R.string.list_facility_room),
        style = MaterialTheme.typography.bodyMedium,
    )
    Spacer(Modifier.height(15.dp))
    if (editKamarUiState.fasilitasKamar.isNotEmpty()){
        editKamarUiState.fasilitasKamar.forEachIndexed { index, fasilitas ->
            InfoItemCard(
                onDeleted = { editKamarActions(EditKamarActions.RemoveFasilitasKamar(index)) },
                title = fasilitas
            )
            Spacer(Modifier.height(15.dp))
        }
    } else {
        InfoItemCard(
            title = stringResource(R.string.no_new_data),
            isShowIconDelete = false
        )
        Spacer(Modifier.height(15.dp))
    }
    Column(Modifier.bringIntoViewRequester(fasilitasKamarRequester)) {
        GeneralTextField(
            modifier = Modifier.fillMaxWidth()
                .onFocusEvent { focusState ->
                    if (focusState.isFocused) {
                        coroutineScope.launch {
                            delay(500)
                            fasilitasKamarRequester.bringIntoView()
                        }
                    }
                },
            state = namaFasilitasState,
            isError = false,
            error = "",
            label = R.string.name_facility,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text,
            ),
            lineLimits = TextFieldLineLimits.SingleLine
        )
        Spacer(Modifier.height(15.dp))
        ActionOutlineButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                editKamarActions(EditKamarActions.AddFasilitasKamar)
                namaFasilitasState.clearText()
                      },
            text = stringResource(R.string.add_facility),
            shape = RoundedCornerShape(12.dp),
            height = 43.dp,
            enabled = editKamarUiState.namaFasilitas.isNotEmpty(),
            strokeWidth = 1.dp,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                )
            }
        )
    }
}

@Composable
private fun EditLayananAlatElektronikGratisContent(
    editKamarUiState: EditKamarUiState,
    editKamarActions: (EditKamarActions) -> Unit,
    namaAlatElektronikState: TextFieldState,
    editPemakaianAlatElektronikRequester : BringIntoViewRequester,
    coroutineScope : CoroutineScope
) {
    Text(
        text = stringResource(R.string.list_layanan_electronik_room),
        style = MaterialTheme.typography.bodyMedium,
    )
    Spacer(Modifier.height(15.dp))
    if (editKamarUiState.layananElektronikGratisKamar.isNotEmpty()){
        editKamarUiState.layananElektronikGratisKamar.forEachIndexed { index, alatElektronik ->
            InfoSectionCard(
                modifier = Modifier.fillMaxWidth(),
                icon = Icons.Filled.ElectricalServices,
                title = stringResource(R.string.usage_tools_electronic_free),
                action = CardAction.Delete { editKamarActions(EditKamarActions.RemovePemakaianAlatElektronikGratis(index)) },
                content = {
                    Text(
                        text = "${alatElektronik.toolName} (Free) - ${alatElektronik.cost.toRupiahFormat()}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            )
            Spacer(Modifier.height(15.dp))
        }
    } else {
        InfoItemCard(
            title = stringResource(R.string.no_new_data),
            isShowIconDelete = false
        )
        Spacer(Modifier.height(15.dp))
    }
    Column(Modifier.bringIntoViewRequester(editPemakaianAlatElektronikRequester)) {
        GeneralTextField(
            modifier = Modifier.fillMaxWidth()
                .onFocusEvent { focusState ->
                    if (focusState.isFocused) {
                        coroutineScope.launch {
                            delay(500)
                            editPemakaianAlatElektronikRequester.bringIntoView()
                        }
                    }
                },
            state = namaAlatElektronikState,
            isError = false,
            error = "",
            label = R.string.name_tool,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text,
            ),
            lineLimits = TextFieldLineLimits.SingleLine
        )
        Spacer(Modifier.height(15.dp))
        ActionOutlineButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                editKamarActions(EditKamarActions.AddPemakaianAlatElektronikGratis)
                namaAlatElektronikState.clearText()
                      },
            text = stringResource(R.string.add_tool),
            shape = RoundedCornerShape(12.dp),
            height = 43.dp,
            enabled = editKamarUiState.namaAlatElektronik.isNotEmpty(),
            strokeWidth = 1.dp,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                )
            }
        )
    }

}

@Composable
private fun EditTarifKamarContent(
    editKamarUiState: EditKamarUiState,
    editKamarActions: (EditKamarActions) -> Unit,
    tarifSatuOrangState: TextFieldState,
    tarifDuaOrangState: TextFieldState,
    tarifSatuOrangRequester : BringIntoViewRequester,
    tarifSatuOrangFocus : FocusRequester,
    tarifDuaOrangRequester : BringIntoViewRequester,
    tarifDuaOrangFocus : FocusRequester,
    coroutineScope : CoroutineScope
) {
    val listJumlahOrang = listOf(1, 2)

    Column {

        Text(
            text = stringResource(R.string.choose_new_capacity_room),
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            listJumlahOrang.forEach { jumlahOrang ->
                CustomRadioButtonV1(
                    modifier = Modifier.weight(1f),
                    text = "$jumlahOrang Orang",
                    selected = jumlahOrang == editKamarUiState.jumlahOrang,
                    onClick = { editKamarActions(EditKamarActions.UpdateSelectJumlahOrang(jumlahOrang)) },
                    enabled = editKamarUiState.enableEditCapacity
                )
            }
        }
        if (!editKamarUiState.enableEditCapacity){
            Spacer(Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.description_cannot_change_capacity_room),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }
        Spacer(Modifier.height(20.dp))
        Text(
            text = stringResource(R.string.bill_per_people),
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(Modifier.height(20.dp))
        GeneralTextField(
            modifier = Modifier.fillMaxWidth()
                .bringIntoViewRequester(tarifSatuOrangRequester)
                .onFocusEvent { focusState ->
                    if (focusState.isFocused) {
                        coroutineScope.launch {
                            delay(500)
                            tarifSatuOrangRequester.bringIntoView()
                        }
                    }
                },
            state = tarifSatuOrangState,
            enabled = editKamarUiState.enableEditCapacity,
            isError = editKamarUiState.isTarifSatuOrangError,
            error = editKamarUiState.tarifSatuOrangError?.asString() ?: "",
            label = R.string.bill_one_people,
            shakeTrigger = editKamarUiState.shakeTriggerTarifSatuOrangError,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number,
            ),
            lineLimits = TextFieldLineLimits.SingleLine,
            inputTransformation = RupiahInputTransformation,
            focusRequester = tarifSatuOrangFocus
        )
        AnimatedVisibility(
            visible = editKamarUiState.jumlahOrang == 2
        ) {
            Spacer(Modifier.height(10.dp))
            GeneralTextField(
                modifier = Modifier.fillMaxWidth()
                    .bringIntoViewRequester(tarifDuaOrangRequester)
                    .onFocusEvent { focusState ->
                        if (focusState.isFocused) {
                            coroutineScope.launch {
                                delay(500)
                                tarifDuaOrangRequester.bringIntoView()
                            }
                        }
                    },
                state = tarifDuaOrangState,
                enabled = editKamarUiState.enableEditCapacity,
                isError = editKamarUiState.isTarifDuaOrangError,
                error = editKamarUiState.tarifDuaOrangError?.asString() ?: "",
                label = R.string.bill_two_people,
                shakeTrigger = editKamarUiState.shakeTriggerTarifDuaOrangError,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number,
                ),
                lineLimits = TextFieldLineLimits.SingleLine,
                inputTransformation = RupiahInputTransformation,
                focusRequester = tarifDuaOrangFocus
            )
        }
    }

}

@Composable
private fun EditNomorKamarContent(
    numberRoomState: TextFieldState,
    editKamarUiState: EditKamarUiState,
    nomorKamarRequester : BringIntoViewRequester,
    nomorKamarFocus : FocusRequester,
    coroutineScope : CoroutineScope

) {
    Text(
        text = stringResource(R.string.enter_new_number_room),
        style = MaterialTheme.typography.bodyMedium,
    )
    Spacer(Modifier.height(20.dp))
    GeneralTextField(
        modifier = Modifier.fillMaxWidth()
            .bringIntoViewRequester(nomorKamarRequester)
            .onFocusEvent { focusState ->
                if (focusState.isFocused) {
                    coroutineScope.launch {
                        delay(500)
                        nomorKamarRequester.bringIntoView()
                    }
                }
            },
        state = numberRoomState,
        isError = editKamarUiState.isnumberRoomError,
        error = editKamarUiState.numberRoomError?.asString() ?: "",
        label = R.string.room_number_completed,
        shakeTrigger = editKamarUiState.shakeTriggerNomorKamarError,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number,
        ),
        lineLimits = TextFieldLineLimits.SingleLine,
        focusRequester = nomorKamarFocus
    )
}

@Composable
private fun EditUkuranKamarContent(
    editKamarUiState: EditKamarUiState,
    editKamarActions: (EditKamarActions) -> Unit,
    editUkuranKamarRequester : BringIntoViewRequester,
    coroutineScope : CoroutineScope
) {
    val listUkuranKamar = listOf(
        "Kecil",
        "Sedang",
        "Besar",
        "Sangat Besar"
    )

    Text(
        text = stringResource(R.string.choose_new_size_room),
        style = MaterialTheme.typography.bodyMedium,
    )
    Spacer(Modifier.height(20.dp))
    DropDownCustomV1(
        modifier = Modifier
            .bringIntoViewRequester(editUkuranKamarRequester)
            .onFocusEvent { focusState ->
                if (focusState.isFocused) {
                    coroutineScope.launch {
                        delay(500)
                        editUkuranKamarRequester.bringIntoView()
                    }
                }
            },
        items = listUkuranKamar,
        selectedItem = editKamarUiState.ukuranKamar,
        onItemSelected = { editKamarActions(EditKamarActions.UpdateUkuranKamar(it))},
        itemToString = { ukuranKamar ->
            ukuranKamar ?: "Pilih Ukuran"
        }
    )

}