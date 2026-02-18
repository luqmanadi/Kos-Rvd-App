package com.kosrvd.app.core.presentation.designsystem.component.radio_button

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme

@Composable
fun CustomRadioButtonV1(
    modifier: Modifier = Modifier,
    iconSelected: ImageVector? = null,
    iconUnselected: ImageVector? = null,
    text: String,
    selected: Boolean,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
    val color =
        if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
    val icon = if (selected) iconSelected else iconUnselected
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, color = color, shape = RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton,
                enabled = enabled
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
        } else {
            RadioButton(
                selected = selected,
                colors = RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colorScheme.primary,
                    unselectedColor = MaterialTheme.colorScheme.outline
                ),
                onClick = null
            )
        }
        Spacer(Modifier.width(15.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = fontWeight,
            color = color
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomRadioButtonV1Preview() {
    KosRvdAppTheme {
        val radioOptions = listOf("Calls", "Missed")
        val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            radioOptions.forEach { text ->
                CustomRadioButtonV1(
                    modifier = Modifier.weight(1f),
                    iconSelected = Icons.Filled.AccountBox,
                    iconUnselected = Icons.Outlined.AccountBox,
                    text = text,
                    selected = text == selectedOption,
                    onClick = {
                        onOptionSelected(text)
                    },
                    enabled = true
                )
            }
        }

    }
}