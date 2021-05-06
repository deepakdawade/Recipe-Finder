package com.devdd.recipe.base_android.utils.extensions

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
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