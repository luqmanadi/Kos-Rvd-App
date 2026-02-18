package com.kosrvd.app.feature.management.presentation.ui.screen.profile.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.presentation.designsystem.component.text.IconTextInfo
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme

@Composable
fun RowMenuProfile (
    text: String,
    icon: ImageVector,
    colorIcon: Color,
    colorText: Color,
    onClick: () -> Unit,
    fontWeight: FontWeight = FontWeight.Normal,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(25.dp)
            .fillMaxWidth()
    ) {
        IconTextInfo(
            fontWeight = fontWeight,
            text = text,
            icon = icon,
            spacing = 25.dp,
            colorIcon = colorIcon,
            colorText = colorText,
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = text,
            tint = colorIcon,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
fun MenuProfileCard(
    modifier: Modifier = Modifier,
    navigateDetailAkun: ()-> Unit,
    navigateLogout: () -> Unit,
    navigateChangePassword: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainer,
        modifier = modifier,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RowMenuProfile(
                text = stringResource(R.string.detail_account),
                icon = ImageVector.vectorResource(R.drawable.ic_fill_profile),
                colorIcon = MaterialTheme.colorScheme.onSurface,
                colorText = MaterialTheme.colorScheme.onSurface,
                onClick = navigateDetailAkun
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline
            )
            RowMenuProfile(
                text = stringResource(R.string.reset_password),
                icon = Icons.Filled.Lock,
                colorIcon = MaterialTheme.colorScheme.onSurface,
                colorText = MaterialTheme.colorScheme.onSurface,
                onClick = navigateChangePassword
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline
            )
            RowMenuProfile(
                text = stringResource(R.string.logout),
                icon = Icons.AutoMirrored.Filled.Logout,
                colorIcon = MaterialTheme.colorScheme.error,
                colorText = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Bold,
                onClick = navigateLogout
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RowMenuProfilePreview() {
    KosRvdAppTheme {
        RowMenuProfile(
            text = "Log Out",
            icon = Icons.AutoMirrored.Filled.Logout,
            colorIcon = MaterialTheme.colorScheme.error,
            colorText = MaterialTheme.colorScheme.error,
            onClick = {},
            modifier = Modifier
                .padding(25.dp)
                .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MenuProfileCardPreview() {
    KosRvdAppTheme {
        MenuProfileCard(
            modifier = Modifier.padding(25.dp),
            navigateDetailAkun = {},
            navigateLogout = {},
            navigateChangePassword = {}
        )
    }
}