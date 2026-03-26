package com.kosrvd.app.feature.auth.presentation.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionButton

@Composable
fun OnBoardingScreen(
    onNavigateLogin: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.logo_samping_teks_very_small),
                contentDescription = "Logo RVD",
                modifier = Modifier.align(Alignment.TopStart)
            )
            Image(
                painter = painterResource(R.drawable.ic_onboarding),
                contentDescription = "Icon Onboarding",
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(250.dp)
            )
            Column(
                modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter).verticalScroll(scrollState)
            ) {
                Text(
                    text = stringResource(R.string.welcome_onboarding),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.description_welcome),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(48.dp))
                ActionButton(
                    text = stringResource(R.string.next),
                    onClick = onNavigateLogin,
                    modifier = Modifier.fillMaxWidth(),
                    height = 45.dp,
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
    }

}