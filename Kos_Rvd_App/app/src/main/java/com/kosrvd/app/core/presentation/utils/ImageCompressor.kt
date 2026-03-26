package com.kosrvd.app.core.presentation.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import android.net.Uri
import android.os.Build
import com.kosrvd.app.core.domain.models.CompressedResult
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
                var bitmap = BitmapFactory.decodeByteArray(inputBytes, 0, inputBytes.size)

                bitmap = getRotateBitmap(bitmap, imageUri)

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

    private fun getRotateBitmap(bitmap: Bitmap, uri: Uri): Bitmap {
        val inputStream = context.contentResolver.openInputStream(uri)
        val exifInterface = inputStream?.use { ExifInterface(it) }

        val orientation = exifInterface?.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        ) ?: ExifInterface.ORIENTATION_NORMAL

        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
            else -> return bitmap // Tidak perlu diputar
        }

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}