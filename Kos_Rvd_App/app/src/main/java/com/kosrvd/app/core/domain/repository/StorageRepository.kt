package com.kosrvd.app.core.domain.repository

import android.net.Uri
import com.kosrvd.app.core.domain.models.KtpFormatResult
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.core.domain.models.CompressedResult

interface StorageRepository {

    suspend fun addImageLaporanKeluhan(compressedResult: CompressedResult): Result<Uri, DataError>

    suspend fun deleteImageReferenceUrlImage(urlImage: String): Result<Unit, DataError>

    suspend fun addImageBuktiPembayaranTagihan(compressedResult: CompressedResult): Result<Uri, DataError>


    suspend fun addImageResponseLaporanKeluhan(compressedResult: CompressedResult): Result<Uri, DataError>

    suspend fun addImageProfile(compressedResult: CompressedResult): Result<Uri, DataError>

    suspend fun addImageKtp(compressedResult: CompressedResult, uuidRandom: String): Result<KtpFormatResult, DataError>

    suspend fun addImageBuktiPembayaranParkirHarianMobil(idParkirHarianMobil: String, compressedResult: CompressedResult): Result<Uri, DataError>

}