package com.getsung.tv.data.networking.error


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import androidx.annotation.Keep

@Keep
@Serializable
data class ApiError(
    @SerialName("code") var statusCode: Int = 0,
    @SerialName("message") var message: String? = null,
    @SerialName("actualCode") var actualCode: String = "-1",
    @SerialName("payload") var pa: List<Payload>? = mutableListOf()
)
