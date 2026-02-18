package com.kosrvd.app.core.domain.repository

interface DeviceInfoProvider {
    fun getDeviceId(): String
    fun getDeviceName(): String
}