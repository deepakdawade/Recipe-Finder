package com.devdd.recipe.domain.result

/**
 * Created by @author Deepak Dawade on 4/4/2021 at 12:57 AM.
 * Copyright (c) 2021 deepak.dawade.dd1@gmail.com All rights reserved.
 *
 */

sealed class InvokeStatus
object InvokeStarted : InvokeStatus()
object InvokeSuccess : InvokeStatus()
data class InvokeError(val throwable: Throwable) : InvokeStatus()

inline fun InvokeStatus.onSuccess(perform: () -> Unit): InvokeStatus {
    if (this is InvokeSuccess) {
        perform()
    }
    return this
}

inline fun InvokeStatus.onError(perform: () -> Unit): InvokeStatus {
    if (this is InvokeError) {
        perform()
    }
    return this
}

inline fun InvokeStatus.onStarted(perform: () -> Unit): InvokeStatus {
    if (this is InvokeStarted) {
        perform()
    }
    return this
}