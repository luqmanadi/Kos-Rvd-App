package com.kosrvd.app.core.data.repository

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kosrvd.app.core.data.networking.safeCall
import com.kosrvd.app.core.domain.repository.AuthRepository
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authFirebase: FirebaseAuth
) : AuthRepository {

    override val currentUser: FirebaseUser?
        get() = authFirebase.currentUser

    override suspend fun loginWithEmailAndPassword(
        email: String,
        password: String
    ): Result<FirebaseUser?, DataError> {
        return safeCall {
            authFirebase
                .signInWithEmailAndPassword(email, password).await().user
        }
    }

    override suspend fun forgotPassword(email: String): Result<Boolean, DataError> {
        return safeCall {
            authFirebase.sendPasswordResetEmail(email).await()
            true
        }

    }

    override suspend fun changePassword(newPassword: String, oldPassword: String): Result<Unit, DataError> {
        return safeCall {
            val currentEmail = currentUser?.email ?: return@safeCall // Ambil email yang sedang login

            // 1. Re-authenticate dulu
            reauthenticateInternal(currentEmail, oldPassword)

            // 2. Jika berhasil, baru update password
            currentUser?.updatePassword(newPassword)?.await()
        }
    }

    override suspend fun changeEmail(newEmail: String, currentPassword: String): Result<Unit, DataError> {
        return safeCall {
            val currentEmail = currentUser?.email ?: return@safeCall // Ambil email yang sedang login

            // 1. Re-authenticate dulu
            reauthenticateInternal(currentEmail, currentPassword)

            // 2. Jika berhasil, baru update email (mengirim verifikasi ke email baru)
            currentUser?.verifyBeforeUpdateEmail(newEmail)?.await()
        }
    }

    override suspend fun signOut(): Result<Unit, DataError> {
        return safeCall {
            authFirebase.signOut()
        }
    }

    override fun getAuthState(): Flow<Boolean> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser == null)
        }
        authFirebase.addAuthStateListener(authStateListener)
        awaitClose {
            authFirebase.removeAuthStateListener(authStateListener)
        }
    }.shareIn(
        scope = CoroutineScope(Dispatchers.IO),
        started = SharingStarted.WhileSubscribed(5000),
        replay = 1
    )

    private suspend fun reauthenticateInternal(email: String, password: String) {
        val credential = EmailAuthProvider.getCredential(email, password)
        currentUser?.reauthenticate(credential)?.await()
    }
}