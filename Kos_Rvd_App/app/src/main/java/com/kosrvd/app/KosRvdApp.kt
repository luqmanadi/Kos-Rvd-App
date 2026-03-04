package com.kosrvd.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.media.AudioAttributes
import android.os.Build
import androidx.core.net.toUri
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class KosRvdApp: Application() {
    override fun onCreate() {
        super.onCreate()
        // Inisialisasi Firebase (mungkin sudah Anda lakukan)
        FirebaseApp.initializeApp(this)

        // --- TARUH KODE APP CHECK DI SINI ---

        val firebaseAppCheck = FirebaseAppCheck.getInstance()

        // Cek apakah ini build DEBUG atau RELEASE
        if (BuildConfig.DEBUG) {
            // Gunakan DEBUG provider untuk emulator/development
            firebaseAppCheck.installAppCheckProviderFactory(
                DebugAppCheckProviderFactory.getInstance()
            )
        } else {
            // Gunakan Play Integrity untuk rilis
            firebaseAppCheck.installAppCheckProviderFactory(
                PlayIntegrityAppCheckProviderFactory.getInstance()
            )
        }

        createNotificationChannel()
    }

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = getString(R.string.kos_rvd_channel_id)
            val channelName = getString(R.string.kos_rvd_channel_name)
            val channelDescription = getString(R.string.kos_rvd_channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val soundUri = "android.resource://${packageName}/${R.raw.notification_default}".toUri()

            // Tambahkan AudioAttributes agar lebih aman di semua merek HP
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()

            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
                enableLights(true)
                setSound(soundUri, audioAttributes)
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}