package com.kosrvd.app.feature.management.presentation.ui.screen.keluhan.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import com.kosrvd.app.R
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme
import com.kosrvd.app.feature.management.presentation.designsystem.component.image.LoadImage
import com.kosrvd.app.feature.management.presentation.ui.models.ListKeluhanUi

@Composable
fun ItemKeluhanCard(
    modifier: Modifier = Modifier,
    keluhanUi: ListKeluhanUi,
    onClick: () -> Unit
) {
    val isNullPhotoComplaint = keluhanUi.photoComplaint.isNullOrBlank()

    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        // Image Section (hanya ditampilkan jika ada image)
        if (!isNullPhotoComplaint) {
            LoadImage(
                url = keluhanUi.photoComplaint,
                contentDescription = "Foto Keluhan",
                errorImg = R.drawable.ic_broken_image,
                shape = RectangleShape,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            )
        }

        // Content Section
        Row(
            Modifier
                .fillMaxWidth()
                .padding(
                    vertical = if (isNullPhotoComplaint) 16.dp else 10.dp,
                    horizontal = if (isNullPhotoComplaint) 22.dp else 20.dp
                ),
            verticalAlignment = if (isNullPhotoComplaint) Alignment.Bottom else Alignment.Top
        ) {

            if (isNullPhotoComplaint){
                Image(
                    painter = painterResource(getStatusIcon(keluhanUi.complaintStatus)),
                    contentDescription = "Icon Keluhan",
                    modifier = Modifier.size(32.dp).align(Alignment.Top)
                )
                Spacer(Modifier.width(13.dp))
            }

            // Text Content
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = keluhanUi.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = keluhanUi.reporterName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Status Text - positioning berbeda berdasarkan ada/tidak image
            if (!isNullPhotoComplaint) {
                Spacer(Modifier.width(13.dp))
                StatusText(
                    keluhanUi.complaintStatus,
                    Modifier.align(Alignment.Bottom)
                )
            }
        }

        // Status Text untuk layout tanpa image (posisi bottom end)
        if (isNullPhotoComplaint) {
            Spacer(Modifier.height(5.dp))
            StatusText(
                keluhanUi.complaintStatus,
                Modifier.align(Alignment.End)
                    .padding(bottom = 16.dp, end = 22.dp)
            )
        }
    }
}

// Helper functions untuk mengurangi kompleksitas
private fun getStatusIcon(status: String): Int {
    return when(status) {
        Constant.MENUNGGU_KONFIRMASI -> R.drawable.ic_report_menunggu_konfirmasi
        Constant.SEDANG_DIPROSES -> R.drawable.ic_report_process
        else -> R.drawable.ic_report_completion
    }
}

@Composable
private fun getStatusColor(status: String): Color {
    return when(status) {
        Constant.MENUNGGU_KONFIRMASI -> MaterialTheme.colorScheme.error
        Constant.SEDANG_DIPROSES -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.primary
    }
}

@Composable
private fun StatusText(status: String, modifier : Modifier = Modifier) {
    Text(
        text = status,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold,
        color = getStatusColor(status),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun ItemKeluhanCardPreview() {
    val keluhanUi = ListKeluhanUi(
        idKeluhan = "",
        idAkun = "",
        reporterName = "Abdul Jafar & Haji Wongso Ringgo",
        title = "Bocor Deras Atap",
        photoComplaint = "jij",
        complaintStatus = "Sedang Diproses",
        reportDate = Timestamp.now(),
        processDate = Timestamp.now(),
        completionDate = Timestamp.now()
    )
    KosRvdAppTheme {
        ItemKeluhanCard(
            keluhanUi = keluhanUi,
            onClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}