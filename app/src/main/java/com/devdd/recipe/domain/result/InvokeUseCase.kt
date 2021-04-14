package com.devdd.recipe.domain.result

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withTimeout
import timber.log.Timber
import java.util.concurrent.TimeUnit


abstract class InvokeUseCase<in P> {
    operator fun invoke(params: P, timeoutMs: Long = defaultTimeoutMs): Flow<InvokeStatus<Unit>> {
        return flow {
            withTimeout(timeoutMs) {
                emit(InvokeStarted)
                doWork(params)
                emit(InvokeSuccess(Unit))
            }
        }.catch { error ->
            Timber.d("Error thrown by InvokeUseCase $error")
            emit(InvokeError(error))
        }
    }

    suspend fun executeSync(params: P): InvokeStatus<*> = try {
        doWork(params)
        InvokeSuccess(Unit)
    } catch (e: Exception) {
        Timber.d("Error thrown by interactor $e")
        InvokeError(e)
    }

    protected abstract suspend fun doWork(params: P)

    companion object {
        private val defaultTimeoutMs = TimeUnit.MINUTES.toMillis(5)
    }
}

abstract class InvokeResultUseCase<in P, R> {
    operator fun invoke(params: P): Flow<InvokeStatus<R>> {
        return flow {
            withTimeout(defaultTimeoutMs) {
                emit(InvokeStarted)
                val result = doWork(params)
                emit(InvokeSuccess(result))
            }

        }.catch { error ->
            Timber.d("Error thrown by InvokeResultUseCase ${error.message}")
            emit(InvokeError(error))
        }
    }

    protected abstract suspend fun doWork(params: P): R

    companion object {
        private val defaultTimeoutMs = TimeUnit.MINUTES.toMillis(5)
    }
}

/**
 * This can be used to collect result by using first() or some terminal operator
 */
abstract class ResultUseCase<in P, R> {
    operator fun invoke(params: P): Flow<R> {
        return flow {
            emit(doWork(params))
        }.catch { error ->
            Timber.d("Error thrown by ResultUseCase $error")
        }
    }

    protected abstract suspend fun doWork(params: P): R
}

/*
abstract class PagingUseCase<P : PagingUseCase.Parameters<T>, T> :
        SubjectUseCase<P, PagedList<T>>() {
    interface Parameters<T> {
        val pagingConfig: PagedList.Config
        val boundaryCallback: PagedList.BoundaryCallback<T>?
    }
}
*/

abstract class SuspendingWorkUseCase<P : Any, T> : SubjectUseCase<P, T>() {
    override fun createObservable(params: P): Flow<T> = flow {
        emit(doWork(params))
    }.catch { error ->
        Timber.d("Error thrown by SuspendingWorkUseCase $error")
    }

    abstract suspend fun doWork(params: P): T
}

abstract class SubjectUseCase<P : Any, T> {
    private val paramState = MutableStateFlow<P?>(null)

    operator fun invoke(params: P) {
        paramState.value = params
    }

    protected abstract fun createObservable(params: P): Flow<T>

    fun observe(): Flow<T> = paramState.filterNotNull().flatMapLatest { createObservable(it) }
        .catch { error ->
            Timber.d("Error thrown by SubjectUseCase $error")
        }
}

operator fun InvokeUseCase<Unit>.invoke() = invoke(Unit)
operator fun <T> SubjectUseCase<Unit, T>.invoke() = invoke(Unit)
