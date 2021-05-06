package com.devdd.recipe.base.result

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

sealed class InvokeStatus<out T>
object InvokeStarted : InvokeStatus<Nothing>()
data class InvokeSuccess<T>(val data: T) : InvokeStatus<T>()
data class InvokeError(val throwable: Throwable) : InvokeStatus<Nothing>()

inline fun <T> InvokeStatus<T>.onSuccess(perform: (T) -> Unit): InvokeStatus<T> {
    if (this is InvokeSuccess<T>) {
        perform(this.data)
    }
    return this
}

inline fun <T> InvokeStatus<T>.onError(perform: (Throwable) -> Unit): InvokeStatus<T> {
    if (this is InvokeError) {
        perform(this.throwable)
    }
    return this
}

inline fun <T> InvokeStatus<T>.onStarted(perform: () -> Unit): InvokeStatus<T> {
    if (this is InvokeStarted) {
        perform()
    }
    return this
}

suspend fun <T> Flow<InvokeStatus<T>>.watchStatus(
        action: InvokeStatus<T>.() -> Unit
): Unit = collect { action(it) }








