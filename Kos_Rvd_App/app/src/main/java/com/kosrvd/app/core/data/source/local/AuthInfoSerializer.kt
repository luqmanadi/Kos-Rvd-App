package com.kosrvd.app.core.data.source.local

import androidx.datastore.core.Serializer
import com.kosrvd.app.core.data.repository.dto.AuthInfoDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object AuthInfoSerializer: Serializer<AuthInfoDto> {
    override val defaultValue: AuthInfoDto = AuthInfoDto()

    override suspend fun readFrom(input: InputStream): AuthInfoDto {
        return try {
            val jsonString = withContext(Dispatchers.IO) {
                input.use { it.readBytes().decodeToString() }
            }
            if (jsonString.isBlank()){
                AuthInfoDto.Empty
            } else {
                Json.decodeFromString(AuthInfoDto.Companion.serializer(), jsonString)
            }
        } catch (e: Exception) {
            AuthInfoDto.Empty
        }
    }

    override suspend fun writeTo(
        t: AuthInfoDto,
        output: OutputStream
    ) {
        withContext(Dispatchers.IO) {
            try {
                val jsonString = Json.encodeToString(AuthInfoDto.Companion.serializer(), t)
                output.use { it.write(jsonString.toByteArray()) }
            } catch (e: Exception) {
                output.use { it.write("{}".toByteArray()) }
            }
        }
    }
}