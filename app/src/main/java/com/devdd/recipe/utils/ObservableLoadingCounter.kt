package com.devdd.recipe.utils

import com.devdd.recipe.domain.InvokeStatus
import com.devdd.recipe.domain.onError
import com.devdd.recipe.domain.onStarted
import com.devdd.recipe.domain.onSuccess
import kotlinx.coroutines.flow.*
import java.util.concurrent.atomic.AtomicInteger

class ObservableLoadingCounter {
    private val count = AtomicInteger()
    private val loadingState = MutableStateFlow(count.get())

    val observable: Flow<Boolean>
        get() = loadingState.map { it > 0 }.distinctUntilChanged()

    fun addLoader() {
        loadingState.value = count.incrementAndGet()
    }

    fun removeLoader() {
        loadingState.value = count.decrementAndGet()
    }
}

suspend fun <T> Flow<InvokeStatus<T>>.collectInto(counter: ObservableLoadingCounter) {
    return onStart { counter.addLoader() }
        .onCompletion { counter.removeLoader() }
        .collect()
}

fun <T> InvokeStatus<T>.updateLoadingState(counter: ObservableLoadingCounter) {
    onStarted { counter.addLoader() }
        .onSuccess { counter.removeLoader() }
        .onError {
            counter.removeLoader()
        }
}

