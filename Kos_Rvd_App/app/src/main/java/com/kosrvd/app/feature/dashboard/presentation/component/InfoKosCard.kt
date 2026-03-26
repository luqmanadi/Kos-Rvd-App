package com.kosrvd.app.feature.dashboard.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionButton
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionTonalButton
import com.kosrvd.app.feature.dashboard.presentation.DashboardActions

@Composable
fun InfoKosCard(
    modifier: Modifier = Modifier,
    dashboardActions: (DashboardActions) -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ){
        Image(
            painter = painterResource(R.drawable.photo_kos),
            contentDescription = "foto kos",
            modifier = Modifier.height(180.dp).fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.fillMaxWidth().padding(20.dp)
        ) {
            Text(
                text = stringResource(R.string.name_kos),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.address_kos),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(20.dp))
            ActionButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { dashboardActions(DashboardActions.NavigateToWhatsAppAdmin) },
                text = stringResource(R.string.whatsapp_admin),
                height = 40.dp
            )
            Spacer(Modifier.height(10.dp))
            ActionTonalButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { dashboardActions(DashboardActions.NavigateToMapsKosRVD) },
                text = stringResource(R.string.enter_google_maps),
                height = 40.dp
            )
        }
    }
}
