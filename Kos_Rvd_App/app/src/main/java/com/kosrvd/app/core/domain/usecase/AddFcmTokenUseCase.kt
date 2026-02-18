package com.kosrvd.app.core.domain.usecase

import com.google.firebase.Timestamp
import com.google.firebase.messaging.FirebaseMessaging
import com.kosrvd.app.core.data.networking.safeCall
import com.kosrvd.app.core.domain.models.FcmToken
import com.kosrvd.app.core.domain.repository.AccountRepository
import com.kosrvd.app.core.domain.repository.DeviceInfoProvider
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.core.domain.utils.onSuccess
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AddFcmTokenUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val deviceInfoRepository: DeviceInfoProvider,
) {
    suspend operator fun invoke(uid: String): Result<Unit, DataError>{
        return safeCall {
            // get token fcm
            val token = FirebaseMessaging.getInstance().token.await()
            // mapping
            val tokenData = FcmToken(
                token = token,
                deviceId = deviceInfoRepository.getDeviceId(),
                deviceName = deviceInfoRepository.getDeviceName(),
                platform = "android",
                lastUpdated = Timestamp.now()
            )

            // call add token fcm
            accountRepository.addFcmToken(uid = uid, tokenData = tokenData)
                .onError {
                    throw Exception(it.message)
                }
                .onSuccess {
                    Result.Success(Unit)
                }
        }
    }
}