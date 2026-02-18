package com.kosrvd.app.core.presentation.designsystem.component.appbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarLogoLeftTextCenter(
    modifier: Modifier = Modifier,
    title: String,
    fontWeight: FontWeight = FontWeight.Normal,
    containerColors: Color = TopAppBarDefaults.topAppBarColors().containerColor
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColors
        ),
        expandedHeight = 74.dp,
        title = {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    modifier = Modifier.align(Alignment.CenterStart),
                    painter = painterResource(R.drawable.logo_rvd_very_small),
                    contentDescription = "Logo"
                )
                Text(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(x = (-10).dp),
                    text = title,
                    fontWeight = fontWeight,
                )
            }
        },
    )
}