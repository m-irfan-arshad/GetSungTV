package com.getsung.tv.data.networking.error

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class ServerError(
    @SerialName("success") var success: Boolean? = null,
    @SerialName("message") var message: String? = null,
    @SerialName("code") var statusCode: Int? = null,
    @SerialName("actualCode") var actualCode: String? = null
)
