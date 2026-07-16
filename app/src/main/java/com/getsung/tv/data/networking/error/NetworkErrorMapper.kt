package com.getsung.tv.data.networking.error

import com.bigbaat.networking.apiclient.base.error.AppException
import com.getsung.tv.data.networking.BAD_GATEWAY
import com.getsung.tv.data.networking.BAD_REQUEST
import com.getsung.tv.data.networking.FORBIDDEN
import com.getsung.tv.data.networking.GATEWAY_TIMEOUT
import com.getsung.tv.data.networking.INTERNAL_SERVER_ERROR
import com.getsung.tv.data.networking.NOT_FOUND
import com.getsung.tv.data.networking.TOO_MANY_REQUESTS
import com.getsung.tv.data.networking.UNAUTHORIZED
import com.getsung.tv.data.networking.intercepters.NoInternetException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

@Singleton
class NetworkErrorMapper
@Inject
constructor(
    private val json: Json,
) {
    fun map(throwable: Throwable): AppException =
        when (throwable) {
            is AppException -> throwable

            is UnknownHostException, is ConnectException, is SocketTimeoutException -> AppException.NoInternet(
                throwable
            )

            is SocketTimeoutException -> AppException.Timeout(throwable)

            is HttpException -> mapHttp(throwable)

            is SerializationException -> AppException.ParseError(throwable)

            is CancellationException -> throw AppException.Unknown()

            // NEVER catch cancellation
            is NoInternetException,
                -> AppException.NoInternet(throwable)

            else -> AppException.Unknown(throwable)
        }

    private fun mapHttp(e: HttpException): AppException {
        val body = e.response()?.errorBody()?.string()
        val parsed =
            body?.let {
                try {
                    json.decodeFromString<ServerError>(it)
                } catch (_: Exception) {
                    null
                }
            }

        return when (e.code()) {
            BAD_REQUEST -> {
                AppException.BadRequest(parsed?.message.toString())
            }

            UNAUTHORIZED -> {
                AppException.Unauthorized(parsed?.message ?: body)
            }

            FORBIDDEN -> {
                AppException.Forbidden(parsed?.message ?: body)
            }

            NOT_FOUND -> {
                AppException.NotFound(parsed?.message ?: body)
            }

            TOO_MANY_REQUESTS -> {
                AppException.RateLimited(parsed?.statusCode)
            }

            BAD_GATEWAY -> {
                AppException.ServerError(e.code(), parsed?.message)
            }

            GATEWAY_TIMEOUT -> {
                AppException.ServerError(e.code(), parsed?.message)
            }

            INTERNAL_SERVER_ERROR -> {
                AppException.ServerError(e.code(), parsed?.message)
            }

            in 500..599 -> {
                AppException.ServerError(e.code(), parsed?.message)
            }

            else -> {
                AppException.ServerError(e.code(), parsed?.message)
            }
        }
    }
}
