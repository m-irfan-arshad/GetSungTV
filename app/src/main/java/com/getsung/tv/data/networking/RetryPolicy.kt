package com.getsung.tv.data.networking

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.min
import kotlin.math.pow

@Singleton
class RetryPolicy @Inject constructor() {
    suspend fun <T> execute(
        maxRetries: Int = 5,
        initialDelay: Double = 1.0, // seconds
        maxDelay: Double = 30.0,    // seconds
        factor: Double = 2.0,
        block: suspend () -> T
    ): T {
        var lastError: Throwable? = null

        repeat(maxRetries - 1) { attempt ->
            try {
                return block()
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                lastError = e
                if (attempt == maxRetries - 1) return throw lastError

                val delaySeconds = initialDelay * factor.pow(attempt.toDouble())
                val clampedDelay = min(delaySeconds, maxDelay)

                delay((clampedDelay * 1000).toLong())
            }
        }

        throw lastError ?: RuntimeException("Unknown error")
    }


}