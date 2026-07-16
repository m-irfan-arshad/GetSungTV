package com.getsung.tv.data.networking.intercepters

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import com.getsung.tv.utils.network.isInternetAvailable
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OfflineCacheInterceptor @Inject constructor(
    @ApplicationContext private val context: Context
) : Interceptor {

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (!context.isInternetAvailable()) {
            request = request.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=${7 * 24 * 60 * 60}")  // 7 days stale
                .build()
        }
        return chain.proceed(request)
    }
}