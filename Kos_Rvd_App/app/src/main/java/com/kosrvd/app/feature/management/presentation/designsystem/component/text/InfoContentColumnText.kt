package com.kosrvd.app.feature.management.presentation.designsystem.component.text

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kosrvd.app.core.presentation.designsystem.component.text.BackgroundInfoText
import com.kosrvd.app.core.presentation.designsystem.component.text.DoubleTextBgInfo
import com.kosrvd.app.core.presentation.designsystem.component.text.IconTextInfo
import com.kosrvd.app.core.presentation.designsystem.component.text.StatusBackgroundText
import com.kosrvd.app.feature.management.domain.model.AlatElektronik
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toRupiahFormat

@Composable
fun InfoContentColumnText(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    isUseInfoStatus: Boolean = false,
    icon: ImageVector,
) {
    Column(
        modifier = modifier
    ) {
        IconTextInfo(
            text = title,
            icon = icon,
        )
        Spacer(Modifier.height(10.dp))
        if (isUseInfoStatus){
            StatusBackgroundText(status = value)
        }else{
            BackgroundInfoText(text = value)
        }
    }
}

@Composable
fun InfoContentColumnText(
    modifier: Modifier = Modifier,
    title: String,
    value: List<String>,
    icon: ImageVector
){
    val size = value.size
    Column(
        modifier = modifier
    ) {
        IconTextInfo(
            text = title,
            icon = icon,
        )
        Spacer(Modifier.height(10.dp))
        value.forEachIndexed { i, it ->
            BackgroundInfoText(
                text = buildString {
                    append(i+1)
                    append(". ")
                    append(it)
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (i != size-1){
                Spacer(Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun InfoContentColumnTextForAlatElektronik(
    modifier: Modifier = Modifier,
    title: String,
    value: List<AlatElektronik>,
    icon: ImageVector
){
    val size = value.size
    Column(
        modifier = modifier
    ) {
        IconTextInfo(
            text = title,
            icon = icon,
        )
        Spacer(Modifier.height(10.dp))
        if (value.isNotEmpty()){
            value.forEachIndexed { i, it ->
                DoubleTextBgInfo(
                    text1 = buildString {
                        append(i+1)
                        append(". ")
                        append(it.toolName)
                    },
                    text2 = it.cost.toRupiahFormat(),
                )
                if (i != size-1){
                    Spacer(Modifier.height(10.dp))
                }
            }
        } else{
            BackgroundInfoText(text = "-")
        }
    }
}

