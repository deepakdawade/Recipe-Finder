package com.devdd.recipe.utils.extensions

import androidx.lifecycle.MutableLiveData
import com.devdd.recipe.domain.result.Result

/**
 * Transform and Updates value of [liveData] if [Result] is of type [Success]
 */
inline fun <reified T, P> Result<T>.updateOnSuccess(
    liveData: MutableLiveData<P>,
    transform: T.() -> P
): Result<T> {
    if (this is Result.Success) {
        liveData.postValue(transform(data))
    }
    return this
}

/**
 * Updates value of [liveData] if [Result] is of type [Success]
 */
inline fun <reified T> Result<T>.updateOnSuccess(liveData: MutableLiveData<T>): Result<T> {
    if (this is Result.Success) {
        liveData.postValue(data)
    }
    return this
}

/**
 * Resets value of [liveData] if [Result] is of type [Result.Error]
 */
inline fun <reified T> Result<T>.resetOnError(liveData: MutableLiveData<T>, default: T): Result<T> {
    if (this is Result.Error) {
        liveData.postValue(default)
    }
    return this
}

/**
 * Transform and Resets value of [liveData] if [Result] is of type [Result.Error]
 */
inline fun <reified T, P> Result<T>.resetOnError(
    liveData: MutableLiveData<P>,
    transform: () -> P
): Result<T> {
    if (this is Result.Error) {
        liveData.postValue(transform())
    }
    return this
}

inline fun <reified T> Result<T>.updateNetworkState(liveData: MutableLiveData<Boolean>): Result<T> {
//    liveData.postValue(data is Result.Loading)
    return this
}
