package com.kosrvd.app.feature.dashboard.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionOutlinePrimaryButton
import com.kosrvd.app.core.presentation.utils.textCopyThenPost

@Composable
fun InfoWifiCard(modifier: Modifier = Modifier) {
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
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(20.dp)
        ) {
            InfoWifiRow(
                ssid = stringResource(R.string.ssid_wifi_1),
                password = stringResource(R.string.password_wifi_1)
            )
            Spacer(Modifier.height(10.dp))
            HorizontalDivider(thickness = 1.dp)
            Spacer(Modifier.height(10.dp))
            InfoWifiRow(
                ssid = stringResource(R.string.ssid_wifi_2),
                password = stringResource(R.string.password_wifi_2)
            )
            Spacer(Modifier.height(10.dp))
            HorizontalDivider(thickness = 1.dp)
            Spacer(Modifier.height(10.dp))
            InfoWifiRow(
                ssid = stringResource(R.string.ssid_wifi_3),
                password = stringResource(R.string.password_wifi_3)
            )
        }

    }
}

@Composable
fun InfoWifiRow(
    modifier: Modifier = Modifier,
    ssid: String,
    password: String
) {
    val context = LocalContext.current
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        InfoSsidAndPasswordText(
            ssid = ssid,
            password = password
        )
        Spacer(Modifier.weight(1f))
        ActionOutlinePrimaryButton(
            onClick = {
                textCopyThenPost(context = context, textCopied = password)
            },
            height = 32.dp,
            contentPadding = PaddingValues(horizontal = 12.dp),
            text = stringResource(R.string.copy)
        )
    }
}
@Composable
fun InfoSsidAndPasswordText(
    modifier: Modifier = Modifier,
    ssid: String,
    password: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = ssid,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Password: $password",
            style = MaterialTheme.typography.bodyMedium)
    }
}