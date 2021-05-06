package com.devdd.recipe.base.result

import com.devdd.recipe.base.result.Result.Success

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out R> {

    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            Loading -> "Loading"
        }
    }
}

/**
 * [Success.data] if [Result] is of type [Success]
 */
fun <T> Result<T>.successOr(fallback: T): T {
    return (this as? Success<T>)?.data ?: fallback
}

val <T> Result<T>.data: T?
    get() = (this as? Success)?.data

/**
 * Do work if [Result] is of type [Success]
 */
inline fun <reified T> Result<T>.onSuccess(doOnSuccess: T.() -> Unit): Result<T> {
    if (this is Success) {
        doOnSuccess(data)
    }
    return this
}

/**
 * Do work if [Result] is of type [Success]
 */
inline fun <reified T> Result<T>.onError(doOnError: (exception: Exception) -> Unit): Result<T> {
    if (this is Result.Error) {
        doOnError(exception)
    }
    return this
}