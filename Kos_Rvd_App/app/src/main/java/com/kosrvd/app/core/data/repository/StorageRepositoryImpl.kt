package com.kosrvd.app.core.data.repository

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storageMetadata
import com.kosrvd.app.BuildConfig
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.data.networking.safeCall
import com.kosrvd.app.core.data.source.local.SessionStorage
import com.kosrvd.app.core.domain.models.KtpFormatResult
import com.kosrvd.app.core.domain.repository.StorageRepository
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.feature.management.domain.model.CompressedResult
import kotlinx.coroutines.tasks.await
import java.net.URLDecoder
import java.util.UUID
import javax.inject.Inject

class StorageRepositoryImpl @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
    private val sessionStorage: SessionStorage
) : StorageRepository {
    override suspend fun addImageLaporanKeluhan(compressedResult: CompressedResult): Result<Uri, DataError> {
        return safeCall {
            val uuidRandom = UUID.randomUUID().toString()
            val idAkun = sessionStorage.getAuthInfo().idAkun
            val fileName =
                "${Constant.IMAGE_LAPORAN_KELUHAN}_${idAkun}_$uuidRandom.${compressedResult.extension}"
            val metadata = storageMetadata {
                contentType = compressedResult.mimeType
            }

            firebaseStorage.reference.child(Constant.IMAGE_LAPORAN_KELUHAN_FOLDER).child(fileName)
                .putBytes(compressedResult.data, metadata).await()
                .storage.downloadUrl.await()
        }
    }

    override suspend fun deleteImageReferenceUrlImage(
        urlImage: String
    ): Result<Unit, DataError> {
        return safeCall {
            val urlFormat = if (BuildConfig.DEBUG) {
                // Jika DEBUG, kita konversi URL HTTP menjadi Path gs://
                val filePath = extractPathFromUrl(urlImage)
                "gs://$BUCKET_NAME/$filePath"
            } else {
                urlImage
            }
            firebaseStorage.getReferenceFromUrl(urlFormat).delete().await()
        }
    }

    override suspend fun addImageBuktiPembayaranTagihan(compressedResult: CompressedResult): Result<Uri, DataError> {
        return safeCall {
            val uuidRandom = UUID.randomUUID().toString()
            val idAkun = sessionStorage.getAuthInfo().idAkun
            val fileName =
                "${Constant.IMAGE_BUKTI_PEMBAYARAN_TAGIHAN}_${idAkun}_$uuidRandom.${compressedResult.extension}"
            val metadata = storageMetadata {
                contentType = compressedResult.mimeType
            }
            firebaseStorage.reference.child(Constant.IMAGE_BUKTI_PEMBAYARAN_TAGIHAN_FOLDER)
                .child(fileName)
                .putBytes(compressedResult.data, metadata).await()
                .storage.downloadUrl.await()
        }
    }

    override suspend fun addImageProfile(compressedResult: CompressedResult): Result<Uri, DataError> {
        return safeCall {
            val uuidRandom = UUID.randomUUID().toString()
            val idAkun = sessionStorage.getAuthInfo().idAkun
            val fileName =
                "${Constant.IMAGE_PROFILE}_${idAkun}_$uuidRandom.${compressedResult.extension}"
            val metadata = storageMetadata {
                contentType = compressedResult.mimeType
            }
            firebaseStorage.reference.child(Constant.IMAGE_PROFILE_FOLDER).child(fileName)
                .putBytes(compressedResult.data, metadata).await()
                .storage.downloadUrl.await()
        }
    }

    override suspend fun addImageResponseLaporanKeluhan(compressedResult: CompressedResult): Result<Uri, DataError> {
        return safeCall {
            val uuidRandom = UUID.randomUUID().toString()
            val idAkun = sessionStorage.getAuthInfo().idAkun
            val fileName =
                "${Constant.IMAGE_RESPONSE_LAPORAN_KELUHAN}_${idAkun}_$uuidRandom.${compressedResult.extension}"
            val metadata = storageMetadata {
                contentType = compressedResult.mimeType
            }

            firebaseStorage.reference.child(Constant.IMAGE_RESPONSE_LAPORAN_KELUHAN_FOLDER)
                .child(fileName)
                .putBytes(compressedResult.data, metadata).await()
                .storage.downloadUrl.await()
        }
    }

    override suspend fun addImageKtp(compressedResult: CompressedResult, uuidRandom: String): Result<KtpFormatResult, DataError> {
        return safeCall {
            val fileName =
                "${Constant.IMAGE_KTP}_$uuidRandom.${compressedResult.extension}"
            val metadata = storageMetadata {
                contentType = compressedResult.mimeType
            }
            val url = firebaseStorage.reference.child(Constant.IMAGE_KTP_FOLDER).child(fileName)
                .putBytes(compressedResult.data, metadata).await()
                .storage.downloadUrl.await()

            KtpFormatResult(
                photoKtp = url.toString(),
                photoKtpFileName = fileName
            )
        }
    }

    override suspend fun addImageBuktiPembayaranParkirHarianMobil(
        idParkirHarianMobil: String,
        compressedResult: CompressedResult
    ): Result<Uri, DataError> {
        return safeCall {
            val uuidRandom = UUID.randomUUID().toString()
            val fileName =
                "${Constant.IMAGE_BUKTI_PEMBAYARAN_PARKIR_HARIAN_MOBIL}_${idParkirHarianMobil}_$uuidRandom.${compressedResult.extension}"
            val metadata = storageMetadata {
                contentType = compressedResult.mimeType
            }
            firebaseStorage.reference.child(Constant.IMAGE_BUKTI_PEMBAYARAN_PARKIR_HARIAN_MOBIL_FOLDER)
                .child(fileName)
                .putBytes(compressedResult.data, metadata).await()
                .storage.downloadUrl.await()
        }
    }
    private fun extractPathFromUrl(url: String): String {
        // Ambil bagian setelah "/o/" dan sebelum "?"
        val encodedPath = url.split("/o/")[1].split("?")[0]
        // Decode %2F menjadi /
        return URLDecoder.decode(encodedPath, "UTF-8")
    }
}

private const val BUCKET_NAME = BuildConfig.BUCKET_NAME
