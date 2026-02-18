package com.kosrvd.app.core.data.repository

import android.content.Context
import android.os.Build
import android.util.Log
import com.kosrvd.app.core.domain.repository.DeviceInfoProvider
import java.util.UUID
import javax.inject.Inject
import androidx.core.content.edit

class DeviceInfoProviderImpl @Inject constructor(
    private val context: Context
): DeviceInfoProvider {

    companion object{
        private const val PREFS_NAME = "device_info"
        private const val KEY_DEVICE_ID = "device_id"
    }

    private val sharedPrefs by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    override fun getDeviceId(): String {
        var deviceId = sharedPrefs.getString(KEY_DEVICE_ID, null)

        if (deviceId == null) {
            deviceId = UUID.randomUUID().toString()
            sharedPrefs.edit { putString(KEY_DEVICE_ID, deviceId) }

            // Log for debugging
            Log.d("DeviceInfo", "Generated new Device ID: $deviceId")
        }

        return deviceId
    }

    override fun getDeviceName(): String {
        return try {
            val manufacturer = Build.MANUFACTURER
            val model = Build.MODEL

            when {
                model.lowercase().startsWith(manufacturer.lowercase()) -> {
                    capitalize(model)
                }
                else -> {
                    "${capitalize(manufacturer)} ${capitalize(model)}"
                }
            }
        } catch (e: Exception) {
            "Unknown Device, message error: $e"
        }
    }

    private fun capitalize(str: String): String {
        return str.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase() else it.toString()
        }
    }
}