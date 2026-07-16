package com.bigbaat.networking.apiclient.base.error

import com.getsung.tv.data.networking.error.ApiError

sealed class AppException(
    open val apiError: ApiError,
    cause: Throwable? = null,
) : Exception(apiError.message, cause) {
    // Network
    data class NoInternet(
        override val cause: Throwable? = null,
    ) : AppException(ApiError(message = "No internet connection"), cause)

    data class Timeout(
        override val cause: Throwable? = null,
    ) : AppException(ApiError(message = "Request timed out"), cause)

    // Server
    data class ServerError(
        val code: Int,
        val errorBody: String?,
    ) : AppException(ApiError(message = errorBody ?: "Server error: $code"))

    data class Unauthorized(
        val errorBody: String? = null,
    ) : AppException(ApiError(message = errorBody ?: "Unauthorized, token is required"))

    data class Forbidden(
        val errorBody: String? = null,
    ) : AppException(ApiError(message = "Access denied"))

    data class NotFound(
        val errorBody: String? = null,
    ) : AppException(ApiError(message = errorBody ?: "User not found"))

    data class RateLimited(
        val retryAfterSeconds: Int?,
    ) : AppException(ApiError(message = "Too many requests"))

    data class Maintenance(
        val msg: String = "Service under maintenance",
    ) : AppException(ApiError(message = msg))

    // Validation
    data class ValidationError(
        val field: String,
        override val message: String,
    ) : AppException(ApiError(message = message))

    data class BadRequest(
        val errors: String?,
    ) : AppException(ApiError(message = errors ?: "Invalid Request"))

    data class ParseError(
        override val cause: Throwable,
    ) : AppException(ApiError(message = "Failed to parse response"), cause)

    // Unknown
    data class Unknown(
        override val cause: Throwable? = null,
    ) : AppException(ApiError(message = cause?.message ?: "An unexpected error occurred"), cause)
}
