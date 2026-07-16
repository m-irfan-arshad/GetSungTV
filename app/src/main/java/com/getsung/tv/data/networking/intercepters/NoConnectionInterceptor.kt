package com.getsung.tv.data.networking.intercepters

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import com.getsung.tv.utils.network.isInternetAvailable
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class NoConnectionInterceptor @Inject constructor(@ApplicationContext private val context: Context) :
    Interceptor {
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override fun intercept(chain: Interceptor.Chain): Response {
        return if (!context.isInternetAvailable()) {
            throw NoInternetException()
        } else {
            chain.proceed(chain.request())
        }
    }
}

class NoInternetException() : IOException() {
    override val message: String
        get() = "No internet available, please check your connected WIFi or Data"
}