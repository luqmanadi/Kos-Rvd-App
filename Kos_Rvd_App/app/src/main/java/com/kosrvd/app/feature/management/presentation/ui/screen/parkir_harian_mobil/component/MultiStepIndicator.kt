package com.kosrvd.app.feature.management.presentation.ui.screen.parkir_harian_mobil.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme
import com.kosrvd.app.core.presentation.utils.customShadow

@Composable
fun MultiStepIndicator(
    listNamePage: List<String>,
    currentStep: Int,
    modifier: Modifier = Modifier
) {
    
    val activeColor = MaterialTheme.colorScheme.primary
    val inactiveColor = MaterialTheme.colorScheme.outlineVariant
    val onActiveColor = MaterialTheme.colorScheme.onPrimary

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top
    ) {
        listNamePage.forEachIndexed { index, title ->
            val stepNumber = index + 1
            val isCompleted = stepNumber < currentStep
            val isActive = stepNumber == currentStep

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Line before (except first)
                    Box(modifier = Modifier.weight(1f)) {
                        if (index > 0) {
                            HorizontalDivider(
                                color = if (stepNumber <= currentStep) activeColor else inactiveColor,
                                thickness = 1.dp
                            )
                        }
                    }

                    // Circle
                    StepCircle(
                        stepNumber = stepNumber,
                        isCompleted = isCompleted,
                        isActive = isActive,
                        activeColor = activeColor,
                        inactiveColor = inactiveColor,
                        onActiveColor = onActiveColor
                    )

                    // Line after (except last)
                    Box(modifier = Modifier.weight(1f)) {
                        if (index < listNamePage.size - 1) {
                            HorizontalDivider(
                                color = if (isCompleted) activeColor else inactiveColor,
                                thickness = 1.dp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Title
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 11.sp,
                        fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                        color = if (isActive) MaterialTheme.colorScheme.onSurface else inactiveColor
                    )
                )
            }
        }
    }
}

@Composable
private fun StepCircle(
    stepNumber: Int,
    isCompleted: Boolean,
    isActive: Boolean,
    activeColor: Color,
    inactiveColor: Color,
    onActiveColor: Color
) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .then(
                if (isActive) {
                    Modifier.customShadow(
                        color = activeColor.copy(alpha = 0.5f),
                        blur = 5.dp,
                        spread = 2.dp
                    )
                } else Modifier
            )
            .clip(CircleShape)
            .background(if (isCompleted || isActive) activeColor else Color.Transparent)
            .then(
                if (!isCompleted && !isActive) Modifier.border(
                    1.dp,
                    inactiveColor,
                    CircleShape
                ) else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        when {
            isCompleted -> {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = onActiveColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            isActive -> {
                Text(
                    text = stepNumber.toString(),
                    color = onActiveColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            else -> {
                Text(
                    text = stepNumber.toString(),
                    color = inactiveColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MultiStepIndicatorPreview() {
    KosRvdAppTheme {
        Column(verticalArrangement = Arrangement.spacedBy(32.dp), modifier = Modifier.padding(24.dp)) {
            MultiStepIndicator(currentStep = 1, listNamePage = listOf("Tanggal", "Zona", "Kendaraan", "Konfirmasi"))
            MultiStepIndicator(currentStep = 2, listNamePage = listOf("Tanggal", "Zona", "Kendaraan", "Konfirmasi"))
            MultiStepIndicator(currentStep = 3, listNamePage = listOf("Tanggal", "Zona", "Kendaraan", "Konfirmasi"))
            MultiStepIndicator(currentStep = 4, listNamePage = listOf("Tanggal", "Zona", "Kendaraan", "Konfirmasi"))
        }
    }
}