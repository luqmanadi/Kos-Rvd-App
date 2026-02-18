package com.kosrvd.app.feature.management.presentation.ui.screen.tagihan.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChangeCircle
import androidx.compose.material.icons.filled.DoorFront
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import com.kosrvd.app.R
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme
import com.kosrvd.app.feature.management.domain.model.AlatElektronik
import com.kosrvd.app.feature.management.presentation.designsystem.component.text.InfoContentColumnText
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toNumber
import com.kosrvd.app.feature.management.presentation.ui.models.DetailTagihanUi

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
        shape = RoundedCornerShape(20.dp),
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

@Preview(showBackground = true)
@Composable
private fun InformasiTagihanCardPreview() {
    KosRvdAppTheme { 
        InformasiTagihanCard(
            modifier = Modifier.padding(20.dp),
            idTagihanOrIdPenyewaan = fakeDetailTagihanUi.idTagihan,
            numberRoom = fakeDetailTagihanUi.numberRoom?.toNumber()?:"",
            month = fakeDetailTagihanUi.billingMonth,
            residentNameList = fakeDetailTagihanUi.residentNameList,
            paymentStatus = fakeDetailTagihanUi.paymentStatus
        )
    }
}

val fakeDetailTagihanUi = DetailTagihanUi(
    adminFees = true,
    billAmount = 100000,
    billingMonth = "November 2025",
    carParkingRentalFeeMonthly = "Rp 0",
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
    numberRoom = 5,
    paymentStatus = "Belum Lunas",
    proofOfPayment = null,
    rejectionStatement = null,
    residentAccountIdList = listOf("wfjifjiefjiejf","inefineifniefn"),
    residentNameList = listOf("Ndiman NN", "HH Juan"),
    roomRentalFee = "Rp 100.000",
    verificationDate = null
)