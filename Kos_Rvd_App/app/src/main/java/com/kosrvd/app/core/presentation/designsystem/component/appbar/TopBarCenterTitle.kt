package com.kosrvd.app.core.presentation.designsystem.component.appbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarCenterTitle(
    modifier: Modifier = Modifier,
    title: String = "",
    onBackClick: () -> Unit = {},
    isNeedBackIcon: Boolean = true,
    fontWeight: FontWeight = FontWeight.Normal,
    isActionIcon: Boolean = false,
    actionIcon: @Composable () -> Unit = {},
    containerColors: Color = TopAppBarDefaults.topAppBarColors().containerColor
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColors
        ),
        title = {
            Text(
                text = title,
                fontWeight = fontWeight
            )
        },
        navigationIcon = {
            if (isNeedBackIcon) {
                IconButton(
                    onClick = onBackClick,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali"
                    )
                }
            }
        },
        actions = {
            if (isActionIcon) {
                actionIcon()
            }
        }
    )
}