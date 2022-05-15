package com.devdd.recipe.domain.executer

import com.devdd.recipe.api.model.request.SavedRecipesRequest
import com.devdd.recipe.data.remote.repository.RecipeRepository
import com.devdd.recipe.domain.InvokeUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchSavedRecipes @Inject constructor(
    private val repository: RecipeRepository
) : InvokeUseCase<SavedRecipesRequest>() {
    override suspend fun doWork(params: SavedRecipesRequest) {
        return withContext(Dispatchers.IO) {
            repository.fetchSavedRecipes(params)
        }
    }
}