package com.kosrvd.app.core.domain.repository

import com.kosrvd.app.core.domain.models.Account
import com.kosrvd.app.core.domain.models.CreateUserRequest
import com.kosrvd.app.core.domain.models.DeleteAccountRequest
import com.kosrvd.app.core.domain.models.DetailAkunPengguna
import com.kosrvd.app.core.domain.models.FcmToken
import com.kosrvd.app.core.domain.models.NonActiveAccountRequest
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    suspend fun getAllAccountRolePenghuniStatusAktifAndNomorKamarNull(): Result<List<Account>, DataError>
    suspend fun getAccountById(idAkun: String): Result<Account, DataError>

    fun getAccountByIdWithFlow(idAkun: String): Flow<Result<Account, DataError>>

    suspend fun getAllAccountWithRolePenghuniStatusIsAktifForPenghuni(withOutIdAkun: String): Result<List<Account>, DataError>

    suspend fun getAllAccountWithRolePenghuniStatusIsAktifForAdmin(): Result<List<Account>, DataError>

    suspend fun updatePhotoProfile(
        id: String,
        downloadUrl: String
    ): Result<Unit, DataError>

    suspend fun updateName(id: String, name: String): Result<Unit, DataError>

    suspend fun updatePhoneNumber(id: String, phoneNumber: String): Result<Unit, DataError>

    suspend fun updateAddress(id: String, address: String): Result<Unit, DataError>


    suspend fun addFcmToken(
        uid: String,
        tokenData: FcmToken
    ): Result<Unit, DataError>

    suspend fun removeFcmToken(
        uid: String,
        deviceId: String
    ): Result<Unit, DataError>


    fun getAllAccountWithFlow(withOutIdAkun: String): Flow<Result<List<Account>, DataError>>

    suspend fun getDetailAkunPengguna(idAkun: String): Result<DetailAkunPengguna, DataError>

    suspend fun createUserAccount(request: CreateUserRequest): Result<Unit, DataError>

    suspend fun nonActiveAccount(request: NonActiveAccountRequest): Result<Unit, DataError>

    suspend fun reActiveAccount(idAkun: String): Result<Unit, DataError>
    suspend fun deleteAccount(request: DeleteAccountRequest): Result<Unit, DataError>
}