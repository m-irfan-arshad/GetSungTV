package com.getsung.tv.data.networking

import android.content.Context
import com.getsung.tv.data.networking.intercepters.IAuthInterceptor
import com.getsung.tv.data.networking.intercepters.NoConnectionInterceptor
import com.getsung.tv.data.networking.intercepters.OfflineCacheInterceptor
import com.bigbaat.networking.apiclient.base.intercepters.RetryInterceptor
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

const val TIME_OUT_READ = 60L
const val DISK_CACHE_SIZE = (50 * 1024 * 1024).toLong()
const val TIME_CONNECT = 60L // In seconds

@Singleton
class RetroNetwork
@Inject
constructor(
    private val context: Context, val baseUrl: String?,
    private val json: Json,
    private val okHttpClient: OkHttpClient
) {
    private lateinit var retrofit: Retrofit
    private val contentType = "application/json".toMediaType()

    fun build() {
        //var url = baseUrl ?: "https://staging.api.bigbaat.app"
        var url = baseUrl ?: "https://fea1-2407-aa80-116-6ca4-61ce-fd3a-8861-752c.ngrok-free.app"

        retrofit =
            Retrofit
                .Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(
                    json.asConverterFactory(contentType),
                ).build()
    }

    private fun getDiskCache(): Cache {
        // Use context.cacheDir instead of Environment.getDataDirectory() for better compatibility
        val cacheDir = File(context.cacheDir, "http-cache")
        return Cache(cacheDir, DISK_CACHE_SIZE)
    }

    fun <S> createService(serviceClass: Class<S>): S = retrofit.create(serviceClass)
}
