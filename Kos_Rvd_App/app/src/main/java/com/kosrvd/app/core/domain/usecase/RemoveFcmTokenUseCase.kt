package com.kosrvd.app.core.domain.usecase

import com.kosrvd.app.core.data.networking.safeCall
import com.kosrvd.app.core.domain.repository.AccountRepository
import com.kosrvd.app.core.domain.repository.DeviceInfoProvider
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.core.domain.utils.onSuccess
import javax.inject.Inject

class RemoveFcmTokenUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val deviceInfoRepository: DeviceInfoProvider
) {
    suspend operator fun invoke(uid: String): Result<Unit, DataError>{
        return safeCall {
            // get device id
            val deviceId = deviceInfoRepository.getDeviceId()
            // call remove token
            accountRepository.removeFcmToken(uid = uid, deviceId = deviceId)
                .onError { throw Exception(it.message) }
                .onSuccess { Result.Success(Unit) }
        }
    }
}