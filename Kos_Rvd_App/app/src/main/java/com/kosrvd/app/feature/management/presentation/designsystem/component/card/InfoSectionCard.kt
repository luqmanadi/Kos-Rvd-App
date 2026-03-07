package com.kosrvd.app.feature.management.presentation.designsystem.component.card

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme
import com.kosrvd.app.feature.management.presentation.designsystem.utils.CardAction

@Composable
fun InfoSectionCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    isRedBackgroundIcon: Boolean = false,
    action: CardAction, // Default tidak ada aksi
    footer: (@Composable () -> Unit)? = null, // Opsional untuk bagian bawah
    content: @Composable () -> Unit // Slot untuk isi (bisa teks/list)
) {
    // Tentukan modifier kartu berdasarkan aksi
    val cardModifier = modifier
        .fillMaxWidth()
        .shadow(
            elevation = 5.dp,
            shape = RoundedCornerShape(20.dp),
            ambientColor = MaterialTheme.colorScheme.primary,
            spotColor = MaterialTheme.colorScheme.primary,
        )
        .then(
            // Jika aksi navigasi, seluruh kartu bisa diklik
            if (action is CardAction.NavigationIconSide || action is CardAction.NavigationIconFooter) {
                Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { action.onClick() }
            } else {
                Modifier
            }
        )

    val bottomPadding = if (footer != null) 10.dp else 16.dp
    val backgroundIconColor = if (isRedBackgroundIcon) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.primary
    }
    val iconColor = if (isRedBackgroundIcon) {
        MaterialTheme.colorScheme.onError
    } else {
        MaterialTheme.colorScheme.onPrimary
    }

    Card(
        modifier = cardModifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            Modifier.padding(start = 16.dp, top = 16.dp, bottom = bottomPadding, end = 16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconColor,
                modifier = Modifier
                    .background(
                        color = backgroundIconColor,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(8.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title.uppercase(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(Modifier.height(5.dp))
                content()
            }
            when(action){
                is CardAction.NavigationIconSide -> {
                    Spacer(Modifier.width(16.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Detail",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                is CardAction.Delete -> {
                    Spacer(Modifier.width(16.dp))
                    IconButton(onClick = action.onDelete) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Hapus",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
                is CardAction.None -> {}
                is CardAction.NavigationIconFooter -> {}
            }
        }
        if (footer != null) {
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(Modifier.height(10.dp))
            Box(modifier = Modifier.padding(start = 16.dp, bottom = 16.dp, end = 16.dp).fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                footer()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InfoSectionCardPreview() {

    val footer = @Composable {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "2 Orang",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.width(10.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Detail",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
    KosRvdAppTheme {
        InfoSectionCard(
            Modifier.padding(20.dp),
            icon = Icons.Filled.Group,
            title = "Kapasitas Penghuni",
            action = CardAction.None,
            footer = footer,
            content = {
                Text(
                    text = "2 Orang",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        )
    }
}