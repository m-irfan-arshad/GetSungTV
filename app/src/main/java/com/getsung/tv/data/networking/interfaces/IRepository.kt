package com.bigbaat.networking.apiclient.base.interfaces

import com.getsung.tv.data.networking.ApiResponse
import com.getsung.tv.data.networking.RetroApiResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

internal interface IRepository {
    suspend fun <T : ApiResponse> executeSafely(call: suspend () -> Response<T>): RetroApiResponse<T>
    suspend fun <T : ApiResponse> executeFlowSafely(call: suspend () -> Response<T>): Flow<RetroApiResponse<T>>
    suspend fun  <T> retryWithExponentialBackoff(
        times: Int = 3, initialDelay: Long = 1000, factor: Double = 2.0, block: suspend () -> T
    ): T
}