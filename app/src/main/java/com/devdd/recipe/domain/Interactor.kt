package com.devdd.recipe.domain

import com.devdd.recipe.domain.result.InvokeError
import com.devdd.recipe.domain.result.InvokeStarted
import com.devdd.recipe.domain.result.InvokeStatus
import com.devdd.recipe.domain.result.InvokeSuccess
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withTimeout
import timber.log.Timber
import java.util.concurrent.TimeUnit

abstract class Interactor<in P> {
    operator fun invoke(params: P, timeoutMs: Long = defaultTimeoutMs): Flow<InvokeStatus> {
        return flow {
            withTimeout(timeoutMs) {
                emit(InvokeStarted)
                doWork(params)
                emit(InvokeSuccess)
            }
        }.catch { t ->
            emit(InvokeError(t))
        }
    }

    suspend fun executeSync(params: P): InvokeStatus = try {
        doWork(params)
        InvokeSuccess
    } catch (e: Exception) {
        Timber.d("Error thrown by interactor ${e.message}")
        InvokeError(e)
    }

    protected abstract suspend fun doWork(params: P)

    companion object {
        private val defaultTimeoutMs = TimeUnit.MINUTES.toMillis(5)
    }
}

abstract class ResultInteractor<in P, R> {
    operator fun invoke(params: P): Flow<R> {
        return flow {
            emit(doWork(params))
        }.catch { t ->
            Timber.e(t)
        }
    }

    protected abstract suspend fun doWork(params: P): R
}

/*
abstract class PagingInteractor<P : PagingInteractor.Parameters<T>, T> :
    SubjectInteractor<P, PagedList<T>>() {
    interface Parameters<T> {
        val pagingConfig: PagedList.Config
        val boundaryCallback: PagedList.BoundaryCallback<T>?
    }
}
*/

abstract class SuspendingWorkInteractor<P : Any, T> : SubjectInteractor<P, T>() {
    override fun createObservable(params: P): Flow<T> = flow {
        emit(doWork(params))
    }.catch { t ->
        Timber.e(t)
    }

    abstract suspend fun doWork(params: P): T
}

abstract class SubjectInteractor<P : Any, T> {
    private val paramState = MutableStateFlow<P?>(null)

    operator fun invoke(params: P) {
        paramState.value = params
    }

    protected abstract fun createObservable(params: P): Flow<T>

    fun observe(): Flow<T> = paramState.filterNotNull().flatMapLatest { createObservable(it) }
}

operator fun Interactor<Unit>.invoke(): Flow<InvokeStatus> = invoke(Unit)
operator fun <T> SubjectInteractor<Unit, T>.invoke(): Unit = invoke(Unit)
