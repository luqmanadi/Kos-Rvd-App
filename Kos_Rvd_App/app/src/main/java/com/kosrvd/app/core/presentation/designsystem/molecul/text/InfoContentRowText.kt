package com.kosrvd.app.core.presentation.designsystem.molecul.text

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Title
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kosrvd.app.core.presentation.designsystem.component.text.BackgroundInfoText
import com.kosrvd.app.core.presentation.designsystem.component.text.IconTextInfo
import com.kosrvd.app.core.presentation.designsystem.component.text.StatusBackgroundText
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme

@Composable
fun InfoContentRowText(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    isKeluhanStatusInfo: Boolean = false,
    icon: ImageVector,
) {
    Row (
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = if (value.length > 15) Alignment.Top else Alignment.CenterVertically
    ) {
        IconTextInfo(
            text = title,
            icon = icon,
            modifier = Modifier.width(140.dp)
        )
        if (isKeluhanStatusInfo){
            StatusBackgroundText(status = value)
        }else{
            BackgroundInfoText(text = value)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InfoContentRowPreview() {
    KosRvdAppTheme {
        InfoContentRowText(
            modifier = Modifier.padding(20.dp),
            title = "Deskripsi",
            value = "Bocor Parah efjiefjie efiefiejfff",
            isKeluhanStatusInfo = false,
            icon = Icons.Filled.Title,
        )
    }
}