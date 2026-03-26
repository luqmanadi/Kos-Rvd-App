package com.kosrvd.app.core.presentation.designsystem.organism.card

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kosrvd.app.R
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionOutlineButton
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionTonalButton
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme
import com.kosrvd.app.core.presentation.utils.dashedBorder
import com.kosrvd.app.core.presentation.utils.getDisplayUrl

@Composable
fun UnggahBuktiPembayaranCard(
    modifier: Modifier = Modifier,
    paymentStatus: String,
    uploadProofOfPayment: Uri,
    rejectionStatement: String?,
    onClickPreview: () -> Unit,
    onClickChooseImage: () -> Unit
) {
    val context = LocalContext.current

    if (uploadProofOfPayment == Uri.EMPTY) {
        val isRejection = paymentStatus == Constant.BELUM_LUNAS && rejectionStatement != null
        val title = if (isRejection) stringResource(R.string.upload_proof_re_upload) else stringResource(R.string.upload_proof)
        val description = if (isRejection) stringResource(R.string.description_re_upload_proof) else stringResource(R.string.description_upload_proof)

        Card(
            modifier = modifier
                .fillMaxWidth()
                .dashedBorder(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(20.dp),
                    strokeWidth = 1.dp,
                    dashLength = 10.dp,
                    gapLength = 10.dp
                )
                .clip(RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
            onClick = onClickChooseImage
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp, horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    modifier = Modifier.size(64.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.FileUpload,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline,
                    textAlign = TextAlign.Center
                )
            }
        }
    } else {
        Card(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.proof_transfer),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                AsyncImage(
                    model = ImageRequest.Builder(context = context)
                        .data(getDisplayUrl(uploadProofOfPayment.toString()))
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(R.string.proof_transfer),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(RoundedCornerShape(20.dp)),
                    placeholder = painterResource(R.drawable.loading_img),
                    error = painterResource(R.drawable.ic_broken_image)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ActionOutlineButton(
                        modifier = Modifier.weight(1f),
                        onClick = onClickPreview,
                        text = stringResource(R.string.look),
                        shape = RoundedCornerShape(24.dp),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    )

                    ActionTonalButton(
                        modifier = Modifier.weight(1f),
                        onClick = onClickChooseImage,
                        text = stringResource(R.string.change),
                        shape = RoundedCornerShape(24.dp),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UnggahBuktiPembayaranCardPreview() {
    KosRvdAppTheme {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            UnggahBuktiPembayaranCard(
                paymentStatus = Constant.BELUM_LUNAS,
                uploadProofOfPayment = Uri.EMPTY,
                rejectionStatement = null,
                onClickPreview = {},
                onClickChooseImage = {}
            )
            UnggahBuktiPembayaranCard(
                paymentStatus = Constant.BELUM_LUNAS,
                uploadProofOfPayment = Uri.EMPTY,
                rejectionStatement = "Foto buram",
                onClickPreview = {},
                onClickChooseImage = {}
            )
            UnggahBuktiPembayaranCard(
                paymentStatus = Constant.BELUM_LUNAS,
                uploadProofOfPayment = "https://example.com/image.jpg".toUri(),
                rejectionStatement = null,
                onClickPreview = {},
                onClickChooseImage = {}
            )
        }
    }
}
