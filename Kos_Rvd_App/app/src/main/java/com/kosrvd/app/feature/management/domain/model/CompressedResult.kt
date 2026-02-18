package com.kosrvd.app.feature.management.domain.model

import kotlin.collections.contentEquals
import kotlin.collections.contentHashCode
import kotlin.jvm.javaClass

data class CompressedResult(
    val data: ByteArray,
    val extension: String, // misal: "jpg", "png"
    val mimeType: String   // misal: "image/jpeg"
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CompressedResult

        if (!data.contentEquals(other.data)) return false
        if (extension != other.extension) return false
        if (mimeType != other.mimeType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = data.contentHashCode()
        result = 31 * result + extension.hashCode()
        result = 31 * result + mimeType.hashCode()
        return result
    }
}
