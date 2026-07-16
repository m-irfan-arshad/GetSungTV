package com.bigbaat.networking.apiclient.base.intercepters

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class RetryInterceptor(private val maxRetries: Int = 3) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        var response: Response? = null
        var exception: IOException? = null

        // Loop through retry attempts from 0 to maxRetries
        for (attempt in 0..maxRetries) {
            try {
                // Close the previous response if it exists
                response?.close()
                // Proceed with the request through the chain
                response = chain.proceed(request)
                // Check if the response is successful or not in the retryable status codes
                if (response.isSuccessful || response.code !in listOf(408, 429, 500, 502, 503, 504)
                ) {
                    return response
                }
            } catch (e: IOException) {
                // Store the exception for later use if needed
                exception = e
                // If we've reached max retries, throw the exception
                if (attempt == maxRetries) throw e
            }
            // Exponential backoff: 1s, 2s, 4s
            Thread.sleep((1000L * (1 shl attempt)).coerceAtMost(4000L))
        }
        return response ?: throw exception ?: IOException("Retry failed")
    }

    private fun process(chain: Interceptor.Chain, attempt: Int): Response {
        var response: Response? = null
        try {
            val request = chain.request()
            response = chain.proceed(request)
            if (attempt < maxRetries && !response.isSuccessful) {
                return delayedAttempt(chain, response, attempt)
            }
            return response
        } catch (e: Exception) {
            if (attempt < maxRetries) {
                return delayedAttempt(chain, response, attempt)
            }
            throw e
        }
    }

    private fun delayedAttempt(
        chain: Interceptor.Chain,
        response: Response?,
        attempt: Int,
    ): Response {
        response?.body?.close()
        Thread.sleep((1000L * (1 shl attempt)).coerceAtMost(4000L))
        return process(chain, attempt = attempt + 1)
    }
}