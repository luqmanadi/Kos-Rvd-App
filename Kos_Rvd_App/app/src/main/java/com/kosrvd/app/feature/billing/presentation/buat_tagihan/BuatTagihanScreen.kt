package com.kosrvd.app.feature.billing.presentation.buat_tagihan

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
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
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastJoinToString
import com.kosrvd.app.R
import com.kosrvd.app.core.domain.utils.CustomToastHostState
import com.kosrvd.app.core.presentation.designsystem.component.appbar.TopBarCenterTitle
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionButton
import com.kosrvd.app.core.presentation.designsystem.component.card.ErrorCard
import com.kosrvd.app.core.presentation.designsystem.component.radio_button.CustomRadioButtonV1
import com.kosrvd.app.core.presentation.designsystem.component.text.CustomToastHost
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme
import com.kosrvd.app.core.presentation.utils.convertMillisToTimeStamp
import com.kosrvd.app.core.presentation.designsystem.organism.card.EmptyItemDataCard
import com.kosrvd.app.core.presentation.designsystem.molecul.dropdown.DropDownCustomV1
import com.kosrvd.app.core.presentation.utils.toNumberRoomFormat
import com.kosrvd.app.feature.billing.presentation.components.AddDiskonCard
import com.kosrvd.app.feature.billing.presentation.components.DateRangePickerModalPeriodTagihan
import com.kosrvd.app.feature.billing.presentation.components.InfoDetailBuatTagihanCard
import com.kosrvd.app.feature.billing.presentation.components.RincianBiayaCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
fun BuatTagihanScreen(
    buatTagihanUiState: BuatTagihanUiState,
    buatTagihanActions: (BuatTagihanActions) -> Unit,
    customToastHostState: CustomToastHostState
) {
    Scaffold(
        topBar = {
            TopBarCenterTitle(
                title = stringResource(R.string.make_a_bill),
                onBackClick = { buatTagihanActions(BuatTagihanActions.NavigateBack) },
                isNeedBackIcon = true,
                fontWeight = FontWeight.Bold,
                containerColors = MaterialTheme.colorScheme.surfaceContainer
            )
        }
    ) { innerPadding ->
        when {
            buatTagihanUiState.loadError != null -> {
                ErrorCard(
                    modifier = Modifier.padding(innerPadding),
                    message = buatTagihanUiState.loadError,
                    onRetry = { buatTagihanActions(BuatTagihanActions.TryAgain) },
                    isLoadingButton = buatTagihanUiState.isButtonErrorLoading
                )
            }

            else -> {
                BuatTagihanContent(
                    modifier = Modifier.padding(innerPadding),
                    buatTagihanUiState = buatTagihanUiState,
                    buatTagihanActions = buatTagihanActions,
                    customToastHostState = customToastHostState
                )
            }
        }
    }
}

@Composable
private fun BuatTagihanContent(
    modifier: Modifier = Modifier,
    buatTagihanUiState: BuatTagihanUiState,
    buatTagihanActions: (BuatTagihanActions) -> Unit,
    customToastHostState: CustomToastHostState
) {
    val state = rememberScrollState()

    val percentageDiscountRequester = remember { BringIntoViewRequester() }
    val percentageDiscountFocus = remember { FocusRequester() }

    val descriptionDiscountRequester = remember { BringIntoViewRequester() }
    val descriptionDiscountFocus = remember { FocusRequester() }

    val selectDateRangeRequester = remember { BringIntoViewRequester() }

    val percentageDiscountState = rememberTextFieldState(buatTagihanUiState.percentageDiscount)
    LaunchedEffect(percentageDiscountState) {
        snapshotFlow { percentageDiscountState.text.toString() }.collectLatest {
            buatTagihanActions(BuatTagihanActions.UpdatePercentageDiscount(it))
        }
    }

    val descriptionDiscountState = rememberTextFieldState(buatTagihanUiState.descriptionDiscount)
    LaunchedEffect(descriptionDiscountState) {
        snapshotFlow { descriptionDiscountState.text.toString() }.collectLatest {
            buatTagihanActions(BuatTagihanActions.UpdateDescriptionDiscount(it))
        }
    }

    LaunchedEffect(
        buatTagihanUiState.isPercentageDiscountError,
        buatTagihanUiState.isDescriptionDiscountError,
        buatTagihanUiState.isSelectedPeriodError
    ) {
        if (buatTagihanUiState.isSelectedPeriodError) {
            delay(300)
            selectDateRangeRequester.bringIntoView()
        } else if (buatTagihanUiState.isPercentageDiscountError) {
            delay(300)
            percentageDiscountRequester.bringIntoView()
            percentageDiscountFocus.requestFocus()
        } else if (buatTagihanUiState.isDescriptionDiscountError) {
            delay(300)
            descriptionDiscountRequester.bringIntoView()
            descriptionDiscountFocus.requestFocus()
        }
    }

    Box(modifier = modifier.fillMaxSize().imePadding()) {
        Column {
            // content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(state)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = stringResource(R.string.title_make_a_new_bill),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(R.string.complete_the_following_data_to_create_a_new_bill),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                // Dropdown choose rental
                DropDownCustomV1(
                    items = buatTagihanUiState.listPenyewaan,
                    selectedItem = buatTagihanUiState.itemSelected,
                    onItemSelected = { buatTagihanActions(BuatTagihanActions.UpdateItemSelected(it)) },
                    itemToString = { penyewaan ->
                        if (penyewaan != null) {
                            val numberRoom = penyewaan.infoKamar.numberRoom.toNumberRoomFormat()
                            val listResidentName = penyewaan.listResident.map {
                                it.name.split(" ").firstOrNull() ?: it.name
                            }.fastJoinToString(separator = " & ")
                            "$numberRoom - $listResidentName"
                        } else {
                            "Pilih Penyewa Kamar"
                        }
                    },
                    textNoData = R.string.no_data_rental
                )

                // radio button choose maintenance fee
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = stringResource(R.string.is_new_resident),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )

                    val penghuniBaruOptions = listOf(true, false)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        penghuniBaruOptions.forEach { text ->
                            CustomRadioButtonV1(
                                modifier = Modifier.weight(1f),
                                text = if (text) stringResource(R.string.iya) else stringResource(R.string.no),
                                selected = text == buatTagihanUiState.maintenanceFee,
                                onClick = {
                                    buatTagihanActions(BuatTagihanActions.UpdateMaintenanceFee(text))
                                }
                            )
                        }
                    }
                }

                // Choose Date for Period Bill
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = stringResource(R.string.period_bill),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = buatTagihanUiState.selectedPeriod ?: "",
                        onValueChange = { },
                        label = { Text(stringResource(R.string.choose_date_range)) },
                        trailingIcon = {
                            Icon(
                                Icons.Filled.DateRange,
                                contentDescription = stringResource(R.string.choose_date_range)
                            )
                        },
                        readOnly = true,
                        isError = buatTagihanUiState.isSelectedPeriodError,
                        supportingText = {
                            if (buatTagihanUiState.isSelectedPeriodError) {
                                Text(
                                    text = buatTagihanUiState.selectedPeriodError?.asString() ?: ""
                                )
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .bringIntoViewRequester(selectDateRangeRequester)
                            .fillMaxWidth()
                            .pointerInput(buatTagihanUiState.selectedPeriod) {
                                awaitEachGesture {
                                    awaitFirstDown(pass = PointerEventPass.Initial)
                                    val upEvent =
                                        waitForUpOrCancellation(pass = PointerEventPass.Initial)
                                    if (upEvent != null) {
                                        buatTagihanActions(BuatTagihanActions.ShowDateRangePickerDialog)
                                    }
                                }
                            }
                    )
                }

                // radio button use discount or not
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                SpanStyle(
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append(stringResource(R.string.title_is_use_discount))
                            }
                            append(" (Opsional)")
                        },
                        style = MaterialTheme.typography.bodyMedium,
                    )

                    val useDiscountOptions = listOf(true, false)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        useDiscountOptions.forEach { text ->
                            CustomRadioButtonV1(
                                modifier = Modifier.weight(1f),
                                text = if (text) stringResource(R.string.iya) else stringResource(R.string.no),
                                selected = text == buatTagihanUiState.useDiscount,
                                onClick = {
                                    buatTagihanActions(BuatTagihanActions.UpdateUseDiscount(text))
                                }
                            )
                        }
                    }

                    if (buatTagihanUiState.useDiscount) {
                        Spacer(modifier = Modifier.height(5.dp))
                        AddDiskonCard(
                            percentageDiscountState = percentageDiscountState,
                            descriptionDiscountState = descriptionDiscountState,
                            buatTagihanUiState = buatTagihanUiState,
                            percentageDiscountRequester = percentageDiscountRequester,
                            percentageDiscountFocus = percentageDiscountFocus,
                            descriptionDiscountRequester = descriptionDiscountRequester,
                            descriptionDiscountFocus = descriptionDiscountFocus
                        )
                    }
                }

                // Informasi Tagihan Card
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = stringResource(R.string.bill_information),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )

                    if (buatTagihanUiState.isShowContent && buatTagihanUiState.itemSelected != null && buatTagihanUiState.selectedPeriodStart != null && buatTagihanUiState.selectedPeriodEnd != null) {
                        InfoDetailBuatTagihanCard(
                            idPenyewa = buatTagihanUiState.itemSelected.idPenyewa,
                            periodStart = convertMillisToTimeStamp(buatTagihanUiState.selectedPeriodStart),
                            periodEnd = convertMillisToTimeStamp(buatTagihanUiState.selectedPeriodEnd),
                            numberRoom = buatTagihanUiState.itemSelected.infoKamar.numberRoom,
                            listResident = buatTagihanUiState.itemSelected.listResident.map { it.name }
                        )
                    } else {
                        EmptyItemDataCard(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.select_a_tenant_first_to_view_this_information),
                            icon = Icons.AutoMirrored.Filled.ReceiptLong
                        )
                    }
                }

                // Informasi Rincian Biaya Card
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = stringResource(R.string.cost_breakdown),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )

                    if (buatTagihanUiState.isShowContent && buatTagihanUiState.itemSelected != null) {
                        RincianBiayaCard(
                            biayaSewaKamar = buatTagihanUiState.currentRentalCostBySumResident,
                            biayaMaintenance = buatTagihanUiState.maintenanceFee,
                            diskon = buatTagihanUiState.discount,
                            biayaSewaParkir = buatTagihanUiState.itemSelected.pemakaianParkirMobilBulanan?.zonaParkir?.monthlyFee,
                            pemakaianElektronik = buatTagihanUiState.itemSelected.pemakaianAlatElektronikBulanan,
                            totalTagihan = buatTagihanUiState.totalBill,
                            sumDayPeriodeBill = buatTagihanUiState.sumDayPeriodeBill,
                            prorataDetail = buatTagihanUiState.prorataDetail
                        )
                    } else {
                        EmptyItemDataCard(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.select_a_tenant_first_to_view_this_information),
                            icon = Icons.Filled.Paid
                        )
                    }
                }

            }

            // button
            Column {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                )
                ActionButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    height = 45.dp,
                    onClick = { buatTagihanActions(BuatTagihanActions.BuatTagihan) },
                    text = stringResource(R.string.make_a_bill),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(15.dp),
                    isLoading = buatTagihanUiState.isButtonLoading,
                    enabled = !buatTagihanUiState.isButtonLoading
                )
            }
        }

        if (buatTagihanUiState.showDialogDatePickerRange) {
            DateRangePickerModalPeriodTagihan(
                initialSelectedStartDateMillis = buatTagihanUiState.selectedPeriodStart,
                initialSelectedEndDateMillis = buatTagihanUiState.selectedPeriodEnd,
                onDissmiss = { buatTagihanActions(BuatTagihanActions.HideDateRangePickerDialog) },
                onDateSelected = { startMillis, endMillis, periodString ->
                    buatTagihanActions(
                        BuatTagihanActions.UpdatePeriod(
                            startMillis,
                            endMillis,
                            periodString
                        )
                    )
                }
            )
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


@Preview(showBackground = true)
@Composable
private fun BuatTagihanScreenPreview() {
    KosRvdAppTheme {
        val buatTagihanUiState = BuatTagihanUiState(
            isButtonLoading = true,
            maintenanceFee = false,
            itemSelected = null,
            loadError = null,
            listPenyewaan = emptyList(),
            showDialogDatePickerRange = false,
            useDiscount = false
        )
        BuatTagihanScreen(
            buatTagihanUiState = buatTagihanUiState,
            buatTagihanActions = {},
            customToastHostState = CustomToastHostState()
        )
    }
}