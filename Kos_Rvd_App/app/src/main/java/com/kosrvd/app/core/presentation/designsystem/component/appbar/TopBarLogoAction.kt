package com.kosrvd.app.core.presentation.designsystem.component.appbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kosrvd.app.R
import com.kosrvd.app.core.presentation.utils.shimmerEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarLogoAction(
    modifier: Modifier = Modifier,
    name: String,
    navigateTo: () -> Unit = {},
    numberOfUnreadNotification: Int = 0
) {
    TopAppBar(
        modifier = modifier,
        expandedHeight = 74.dp,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        title = {
            Row {
                Image(
                    painter = painterResource(R.drawable.logo_rvd_very_small),
                    contentDescription = "Logo RVD"
                )
                Column(
                    modifier = Modifier.padding(start = 10.dp)
                ) {
                    Text(
                        text = "Selamat Datang",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        actions = {
            IconButton(
                onClick = navigateTo,
                content = {
                    BadgedBox(
                        badge = {
                            if (numberOfUnreadNotification > 0) {
                                Badge {
                                    Text(
                                        text = numberOfUnreadNotification.toString(),
                                        fontSize = 9.sp
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Notification",
                        )
                    }
                }
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarLogoActionShimmer(
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier,
        expandedHeight = 74.dp,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        title = {
            Row {
                Box(
                    modifier = Modifier
                        .size(width = 54.dp, height = 45.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .shimmerEffect()
                )
                Column(
                    modifier = Modifier.padding(start = 10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(width = 110.dp, height = 20.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .shimmerEffect()
                    )
                    Spacer(Modifier.height(5.dp))
                    Box(
                        modifier = Modifier
                            .size(width = 60.dp, height = 20.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .shimmerEffect()
                    )
                }
            }
        },
        actions = {
            Box(
                modifier = Modifier
                    .offset(x = (-8).dp)
                    .size(size = 45.dp)
                    .clip(CircleShape)
                    .shimmerEffect()
            )
        }
    )
}