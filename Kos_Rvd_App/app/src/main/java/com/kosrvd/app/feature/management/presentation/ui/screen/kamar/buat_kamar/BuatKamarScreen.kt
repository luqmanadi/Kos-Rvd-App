package com.kosrvd.app.feature.management.presentation.ui.screen.kamar.buat_kamar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
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
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.domain.utils.CustomToastHostState
import com.kosrvd.app.core.presentation.designsystem.component.appbar.TopBarLeftTitle
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionButton
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionOutlineButton
import com.kosrvd.app.core.presentation.designsystem.component.radio_button.CustomRadioButtonV1
import com.kosrvd.app.core.presentation.designsystem.component.text.CustomToastHost
import com.kosrvd.app.core.presentation.designsystem.component.textfield.GeneralTextField
import com.kosrvd.app.feature.management.presentation.designsystem.component.card.InfoItemCard
import com.kosrvd.app.feature.management.presentation.designsystem.component.card.InfoSectionCard
import com.kosrvd.app.feature.management.presentation.designsystem.component.dropdown.DropDownCustomV1
import com.kosrvd.app.feature.management.presentation.designsystem.utils.CardAction
import com.kosrvd.app.feature.management.presentation.designsystem.utils.RupiahFormatter
import com.kosrvd.app.feature.management.presentation.designsystem.utils.RupiahInputTransformation
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toRupiahFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun BuatKamarScreen(
    buatKamarUiState: BuatKamarUiState,
    buatKamarActions: (BuatKamarActions) -> Unit,
    customToastHostState: CustomToastHostState,
) {

    val numberRoomState = rememberTextFieldState(buatKamarUiState.numberRoom)
    LaunchedEffect(numberRoomState) {
        snapshotFlow { numberRoomState.text.toString() }.collectLatest {
            buatKamarActions(BuatKamarActions.UpdateNumberRoom(it))
        }
    }

    val initialTarifSatuOrangText = remember(buatKamarUiState.tarifSatuOrang) {
        RupiahFormatter.format(buatKamarUiState.tarifSatuOrang)
    }

    val tarifSatuOrangState = rememberTextFieldState(initialTarifSatuOrangText)
    LaunchedEffect(tarifSatuOrangState) {
        snapshotFlow { tarifSatuOrangState.text.toString() }.collectLatest {
            val cleanValue = RupiahFormatter.parseToRaw(it)
            buatKamarActions(BuatKamarActions.UpdateTarifSatuOrang(cleanValue))
        }
    }

    val initialTarifDuaOrangText = remember(buatKamarUiState.tarifDuaOrang) {
        RupiahFormatter.format(buatKamarUiState.tarifDuaOrang ?: "")
    }
    val tarifDuaOrangState = rememberTextFieldState(initialTarifDuaOrangText)
    LaunchedEffect(tarifDuaOrangState) {
        snapshotFlow { tarifDuaOrangState.text.toString() }.collectLatest {
            val cleanValue = RupiahFormatter.parseToRaw(it)
            buatKamarActions(BuatKamarActions.UpdateTarifDuaOrang(cleanValue))
        }
    }

    val namaFasilitasState = rememberTextFieldState(buatKamarUiState.namaFasilitas)
    LaunchedEffect(namaFasilitasState) {
        snapshotFlow { namaFasilitasState.text.toString() }.collectLatest {
            buatKamarActions(BuatKamarActions.UpdateNamaFasilitas(it))
        }
    }

    val namaAlatElektronikState = rememberTextFieldState(buatKamarUiState.namaAlatElektronik)
    LaunchedEffect(namaAlatElektronikState) {
        snapshotFlow { namaAlatElektronikState.text.toString() }.collectLatest {
            buatKamarActions(BuatKamarActions.UpdateNamaAlatElektronik(it))
        }
    }

    Scaffold(
        topBar = {
            TopBarLeftTitle(
                title = stringResource(R.string.create_room)
            ) {
                buatKamarActions(BuatKamarActions.NavigateBack)
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ){

            BuatKamarMainContent(
                buatKamarUiState = buatKamarUiState,
                buatKamarActions = buatKamarActions,
                numberRoomState = numberRoomState,
                tarifSatuOrangState = tarifSatuOrangState,
                tarifDuaOrangState = tarifDuaOrangState,
                namaFasilitasState = namaFasilitasState,
                namaAlatElektronikState = namaAlatElektronikState
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
private fun BuatKamarMainContent(
    buatKamarUiState: BuatKamarUiState,
    buatKamarActions: (BuatKamarActions) -> Unit,
    numberRoomState: TextFieldState,
    namaAlatElektronikState: TextFieldState,
    tarifSatuOrangState: TextFieldState,
    tarifDuaOrangState: TextFieldState,
    namaFasilitasState: TextFieldState
) {

    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

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
        buatKamarUiState.shakeTriggerNomorKamarError,
        buatKamarUiState.shakeTriggerTarifSatuOrangError,
        buatKamarUiState.shakeTriggerTarifDuaOrangError
    ) {
        // Cek Prioritas DARI ATAS KE BAWAH

        // Prioritas 1: Nomor Kamar
        if (buatKamarUiState.isnumberRoomError) {
            listState.animateScrollToItem(2)
            delay(400)
            numberRoomRequester.bringIntoView()
            numberRoomFocus.requestFocus()
        }
        // Prioritas 2: Tarif Satu Orang (Hanya jalan jika Nomor Kamar TIDAK error)
        else if (buatKamarUiState.isTarifSatuOrangError) {
            listState.animateScrollToItem(4)
            delay(400)
            tarifSatuRequester.bringIntoView()
            tarifSatuFocus.requestFocus()
        }
        // Prioritas 3: Tarif Dua Orang (Hanya jalan jika 1 & 2 TIDAK error)
        else if (buatKamarUiState.isTarifDuaOrangError && buatKamarUiState.jumlahOrang == 2) {
            listState.animateScrollToItem(4)
            delay(400)
            tarifDuaRequester.bringIntoView()
            tarifDuaFocus.requestFocus()
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        contentPadding = PaddingValues(vertical = 20.dp, horizontal = 16.dp)
    ) {
        item(key = "Judul dan deskripsi buat kamar") { TitleBuatKamarSection() }
        item { Spacer(Modifier.height(15.dp)) }
        item(key = "Mengisi Nomor Kamar Section") {
            Column(
                modifier = Modifier.bringIntoViewRequester(numberRoomRequester)
            ) {
                GeneralTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusEvent { focusState ->
                            if (focusState.isFocused) {
                                coroutineScope.launch {
                                    delay(500)
                                    numberRoomRequester.bringIntoView()
                                }
                            }
                        },
                    focusRequester = numberRoomFocus,
                    state = numberRoomState,
                    isError = buatKamarUiState.isnumberRoomError,
                    error = buatKamarUiState.numberRoomError?.asString() ?: "",
                    label = R.string.room_number_completed,
                    shakeTrigger = buatKamarUiState.shakeTriggerNomorKamarError,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Number,
                    ),
                    lineLimits = TextFieldLineLimits.SingleLine
                )
            }
        }
        item { Spacer(Modifier.height(15.dp)) }
        item(key = "Mengisi Tarif dan Kapasitas Kamar Section") {
            TarifDanKapasitasKamarSection(
                buatKamarUiState = buatKamarUiState,
                buatKamarActions = buatKamarActions,
                tarifSatuOrangState = tarifSatuOrangState,
                tarifDuaOrangState = tarifDuaOrangState,
                tarifSatuRequester = tarifSatuRequester,
                tarifSatuFocus = tarifSatuFocus,
                tarifDuaRequester = tarifDuaRequester,
                tarifDuaFocus = tarifDuaFocus,
                coroutineScope = coroutineScope
            )
        }
        item { Spacer(Modifier.height(15.dp)) }
        item(key = "Memilih Ukuran Kamar Section") {
            UkuranKamarSection(
                buatKamarUiState = buatKamarUiState,
                buatKamarActions = buatKamarActions,
                coroutineScope = coroutineScope,
                ukuranKamarRequester = ukuranKamarRequester
            )
        }
        item { Spacer(Modifier.height(15.dp)) }
        item(key = "Mengisi Fasilitas Kamar Section") {
            FasilitasKamarSection(
                buatKamarUiState = buatKamarUiState,
                buatKamarActions = buatKamarActions,
                namaFasilitasState = namaFasilitasState,
                coroutineScope = coroutineScope,
                fasilitasKamarRequester = fasilitasKamarRequester
            )
        }
        item { Spacer(Modifier.height(15.dp)) }
        item(key = "Mengisi Pemakaian Alat Elektronik Gratis Kamar Section") {
            PemakaianAlatElektronikSection(
                buatKamarUiState = buatKamarUiState,
                buatKamarActions = buatKamarActions,
                namaAlatElektronikState = namaAlatElektronikState,
                coroutineScope = coroutineScope,
                pemakaianAlatElektronikKamarRequester = pemakaianAlatElektronikKamarRequester
            )
        }
        item {
            Spacer(Modifier.height(15.dp))
            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(Modifier.height(15.dp))
        }
        item(key = "Tombol Tambah Kamar Section") {
            ActionButton(
                modifier = Modifier.fillMaxWidth(),
                height = 43.dp,
                onClick = {
                    buatKamarActions(BuatKamarActions.BuatKamarBaru)
                          },
                text = stringResource(R.string.create_room),
                shape = RoundedCornerShape(12.dp),
                isLoading = buatKamarUiState.isButtonLoading,
                enabled = !buatKamarUiState.isButtonLoading
            )
        }
    }
}

@Composable
private fun TitleBuatKamarSection() {
    Column {
        Text(
            text = stringResource(R.string.title_create_new_room),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(5.dp))
        Text(
            text = stringResource(R.string.description_create_new_room),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun TarifDanKapasitasKamarSection(
    coroutineScope: CoroutineScope,
    buatKamarUiState: BuatKamarUiState,
    buatKamarActions: (BuatKamarActions) -> Unit,
    tarifSatuOrangState: TextFieldState,
    tarifDuaOrangState: TextFieldState,
    tarifSatuRequester: BringIntoViewRequester,
    tarifSatuFocus: FocusRequester,
    tarifDuaRequester: BringIntoViewRequester,
    tarifDuaFocus: FocusRequester
) {
    val listJumlahOrang = listOf(1, 2)

    val bringIntoViewUseSatuRequestOrDuaRequest = remember(buatKamarUiState.jumlahOrang) {
        if (buatKamarUiState.jumlahOrang == 2) tarifDuaRequester else tarifSatuRequester
    }

    Column(
        modifier = Modifier.bringIntoViewRequester(bringIntoViewUseSatuRequestOrDuaRequest)
    ) {
        Text(
            text = stringResource(R.string.choose_capacity_room),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(15.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            listJumlahOrang.forEach { jumlahOrang ->
                CustomRadioButtonV1(
                    modifier = Modifier.weight(1f),
                    text = "$jumlahOrang Orang",
                    selected = jumlahOrang == buatKamarUiState.jumlahOrang,
                    onClick = { buatKamarActions(BuatKamarActions.UpdateSelectJumlahOrang(jumlahOrang)) }
                )
            }
        }
        Spacer(Modifier.height(15.dp))
        Text(
            text = stringResource(R.string.bill_per_people),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(15.dp))
        GeneralTextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusEvent { focusState ->
                    if (focusState.isFocused) {
                        coroutineScope.launch {
                            delay(500)
                            tarifSatuRequester.bringIntoView()
                        }
                    }
                },
            focusRequester = tarifSatuFocus,
            state = tarifSatuOrangState,
            isError = buatKamarUiState.isTarifSatuOrangError,
            error = buatKamarUiState.tarifSatuOrangError?.asString() ?: "",
            label = R.string.bill_one_people,
            shakeTrigger = buatKamarUiState.shakeTriggerTarifSatuOrangError,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number,
            ),
            lineLimits = TextFieldLineLimits.SingleLine,
            inputTransformation = RupiahInputTransformation
        )
        AnimatedVisibility(
            visible = buatKamarUiState.jumlahOrang == 2
        ) {
            Spacer(Modifier.height(10.dp))
            GeneralTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusEvent { focusState ->
                        if (focusState.isFocused) {
                            coroutineScope.launch {
                                delay(500)
                                tarifDuaRequester.bringIntoView()
                            }
                        }
                    },
                focusRequester = tarifDuaFocus,
                state = tarifDuaOrangState,
                isError = buatKamarUiState.isTarifDuaOrangError,
                error = buatKamarUiState.tarifDuaOrangError?.asString() ?: "",
                label = R.string.bill_two_people,
                shakeTrigger = buatKamarUiState.shakeTriggerTarifDuaOrangError,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number,
                ),
                lineLimits = TextFieldLineLimits.SingleLine,
                inputTransformation = RupiahInputTransformation
            )
        }
    }
}

@Composable
fun UkuranKamarSection(
    buatKamarUiState: BuatKamarUiState,
    buatKamarActions: (BuatKamarActions) -> Unit,
    coroutineScope: CoroutineScope,
    ukuranKamarRequester: BringIntoViewRequester
) {
    val listUkuranKamar = listOf(
        "Kecil",
        "Sedang",
        "Besar",
        "Sangat Besar"
    )

    Column(modifier = Modifier.bringIntoViewRequester(ukuranKamarRequester)) {
        Text(
            text = stringResource(R.string.size_room),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(15.dp))
        DropDownCustomV1(
            modifier = Modifier
                .onFocusEvent { focusState ->
                    if (focusState.isFocused) {
                        coroutineScope.launch {
                            delay(500)
                            ukuranKamarRequester.bringIntoView()
                        }
                    }
                },
            items = listUkuranKamar,
            selectedItem = buatKamarUiState.ukuranKamar,
            onItemSelected = { buatKamarActions(BuatKamarActions.UpdateUkuranKamar(it))},
            itemToString = { ukuranKamar ->
                ukuranKamar ?: "Pilih Ukuran"
            }
        )
    }
}

@Composable
fun FasilitasKamarSection(
    buatKamarUiState: BuatKamarUiState,
    buatKamarActions: (BuatKamarActions) -> Unit,
    namaFasilitasState: TextFieldState,
    coroutineScope: CoroutineScope,
    fasilitasKamarRequester: BringIntoViewRequester
) {
    Column {
        Text(
            text = stringResource(R.string.list_facility_room),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(15.dp))
        Column(
            modifier = Modifier.animateContentSize(
                animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
            )
        ) {
            if (buatKamarUiState.fasilitasKamar.isNotEmpty()){
                buatKamarUiState.fasilitasKamar.forEachIndexed { index, fasilitas ->
                    key(fasilitas.hashCode()) {
                        val isVisible = remember {
                            MutableTransitionState(false).apply { targetState = true }
                        }

                        AnimatedVisibility(
                            visibleState = isVisible,
                            enter = slideInVertically { -it } + expandVertically() + fadeIn(),
                            exit = shrinkVertically() + fadeOut()
                        ) {
                            Column{
                                InfoItemCard(
                                    onDeleted = { buatKamarActions(BuatKamarActions.RemoveFasilitasKamar(index)) },
                                    title = fasilitas
                                )
                                Spacer(Modifier.height(15.dp))
                            }
                        }
                    }
                }
            } else {
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(animationSpec = tween(500)) + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column {
                        InfoItemCard(
                            title = stringResource(R.string.no_new_data),
                            isShowIconDelete = false
                        )
                        Spacer(Modifier.height(15.dp))
                    }
                }
            }
        }
        GeneralTextField(
            modifier = Modifier
                .fillMaxWidth()
                .bringIntoViewRequester(fasilitasKamarRequester)
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
                buatKamarActions(BuatKamarActions.AddFasilitasKamar)
                namaFasilitasState.clearText()
                      },
            text = stringResource(R.string.add_facility),
            shape = RoundedCornerShape(12.dp),
            height = 43.dp,
            enabled = buatKamarUiState.namaFasilitas.isNotEmpty(),
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
fun PemakaianAlatElektronikSection(
    buatKamarUiState: BuatKamarUiState,
    buatKamarActions: (BuatKamarActions) -> Unit,
    namaAlatElektronikState: TextFieldState,
    coroutineScope: CoroutineScope,
    pemakaianAlatElektronikKamarRequester: BringIntoViewRequester,
) {
    Column {
        Text(
            text = stringResource(R.string.pemakaian_elektronik_gratis),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(15.dp))
        Column(
            modifier = Modifier.animateContentSize(
                animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
            )
        ) {
            if (buatKamarUiState.layananElektronikGratisKamar.isNotEmpty()) {
                buatKamarUiState.layananElektronikGratisKamar.forEachIndexed { index, alatElektronik ->
                    key(alatElektronik.hashCode()) {

                        val isVisible = remember {
                            MutableTransitionState(false).apply { targetState = true }
                        }

                        AnimatedVisibility(
                            visibleState = isVisible,
                            enter = slideInVertically { -it } + expandVertically() + fadeIn(),
                            exit = shrinkVertically() + fadeOut()
                        ) {
                            Column {
                                InfoSectionCard(
                                    modifier = Modifier.fillMaxWidth(),
                                    icon = Icons.Filled.ElectricalServices,
                                    title = stringResource(R.string.usage_tools_electronic_free),
                                    action = CardAction.Delete {
                                        buatKamarActions(BuatKamarActions.RemovePemakaianAlatElektronikGratis(index))
                                    },
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
                        }
                    }
                }
            } else {
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(animationSpec = tween(500)) + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column {
                        InfoItemCard(
                            title = stringResource(R.string.no_new_data),
                            isShowIconDelete = false
                        )
                        Spacer(Modifier.height(15.dp))
                    }
                }
            }
        }
        GeneralTextField(
            modifier = Modifier.fillMaxWidth()
                .bringIntoViewRequester(pemakaianAlatElektronikKamarRequester)
                .onFocusEvent { focusState ->
                    if (focusState.isFocused) {
                        coroutineScope.launch {
                            delay(500)
                            pemakaianAlatElektronikKamarRequester.bringIntoView()
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
                buatKamarActions(BuatKamarActions.AddPemakaianAlatElektronikGratis)
                namaAlatElektronikState.clearText()
                      },
            text = stringResource(R.string.add_tool),
            shape = RoundedCornerShape(12.dp),
            height = 43.dp,
            enabled = buatKamarUiState.namaAlatElektronik.isNotEmpty(),
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