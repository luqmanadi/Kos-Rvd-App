package com.kosrvd.app.core.presentation.designsystem.organism.card

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kosrvd.app.R
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionButton
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionOutlineButton
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionTonalButton
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme
import com.kosrvd.app.core.presentation.utils.dashedBorder
import com.kosrvd.app.core.presentation.utils.getDisplayUrl
import androidx.core.net.toUri

@Composable
fun BuktiFotoCard(
    modifier: Modifier = Modifier,
    previewOnly: Boolean = false,
    title: String,
    description: String,
    imageUri: Uri = Uri.EMPTY,
    isCanChooseImage: Boolean = false,
    isBuktiLaporan: Boolean = false,
    onChooseImage: () -> Unit = {},
    onPreviewImage: () -> Unit = {}
) {
    val context = LocalContext.current

    val finalImageSource = remember(imageUri) {
        getDisplayUrl(imageUri.toString())
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .dashedBorder(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(20.dp),
                    strokeWidth = 1.dp,
                    dashLength = 10.dp,
                    gapLength = 10.dp
                )
                .padding(1.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.outlinedCardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            if (imageUri == Uri.EMPTY) {
                if (isCanChooseImage){
                    StarterTitleAndDescription(
                        title = title,
                        description = description,
                        onChooseImage = onChooseImage
                    )
                } else {
                    val text = if (isBuktiLaporan) stringResource(R.string.no_photo_evidence_attached) else stringResource(R.string.no_proof_of_photo_response)

                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp)
                    )
                }

            } else {
                AsyncImage(
                    model = ImageRequest.Builder(context = context)
                        .data(finalImageSource)
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(R.string.proof_of_complain),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(height = 230.dp)
                        .fillMaxWidth(),
                    placeholder = painterResource(R.drawable.loading_img),
                    error = painterResource(R.drawable.ic_broken_image),
                    clipToBounds = true,
                )
            }
        }
        if (imageUri != Uri.EMPTY) {
            Spacer(Modifier.height(16.dp))
            ActionRowButton(
                modifier = Modifier.fillMaxWidth(),
                isPreviewOnly = previewOnly,
                onPreviewImage = onPreviewImage,
                onChooseImage = onChooseImage
            )
        }
    }
}

@Composable
fun StarterTitleAndDescription(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    onChooseImage: () -> Unit
) {
    Column(
        modifier = modifier
            .padding(horizontal = 25.dp, vertical = 40.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(5.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(15.dp))
        ActionButton(
            text = stringResource(R.string.choose_photo),
            onClick = onChooseImage,
            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
        )
    }
}

@Composable
fun ActionRowButton(
    modifier: Modifier = Modifier,
    isPreviewOnly: Boolean = false,
    onPreviewImage: () -> Unit,
    onChooseImage: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!isPreviewOnly) {
            ActionOutlineButton(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.change_photo),
                onClick = onChooseImage,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(R.string.change_photo),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
            )
        }
        ActionTonalButton(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.look),
            onClick = onPreviewImage,
            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Visibility,
                    contentDescription = stringResource(R.string.look)
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BuktiFotoCardPreview() {
    KosRvdAppTheme {
        BuktiFotoCard(
            modifier = Modifier.padding(16.dp),
            previewOnly = false,
            title = "Belum ada bukti foto laporan",
            description = "Silahkan unggah bukti laporan.",
            imageUri = "efefefefefef".toUri(),
            isCanChooseImage = false,
            isBuktiLaporan = false,
            onChooseImage = {},
            onPreviewImage = {}
        )
    }
}