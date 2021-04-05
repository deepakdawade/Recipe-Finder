package com.devdd.recipe.utils.extensions

import com.devdd.recipe.data.remote.models.ServerResponse
import com.devdd.recipe.domain.result.ServerException
import com.devdd.recipe.domain.result.Result
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

fun <T> ServerResponse<T>.dataOrThrowException(executeOnSuccess: T.() -> Unit = {}): T {
    return if (status == 0) data.also { executeOnSuccess(it) }
    else throw ServerException(errCode, message)
}

fun <T> Response<T>.bodyOrThrow(): T {
    if (!isSuccessful) throw HttpException(this)
    return body()!!
}

fun <T> Response<T>.toException(): HttpException = HttpException(this)

suspend inline fun <T> Call<T>.executeWithRetry(
    defaultDelay: Long = 100,
    maxAttempts: Int = 3,
    shouldRetry: (Exception) -> Boolean = ::defaultShouldRetry
): Response<T> {
    repeat(maxAttempts) { attempt ->
        var nextDelay = attempt * attempt * defaultDelay

        try {
            // Clone a new ready call if needed
            val call = if (isExecuted) clone() else this
            return call.execute()
        } catch (e: Exception) {
            // The response failed, so lets see if we should retry again
            if (attempt == (maxAttempts - 1) || !shouldRetry(e)) {
                throw e
            }

            if (e is HttpException) {
                // If we have a HttpException, check whether we have a Retry-After
                // header to decide how long to delay
                val retryAfterHeader = e.response()?.headers()?.get("Retry-After")
                if (retryAfterHeader != null && retryAfterHeader.isNotEmpty()) {
                    // Got a Retry-After value, try and parse it to an long
                    try {
                        nextDelay = (retryAfterHeader.toLong() + 10).coerceAtLeast(defaultDelay)
                    } catch (nfe: NumberFormatException) {
                        // Probably won't happen, ignore the value and use the generated default above
                    }
                }
            }
        }

        delay(nextDelay)
    }

    // We should never hit here
    throw IllegalStateException("Unknown exception from executeWithRetry")
}

suspend inline fun <T> Call<T>.fetchBodyWithRetry(
    firstDelay: Long = 100,
    maxAttempts: Int = 3,
    shouldRetry: (Exception) -> Boolean = ::defaultShouldRetry
): T = executeWithRetry(firstDelay, maxAttempts, shouldRetry).bodyOrThrow()

fun defaultShouldRetry(exception: Exception): Boolean = when (exception) {
    is HttpException -> exception.code() == 429
    is IOException -> true
    else -> false
}

fun <T> Response<T>.toResultUnit(): Result<Unit> = try {
    if (isSuccessful) {
        Result.Success(data = Unit)
    } else {
        Result.Error(toException())
    }
} catch (e: Exception) {
    Result.Error(e)
}

fun <T> Response<ServerResponse<T>>.toResult(): Result<T> = try {
    if (isSuccessful) {
        val data = bodyOrThrow().dataOrThrowException()
        Result.Success(data = data)
    } else {
        Result.Error(toException())
    }
} catch (e: Exception) {
    Result.Error(e)
}

@Suppress("REDUNDANT_INLINE_SUSPEND_FUNCTION_TYPE")
suspend inline fun <T, E> Response<ServerResponse<T>>.toResult(mapper: suspend (T) -> E): Result<E> =
    try {
        if (isSuccessful) {
            val data = bodyOrThrow().dataOrThrowException()
            Result.Success(data = mapper(data))
        } else {
            Result.Error(toException())
        }
    } catch (e: Exception) {
        Result.Error(e)
    }

fun <T> Result<T>.dataOrThrow(executeOnSuccess: T.() -> Unit = {}): T {
    return when (this) {
        is Result.Success -> data.also { executeOnSuccess(it) }
        else -> throw (this as? Result.Error)?.exception ?: IllegalArgumentException("Call failed")
    }
}
