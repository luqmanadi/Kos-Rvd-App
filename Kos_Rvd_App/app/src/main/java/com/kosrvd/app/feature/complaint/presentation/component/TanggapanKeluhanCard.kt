package com.kosrvd.app.feature.complaint.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme
import com.kosrvd.app.core.presentation.designsystem.molecul.text.InfoContentColumnText

@Composable
fun TanggapanKeluhanCard(
    modifier: Modifier = Modifier,
    text: String
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 5.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = MaterialTheme.colorScheme.primary,
                spotColor = MaterialTheme.colorScheme.primary,
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        InfoContentColumnText(
            modifier = Modifier.padding(20.dp),
            title = stringResource(R.string.response),
            value = text,
            icon = Icons.AutoMirrored.Filled.Comment
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TanggapanKeluhanCardPreview() {
    KosRvdAppTheme {
        TanggapanKeluhanCard(
            modifier = Modifier.padding(20.dp),
            text = "fjowrofwroihrwognrwinrwio iwrigjwrij ipwrgjipwrjpiwr  gpiwrjg"
        )
    }
}