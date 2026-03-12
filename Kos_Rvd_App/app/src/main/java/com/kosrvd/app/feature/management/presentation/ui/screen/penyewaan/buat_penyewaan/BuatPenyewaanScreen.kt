package com.kosrvd.app.feature.management.presentation.ui.screen.penyewaan.buat_penyewaan

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DoorFront
import androidx.compose.material.icons.filled.ElectricalServices
import androidx.compose.material.icons.filled.Garage
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import com.kosrvd.app.R
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.domain.models.Account
import com.kosrvd.app.core.domain.utils.CustomToastHostState
import com.kosrvd.app.core.presentation.designsystem.component.appbar.TopBarLeftTitle
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionOutlineButton
import com.kosrvd.app.core.presentation.designsystem.component.card.ErrorCard
import com.kosrvd.app.core.presentation.designsystem.component.radio_button.CustomRadioButtonV3
import com.kosrvd.app.core.presentation.designsystem.component.text.CustomToastHost
import com.kosrvd.app.core.presentation.designsystem.component.textfield.GeneralTextField
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme
import com.kosrvd.app.core.presentation.utils.shimmerEffect
import com.kosrvd.app.feature.management.domain.model.AlatElektronik
import com.kosrvd.app.feature.management.domain.model.Harga
import com.kosrvd.app.feature.management.domain.model.Kamar
import com.kosrvd.app.feature.management.presentation.designsystem.component.card.EmptyDataListCard
import com.kosrvd.app.feature.management.presentation.designsystem.component.card.InfoItemCard
import com.kosrvd.app.feature.management.presentation.designsystem.component.card.InfoSectionCard
import com.kosrvd.app.feature.management.presentation.designsystem.component.dropdown.DropDownCustomV1
import com.kosrvd.app.feature.management.presentation.designsystem.organism.MultiStepIndicator
import com.kosrvd.app.feature.management.presentation.designsystem.organism.StepperControlBar
import com.kosrvd.app.feature.management.presentation.designsystem.utils.CardAction
import com.kosrvd.app.feature.management.presentation.designsystem.utils.RupiahFormatter
import com.kosrvd.app.feature.management.presentation.designsystem.utils.RupiahInputTransformation
import com.kosrvd.app.feature.management.presentation.designsystem.utils.ZonaParkirFormatter
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toNumberRoomFormat
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toRupiahFormat
import com.kosrvd.app.feature.management.presentation.ui.screen.penyewaan.component.DataPenghuniCard
import com.kosrvd.app.feature.management.presentation.ui.screen.penyewaan.component.InfoAkunPenghuniCard
import com.kosrvd.app.feature.management.presentation.ui.screen.penyewaan.component.InformasiKamarCard
import com.kosrvd.app.feature.management.presentation.ui.screen.penyewaan.component.PemakaianElektronikCard
import com.kosrvd.app.feature.management.presentation.ui.screen.penyewaan.component.PemakaianParkirMobilCard
import com.kosrvd.app.feature.management.presentation.ui.screen.penyewaan.component.RincianBiayaFinalCard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun BuatPenyewaanScreen(
    buatPenyewaanUiState: BuatPenyewaanUiState,
    buatPenyewaanActions: (BuatPenyewaanActions) -> Unit,
    customToastHostState: CustomToastHostState
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()

    val coroutineScope = rememberCoroutineScope()
    val nameElectronicRequester = remember { BringIntoViewRequester() }
    val priceElectronicRequester = remember { BringIntoViewRequester() }
    val carNameRequester = remember { BringIntoViewRequester() }
    val carBrandRequester = remember { BringIntoViewRequester() }
    val numberPlateRequester = remember { BringIntoViewRequester() }
    val notesRequester = remember { BringIntoViewRequester() }

    val nameElectronicState = rememberTextFieldState(buatPenyewaanUiState.namaAlatElektronik)
    LaunchedEffect(nameElectronicState) {
        snapshotFlow { nameElectronicState.text.toString() }.collectLatest {
            buatPenyewaanActions(BuatPenyewaanActions.OnUpdateNamaAlatElektronik(it))
        }
    }

    val initialPriceElectronicText = rememberSaveable(buatPenyewaanUiState.priceAlatElektronik) {
        RupiahFormatter.format(buatPenyewaanUiState.priceAlatElektronik)
    }
    val priceElectronicState = rememberTextFieldState(initialPriceElectronicText)
    LaunchedEffect(priceElectronicState) {
        snapshotFlow { priceElectronicState.text.toString() }.collectLatest {
            val cleanValue = RupiahFormatter.parseToRaw(it)
            buatPenyewaanActions(BuatPenyewaanActions.OnUpdatePriceAlatElektronik(cleanValue))
        }
    }

    val carNameState = rememberTextFieldState(buatPenyewaanUiState.carName)
    LaunchedEffect(carNameState) {
        snapshotFlow { carNameState.text.toString() }.collectLatest {
            buatPenyewaanActions(BuatPenyewaanActions.OnCarNameChange(it))
        }
    }

    val carBrandState = rememberTextFieldState(buatPenyewaanUiState.carBrand)
    LaunchedEffect(carBrandState) {
        snapshotFlow { carBrandState.text.toString() }.collectLatest {
            buatPenyewaanActions(BuatPenyewaanActions.OnCarBrandChange(it))
        }
    }

    val numberPlateState = rememberTextFieldState(buatPenyewaanUiState.numberPlate)
    LaunchedEffect(numberPlateState) {
        snapshotFlow { numberPlateState.text.toString() }.collectLatest {
            buatPenyewaanActions(BuatPenyewaanActions.OnNumberPlateChange(it))
        }
    }

    val notesState = rememberTextFieldState(buatPenyewaanUiState.notes)
    LaunchedEffect(notesState) {
        snapshotFlow { notesState.text.toString() }.collectLatest {
            buatPenyewaanActions(BuatPenyewaanActions.OnNotesChange(it))
        }
    }

    Scaffold(
        topBar = {
            TopBarLeftTitle(
                title = stringResource(R.string.add_rental),
                onBackClick = {
                    keyboardController?.hide()
                    buatPenyewaanActions(BuatPenyewaanActions.NavigateBack)
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
            MultiStepIndicator(
                modifier = Modifier.padding(vertical = 16.dp),
                listNamePage = buatPenyewaanUiState.listNamePage,
                currentStep = buatPenyewaanUiState.currentPage
            )
            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
            Box(Modifier
                .weight(1f)
                .clipToBounds()
            ){
                AnimatedContent(
                    targetState = buatPenyewaanUiState.currentPage,
                    transitionSpec = {
                        if (targetState > initialState) {
                            slideInHorizontally { it } + fadeIn() togetherWith
                                    slideOutHorizontally { -it } + fadeOut()
                        } else {
                            slideInHorizontally { -it } + fadeIn() togetherWith
                                    slideOutHorizontally { it } + fadeOut()
                        }
                    },
                    label = "Step Animasi"
                ) {step ->
                    when(step){
                        1 -> StepOneSelectKamar(
                            buatPenyewaanUiState = buatPenyewaanUiState,
                            buatPenyewaanActions = buatPenyewaanActions,
                            scrollState = scrollState
                        )
                        2 -> StepTwoSelectPenghuni(
                            buatPenyewaanUiState = buatPenyewaanUiState,
                            buatPenyewaanActions = buatPenyewaanActions,
                            scrollState = scrollState
                        )
                        3 -> StepThreeAddElectronicUsage(
                            buatPenyewaanUiState = buatPenyewaanUiState,
                            buatPenyewaanActions = buatPenyewaanActions,
                            scrollState = scrollState,
                            coroutineScope = coroutineScope,
                            nameElectronicRequester = nameElectronicRequester,
                            priceElectronicRequester = priceElectronicRequester,
                            nameElectronicState = nameElectronicState,
                            priceElectronicState = priceElectronicState
                        )
                        4 -> StepFourAddParkingCarUsage(
                            buatPenyewaanUiState = buatPenyewaanUiState,
                            buatPenyewaanActions = buatPenyewaanActions,
                            scrollState = scrollState,
                            coroutineScope = coroutineScope,
                            carNameRequester = carNameRequester,
                            carBrandRequester = carBrandRequester,
                            numberPlateRequester = numberPlateRequester,
                            notesRequester = notesRequester,
                            carNameState = carNameState,
                            carBrandState = carBrandState,
                            numberPlateState = numberPlateState,
                            notesState = notesState
                        )
                        5 -> StepFiveSummary(
                            buatPenyewaanUiState = buatPenyewaanUiState,
                            scrollState = scrollState
                        )
                    }
                }

                CustomToastHost(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    hostState = customToastHostState,
                    color = MaterialTheme.colorScheme.error,
                    enter = slideInVertically(
                        // Enters by sliding in from offset fullHeight to 0.
                        initialOffsetY = { fullHeight -> fullHeight },
                        animationSpec = tween(durationMillis = 150, easing = LinearOutSlowInEasing)
                    ),
                    exit = slideOutVertically(
                        // Exits by sliding out from offset 0 to fullHeight.
                        targetOffsetY = { fullHeight -> fullHeight },
                        animationSpec = tween(durationMillis = 250, easing = LinearOutSlowInEasing)
                    )
                )
            }
            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
            StepperControlBar(
                currentStep = buatPenyewaanUiState.currentPage,
                totalSteps = buatPenyewaanUiState.listNamePage.size,
                isNextEnabled = buatPenyewaanUiState.isButtonNextEnabled,
                isButtonSubmitLoading = buatPenyewaanUiState.isButtonSubmitLoading,
                onBack = {
                    if (buatPenyewaanUiState.currentPage == 3){
                        nameElectronicState.clearText()
                        priceElectronicState.clearText()
                    }
                    buatPenyewaanActions(BuatPenyewaanActions.BackPage)
                         },
                onNext = {
                    if (buatPenyewaanUiState.currentPage == 3){
                        nameElectronicState.clearText()
                        priceElectronicState.clearText()
                    }
                    buatPenyewaanActions(BuatPenyewaanActions.NextPage)
                         },
                onSubmit = { buatPenyewaanActions(BuatPenyewaanActions.AddPenyewaan)},
                keyboardController = keyboardController,
                lastTextButton = R.string.add_rental
            )
        }
    }
}


@Composable
private fun StepOneSelectKamar(
    modifier: Modifier = Modifier,
    buatPenyewaanUiState: BuatPenyewaanUiState,
    buatPenyewaanActions: (BuatPenyewaanActions) -> Unit,
    scrollState: ScrollState
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(vertical = 16.dp, horizontal = 20.dp),
    ) {
        Column {
            Text(
                text = stringResource(R.string.title_page_1_add_rental),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.subtitle_page_1_add_rental),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(Modifier.height(20.dp))
        when{
            buatPenyewaanUiState.isListKamarLoading -> {
                ListKamarLoading()
            }
            buatPenyewaanUiState.loadKamarError != null -> {
                ErrorCard(
                    modifier = Modifier.weight(1f),
                    message = buatPenyewaanUiState.loadKamarError,
                    onRetry = { buatPenyewaanActions(BuatPenyewaanActions.TryAgain) }
                )
            }
            buatPenyewaanUiState.listKamar.isNotEmpty() -> {
                Column(
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    buatPenyewaanUiState.listKamar.forEach { data ->
                        CustomRadioButtonV3(
                            icon = Icons.Filled.DoorFront,
                            title = data.numberRoom.toNumberRoomFormat(),
                            isTwoCapacity = data.price.twoPersons != null,
                            selected = data == buatPenyewaanUiState.selectedKamar,
                            onClick = {
                                buatPenyewaanActions(BuatPenyewaanActions.OnSelectedKamarChange(data))
                            }
                        )
                    }
                }
            }
            else -> {
                EmptyDataListCard(
                    title = R.string.title_empty_list_kamar_for_rental
                )
            }
        }
    }
}

@Composable
private fun ListKamarLoading(modifier: Modifier = Modifier) {
    val listNumber = 1..5
    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp),
        modifier = modifier.fillMaxSize()
    ) {
        listNumber.forEach { _ ->
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(73.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .shimmerEffect()
            )
        }
    }
}

@Composable
private fun StepTwoSelectPenghuni(
    modifier: Modifier = Modifier,
    buatPenyewaanUiState: BuatPenyewaanUiState,
    buatPenyewaanActions: (BuatPenyewaanActions) -> Unit,
    scrollState: ScrollState
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(vertical = 16.dp, horizontal = 20.dp),
    ) {
        Column {
            Text(
                text = stringResource(R.string.title_page_2_add_rental),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.subtitle_page_2_add_rental),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(Modifier.height(20.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Penghuni 1 ")
                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)){
                            append("(Wajib)")
                        }
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )

                DropDownCustomV1(
                    items = buatPenyewaanUiState.listPenghuni,
                    selectedItem = buatPenyewaanUiState.selectedPenghuniPertama,
                    onItemSelected = { buatPenyewaanActions(BuatPenyewaanActions.OnSelectedPenghuniPertamaChange(it)) },
                    itemToString = { data ->
                        data?.name ?: "Belum Memilih Penghuni 1 (Kosong)"
                    },
                    textNoData = R.string.no_data_resident,
                    itemEnabled = { item ->
                        item?.idAkun != buatPenyewaanUiState.selectedPenghuniKedua?.idAkun
                    }
                )
            }
            if (buatPenyewaanUiState.selectedKamar?.price?.twoPersons != null){
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = buildAnnotatedString {
                            append("Penghuni 2 ")
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.outline)){
                                append("(Opsional)")
                            }
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )

                    DropDownCustomV1(
                        items = buatPenyewaanUiState.listPenghuni,
                        selectedItem = buatPenyewaanUiState.selectedPenghuniKedua,
                        onItemSelected = { buatPenyewaanActions(BuatPenyewaanActions.OnSelectedPenghuniKeduaChange(it)) },
                        itemToString = { data ->
                            data?.name ?: "Belum Memilih Penghuni 2 (Kosong)"
                        },
                        textNoData = R.string.no_data_resident,
                        itemEnabled = { item ->
                            item?.idAkun != buatPenyewaanUiState.selectedPenghuniPertama?.idAkun
                        }
                    )
                }
            }
        }
        Spacer(Modifier.height(30.dp))
        InfoAkunPenghuniCard()
    }
}

@Composable
private fun StepThreeAddElectronicUsage(
    modifier: Modifier = Modifier,
    buatPenyewaanUiState: BuatPenyewaanUiState,
    buatPenyewaanActions: (BuatPenyewaanActions) -> Unit,
    scrollState: ScrollState,
    coroutineScope: CoroutineScope,
    nameElectronicRequester: BringIntoViewRequester,
    priceElectronicRequester: BringIntoViewRequester,
    nameElectronicState: TextFieldState,
    priceElectronicState: TextFieldState
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(vertical = 16.dp, horizontal = 20.dp),
    ) {
        Column {
            Text(
                text = stringResource(R.string.title_page_3_add_rental),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.subtitle_page_3_add_rental),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(Modifier.height(20.dp))
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
                lineLimits = TextFieldLineLimits.SingleLine
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
                    buatPenyewaanActions(BuatPenyewaanActions.AddAlatElektronik)
                    nameElectronicState.clearText()
                    priceElectronicState.clearText()
                },
                text = stringResource(R.string.add_tool),
                shape = RoundedCornerShape(12.dp),
                height = 43.dp,
                enabled = buatPenyewaanUiState.isButtonAddAlatElektronikEnabled,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null,
                    )
                }
            )
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
                if (buatPenyewaanUiState.listAlatElektronik.isNotEmpty()){
                    buatPenyewaanUiState.listAlatElektronik.forEachIndexed { index, alatElektronik ->
                        InfoSectionCard(
                            modifier = Modifier.fillMaxWidth(),
                            icon = Icons.Filled.ElectricalServices,
                            title = stringResource(R.string.name_tools_price),
                            action = if (alatElektronik.origin != Constant.ORIGIN_KAMAR_DEFAULT) {
                                CardAction.Delete {
                                    buatPenyewaanActions(BuatPenyewaanActions.RemoveAlatElektronik(index))
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

@Composable
private fun StepFourAddParkingCarUsage(
    modifier: Modifier = Modifier,
    buatPenyewaanUiState: BuatPenyewaanUiState,
    buatPenyewaanActions: (BuatPenyewaanActions) -> Unit,
    scrollState: ScrollState,
    coroutineScope: CoroutineScope,
    carNameRequester: BringIntoViewRequester,
    carBrandRequester: BringIntoViewRequester,
    numberPlateRequester: BringIntoViewRequester,
    notesRequester: BringIntoViewRequester,
    carNameState: TextFieldState,
    carBrandState: TextFieldState,
    numberPlateState: TextFieldState,
    notesState: TextFieldState
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(vertical = 16.dp, horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Column {
            Text(
                text = stringResource(R.string.title_page_4_add_rental),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.subtitle_page_4_add_rental),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            InfoSectionCard(
                icon = Icons.Filled.Garage,
                title = stringResource(R.string.parking_zone_price),
                action = CardAction.None,
                content = {
                    val textBody = if (buatPenyewaanUiState.selectedZoneParking != null){
                        val nameZone = ZonaParkirFormatter.format(buatPenyewaanUiState.selectedZoneParking.zoneName)
                        val priceZone = buatPenyewaanUiState.selectedZoneParking.monthlyFee.toRupiahFormat()
                        "$nameZone - $priceZone"
                    } else {
                        "Belum Pilih Zona Parkir Mobil"
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
                items = buatPenyewaanUiState.listZoneParking,
                selectedItem = buatPenyewaanUiState.selectedZoneParking,
                onItemSelected = { data ->
                    buatPenyewaanActions(BuatPenyewaanActions.OnSelectedZoneParkingChange(data))
                    if (data == null){
                        carNameState.clearText()
                        carBrandState.clearText()
                        numberPlateState.clearText()
                        notesState.clearText()
                    }
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
                    enabled = buatPenyewaanUiState.selectedZoneParking != null,
                    isError = buatPenyewaanUiState.isCarNameError,
                    error = buatPenyewaanUiState.carNameError?.asString() ?: "",
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
                    enabled = buatPenyewaanUiState.selectedZoneParking != null,
                    isError = buatPenyewaanUiState.isCarBrandError,
                    error = buatPenyewaanUiState.carBrandError?.asString() ?: "",
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
                    enabled = buatPenyewaanUiState.selectedZoneParking != null,
                    isError = buatPenyewaanUiState.isNumberPlateError,
                    error = buatPenyewaanUiState.numberPlateError?.asString() ?: "",
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
                text = stringResource(R.string.notes_for_usage_parking_car_monthly),
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
                enabled = buatPenyewaanUiState.selectedZoneParking != null,
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
private fun StepFiveSummary(
    modifier: Modifier = Modifier,
    buatPenyewaanUiState: BuatPenyewaanUiState,
    scrollState: ScrollState
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(vertical = 16.dp, horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Column {
            Text(
                text = stringResource(R.string.title_page_5_add_rental),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.subtitle_page_5_add_rental),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodySmall
            )
        }
        InformasiKamarCard(
            numberRoom = buatPenyewaanUiState.selectedKamar?.numberRoom.toString(),
            sizeRoom = buatPenyewaanUiState.selectedKamar?.size ?: "",
            facilityRoom = buatPenyewaanUiState.selectedKamar?.facility ?: emptyList()
        )
        DataPenghuniCard(listPenghuni = buatPenyewaanUiState.listNamePenghuni)
        if (buatPenyewaanUiState.listAlatElektronik.isNotEmpty()){
            PemakaianElektronikCard(listAlatElektronik = buatPenyewaanUiState.listAlatElektronik)
        }
        if (buatPenyewaanUiState.selectedZoneParking != null){
            PemakaianParkirMobilCard(
                zoneName = buatPenyewaanUiState.selectedZoneParking.zoneName,
                carBrand = buatPenyewaanUiState.carBrand,
                carName = buatPenyewaanUiState.carName,
                numberPlate = buatPenyewaanUiState.numberPlate,
                notes = buatPenyewaanUiState.notes
            )
        }
        RincianBiayaFinalCard(
            biayaKamar = buatPenyewaanUiState.roomPrice,
            biayaPemakaianParkir = buatPenyewaanUiState.selectedZoneParking?.monthlyFee,
            listAlatElektronik = buatPenyewaanUiState.listAlatElektronik,
            totalBiaya = buatPenyewaanUiState.totalBiaya
        )
    }
}

@Preview(showBackground = true)@Composable
private fun BuatPenyewaanScreenPreview() {
    KosRvdAppTheme {
        // Mock data untuk Kamar
        val mockKamar = Kamar(
            idKamar = "1",
            numberRoom = 7,
            price = Harga(onePerson = 1800000L, twoPersons = 2000000L),
            facility = listOf("Kamar Mandi Dalam", "Kasur Queen", "Lemari", "Meja Belajar"),
            size = "Sedang",
            freeService = listOf(AlatElektronik("Magicom", 0L, Constant.ORIGIN_KAMAR_DEFAULT)),
            status = Constant.ACTIVE
        )

        // Mock data untuk Akun Penghuni
        val mockAccount = Account(
            idAkun = "acc1",
            name = "Aditya Pratama",
            photo = "",
            role = Constant.PENGHUNI_ROLE,
            status = Constant.ACTIVE,
            numberOfUnreadNotification = 0,
            dateCreated = Timestamp.now()
        )

        // Mock UiState diset ke halaman konfirmasi (Step 5)
        val mockUiState = BuatPenyewaanUiState(
            isListKamarLoading = false,
            listKamar = listOf(mockKamar),
            selectedKamar = mockKamar,
            listPenghuni = listOf(null, mockAccount),
            selectedPenghuniPertama = mockAccount,
            listNamePenghuni = listOf("Aditya Pratama"),
            currentPage = 5,
            isButtonSubmitLoading = true,
            listAlatElektronik = listOf(
                AlatElektronik("Magicom", 0L, Constant.ORIGIN_KAMAR_DEFAULT),
                AlatElektronik("Dispenser", 25000L, Constant.ADD_ON)
            )
        )

        BuatPenyewaanScreen(
            buatPenyewaanUiState = mockUiState,
            buatPenyewaanActions = {},
            customToastHostState = CustomToastHostState()
        )
    }
}