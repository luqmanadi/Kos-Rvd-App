package com.kosrvd.app.feature.dashboard.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme

@Composable
fun MenuDashboardSmallCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    title: String,
    icon: Painter
) {
    Column(
        modifier = modifier
            .size(85.dp, 110.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable(
                onClick = onClick,
                onClickLabel = title
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = icon,
            contentDescription = title,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(60.dp)
        )
        Spacer(Modifier.height(5.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MenuDashboardSmallCardPreview() {
    KosRvdAppTheme {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MenuDashboardSmallCard(
                title = "Penghuni",
                icon = painterResource(R.drawable.ic_penghuni),
                onClick = {}
            )
            MenuDashboardSmallCard(
                title = "Pengumuman",
                icon = painterResource(R.drawable.ic_pengumuman),
                onClick = {}
            )
            MenuDashboardSmallCard(
                title = "Penyewaan",
                icon = painterResource(R.drawable.ic_penyewaan),
                onClick = {}
            )
            MenuDashboardSmallCard(
                title = "Akun Pengguna",
                icon = painterResource(R.drawable.ic_akun_pengguna),
                onClick = {}
            )
        }

    }
}