package com.kosrvd.app.feature.management.domain.usecase

import android.util.Log
import com.kosrvd.app.core.domain.repository.AccountRepository
import com.kosrvd.app.core.domain.repository.StorageRepository
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.feature.management.domain.model.CompressedResult
import javax.inject.Inject

class SavePhotoProfileUseCase @Inject constructor(
    private val storageRepository: StorageRepository,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(idAkun: String, oldPhotoUri: String, compressedResult: CompressedResult?): Result<Unit, DataError> {
        if (oldPhotoUri.isNotBlank()){
            Log.d("TAG Inital OldPhoto", "invoke: $oldPhotoUri")
            storageRepository.deleteImageReferenceUrlImage(oldPhotoUri).onError {
                Log.e("TAG Delete Photo", "invoke: ${it.message}")
                return  Result.Error(it) }
        }
        val photoUrl = if (compressedResult != null) {
            Log.d("TAG Inital NewPhoto", "invoke: ${compressedResult.data}")
            when(val storageResult = storageRepository.addImageProfile(compressedResult)){
                is Result.Success -> {
                    storageResult.data.toString()
                }
                is Result.Error -> {
                    Log.e("TAG Save Photo", "invoke: ${storageResult.error.message}")
                    return Result.Error(storageResult.error)
                }
            }
        } else {
            ""
        }

        return accountRepository.updatePhotoProfile(idAkun, photoUrl)

    }
}