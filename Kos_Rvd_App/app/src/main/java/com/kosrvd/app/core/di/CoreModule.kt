package com.kosrvd.app.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.memoryCacheSettings
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.functions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.kosrvd.app.BuildConfig
import com.kosrvd.app.core.data.networking.HttpClientFactory
import com.kosrvd.app.core.data.repository.AccountRepositoryImpl
import com.kosrvd.app.core.data.repository.AuthRepositoryImpl
import com.kosrvd.app.core.data.repository.DeviceInfoProviderImpl
import com.kosrvd.app.core.data.repository.StorageRepositoryImpl
import com.kosrvd.app.core.data.repository.dto.AuthInfoDto
import com.kosrvd.app.core.data.source.local.AuthInfoSerializer
import com.kosrvd.app.core.data.source.local.SessionStorage
import com.kosrvd.app.core.data.source.local.SessionStorageImpl
import com.kosrvd.app.core.domain.repository.AccountRepository
import com.kosrvd.app.core.domain.repository.AuthRepository
import com.kosrvd.app.core.domain.repository.DeviceInfoProvider
import com.kosrvd.app.core.domain.repository.StorageRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.http.ContentDisposition.Companion.File
import okhttp3.Cache
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.dnsoverhttps.DnsOverHttps
import java.io.File
import java.net.InetAddress
import javax.inject.Singleton

val Context.authInfoDataStore: DataStore<AuthInfoDto> by dataStore(
    fileName = "auth_info.json",
    serializer = AuthInfoSerializer
)

const val laptopIp = BuildConfig.IP_LAPTOP

@Module
@InstallIn(SingletonComponent::class)
abstract class CoreModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindAccountRepository(accountRepositoryImpl: AccountRepositoryImpl): AccountRepository

    @Binds
    @Singleton
    abstract fun bindDeviceInfoProvider(deviceInfoProviderImpl: DeviceInfoProviderImpl): DeviceInfoProvider

    @Binds
    @Singleton
    abstract fun bindSessionStorage(sessionStorageImpl: SessionStorageImpl): SessionStorage

    @Binds
    @Singleton
    abstract fun bindStorageRepository(storageRepositoryImpl: StorageRepositoryImpl): StorageRepository


    companion object{

        @Provides
        @Singleton
        fun provideFirebaseAuth(): FirebaseAuth {
            val auth = Firebase.auth
            // Cek apakah aplikasi sedang dalam mode debug (untuk testing)
            if (BuildConfig.DEBUG) {
                // Port default Auth emulator adalah 9099
                auth.useEmulator(laptopIp, 9099)
            }
            return auth
        }

        @Provides
        @Singleton
        fun provideFirebaseFirestore(): FirebaseFirestore{
            val firestore = Firebase.firestore
            if (BuildConfig.DEBUG) {
                // Port default Firestore emulator adalah 8080
                firestore.useEmulator(laptopIp, 8080)

                // Penting: Firestore terkadang butuh setting tambahan agar tidak error saat local
                val settings = firestoreSettings {
                    setLocalCacheSettings(
                        memoryCacheSettings{})
                }
                firestore.firestoreSettings = settings
            }
            return firestore
        }

        @Provides
        @Singleton
        fun provideFirebaseStorage(): FirebaseStorage {
            val storage = Firebase.storage
            if (BuildConfig.DEBUG) {
                // Port default Storage emulator adalah 9199
                storage.useEmulator(laptopIp, 9199)
            }
            return storage
        }

        @Provides
        @Singleton
        fun provideContext(
            @ApplicationContext context: Context
        ): Context {
            return context
        }


        @Provides
        @Singleton
        fun provideAuthDataStore(
            context: Context
        ): DataStore<AuthInfoDto> {
            return context.authInfoDataStore
        }


        @Provides
        @Singleton
        fun provideFirebaseFunctions(): FirebaseFunctions {
            val functions = Firebase.functions("asia-southeast2")
            if (BuildConfig.DEBUG) {
                functions.useEmulator(laptopIp, 5001)
            }
            return functions
        }

        @Provides
        @Singleton
        fun provideHttpClient(): HttpClient {
            val appCache = Cache(File("cacheDir", "okhttpCache"), 10 * 1024 * 1024)
            val bootstrapClient = OkHttpClient.Builder().cache(appCache).build()

            val dns = DnsOverHttps.Builder()
                .client(bootstrapClient)
                .url("https://dns.google/dns-query".toHttpUrl()) // Pakai Server Google
                .bootstrapDnsHosts(
                    InetAddress.getByName("8.8.8.8"),
                    InetAddress.getByName("8.8.4.4")
                )
                .build()

            return HttpClientFactory.create(OkHttp.create {
                config {
                    dns(dns)
                }
            })
        }
    }
}