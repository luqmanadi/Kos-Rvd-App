package com.kosrvd.app.feature.management.presentation.ui.screen.parkir_harian_mobil.tambah_parkir_harian_mobil

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
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalParking
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.domain.utils.CustomToastHostState
import com.kosrvd.app.core.presentation.designsystem.component.appbar.TopBarLeftTitle
import com.kosrvd.app.core.presentation.designsystem.component.card.ErrorCard
import com.kosrvd.app.core.presentation.designsystem.component.radio_button.CustomRadioButtonV2
import com.kosrvd.app.core.presentation.designsystem.component.text.CustomToastHost
import com.kosrvd.app.core.presentation.designsystem.component.textfield.GeneralTextField
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme
import com.kosrvd.app.core.presentation.utils.shimmerEffect
import com.kosrvd.app.feature.management.domain.model.ZonaParkiran
import com.kosrvd.app.feature.management.presentation.designsystem.utils.ZonaParkirFormatter
import com.kosrvd.app.feature.management.presentation.ui.screen.parkir_harian_mobil.component.DateRangePickerParkirHarianMobilCard
import com.kosrvd.app.feature.management.presentation.ui.screen.parkir_harian_mobil.component.EmptyListZoneParkingCard
import com.kosrvd.app.feature.management.presentation.ui.screen.parkir_harian_mobil.component.KonfirmasiDataTambahParkirHarianMobilCard
import com.kosrvd.app.feature.management.presentation.ui.screen.parkir_harian_mobil.component.MultiStepIndicator
import com.kosrvd.app.feature.management.presentation.ui.screen.parkir_harian_mobil.component.StepperControlBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun TambahParkirHarianMobilScreen(
    tambahParkirHarianMobilUiState: TambahParkirHarianMobilUiState,
    tambahParkirHarianMobilActions: (TambahParkirHarianMobilActions) -> Unit,
    customToastHostState: CustomToastHostState
) {
    Scaffold(
        topBar = {
            TopBarLeftTitle(
                title = stringResource(R.string.add_usage_parking_card),
                onBackClick = { tambahParkirHarianMobilActions(TambahParkirHarianMobilActions.NavigateBack) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            MultiStepIndicator(
                modifier = Modifier.padding(vertical = 16.dp),
                listNamePage = tambahParkirHarianMobilUiState.listNamePage,
                currentStep = tambahParkirHarianMobilUiState.currentPage
            )
            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
            Box(Modifier
                .weight(1f)
                .clipToBounds()){

                AnimatedContent(
                    targetState = tambahParkirHarianMobilUiState.currentPage,
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
                        1 -> StepOneSelectDate(
                            tambahParkirHarianMobilUiState = tambahParkirHarianMobilUiState,
                            tambahParkirHarianMobilActions = tambahParkirHarianMobilActions
                        )
                        2 -> StepTwoSelectZone(
                            tambahParkirHarianMobilUiState = tambahParkirHarianMobilUiState,
                            tambahParkirHarianMobilActions = tambahParkirHarianMobilActions
                        )
                        3 -> StepThreeFillData(
                            tambahParkirHarianMobilUiState = tambahParkirHarianMobilUiState,
                            tambahParkirHarianMobilActions = tambahParkirHarianMobilActions
                        )
                        4 -> StepFourConfirm(
                            tambahParkirHarianMobilUiState = tambahParkirHarianMobilUiState
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
                currentStep = tambahParkirHarianMobilUiState.currentPage,
                totalSteps = tambahParkirHarianMobilUiState.listNamePage.size,
                isNextEnabled = tambahParkirHarianMobilUiState.isButtonNextEnabled,
                isButtonSubmitLoading = tambahParkirHarianMobilUiState.isButtonSubmitLoading,
                onBack = { tambahParkirHarianMobilActions(TambahParkirHarianMobilActions.BackPage)},
                onNext = { tambahParkirHarianMobilActions(TambahParkirHarianMobilActions.NextPage)},
                onSubmit = { tambahParkirHarianMobilActions(TambahParkirHarianMobilActions.TambahParkirHarianMobil)}
            )
        }
    }
}

@Composable
fun StepOneSelectDate(
    modifier: Modifier = Modifier,
    tambahParkirHarianMobilUiState: TambahParkirHarianMobilUiState,
    tambahParkirHarianMobilActions: (TambahParkirHarianMobilActions) -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(vertical = 16.dp, horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DateRangePickerParkirHarianMobilCard(
            startDate = tambahParkirHarianMobilUiState.startDate,
            completionDate = tambahParkirHarianMobilUiState.completionDate,
            onSelectedDate = { startDate, completionDate ->
                tambahParkirHarianMobilActions(TambahParkirHarianMobilActions.UpdateSelectedDate(startDate, completionDate))
            }
        )
        Spacer(Modifier.height(15.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = tambahParkirHarianMobilUiState.startDateFormat ?: "",
                onValueChange = { },
                label = { Text(stringResource(R.string.start_date)) },
                readOnly = true,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = tambahParkirHarianMobilUiState.completionDateFormat ?: "",
                onValueChange = { },
                label = { Text(stringResource(R.string.end_date)) },
                readOnly = true,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f)
            )
        }
    }

}

@Composable
private fun StepTwoSelectZone(
    modifier: Modifier = Modifier,
    tambahParkirHarianMobilUiState: TambahParkirHarianMobilUiState,
    tambahParkirHarianMobilActions: (TambahParkirHarianMobilActions) -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(vertical = 16.dp, horizontal = 20.dp),
    ) {
        Text(
            text = stringResource(R.string.choose_zone_parking),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(20.dp))
        when {
            tambahParkirHarianMobilUiState.isListZoneParkingLoading -> {
                ListZoneParkingLoading()
            }
            tambahParkirHarianMobilUiState.loadError != null -> {
                ErrorCard(
                    modifier = Modifier.weight(1f),
                    message = tambahParkirHarianMobilUiState.loadError,
                    onRetry = { tambahParkirHarianMobilActions(TambahParkirHarianMobilActions.TryAgain) }
                )
            }
            tambahParkirHarianMobilUiState.listZoneParking.isNotEmpty() -> {
                Column(
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    tambahParkirHarianMobilUiState.listZoneParking.forEach { data ->
                        CustomRadioButtonV2(
                            icon = Icons.Filled.LocalParking,
                            title = ZonaParkirFormatter.format(data.zoneName),
                            subtitle = "1 Slot Mobil",
                            selected = data == tambahParkirHarianMobilUiState.selectedZoneParking,
                            onClick = {
                                tambahParkirHarianMobilActions(TambahParkirHarianMobilActions.UpdateSelectedZoneParking(data))
                            }
                        )
                    }
                }
            }
            else -> {
                EmptyListZoneParkingCard()
            }

        }
    }
}

@Composable
private fun StepThreeFillData(
    modifier: Modifier = Modifier,
    tambahParkirHarianMobilUiState: TambahParkirHarianMobilUiState,
    tambahParkirHarianMobilActions: (TambahParkirHarianMobilActions) -> Unit
) {
    val scrollState = rememberScrollState()

    val coroutineScope = rememberCoroutineScope()
    val userNameRequester = remember { BringIntoViewRequester() }
    val numberPlateRequester = remember { BringIntoViewRequester() }
    val carNameRequester = remember { BringIntoViewRequester() }
    val carBrandRequester = remember { BringIntoViewRequester() }
    val notesRequester = remember { BringIntoViewRequester() }

    val userNameState = rememberTextFieldState(tambahParkirHarianMobilUiState.userName)
    LaunchedEffect(userNameState) {
        snapshotFlow { userNameState.text.toString() }.collectLatest {
            tambahParkirHarianMobilActions(TambahParkirHarianMobilActions.UpdateUserName(it))
        }
    }

    val numberPlateState = rememberTextFieldState(tambahParkirHarianMobilUiState.numberPlate)
    LaunchedEffect(numberPlateState) {
        snapshotFlow { numberPlateState.text.toString() }.collectLatest {
            tambahParkirHarianMobilActions(TambahParkirHarianMobilActions.UpdateNumberPlate(it))
        }
    }

    val carNameState = rememberTextFieldState(tambahParkirHarianMobilUiState.carName)
    LaunchedEffect(carNameState) {
        snapshotFlow { carNameState.text.toString() }.collectLatest {
            tambahParkirHarianMobilActions(TambahParkirHarianMobilActions.UpdateCarName(it))
        }
    }

    val carBrandState = rememberTextFieldState(tambahParkirHarianMobilUiState.carBrand)
    LaunchedEffect(carBrandState) {
        snapshotFlow { carBrandState.text.toString() }.collectLatest {
            tambahParkirHarianMobilActions(TambahParkirHarianMobilActions.UpdateCarBrand(it))
        }
    }

    val notesState = rememberTextFieldState(tambahParkirHarianMobilUiState.notes)
    LaunchedEffect(notesState) {
        snapshotFlow { notesState.text.toString() }.collectLatest {
            tambahParkirHarianMobilActions(TambahParkirHarianMobilActions.UpdateNotes(it))
        }
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(vertical = 16.dp, horizontal = 20.dp)
            .imePadding(),
    ) {
        Text(
            text = stringResource(R.string.title_fill_data_usage_parking_daily),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(20.dp))
        GeneralTextField(
            modifier = Modifier
                .fillMaxWidth()
                .bringIntoViewRequester(userNameRequester)
                .onFocusEvent { focusState ->
                    if (focusState.isFocused) {
                        coroutineScope.launch {
                            delay(500)
                            userNameRequester.bringIntoView()
                        }
                    }
                },
            state = userNameState,
            isError = tambahParkirHarianMobilUiState.isUserNameError,
            error = tambahParkirHarianMobilUiState.userNameError?.asString() ?: "",
            label = R.string.name_tenant,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text,
            ),
            lineLimits = TextFieldLineLimits.SingleLine,
        )
        Spacer(Modifier.height(5.dp))
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
            isError = tambahParkirHarianMobilUiState.isNumberPlateError,
            error = tambahParkirHarianMobilUiState.numberPlateError?.asString() ?: "",
            label = R.string.number_police,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text,
            ),
            lineLimits = TextFieldLineLimits.SingleLine,
        )
        Spacer(Modifier.height(5.dp))
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
            isError = tambahParkirHarianMobilUiState.isCarNameError,
            error = tambahParkirHarianMobilUiState.carNameError?.asString() ?: "",
            label = R.string.name_car,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text,
            ),
            lineLimits = TextFieldLineLimits.SingleLine,
        )
        Spacer(Modifier.height(5.dp))
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
            isError = tambahParkirHarianMobilUiState.isCarBrandError,
            error = tambahParkirHarianMobilUiState.carBrandError?.asString() ?: "",
            label = R.string.brand_car,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text,
            ),
            lineLimits = TextFieldLineLimits.SingleLine,
        )
        Spacer(Modifier.height(5.dp))
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
            label = R.string.note_optional,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text,
            ),
            lineLimits = TextFieldLineLimits.SingleLine,
        )
    }
}

@Composable
private fun StepFourConfirm(
    modifier: Modifier = Modifier,
    tambahParkirHarianMobilUiState: TambahParkirHarianMobilUiState
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(vertical = 16.dp, horizontal = 20.dp),
    ) {
        Text(
            text = stringResource(R.string.confirmation_data),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(20.dp))
        KonfirmasiDataTambahParkirHarianMobilCard(
            tambahParkirHarianMobilUiState = tambahParkirHarianMobilUiState
        )
    }
}

@Composable
private fun ListZoneParkingLoading(modifier: Modifier = Modifier) {
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

@Preview(showSystemUi = true)
@Composable
private fun TambahParkirHarianMobilScreenPreview() {
    KosRvdAppTheme {
        val listZonaParkiran = listOf(
            ZonaParkiran(
                monthlyFee = 100000,
                idZonaParkir = "feojfoejf",
                zoneName = "A",
                dailyCosts = 2000,
                status = "Kosong",
            ),
            ZonaParkiran(
                monthlyFee = 100000,
                idZonaParkir = "feojfoejf",
                zoneName = "B",
                dailyCosts = 2000,
                status = "Kosong",
            )
        )
        TambahParkirHarianMobilScreen(
            tambahParkirHarianMobilUiState = TambahParkirHarianMobilUiState(
                startDateFormat = "16 Feb 2020",
                completionDateFormat = "16 Feb 2020",
                startDate = 1755216000000L,
                completionDate = 1755648000000L,
                sumDayBooking = 10,
                carName = "Innova",
                carBrand = "Toyota",
                userName = "Gojo",
                totalCost = 100000,
                numberPlate = "Y 9389 H",
                notes = "oeihfiwf piwefipwejf pwiejfpiwejf pwejfpiwejf",
                currentPage = 2,
                loadError = null,
                isListZoneParkingLoading = false,
                listZoneParking = listZonaParkiran,
                selectedZoneParking = listZonaParkiran[0]
            ),
            tambahParkirHarianMobilActions = {},
            customToastHostState = CustomToastHostState()
        )
    }
}