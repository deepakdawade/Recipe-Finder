package com.devdd.recipe.domain

import com.devdd.recipe.domain.result.Result
import kotlinx.coroutines.flow.*


inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = flow {
    val data = query().first()

    val flow = if (shouldFetch(data)) {
        emit(Result.Loading)

        try {
            saveFetchResult(fetch())
            query().map { Result.Success(it) }
        } catch (throwable: Exception) {
            query().map { Result.Error(throwable) }
        }
    } else {
        query().map { Result.Success(it) }
    }

    emitAll(flow)
}