package com.devdd.recipe.domain.usecase

import com.devdd.recipe.domain.result.Result
import timber.log.Timber

/**
 * Executes business logic synchronously.
 */
abstract class SynchronousUseCase<in P, R> {

    /** Executes the use case asynchronously and returns a [Result].
     *
     * @return a [Result].
     *
     * @param parameters the input parameters to run the use case with
     */
    @Synchronized
    operator fun invoke(parameters: P): Result<R> {
        return try {
            execute(parameters).let {
                Result.Success(it)
            }
        } catch (e: Exception) {
            Timber.d(e)
            Result.Error(e)
        }
    }

    /**
     * Override this to set the code to be executed.
     */
    @Throws(RuntimeException::class)
    protected abstract fun execute(parameters: P): R
}