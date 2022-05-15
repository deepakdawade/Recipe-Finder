package com.devdd.recipe.domain.executer

import com.devdd.recipe.data.remote.repository.RecipeRepository
import com.devdd.recipe.domain.InvokeUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchRecipes @Inject constructor(
    private val repository: RecipeRepository
) : InvokeUseCase<String>() {
    override suspend fun doWork(params: String) {
        return withContext(Dispatchers.IO) {
            repository.getRecipes(params)
        }
    }
}