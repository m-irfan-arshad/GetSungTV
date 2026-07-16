package com.getsung.tv.data.networking.error

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class Payload(
    @SerialName("message") var message: String? = "",
    @SerialName("path") var path: List<String>? = mutableListOf()
)
