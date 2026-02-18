package com.kosrvd.app.core.domain.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * State untuk mengelola Custom Toast. Mirip dengan SnackbarHostState.
 *
 * @property currentMessage Pesan yang sedang ditampilkan.
 */
class CustomToastHostState {

    // Menyimpan pesan yang sedang ditampilkan.
    var currentMessage by mutableStateOf("")
        private set

    var isVisible by mutableStateOf(false)
        private set


    // Mutex memastikan hanya satu toast yang tampil pada satu waktu.
    private val mutex = Mutex()

    /**
     * Menampilkan pesan toast baru.
     * @param message Pesan yang akan ditampilkan.
     * @param durationMillis Durasi tampilan (default 3 detik).
     */
    suspend fun showToast(message: String, durationMillis: Long = 3000L) {
        // withLock akan menunggu jika ada toast lain yang sedang tampil.
        mutex.withLock {
            currentMessage = message
            isVisible = true
            delay(durationMillis)
            // Pastikan pesan dihapus walaupun coroutine dibatalkan
            isVisible = false
        }
    }
}

/**
 * Membuat dan mengingat CustomToastHostState.
 */
@Composable
fun rememberCustomToastHostState(): CustomToastHostState {
    return remember { CustomToastHostState() }
}