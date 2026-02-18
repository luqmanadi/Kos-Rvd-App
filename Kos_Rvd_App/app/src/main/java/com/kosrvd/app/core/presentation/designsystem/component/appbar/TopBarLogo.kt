package com.kosrvd.app.core.presentation.designsystem.component.appbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarLogo(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = {}
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Image(
                painter = painterResource(R.drawable.logo_samping_teks_very_small),
                contentDescription = "Logo KosRVD",
                modifier = Modifier
                    .padding(start = 5.dp)
                    .size(width = 96.dp, height = 40.dp)
            )
        },
        navigationIcon = {
            IconButton(
                onClick = navigateUp,
                modifier = Modifier
            ){
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }
    )
}