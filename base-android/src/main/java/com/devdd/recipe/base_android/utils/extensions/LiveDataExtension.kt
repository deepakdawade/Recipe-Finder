package com.devdd.recipe.base_android.utils.extensions

import androidx.annotation.MainThread
import androidx.lifecycle.*
import com.devdd.recipe.base.result.Event
import com.devdd.recipe.base_android.utils.EventObserver
import com.devdd.recipe.base.result.Result

fun <T> MutableLiveData<T>.postValueIfNew(newValue: T) {
    if (value != newValue)
        postValue(newValue)
}

var <T> MutableLiveData<T>.valueIfNew: T?
    get() = value
    set(newValue) {
        if (value != newValue)
            value = newValue
    }

fun <T> MutableLiveData<Result<T>>.updateNetworkState(): MutableLiveData<Result<T>> {
    postValue(Result.Loading)
    return this
}

/** Uses `Transformations.map` on a LiveData */
fun <X, Y> LiveData<X>.map(body: (X) -> Y): LiveData<Y> {
    return Transformations.map(this, body)
}

@MainThread
inline fun <T> LiveData<Event<T>>.observeEvent(
    owner: LifecycleOwner,
    crossinline onEventUnhandledContent: (T) -> Unit,
) {
    observe(owner, EventObserver { onEventUnhandledContent(it) })
}

@MainThread
inline fun <T> LiveData<Event<T>>.observeEventForever(
    crossinline onEventUnhandledContent: (T) -> Unit,
) {
    observeForever(EventObserver { onEventUnhandledContent(it) })
}

// region LiveData
/** Uses `Transformations.switchMap` on a LiveData */
fun <X, Y> LiveData<X>.switchMap(body: (X) -> LiveData<Y>): LiveData<Y> {
    return Transformations.switchMap(this, body)
}
/**
 * Combines this [LiveData] with another [LiveData] using the specified [combiner] and returns the
 * oldResult as a [LiveData].
 */
fun <A, B, Result> LiveData<A>.combine(
    other: LiveData<B>,
    combiner: (A, B) -> Result
): LiveData<Result> {
    val result = MediatorLiveData<Result>()
    result.addSource(this) { a ->
        val b = other.value
        if (b != null) {
            result.postValue(combiner(a, b))
        }
    }
    result.addSource(other) { b ->
        val a = this@combine.value
        if (a != null) {
            result.postValue(combiner(a, b))
        }
    }
    return result
}