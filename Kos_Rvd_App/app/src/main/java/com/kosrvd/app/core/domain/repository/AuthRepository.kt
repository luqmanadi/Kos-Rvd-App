package com.kosrvd.app.core.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    val currentUser: FirebaseUser?

    suspend fun loginWithEmailAndPassword(email: String, password: String): Result<FirebaseUser?, DataError>

    suspend fun forgotPassword(email: String): Result<Boolean, DataError>

    suspend fun changePassword(newPassword: String, oldPassword: String): Result<Unit, DataError>

    suspend fun changeEmail(newEmail: String, currentPassword: String): Result<Unit, DataError>

    suspend fun signOut() : Result<Unit, DataError>

    fun getAuthState(): Flow<Boolean>

}