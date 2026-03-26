package com.kosrvd.app.core.presentation.designsystem.atom.image

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kosrvd.app.R
import com.kosrvd.app.core.presentation.utils.getDisplayUrl

@Composable
fun LoadImage(
    modifier: Modifier = Modifier,
    url: String,
    contentDescription: String,
    errorImg: Int,
    shape: Shape = CircleShape
) {
    val finalImageSource = remember(url) {
        getDisplayUrl(url)
    }

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(finalImageSource)
            .crossfade(true)
            .build(),
        placeholder = painterResource(R.drawable.loading_img),
        error = painterResource(errorImg),
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        modifier = modifier.clip(shape),
    )
}