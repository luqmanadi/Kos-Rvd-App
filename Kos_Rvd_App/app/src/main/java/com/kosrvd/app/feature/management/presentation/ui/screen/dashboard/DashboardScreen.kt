package com.kosrvd.app.feature.management.presentation.ui.screen.dashboard

import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.DoorFront
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.Timestamp
import com.kosrvd.app.R
import com.kosrvd.app.core.presentation.designsystem.component.card.ErrorCard
import com.kosrvd.app.core.presentation.designsystem.component.dialog.DialogPermission
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme
import com.kosrvd.app.core.presentation.utils.shimmerEffect
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toDayMonthAndYear
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toMonth
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toNumberRoomFormat
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toRupiahFormat
import com.kosrvd.app.feature.management.presentation.ui.models.AdminDashboardUi
import com.kosrvd.app.feature.management.presentation.ui.models.LastBill
import com.kosrvd.app.feature.management.presentation.ui.models.PenghuniDashboardUi
import com.kosrvd.app.feature.management.presentation.ui.screen.dashboard.component.InfoKosCard
import com.kosrvd.app.feature.management.presentation.ui.screen.dashboard.component.InfoLastBillCard
import com.kosrvd.app.feature.management.presentation.ui.screen.dashboard.component.InfoStatistikCard
import com.kosrvd.app.feature.management.presentation.ui.screen.dashboard.component.InfoWifiCard
import com.kosrvd.app.feature.management.presentation.ui.screen.dashboard.component.MenuDashboardLargeCard
import com.kosrvd.app.feature.management.presentation.ui.screen.dashboard.component.MenuDashboardSmallCard

@Composable
fun DashboardScreen(
    dashboardUiState: DashboardUiState,
    dashboardActions: (DashboardActions) -> Unit
) {
    val context = LocalContext.current
    val activity = LocalActivity.current
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted){
            Toast.makeText(context, "Izin notifikasi diaktifkan", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Izin notifikasi ditolak", Toast.LENGTH_SHORT).show()
        }
    }

    fun checkAndRequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = android.Manifest.permission.POST_NOTIFICATIONS
            when{
                ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED -> {}
                activity != null && ActivityCompat.shouldShowRequestPermissionRationale(activity, permission) -> {
                    dashboardActions(DashboardActions.OpenDialogRationale)
                }
                else -> requestPermissionLauncher.launch(permission)
            }
        }
    }

    LaunchedEffect(Unit) {
        checkAndRequestPermission()
    }

    if (dashboardUiState.showRationaleDialog){
        DialogPermission(
            onDismissRequest = {
                dashboardActions(DashboardActions.CloseDialogRationale)
                Toast.makeText(context, "Izin notifikasi ditolak", Toast.LENGTH_SHORT).show()
                               },
            onConfirmation = {
                dashboardActions(DashboardActions.CloseDialogRationale)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            },
            dialogTitle = "Izin Notifikasi",
            dialogText = buildAnnotatedString {
                append("Aplikasi membutuhkan izin notifikasi agar Anda tidak melewatkan ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){
                    append("Info Tagihan, Status Keluhan, dan Pengumuman Penting.")
                }
            },
            icon = Icons.Filled.NotificationsActive
        )
    }

    when {
        dashboardUiState.isLoading -> {
            LoadingDashboard(modifier = Modifier)
        }
        dashboardUiState.loadError != null -> {
            ErrorCard(
                message = dashboardUiState.loadError,
                onRetry = { dashboardActions(DashboardActions.TryAgain) },
                modifier = Modifier
            )
        }
        else -> {
            ContentDashboard(
                modifier = Modifier,
                dashboardUiState = dashboardUiState,
                dashboardActions = dashboardActions
            )
        }
    }
}

@Composable
fun ContentDashboard(
    modifier: Modifier = Modifier,
    dashboardUiState: DashboardUiState,
    dashboardActions: (DashboardActions) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
        modifier = modifier.fillMaxSize()
    ) {
        if (dashboardUiState.adminDashboardUi != null){
            item(key = "admin_info_statistik") {
                InfoStatistikSection(
                    adminDashboardUi = dashboardUiState.adminDashboardUi
                )
            }
            item { Spacer(Modifier.height(20.dp)) }
            item(key = "admin_menu_dashboard"){
                MenuDashboardAdminSection(
                    navigateToRiwayatParkiran = { dashboardActions(DashboardActions.NavigateToRiwayatParkirMobil) },
                    navigateToPenyewaan = { dashboardActions(DashboardActions.NavigateToPenyewaan) },
                    navigateToKamar = { dashboardActions(DashboardActions.NavigateToKamar) },
                    navigateToPengumuman = { dashboardActions(DashboardActions.NavigateToAnnouncement) },
                    navigateToPenghuni = { dashboardActions(DashboardActions.NavigateToListResident) },
                    navigateToZonaParkir = { dashboardActions(DashboardActions.NavigateToZonaParkir) },
                    navigateToAkunPengguna = { dashboardActions(DashboardActions.NavigateToAkunPengguna) }
                )
            }
        }

        if (dashboardUiState.penghuniDashboardUi != null){
            item(key = "penghuni_last_bill") {
                InfoLastBillCard(
                    numberRoom = dashboardUiState.penghuniDashboardUi.numberRoom,
                    lastBill = dashboardUiState.penghuniDashboardUi.lastBill,
                    navigateToDetailBill = {
                        dashboardActions(
                            DashboardActions.NavigateToDetailBill(
                                idBill = dashboardUiState.penghuniDashboardUi.lastBill?.idTagihan
                                    ?: ""
                            )
                        )
                    }
                )
            }
            item { Spacer(Modifier.height(20.dp)) }
            item(key = "penghuni_menu_dashboard") {
                MenuDashboardPenghuniSection(
                    navigateToPenghuni = { dashboardActions(DashboardActions.NavigateToListResident) },
                    navigateToPengumuman = { dashboardActions(DashboardActions.NavigateToAnnouncement) }
                )
            }
        }
        item { Spacer(Modifier.height(20.dp)) }
        item(key = "info_wifi") {
            Text(
                text = stringResource(R.string.info_wifi_kos),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
        item { Spacer(Modifier.height(15.dp)) }
        item(key = "info_wifi_card") {
            InfoWifiCard(
                modifier = Modifier.fillMaxWidth()
            )
        }
        item { Spacer(Modifier.height(20.dp)) }
        item(key = "info_kos") {
            Text(
                text = stringResource(R.string.info_kos),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
        item { Spacer(Modifier.height(15.dp)) }
        item(key = "info_kos_card") {
            InfoKosCard(
                modifier = Modifier.fillMaxWidth(),
                dashboardActions = dashboardActions
            )
        }
    }
}

@Composable
private fun LoadingDashboard(modifier: Modifier = Modifier) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
        modifier = modifier.fillMaxSize()
    ) {
        item {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .shimmerEffect()
            )
        }
        item { Spacer(Modifier.height(20.dp)) }
        item {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                Box(
                    Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .clip(RoundedCornerShape(20.dp))
                        .shimmerEffect()
                )
                Spacer(Modifier.width(16.dp))
                Box(
                    Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .clip(RoundedCornerShape(20.dp))
                        .shimmerEffect()
                )
            }
        }
        item { Spacer(Modifier.height(20.dp)) }
        item {
            Box(
                Modifier
                    .size(100.dp, 25.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .shimmerEffect()
            )
        }
        item { Spacer(Modifier.height(15.dp)) }
        item {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .shimmerEffect()
            )
        }
    }
}

@Composable
private fun InfoStatistikSection(
    modifier: Modifier = Modifier,
    adminDashboardUi: AdminDashboardUi
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row {
            InfoStatistikCard(
                title = stringResource(R.string.bill_not_pay_off),
                icon = Icons.AutoMirrored.Filled.ReceiptLong,
                value = adminDashboardUi.amountOfUnpaidBills,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(16.dp))
            InfoStatistikCard(
                title = stringResource(R.string.bill_need_verification),
                icon = ImageVector.vectorResource(R.drawable.ic_order_approve),
                value = adminDashboardUi.billAmountNeedsVerification,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(Modifier.height(20.dp))
        Row {
            InfoStatistikCard(
                title = stringResource(R.string.new_report_complaint),
                icon = ImageVector.vectorResource(R.drawable.ic_fill_keluhan),
                value = adminDashboardUi.numberOfNewComplaints,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(16.dp))
            InfoStatistikCard(
                title = stringResource(R.string.empty_room),
                icon = Icons.Default.DoorFront,
                value = adminDashboardUi.numberOfEmptyRooms,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun MenuDashboardPenghuniSection(
    modifier: Modifier = Modifier,
    navigateToPenghuni: () -> Unit,
    navigateToPengumuman: () -> Unit
) {
    Row(modifier.fillMaxWidth()) {
        MenuDashboardLargeCard(
            onClick = navigateToPenghuni,
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.list_residents),
            icon = painterResource(R.drawable.ic_penghuni)
        )
        Spacer(Modifier.width(16.dp))
        MenuDashboardLargeCard(
            onClick = navigateToPengumuman,
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.announcement),
            icon = painterResource(R.drawable.ic_pengumuman)
        )
    }
}

@Composable
private fun MenuDashboardAdminSection(
    modifier: Modifier = Modifier,
    navigateToRiwayatParkiran: () -> Unit,
    navigateToPenyewaan: () -> Unit,
    navigateToKamar: () -> Unit,
    navigateToPengumuman: () -> Unit,
    navigateToPenghuni: () -> Unit,
    navigateToZonaParkir: () -> Unit,
    navigateToAkunPengguna: () -> Unit
) {
    Column(modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MenuDashboardSmallCard(
                title = stringResource(R.string.residents),
                icon = painterResource(R.drawable.ic_penghuni),
                onClick = navigateToPenghuni
            )
            MenuDashboardSmallCard(
                title = stringResource(R.string.announcement),
                icon = painterResource(R.drawable.ic_pengumuman),
                onClick = navigateToPengumuman
            )
            MenuDashboardSmallCard(
                title = stringResource(R.string.rental),
                icon = painterResource(R.drawable.ic_penyewaan),
                onClick = navigateToPenyewaan
            )
            MenuDashboardSmallCard(
                title = stringResource(R.string.user_account),
                icon = painterResource(R.drawable.ic_akun_pengguna),
                onClick = navigateToAkunPengguna
            )
        }
        Spacer(Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            MenuDashboardSmallCard(
                title = stringResource(R.string.history_parking_car),
                icon = painterResource(R.drawable.ic_pemakaian_parkiran),
                onClick = navigateToRiwayatParkiran
            )
            MenuDashboardSmallCard(
                title = stringResource(R.string.parking_zone),
                icon = painterResource(R.drawable.ic_zona_parkiran),
                onClick = navigateToZonaParkir
            )
            MenuDashboardSmallCard(
                title = stringResource(R.string.room),
                icon = painterResource(R.drawable.ic_kamar),
                onClick = navigateToKamar
            )
        }
    }

}

@Preview(showSystemUi = true)
@Composable
private fun DashboardScreenPreview() {
    KosRvdAppTheme {
        val dueDate = Timestamp.now()
        val total: Long = 550000
        val numberRoom = 8
        val lastBill = LastBill(
            idTagihan = "ofjoejfeojfoejfoef",
            paymentStatus = "Menunggu Verifikasi",
            total = total.toRupiahFormat(),
            dueDate = dueDate.toDayMonthAndYear(),
            billingMonth = dueDate.toMonth()
        )
        val adminDashboardUi = AdminDashboardUi(
            name = "Admin",
            amountOfUnpaidBills = "1",
            billAmountNeedsVerification = "1",
            numberOfNewComplaints = "1",
            numberOfEmptyRooms = "1"
        )
        val penghuniDashboardUi = PenghuniDashboardUi(
            name = "Ndiman",
            lastBill = lastBill,
            numberRoom = numberRoom.toNumberRoomFormat()
        )
        val dashboardUiState = DashboardUiState(
            isLoading = false,
            loadError = null,
            penghuniDashboardUi = null,
            adminDashboardUi = adminDashboardUi
        )
        DashboardScreen(
            dashboardUiState = dashboardUiState,
            dashboardActions = {}
        )

    }
}