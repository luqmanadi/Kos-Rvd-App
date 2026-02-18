package com.kosrvd.app.feature.management.presentation.ui.screen.tagihan.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoorFront
import androidx.compose.material.icons.filled.ElectricalServices
import androidx.compose.material.icons.filled.Garage
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme
import com.kosrvd.app.feature.management.domain.model.AlatElektronik
import com.kosrvd.app.feature.management.presentation.designsystem.component.text.InfoContentColumnText
import com.kosrvd.app.feature.management.presentation.designsystem.component.text.InfoContentColumnTextForAlatElektronik

@Composable
fun RincianBiayaTagihanCard(
    modifier: Modifier = Modifier,
    adminFees: Boolean,
    highPowerElectronicEquipmentUsageCosts: List<AlatElektronik>,
    roomRentalFee: String,
    carParkingRentalFee: String
) {
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
        Row(
            Modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp)
        ) {
            InfoContentColumnText(
                title = stringResource(R.string.room_rental),
                value = roomRentalFee,
                icon = Icons.Filled.DoorFront,
                modifier = Modifier.weight(1f)
            )
            InfoContentColumnText(
                title = stringResource(R.string.car_parking_rental),
                value = carParkingRentalFee,
                icon = Icons.Filled.Garage,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(Modifier.height(15.dp))
        if (adminFees){
            InfoContentColumnText(
                title = stringResource(R.string.admin_fees),
                value = stringResource(R.string.nominal_admin_fees),
                icon = ImageVector.vectorResource(R.drawable.ic_jumlah_bayar),
                modifier = Modifier.padding(start = 20.dp)
            )
            Spacer(Modifier.height(15.dp))
        }
        InfoContentColumnTextForAlatElektronik(
            modifier = Modifier.padding(start = 20.dp, bottom = 20.dp),
            title = stringResource(R.string.use_of_electronic_devices),
            value = highPowerElectronicEquipmentUsageCosts,
            icon = Icons.Filled.ElectricalServices
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RincianBiayaTagihanCardPreview() {
    KosRvdAppTheme {
        RincianBiayaTagihanCard(
            modifier = Modifier.padding(20.dp),
            adminFees = fakeDetailTagihanUi.adminFees,
            highPowerElectronicEquipmentUsageCosts = fakeDetailTagihanUi.highPowerElectronicEquipmentUsageCostsMonthly,
            roomRentalFee = fakeDetailTagihanUi.roomRentalFee,
            carParkingRentalFee = fakeDetailTagihanUi.carParkingRentalFeeMonthly,
        )
    }
}