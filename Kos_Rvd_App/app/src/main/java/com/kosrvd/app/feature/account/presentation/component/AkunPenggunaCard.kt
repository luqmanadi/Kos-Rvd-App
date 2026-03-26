package com.kosrvd.app.feature.account.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AssignmentInd
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.presentation.designsystem.component.text.IconTextInfo
import com.kosrvd.app.core.presentation.designsystem.atom.image.LoadImage
import com.kosrvd.app.feature.account.presentation.models.ListAkunUi

@Composable
fun AkunPenggunaCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    listAkunUi: ListAkunUi
) {
    val colorRole = if (listAkunUi.role == Constant.ADMIN_ROLE) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp).fillMaxWidth()
        ) {
            LoadImage(
                url = listAkunUi.photo,
                contentDescription = "Foto Orang",
                errorImg = R.drawable.ic_fill_profile,
                modifier = Modifier.border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = CircleShape
                ).size(60.dp)
            )
            Spacer(Modifier.width(15.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = listAkunUi.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(5.dp))
                IconTextInfo(
                    text = listAkunUi.role,
                    icon = Icons.Filled.AssignmentInd,
                    colorText = colorRole,
                    colorIcon = colorRole,
                    sizeIcon = 24.dp,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    spacing = 5.dp
                )
            }
            Spacer(Modifier.width(10.dp))
            IconTextInfo(
                text = listAkunUi.status,
                icon = Icons.Filled.Circle,
                colorText = MaterialTheme.colorScheme.onSurface,
                colorIcon = if (listAkunUi.status.capitalize(Locale.current) == "Aktif") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                sizeIcon = 10.dp,
                textStyle = MaterialTheme.typography.bodyMedium,
                spacing = 5.dp
            )
            Spacer(Modifier.width(10.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "Next",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}