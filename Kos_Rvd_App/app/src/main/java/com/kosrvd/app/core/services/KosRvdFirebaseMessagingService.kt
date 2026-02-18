package com.kosrvd.app.core.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.Timestamp
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kosrvd.app.MainActivity
import com.kosrvd.app.R
import com.kosrvd.app.core.domain.models.FcmToken
import com.kosrvd.app.core.domain.repository.AccountRepository
import com.kosrvd.app.core.domain.repository.AuthRepository
import com.kosrvd.app.core.domain.repository.DeviceInfoProvider
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.core.domain.utils.onSuccess
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.Random
import javax.inject.Inject

@AndroidEntryPoint
class KosRvdFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var deviceInfoProvider: DeviceInfoProvider

    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var accountRepository: AccountRepository

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onNewToken(token: String) {
        super.onNewToken(token)
       updateFcmToken(token)
    }

    private fun updateFcmToken(token: String) {
        serviceScope.launch {
            try {
                val devidId = deviceInfoProvider.getDeviceId()
                val deviceName = deviceInfoProvider.getDeviceName()
                val idAkun = authRepository.currentUser?.uid ?: return@launch

                val tokenData = FcmToken(
                    token = token,
                    deviceId = devidId,
                    deviceName = deviceName,
                    platform = "android",
                    lastUpdated = Timestamp.now()
                )

                accountRepository.addFcmToken(uid = idAkun, tokenData = tokenData)
                    .onSuccess {
                        Log.d("TAG", "updateFcmToken: success")
                    }
                    .onError { result ->
                        Log.d("TAG", "updateFcmToken Error: ${result.message}")
                    }
            } catch (e: Exception){
                Log.e("FCM", "CRASH PREVENTED: Error sistem di service", e)
            }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val data = message.data
        val idNotifikasi = data["idNotifikasi"]
        val typeNotification = data["typeNotification"]
        val idDetailReferensi = data["idDetailReferensi"]

        message.notification?.let {
            val title = it.title ?: "Notifikasi default"
            val body = it.body ?: ""

            showNotificationMessage(
                title = title,
                body = body,
                idNotif = idNotifikasi,
                type = typeNotification,
                refId = idDetailReferensi
            )
        }
    }

    private fun showNotificationMessage(
        title: String,
        body: String,
        idNotif: String?,
        type: String?,
        refId: String?
    ){
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            putExtra("idNotifikasi", idNotif)
            putExtra("typeNotification", type)
            putExtra("idDetailReferensi", refId)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )


        val channelId = this.getString(R.string.kos_rvd_channel_id)
        val random = Random()

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(random.nextInt(), notificationBuilder.build())
    }
}