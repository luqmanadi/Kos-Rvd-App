package com.kosrvd.app.core.domain.usecase

import android.util.Log
import com.kosrvd.app.core.domain.models.CreateUserRequest
import com.kosrvd.app.core.domain.repository.AccountRepository
import com.kosrvd.app.core.domain.repository.StorageRepository
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.core.domain.utils.getOrElse
import com.kosrvd.app.core.domain.models.CompressedResult
import java.util.UUID
import javax.inject.Inject

class CreateUserAccountUseCase @Inject constructor(
    private val storageRepository: StorageRepository,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        name: String,
        role: String,
        address: String? = null,
        phoneNumber: String? = null,
        compressedResult: CompressedResult? = null
    ): Result<Unit, DataError>{
        val uuidRandom = UUID.randomUUID().toString()

        val resultKtp = if (compressedResult != null){
            storageRepository.addImageKtp(compressedResult, uuidRandom)
                .getOrElse { return Result.Error(it) }
        } else null

        val request = CreateUserRequest(
            idAkun = uuidRandom,
            email = email,
            password = password,
            name = name,
            role = role,
            address = address,
            phoneNumber = phoneNumber,
            photoKtp = resultKtp?.photoKtp,
            photoKtpFileName = resultKtp?.photoKtpFileName
        )
        Log.d("CreateUserAccountUseCase", "Request: $request")
        return accountRepository.createUserAccount(request = request)
    }
}