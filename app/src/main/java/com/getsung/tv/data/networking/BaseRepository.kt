package com.getsung.tv.data.networking

import com.bigbaat.networking.apiclient.base.interfaces.IRepository
import com.getsung.tv.data.networking.error.NetworkErrorMapper
import com.getsung.tv.data.networking.intercepters.NoInternetException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

abstract class BaseRepository(val errorMapper: NetworkErrorMapper) : IRepository {
    override suspend fun <T : ApiResponse> executeSafely(call: suspend () -> Response<T>): RetroApiResponse<T> {
        return try {
            RetroApiResponse.Loading
            val response = call.invoke()
            if (response.isSuccessful) {
                response.body()?.let {
                    return RetroApiResponse.Success(response.code(), it)
                } ?: RetroApiResponse.Error(
                    errorMapper.map(NullPointerException("Response body is null"))
                )

            }
            return RetroApiResponse.Error(errorMapper.map(HttpException(response)))
        } catch (e: HttpException) {
            RetroApiResponse.Error(errorMapper.map(e))
        } catch (e: IllegalArgumentException) {
            RetroApiResponse.Error(errorMapper.map(e))
        } catch (e: SocketTimeoutException) {
            RetroApiResponse.Error(errorMapper.map(e))
        } catch (e: NoInternetException) {
            RetroApiResponse.Error(errorMapper.map(e))
        }
    }

    override suspend fun <T : ApiResponse> executeFlowSafely(call: suspend () -> Response<T>): Flow<RetroApiResponse<T>> =
        flow {
            emit(RetroApiResponse.Loading)
            emit(executeSafely(call))
        }

    override suspend fun <T> retryWithExponentialBackoff(
        times: Int,
        initialDelay: Long,
        factor: Double,
        block: suspend () -> T
    ): T {
        var currentDelay = initialDelay
        repeat(times - 1) { attempt ->
            try {
                return block()
            } catch (e: IOException) {
                // Only retry on network errors
                delay(currentDelay)
                currentDelay = (currentDelay * factor).toLong()
            }
        }
        return block() // Last attempt
    }
}