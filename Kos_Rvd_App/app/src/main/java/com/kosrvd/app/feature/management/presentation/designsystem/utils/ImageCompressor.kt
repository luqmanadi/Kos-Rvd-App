package com.kosrvd.app.feature.management.presentation.designsystem.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import com.kosrvd.app.feature.management.domain.model.CompressedResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import kotlin.math.roundToInt

class ImageCompressor @Inject constructor(
    private val context: Context
) {
    suspend fun compressImage(
        imageUri: Uri,
        compressionThreshold: Long
    ): CompressedResult? {
        return withContext(Dispatchers.IO) {
            val mimeType = context.contentResolver.getType(imageUri)
            val inputBytes = context
                .contentResolver
                .openInputStream(imageUri)
                ?.use { inputStream ->
                    inputStream.readBytes()
                }?: return@withContext null

            ensureActive()

            withContext(Dispatchers.Default){
                val bitmap = BitmapFactory.decodeByteArray(inputBytes, 0, inputBytes.size)

                ensureActive()

                val (compressFormat, extension, outputMimeType) = when(mimeType){
                    "image/png" -> Triple(Bitmap.CompressFormat.PNG, "png", "image/png")
                    "image/webp" -> if (Build.VERSION.SDK_INT >= 30){
                        Triple(Bitmap.CompressFormat.WEBP_LOSSLESS, "webp", "image/webp")
                    } else {
                        Triple(Bitmap.CompressFormat.WEBP, "webp", "image/webp")
                    }
                    else -> Triple(Bitmap.CompressFormat.JPEG, "jpg", "image/jpeg")
                }

                var outputBytes: ByteArray
                var quality = 90
                do {
                    ByteArrayOutputStream().use { outputStream ->
                        bitmap.compress(compressFormat, quality, outputStream)
                        outputBytes = outputStream.toByteArray()
                        quality -= (quality * 0.1).roundToInt()
                    }
                } while (isActive &&
                    outputBytes.size > compressionThreshold &&
                    quality > 5 &&
                    compressFormat != Bitmap.CompressFormat.PNG
                )

                // the return
                CompressedResult(outputBytes, extension, outputMimeType)
            }

        }
    }
}