package com.getsung.tv.data.networking

import android.os.Environment
import com.getsung.tv.data.networking.intercepters.IAuthInterceptor
import okhttp3.Cache
import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.MINUTES
import javax.inject.Inject

class OkHttpNetwork @Inject constructor(private val authInterceptor: IAuthInterceptor) {
    private val okHttpBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
    fun providesLoggingInterceptor(): HttpLoggingInterceptor {
        val logger = HttpLoggingInterceptor()
        logger.level =
            if (true) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        return logger
    }

    fun provideConnectionPool(): ConnectionPool {
        return ConnectionPool(
            maxIdleConnections = 10, // increase for parallel uploads
            keepAliveDuration = 5,
            timeUnit = MINUTES
        )
    }

    fun provideDispatcher(): Dispatcher {
        return Dispatcher().apply {
            maxRequests = 20
            maxRequestsPerHost = 10 // important for S3
        }
    }


    val okHttpClient: OkHttpClient
        get() {
            return okHttpBuilder
                .connectTimeout(TIME_CONNECT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT_READ, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT_READ, TimeUnit.SECONDS)
                .cache(getDiskCache())
                .retryOnConnectionFailure(true)
                .dispatcher(provideDispatcher())
                .connectionPool(provideConnectionPool())
//                .addInterceptor(RetryInterceptor(3))
                .addInterceptor(providesLoggingInterceptor())
                .build()
        }

    private fun getDiskCache(): Cache {
        val cacheDir = File(Environment.getDataDirectory(), "cache")
        return Cache(cacheDir, DISK_CACHE_SIZE)
    }
}