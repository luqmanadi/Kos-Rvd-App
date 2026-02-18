package com.kosrvd.app.feature.management.presentation.ui.screen.tagihan.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.presentation.designsystem.component.text.IconTextInfo
import com.kosrvd.app.feature.management.presentation.designsystem.component.text.InfoContentColumnText

@Composable
fun RejectionPaymentCard(
    modifier: Modifier = Modifier,
    dateUploadProofOfPayment: String,
    response: String
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
        IconTextInfo(
            modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp),
            icon = Icons.Filled.Feedback,
            text = stringResource(R.string.payment_rejected),
            colorText = MaterialTheme.colorScheme.error,
            colorIcon = MaterialTheme.colorScheme.error
        )
        Spacer(Modifier.height(15.dp))
        InfoContentColumnText(
            modifier = Modifier.padding(start = 20.dp),
            title = stringResource(R.string.upload_proof_date),
            value = dateUploadProofOfPayment,
            icon = Icons.Filled.EditCalendar
        )
        Spacer(Modifier.height(15.dp))
        InfoContentColumnText(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
            title = stringResource(R.string.information),
            value = response,
            icon = Icons.AutoMirrored.Filled.Comment
        )
    }
}