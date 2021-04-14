package com.devdd.recipe.domain.executers

import com.devdd.recipe.data.remote.repository.RecipeRepository
import com.devdd.recipe.domain.result.InvokeUseCase
import com.devdd.recipe.utils.AppCoroutineDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchRecipes @Inject constructor(
    private val appCoroutineDispatchers: AppCoroutineDispatchers,
    private val repository: RecipeRepository
) : InvokeUseCase<String>() {
    override suspend fun doWork(params: String) {
        return withContext(appCoroutineDispatchers.io) {
            repository.getRecipes(params)
        }
    }
}