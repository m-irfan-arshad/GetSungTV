package com.getsung.tv.data.networking

import com.bigbaat.networking.apiclient.base.error.AppException

sealed class RetroApiResponse<out T : ApiResponse> {
    data class Success<out T : ApiResponse>(
        val code: Int,
        val data: T,
    ) : RetroApiResponse<T>()

    data class Error(
        val error: AppException,
    ) : RetroApiResponse<Nothing>()

    data object Loading : RetroApiResponse<Nothing>()
}
