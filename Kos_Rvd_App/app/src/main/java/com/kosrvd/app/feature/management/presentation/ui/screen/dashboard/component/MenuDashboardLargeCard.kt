package com.kosrvd.app.feature.management.presentation.ui.screen.dashboard.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme

@Composable
fun MenuDashboardLargeCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    title: String,
    icon: Painter
) {
    Card (
        modifier = modifier.height(148.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick, onClickLabel = title),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(vertical = 22.dp).align(Alignment.CenterHorizontally),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = icon,
                contentDescription = title
            )
            Spacer(Modifier.height(15.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MenuDashboardLargeCardPreview() {
    KosRvdAppTheme {
        Row(
        ) {
            MenuDashboardLargeCard(
                modifier = Modifier.weight(1f),
                title = "Daftar Penghuni",
                icon = painterResource(R.drawable.ic_penghuni)
            )
            Spacer(Modifier.width(16.dp))
            MenuDashboardLargeCard(
                modifier = Modifier.weight(1f),
                title = "Pengumuman",
                icon = painterResource(R.drawable.ic_pengumuman)
            )
        }

    }
}