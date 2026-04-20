package com.kosrvd.app.feature.billing.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChangeCircle
import androidx.compose.material.icons.filled.DoorFront
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.DoorFront
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import com.kosrvd.app.R
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.presentation.designsystem.component.text.IconTextInfo
import com.kosrvd.app.core.presentation.designsystem.component.text.StatusTagihanBackgroundText
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme
import com.kosrvd.app.core.domain.models.AlatElektronik
import com.kosrvd.app.core.presentation.designsystem.molecul.text.InfoContentColumnText
import com.kosrvd.app.core.presentation.designsystem.molecul.text.InfoIconTextColumnRow
import com.kosrvd.app.core.presentation.designsystem.molecul.text.InfoIconTextListColumnRow
import com.kosrvd.app.core.presentation.utils.toDayMonthShortAndYear
import com.kosrvd.app.core.presentation.utils.toMonth
import com.kosrvd.app.core.presentation.utils.toNumber
import com.kosrvd.app.feature.billing.domain.model.Diskon
import com.kosrvd.app.feature.billing.presentation.models.DetailTagihanUi

@Composable
fun InformasiTagihanCard(
    modifier: Modifier = Modifier,
    idTagihanOrIdPenyewaan: String,
    numberRoom: String,
    isUseIdPenyewa: Boolean = false,
    month: String,
    residentNameList: List<String>,
    paymentStatus: String,
) {
    val titleID = if (isUseIdPenyewa) stringResource(R.string.id_rental) else stringResource(R.string.id_bill)
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 5.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = MaterialTheme.colorScheme.primary,
                spotColor = MaterialTheme.colorScheme.primary,
            ),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        InfoContentColumnText(
            modifier = Modifier.padding(start = 20.dp, top = 20.dp),
            title = titleID,
            value = idTagihanOrIdPenyewaan,
            icon = Icons.AutoMirrored.Filled.Article
        )
        Spacer(Modifier.height(15.dp))
        Row(
            Modifier.padding(start = 20.dp, end = 20.dp)
        ) {
            InfoContentColumnText(
                title = stringResource(R.string.number_room),
                value = numberRoom,
                icon = Icons.Filled.DoorFront,
                modifier = Modifier.weight(1f)
            )
            InfoContentColumnText(
                title = stringResource(R.string.month),
                value = month,
                icon = Icons.Filled.CalendarMonth,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(Modifier.height(15.dp))
        InfoContentColumnText(
            modifier = Modifier.padding(start = 20.dp),
            title = stringResource(R.string.residents),
            value = residentNameList,
            icon = Icons.Filled.People
        )
        Spacer(Modifier.height(15.dp))
        InfoContentColumnText(
            modifier = Modifier.padding(start = 20.dp, bottom = 20.dp),
            title = stringResource(R.string.status),
            value = paymentStatus,
            isUseInfoStatus = true,
            icon = Icons.Filled.ChangeCircle
        )
    }
}

@Composable
fun InfoDetailTagihanCard(
    modifier: Modifier = Modifier,
    detailTagihanUi: DetailTagihanUi
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
    ) {
        InfoIconTextColumnRow(
            modifier = Modifier.padding(16.dp),
            icon = Icons.AutoMirrored.Outlined.ReceiptLong,
            title = stringResource(R.string.id_bill),
            description = detailTagihanUi.idTagihan
        )
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        )
        Row(
            Modifier
                .fillMaxWidth()
                .heightIn(max = 97.dp)
        ) {
            InfoIconTextColumnRow(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f),
                icon = Icons.Outlined.DoorFront,
                title = stringResource(R.string.number_room),
                description = detailTagihanUi.numberRoom
            )
            VerticalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
            InfoIconTextColumnRow(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f),
                icon = Icons.Outlined.CalendarMonth,
                title = stringResource(R.string.period),
                description = "${detailTagihanUi.periodStart.toDayMonthShortAndYear()} - ${detailTagihanUi.periodEnd.toDayMonthShortAndYear()}"
            )
        }
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        )
        InfoIconTextListColumnRow(
            modifier = Modifier.padding(16.dp),
            icon = Icons.Outlined.Group,
            title = stringResource(R.string.residents),
            description = detailTagihanUi.residentNameList,
        )
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        )
        InfoIconTextColumnRow(
            modifier = Modifier.padding(16.dp),
            icon = Icons.Outlined.CalendarToday,
            title = stringResource(R.string.date_created),
            description = detailTagihanUi.dateCreated
        )
        if (detailTagihanUi.paymentStatus == Constant.LUNAS && detailTagihanUi.datePaidOff != null) {
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
            InfoIconTextColumnRow(
                modifier = Modifier.padding(16.dp),
                icon = Icons.Outlined.CalendarToday,
                title = stringResource(R.string.payment_date),
                description = detailTagihanUi.datePaidOff
            )
        }
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        )
        Row(
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    RoundedCornerShape(bottomEnd = 24.dp, bottomStart = 24.dp)
                )
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconTextInfo(
                text = stringResource(R.string.status),
                icon = Icons.Outlined.Info,
                colorText = MaterialTheme.colorScheme.secondary,
                colorIcon = MaterialTheme.colorScheme.secondary,
                spacing = 16.dp
            )
            Spacer(Modifier.weight(1f))
            StatusTagihanBackgroundText(status = detailTagihanUi.paymentStatus)
        }
    }
}

@Composable
fun InfoDetailBuatTagihanCard(
    modifier: Modifier = Modifier,
    idPenyewa: String,
    periodStart: Timestamp,
    periodEnd: Timestamp,
    numberRoom: String,
    listResident: List<String>
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
    ) {
        InfoIconTextColumnRow(
            modifier = Modifier.padding(16.dp),
            icon = Icons.AutoMirrored.Outlined.ReceiptLong,
            title = stringResource(R.string.id_rental),
            description = idPenyewa
        )
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        )
        InfoIconTextColumnRow(
            modifier = Modifier
                .padding(16.dp),
            icon = Icons.Outlined.CalendarMonth,
            title = stringResource(R.string.period),
            description = "${periodStart.toDayMonthShortAndYear()} - ${periodEnd.toDayMonthShortAndYear()}"
        )
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        )
        InfoIconTextColumnRow(
            modifier = Modifier
                .padding(16.dp),
            icon = Icons.Outlined.DoorFront,
            title = stringResource(R.string.number_room),
            description = numberRoom
        )
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        )
        InfoIconTextListColumnRow(
            modifier = Modifier.padding(16.dp),
            icon = Icons.Outlined.Group,
            title = stringResource(R.string.residents),
            description = listResident,
        )
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        )
        Row(
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    RoundedCornerShape(bottomEnd = 24.dp, bottomStart = 24.dp)
                )
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconTextInfo(
                text = stringResource(R.string.status),
                icon = Icons.Outlined.Info,
                colorText = MaterialTheme.colorScheme.secondary,
                colorIcon = MaterialTheme.colorScheme.secondary,
                spacing = 16.dp
            )
            Spacer(Modifier.weight(1f))
            StatusTagihanBackgroundText(status = Constant.BELUM_LUNAS)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InformasiTagihanCardPreview() {
    KosRvdAppTheme {
        InformasiTagihanCard(
            modifier = Modifier.padding(20.dp),
            idTagihanOrIdPenyewaan = fakeDetailTagihanUi.idTagihan,
            numberRoom = fakeDetailTagihanUi.numberRoom.toNumber(),
            month = fakeDetailTagihanUi.periodEnd.toMonth(),
            residentNameList = fakeDetailTagihanUi.residentNameList,
            paymentStatus = fakeDetailTagihanUi.paymentStatus
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun InfoDetailTagihanCardPreview() {
    KosRvdAppTheme {
        Column(Modifier.padding(16.dp)) {
            InfoDetailTagihanCard(
                detailTagihanUi = fakeDetailTagihanUi
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InfoDetailBuatTagihanCardPreview() {
    KosRvdAppTheme {
        InfoDetailBuatTagihanCard(
            modifier = Modifier.padding(20.dp),
            idPenyewa = "eofeofjeo20o02",
            periodStart = Timestamp.now(),
            periodEnd = Timestamp.now(),
            numberRoom = "5",
            listResident = listOf(
                "Arjuna"
            )
        )
    }
}

val diskon = Diskon(
    percent = 10,
    price = 20000,
    description = "Promo Imlek"
)
val fakeDetailTagihanUi = DetailTagihanUi(
    maintenanceFee = true,
    billAmount = 100000,
    periodStart = Timestamp.now(),
    periodEnd = Timestamp.now(),
    carParkingRentalFeeMonthly = null,
    dateCreated = "10 Janurari 2025",
    datePaidOff = null,
    dateUploadProof = null,
    dueDate = Timestamp.now(),
    highPowerElectronicEquipmentUsageCostsMonthly = listOf(
        AlatElektronik("Setrika", 10000, ""),
        AlatElektronik("Setrika", 10000, ""),
    ),
    idPenyewa = "103013901iefhiefhe",
    idTagihan = "0190190ehfiehfiefhief",
    numberRoom = "5",
    paymentStatus = Constant.MENUNGGU_VERIFIKASI,
    proofOfPayment = null,
    rejectionStatement = null,
    residentAccountIdList = listOf("wfjifjiefjiejf", "inefineifniefn"),
    residentNameList = listOf("Ndiman NN", "HH Juan"),
    roomRentalFee = 100000,
    verificationDate = null,
    diskon = null,
    sumDayPeriodeBill = 30,
    prorataDetail = null,
)