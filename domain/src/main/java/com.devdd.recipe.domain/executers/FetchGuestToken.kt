package com.devdd.recipe.domain.executers

import com.devdd.recipe.base.utils.AppCoroutineDispatchers
import com.devdd.recipe.data.repository.RecipeRepository
import com.devdd.recipe.domain.ResultUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject


class FetchGuestToken @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val repository: RecipeRepository
) : ResultUseCase<Unit, String>() {
    override suspend fun doWork(params: Unit): String {
        return withContext(dispatchers.io) {
            repository.getGuestToken()
        }
    }
}