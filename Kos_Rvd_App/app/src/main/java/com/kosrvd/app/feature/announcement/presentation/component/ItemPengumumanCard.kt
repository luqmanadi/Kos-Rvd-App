package com.kosrvd.app.feature.announcement.presentation.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.domain.utils.Role
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionDangerButton
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme
import com.kosrvd.app.feature.announcement.presentation.models.PengumumanUi

@Composable
fun ItemPengumumanCard(
    modifier: Modifier = Modifier,
    dataPengumumanUi: PengumumanUi,
    onDeleted: () -> Unit = {},
    role: Role = Role.EMPTY
) {
    var expanded by remember { mutableStateOf(false) }
    var showExpandButtonTrigger by remember { mutableStateOf(false) }
    Card(
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outline),
        shape = RoundedCornerShape(20.dp),
        modifier = modifier.fillMaxWidth()
            .animateContentSize(
                animationSpec = spring(
                    stiffness = Spring.StiffnessMedium
                )
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Image(
                painter = painterResource(R.drawable.ic_pengumuman),
                contentDescription = "Icon Pengumuman",
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = dataPengumumanUi.title.capitalize(Locale.current),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(Modifier.height(5.dp))
                Text(
                    text = dataPengumumanUi.dateCreated,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            Spacer(Modifier.width(20.dp))
            val showIconButton = showExpandButtonTrigger || role == Role.ADMIN || expanded
            if (showIconButton){
                IconButton(
                    onClick = {
                        expanded = !expanded
                    },
                    shape = CircleShape,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.size(32.dp)
                ){
                    Icon(
                        imageVector = if (!expanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                        contentDescription = "Expand Pengumuman",
                    )
                }
            }
        }
        Spacer(Modifier.height(15.dp))
        Text(
            text = dataPengumumanUi.content,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(start = 16.dp, bottom = 16.dp, end = 16.dp)
            ,
            maxLines = if (expanded) Int.MAX_VALUE else 3,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { textLayoutResult ->
                showExpandButtonTrigger = textLayoutResult.hasVisualOverflow
            }
        )
        if (expanded && role == Role.ADMIN) {
            ActionDangerButton(
                modifier = Modifier.align(Alignment.End).padding(end = 16.dp, bottom = 16.dp),
                onClick = onDeleted,
                text = stringResource(R.string.delete),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete)
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ItemPengumumanCardPreview() {
    KosRvdAppTheme {
        ItemPengumumanCard(
            modifier = Modifier.padding(20.dp),
            dataPengumumanUi = PengumumanUi(
                idPengumuman = "",
                createdById = "",
                madeBy = "Admin 1",
                title = "Harap Parkir Motor selalu rapi",
                content = "Menurut mu ini apakah sudah 150 panjangnya? Testetstetstetst \nHahahahahahahhaah Kalau spasi gini kira kira diitung max line ga ya",
                dateCreated = "10 Juli 2025, 07:00",
            ),
            role = Role.PENGHUNI
        )
    }
}