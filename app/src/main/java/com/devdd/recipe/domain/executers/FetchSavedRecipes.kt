package com.devdd.recipe.domain.executers

import com.devdd.recipe.data.remote.models.request.SavedRecipesRequest
import com.devdd.recipe.data.remote.repository.RecipeRepository
import com.devdd.recipe.domain.result.InvokeUseCase
import com.devdd.recipe.utils.AppCoroutineDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchSavedRecipes @Inject constructor(
    private val appCoroutineDispatchers: AppCoroutineDispatchers,
    private val repository: RecipeRepository
) : InvokeUseCase<SavedRecipesRequest>() {
    override suspend fun doWork(params: SavedRecipesRequest) {
        return withContext(appCoroutineDispatchers.io) {
            repository.getSavedRecipes(params)
        }
    }
}