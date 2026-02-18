package com.kosrvd.app.feature.management.presentation.ui.screen.tagihan.buat_tagihan

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastJoinToString
import com.kosrvd.app.R
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.domain.utils.CustomToastHostState
import com.kosrvd.app.core.presentation.designsystem.component.appbar.TopBarCenterTitle
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionButton
import com.kosrvd.app.core.presentation.designsystem.component.card.ErrorCard
import com.kosrvd.app.core.presentation.designsystem.component.radio_button.CustomRadioButtonV1
import com.kosrvd.app.core.presentation.designsystem.component.text.CustomToastHost
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme
import com.kosrvd.app.feature.management.presentation.designsystem.component.card.EmptyItemDataCard
import com.kosrvd.app.feature.management.presentation.designsystem.component.dropdown.DropDownCustomV1
import com.kosrvd.app.feature.management.presentation.designsystem.utils.getDueDateAsFifteenthOfMonth
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toMonthAndYear
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toNumber
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toNumberRoomFormat
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toRupiahFormat
import com.kosrvd.app.feature.management.presentation.ui.screen.tagihan.component.InformasiTagihanCard
import com.kosrvd.app.feature.management.presentation.ui.screen.tagihan.component.MetodePembayaranCardV2
import com.kosrvd.app.feature.management.presentation.ui.screen.tagihan.component.RincianBiayaTagihanCard

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
        when{
            buatTagihanUiState.loadError != null -> {
                ErrorCard(
                    modifier = Modifier.padding(innerPadding),
                    message = buatTagihanUiState.loadError,
                    onRetry = { buatTagihanActions(BuatTagihanActions.TryAgain)},
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
    Box(modifier = modifier.fillMaxSize()){

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 25.dp, horizontal = 16.dp)
        ) {
            item(key = "Header Tagihan Baru") {
                Text(
                    text = stringResource(R.string.title_make_a_new_bill),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            item { Spacer(Modifier.height(5.dp)) }
            item(key = "Description Tagihan Baru") {
                Text(
                    text = stringResource(R.string.complete_the_following_data_to_create_a_new_bill),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            item { Spacer(Modifier.height(20.dp)) }
            item(key = "Dropdown Memilih Penyewa") {
                DropDownCustomV1(
                    items = buatTagihanUiState.listPenyewaan,
                    selectedItem = buatTagihanUiState.itemSelected,
                    onItemSelected = { buatTagihanActions(BuatTagihanActions.UpdateItemSelected(it))},
                    itemToString = { penyewaan ->
                        if (penyewaan != null) {
                            val numberRoom = penyewaan.infoKamar.numberRoom.toNumberRoomFormat()
                            val listResidentName = penyewaan.listResident.map { it.name.split(" ").firstOrNull() ?: it.name }.fastJoinToString(separator = " & ")
                            "$numberRoom - $listResidentName"
                        } else {
                            "Pilih Penyewa Kamar"
                        }
                    }
                )
            }
            item { Spacer(Modifier.height(20.dp)) }
            item( key = "Header Memilih Penghuni Baru") {
                Text(
                    text = stringResource(R.string.is_new_resident),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            item { Spacer(Modifier.height(15.dp)) }
            item(key = "Radio Button memilih iya atau tidak") {
                val penghuniBaruOptions = listOf(true , false)
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    penghuniBaruOptions.forEach { text ->
                        CustomRadioButtonV1(
                            modifier = Modifier.weight(1f),
                            text = if (text) stringResource(R.string.iya) else stringResource(R.string.no),
                            selected = text == buatTagihanUiState.adminFees,
                            onClick = {
                                buatTagihanActions(BuatTagihanActions.UpdateAdminFees(text))
                            }
                        )
                    }
                }
            }
            item { Spacer(Modifier.height(20.dp)) }
            item(key = "Header Infromasi Tagihan") {
                Text(
                    text = stringResource(R.string.bill_information),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            item { Spacer(Modifier.height(15.dp)) }
            item(key = "Informasi Tagihan Card") {
                if (buatTagihanUiState.itemSelected != null){
                    val getMonth = getDueDateAsFifteenthOfMonth().toMonthAndYear()
                    InformasiTagihanCard(
                        modifier = Modifier.fillMaxWidth(),
                        idTagihanOrIdPenyewaan = buatTagihanUiState.itemSelected.idPenyewa,
                        numberRoom = buatTagihanUiState.itemSelected.infoKamar.numberRoom.toNumber(),
                        month = getMonth,
                        residentNameList = buatTagihanUiState.itemSelected.listResident.map { it.name },
                        paymentStatus = Constant.BELUM_LUNAS
                    )
                } else {
                    EmptyItemDataCard(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.select_a_tenant_first_to_view_this_information)
                    )
                }
            }
            item { Spacer(Modifier.height(20.dp)) }
            item(key = "Header Rincian Biaya") {
                Text(
                    text = stringResource(R.string.cost_breakdown),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            item { Spacer(Modifier.height(15.dp)) }
            item(key = "Rincian Biaya Card") {
                if (buatTagihanUiState.itemSelected != null){
                    val highPowerElectronicEquipmentUsageCosts = buatTagihanUiState.itemSelected.pemakaianAlatElektronikBulanan
                    val jumlahPenghuni = buatTagihanUiState.itemSelected.listResident.size
                    val roomRentalCost = buatTagihanUiState.itemSelected.infoKamar.currentRoomRentalCost
                    val roomRentalFee = if (jumlahPenghuni > 1) {
                        roomRentalCost.twoPersons ?: roomRentalCost.onePerson
                    } else {
                        roomRentalCost.onePerson
                    }
                    RincianBiayaTagihanCard(
                        modifier = Modifier.fillMaxWidth(),
                        adminFees = buatTagihanUiState.adminFees,
                        highPowerElectronicEquipmentUsageCosts = highPowerElectronicEquipmentUsageCosts,
                        roomRentalFee = roomRentalFee.toRupiahFormat(),
                        carParkingRentalFee = buatTagihanUiState.itemSelected.pemakaianParkirMobilBulanan?.zonaParkir?.monthlyFee?.toRupiahFormat() ?: "Rp 0",
                    )
                } else {
                    EmptyItemDataCard(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.select_a_tenant_first_to_view_this_information)
                    )
                }
            }
            item { Spacer(Modifier.height(20.dp)) }
            item(key = "Header Metode Pembayaran Transfer") {
                Text(
                    text = stringResource(R.string.payment_method),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            item { Spacer(Modifier.height(15.dp)) }
            item(key = "Metode Pembayaran Transfer Card") {
                if (buatTagihanUiState.itemSelected != null){
                    val totalMonthlyBill = buatTagihanUiState.itemSelected.totalMonthlyBill
                    val jumlahTransfer = if (buatTagihanUiState.adminFees){
                        totalMonthlyBill + 50000
                    } else {
                        totalMonthlyBill
                    }
                    MetodePembayaranCardV2(
                        modifier = Modifier.fillMaxWidth(),
                        jumlahTransfer = jumlahTransfer
                    )
                } else {
                    EmptyItemDataCard(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.select_a_tenant_first_to_view_this_information)
                    )
                }
            }
            item { Spacer(Modifier.height(30.dp)) }
            item(key = "Button Buat Tagihan") {
                ActionButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.make_a_bill),
                    onClick = { buatTagihanActions(BuatTagihanActions.BuatTagihan) },
                    shape = RoundedCornerShape(12.dp),
                    height = 43.dp,
                    isLoading = buatTagihanUiState.isButtonLoading
                )
            }
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
            isButtonLoading = false,
            adminFees = false,
            itemSelected = null,
            loadError = null,
            listPenyewaan = emptyList()
        )
        BuatTagihanScreen(
            buatTagihanUiState = buatTagihanUiState,
            buatTagihanActions = {},
            customToastHostState = CustomToastHostState()
        )
    }
}