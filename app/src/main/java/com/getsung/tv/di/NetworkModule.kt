package com.getsung.tv.di

import android.content.Context
import com.bigbaat.networking.apiclient.base.intercepters.RetryInterceptor
import com.getsung.tv.BuildConfig
import com.getsung.tv.data.networking.DISK_CACHE_SIZE
import com.getsung.tv.data.networking.RetroNetwork
import com.getsung.tv.data.networking.TIME_CONNECT
import com.getsung.tv.data.networking.TIME_OUT_READ
import com.getsung.tv.data.networking.intercepters.IAuthInterceptor
import com.getsung.tv.data.networking.intercepters.OfflineCacheInterceptor
import com.getsung.tv.utils.network.ConnectivityObserver
import com.getsung.tv.utils.network.NetworkConnectivityMonitor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.MINUTES
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json =
        Json {
            ignoreUnknownKeys = true // ignores extra fields from API
            isLenient = true // allows relaxed JSON
            prettyPrint = true // formatted output
            encodeDefaults = true
            explicitNulls = false
        }

    @Provides
    @Singleton
    fun provideRetroNetwork(
        @ApplicationContext context: Context,
        @Named("BaseUrl") baseUrl: String,
        json: Json,
        okHttpClient: OkHttpClient
    ): RetroNetwork {
        val retroNetwork =
            RetroNetwork(
                context,
                baseUrl,
                json,
                okHttpClient
            )
        retroNetwork.build()
        return retroNetwork
    }

    fun providesLoggingInterceptor(): HttpLoggingInterceptor {
        val logger = HttpLoggingInterceptor()
        logger.level =
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        return logger
    }

    @Provides
    @Singleton
    fun provideConnectionPool(): ConnectionPool {
        return ConnectionPool(
            maxIdleConnections = 10, // increase for parallel uploads
            keepAliveDuration = 5,
            timeUnit = MINUTES
        )
    }

    @Provides
    @Singleton
    fun provideDispatcher(): Dispatcher {
        return Dispatcher().apply {
            maxRequests = 20
            maxRequestsPerHost = 10 // important for S3
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: IAuthInterceptor,
        offlineCacheInterceptor: OfflineCacheInterceptor,
        retryInterceptor: RetryInterceptor,
        loggingInterceptor: HttpLoggingInterceptor,
        @Named("cacheInterceptor") cacheInterceptor: Interceptor,
        cache: Cache,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(TIME_CONNECT, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT_READ, TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT_READ, TimeUnit.SECONDS)
            .cache(cache)
            .retryOnConnectionFailure(true)
            .dispatcher(provideDispatcher())
            .connectionPool(provideConnectionPool())
            .addInterceptor(loggingInterceptor)
            .addInterceptor(retryInterceptor)
            .addInterceptor(authInterceptor)
            .addInterceptor(cacheInterceptor)
            .addInterceptor(offlineCacheInterceptor).build()
    }

    @Provides
    fun provideOfflineCacheInterceptor(
        @ApplicationContext context: Context,
    ): OfflineCacheInterceptor = OfflineCacheInterceptor(context)

    @Provides
    @Singleton
    fun provideRetryInterceptor(): RetryInterceptor = RetryInterceptor()

    @Provides
    @Singleton
    @Named("cacheInterceptor")
    fun provideCacheInterceptor() =
        Interceptor { chain ->
            val response = chain.proceed(chain.request())
            response
                .newBuilder()
                .header("Cache-Control", "public, max-age=${5 * 60}") // Cache 5 minutes
                .removeHeader("Pragma")
                .build()
        }

    @Provides
    @Singleton
    fun provideDiskCache(@ApplicationContext context: Context): Cache {
        // Use context.cacheDir instead of Environment.getDataDirectory() for better compatibility
        val cacheDir = File(context.cacheDir, "http-cache")
        return Cache(cacheDir, DISK_CACHE_SIZE)
    }

    @Provides
    @Singleton
    fun provideNetworkConnectivityObserver(
        @ApplicationContext context: Context,
    ): ConnectivityObserver = NetworkConnectivityMonitor(context)

    @Named("BaseUrl")
    @Provides
    fun provideBaseUrl(): String = BuildConfig.BASE_URL
}