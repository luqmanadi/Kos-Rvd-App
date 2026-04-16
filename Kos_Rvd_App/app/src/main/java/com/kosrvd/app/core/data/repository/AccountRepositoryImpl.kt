package com.kosrvd.app.core.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.data.constant.ErrorMessages
import com.kosrvd.app.core.data.mappers.toAccount
import com.kosrvd.app.core.data.mappers.toDetailAkunPengguna
import com.kosrvd.app.core.data.mappers.toFcmTokenDataDto
import com.kosrvd.app.core.data.mappers.toListAccount
import com.kosrvd.app.core.data.networking.constructUrl
import com.kosrvd.app.core.data.networking.safeCall
import com.kosrvd.app.core.data.networking.safeCallApi
import com.kosrvd.app.core.data.networking.toObjectListOrThrow
import com.kosrvd.app.core.data.repository.dto.AccountDto
import com.kosrvd.app.core.data.repository.dto.DetailAkunPenggunaDto
import com.kosrvd.app.core.data.repository.dto.SimpleResponseDto
import com.kosrvd.app.core.domain.models.Account
import com.kosrvd.app.core.domain.models.CreateUserRequest
import com.kosrvd.app.core.domain.models.DeleteAccountRequest
import com.kosrvd.app.core.domain.models.DetailAkunPengguna
import com.kosrvd.app.core.domain.models.FcmToken
import com.kosrvd.app.core.domain.models.NonActiveAccountRequest
import com.kosrvd.app.core.domain.repository.AccountRepository
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.core.domain.utils.asCollectionFlow
import com.kosrvd.app.core.domain.utils.asFlow
import com.kosrvd.app.core.domain.utils.map
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val httpClient: HttpClient,
    private val auth: FirebaseAuth
): AccountRepository {

    private suspend fun getToken(): String {
        // getIdToken(true) memaksa refresh jika expired
        return auth.currentUser?.getIdToken(true)?.await()?.token.orEmpty()
    }

    override suspend fun getAllAccountRolePenghuniStatusAktifAndNomorKamarNull(): Result<List<Account>, DataError> {
        return safeCall {
            db.collection(Constant.AKUN_COLLECTION)
                .whereEqualTo(Constant.ROLE_FIELD, Constant.PENGHUNI_ROLE)
                .whereEqualTo(Constant.STATUS_FIELD, Constant.ACTIVE)
                .whereEqualTo(Constant.NUMBER_ROOM_FROM_DATA_PENGHUNI_FIELD, null)
                .get()
                .await()
                .toObjectListOrThrow<AccountDto>(
                    mappingErrorMessage = ErrorMessages.ACCOUNT_MAPPING_ERROR
                )
        }.map { it.toListAccount() }
    }

    override suspend fun getDetailAkunPengguna(idAkun: String): Result<DetailAkunPengguna, DataError> {
        return safeCallApi<DetailAkunPenggunaDto> {
            httpClient.get(
                urlString = constructUrl("getDetailAkunPengguna")
            ){
                parameter("idAkun", idAkun)
                header("Authorization", "Bearer ${getToken()}")
            }
        }.map { dto -> dto.toDetailAkunPengguna() }
    }

    override suspend fun getAccountById(idAkun: String): Result<Account, DataError> {
        return safeCall {
            db.collection(Constant.AKUN_COLLECTION)
                .document(idAkun)
                .get()
                .await()
                .toObject<AccountDto>() ?: throw Exception(ErrorMessages.ACCOUNT_NOT_FOUND)
        }.map {
            it.toAccount()
        }
    }

    override fun getAllAccountWithFlow(withOutIdAkun: String): Flow<Result<List<Account>, DataError>> {
        return db.collection(Constant.AKUN_COLLECTION)
            .whereNotEqualTo(FieldPath.documentId(), withOutIdAkun)
            .orderBy(Constant.ROLE_FIELD, Query.Direction.ASCENDING)
            .orderBy(Constant.STATUS_FIELD, Query.Direction.ASCENDING)
            .asCollectionFlow<AccountDto, Account>(
                logTag = "Account Repo",
                mapper = { it.toAccount() }
            )
    }

    override fun getAccountByIdWithFlow(idAkun: String): Flow<Result<Account, DataError>> {
        return db.collection(Constant.AKUN_COLLECTION)
            .document(idAkun)
            .asFlow<AccountDto, Account>(
                mapper = { it.toAccount() },
                onNotFound = { DataError.BUSINESS_ACCOUNT_NOT_FOUND },
                onMappingError = { DataError.BUSINESS_ACCOUNT_MAPPING_ERROR }
            )
    }

    override suspend fun getAllAccountWithRolePenghuniStatusIsAktifForAdmin(): Result<List<Account>, DataError> {
        return safeCall {
            db.collection(Constant.AKUN_COLLECTION)
                .whereEqualTo(Constant.ROLE_FIELD, Constant.PENGHUNI_ROLE)
                .whereEqualTo(Constant.STATUS_FIELD, Constant.ACTIVE)
                .orderBy(Constant.NUMBER_ROOM_FROM_DATA_PENGHUNI_FIELD)
                .get()
                .await()
                .toObjectListOrThrow<AccountDto>(
                    mappingErrorMessage = ErrorMessages.ACCOUNT_MAPPING_ERROR
                )
        }.map { it.toListAccount() }
    }

    override suspend fun getAllAccountWithRolePenghuniStatusIsAktifForPenghuni(withOutIdAkun: String): Result<List<Account>, DataError> {
        return safeCall {
            db.collection(Constant.AKUN_COLLECTION)
                .whereNotEqualTo(FieldPath.documentId(), withOutIdAkun)
                .whereEqualTo(Constant.ROLE_FIELD, Constant.PENGHUNI_ROLE)
                .whereEqualTo(Constant.STATUS_FIELD, Constant.ACTIVE)
                .orderBy(Constant.NUMBER_ROOM_FROM_DATA_PENGHUNI_FIELD)
                .get()
                .await()
                .toObjectListOrThrow<AccountDto>(
                    mappingErrorMessage = ErrorMessages.ACCOUNT_MAPPING_ERROR
                )
        }.map { it.toListAccount() }
    }

    override suspend fun updatePhotoProfile(
        id: String,
        downloadUrl: String
    ): Result<Unit, DataError> {
        return safeCall {
            db.collection(Constant.AKUN_COLLECTION)
                .document(id)
                .update(Constant.PHOTO_FIELD, downloadUrl)
                .await()
        }
    }

    override suspend fun updateName(
        id: String,
        name: String
    ): Result<Unit, DataError> {
        return safeCall {
            db.collection(Constant.AKUN_COLLECTION)
                .document(id)
                .update(Constant.NAME_FIELD, name)
                .await()
        }
    }

    override suspend fun updatePhoneNumber(
        id: String,
        phoneNumber: String
    ): Result<Unit, DataError> {
        return safeCall {
            db.collection(Constant.AKUN_COLLECTION)
                .document(id)
                .update(Constant.PHONE_NUMBER_FIELD, phoneNumber)
                .await()
        }
    }

    override suspend fun updateAddress(id: String, address: String): Result<Unit, DataError> {
        return safeCall {
            db.collection(Constant.AKUN_COLLECTION)
                .document(id)
                .update(Constant.ADDRESS_FIELD, address)
                .await()
        }
    }

    override suspend fun addFcmToken(
        uid: String,
        tokenData: FcmToken
    ): Result<Unit, DataError> {
        return safeCall {
            val docRef = db.collection(Constant.AKUN_COLLECTION).document(uid)
            db.runTransaction { transaction ->
                val snapshot = transaction.get(docRef)

                // Mendapatkan data akun yang sudah ada
                val existingAccount = snapshot.toObject<AccountDto>()

                // Mendapatkan token fcm yang sudah ada
                val existingFcmTokens = existingAccount?.fcmTokens ?: emptyList()

                // Memfilter token fcm yang sudah ada dengan tidak sama dengan device Id yang baru
                val updatedTokens =
                    existingFcmTokens.filter { it.deviceId != tokenData.deviceId }.toMutableList()

                // Menambahkan token fcm baru
                updatedTokens.add(tokenData.toFcmTokenDataDto())

                // Memperbarui dokumen dengan token fcm baru
                transaction.update(docRef, Constant.FCM_TOKEN_FIELD, updatedTokens)

                null
            }.await()
        }
    }

    override suspend fun removeFcmToken(
        uid: String,
        deviceId: String
    ): Result<Unit, DataError> {
        return safeCall {
            val docRef = db.collection(Constant.AKUN_COLLECTION).document(uid)

            db.runTransaction { transaction ->
                val snapshot = transaction.get(docRef)

                // Mendapatkan data akun yang sudah ada
                val existingAccount = snapshot.toObject<AccountDto>()

                // Mendapatkan token fcm yang sudah ada
                val existingFcmTokens = existingAccount?.fcmTokens ?: emptyList()

                // Memfilter token fcm yang sudah ada dengan tidak sama dengan device Id yan baru
                val updatedTokens = existingFcmTokens.filter { it.deviceId != deviceId }

                // Memperbarui dokumen dengan token fcm baru
                transaction.update(docRef, Constant.FCM_TOKEN_FIELD, updatedTokens)

                null
            }.await()
        }
    }

    override suspend fun createUserAccount(request: CreateUserRequest): Result<Unit, DataError> {
        return safeCallApi<SimpleResponseDto> {
            val params = hashMapOf(
                "idAkun" to request.idAkun,
                "email" to request.email,
                "password" to request.password,
                "name" to request.name,
                "role" to request.role,
                "address" to request.address,
                "phoneNumber" to request.phoneNumber,
                "photoKtp" to request.photoKtp,
                "photoKtpFileName" to request.photoKtpFileName
            )

            httpClient.post(
                urlString = constructUrl("createAkunPengguna")
            ){
                setBody(params)
                header("Authorization", "Bearer ${getToken()}")
            }
        }.map {  }
    }

    override suspend fun nonActiveAccount(request: NonActiveAccountRequest): Result<Unit, DataError> {
        return safeCallApi<SimpleResponseDto> {
            val params = hashMapOf(
                "idAkun" to request.idAkun,
                "role" to request.role,
                "photoUrl" to request.photoUrl
            )
            httpClient.post(
                urlString = constructUrl("deactivateUserAccount")
            ){
                setBody(params)
                header("Authorization", "Bearer ${getToken()}")
            }
        }.map {  }
    }

    override suspend fun reActiveAccount(idAkun: String): Result<Unit, DataError> {
        return safeCallApi<SimpleResponseDto> {
            httpClient.post(
                urlString = constructUrl("reactivateUserAccount")
            ) {
                setBody(mapOf("idAkun" to idAkun))
                header("Authorization", "Bearer ${getToken()}")
            }
        }.map {  }
    }

    override suspend fun deleteAccount(request: DeleteAccountRequest): Result<Unit, DataError> {
        return safeCallApi<SimpleResponseDto> {
            val params = hashMapOf(
                "idAkun" to request.idAkun,
                "role" to request.role,
                "ktpUrl" to request.ktpUrl
            )
            httpClient.delete(
                urlString = constructUrl("deleteAccount")
            ){
                setBody(params)
                header("Authorization", "Bearer ${getToken()}")
            }
        }.map {  }
    }
}