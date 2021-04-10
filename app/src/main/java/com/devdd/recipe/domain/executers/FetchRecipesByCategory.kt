package com.devdd.recipe.domain.executers

import com.devdd.recipe.data.db.toRecipeViewState
import com.devdd.recipe.data.prefs.manager.LocaleManager
import com.devdd.recipe.data.remote.models.request.RecipesByCategoryRequest
import com.devdd.recipe.data.remote.repository.RecipeRepository
import com.devdd.recipe.domain.viewstate.RecipeViewState
import com.devdd.recipe.domain.result.InvokeResultUseCase
import com.devdd.recipe.utils.AppCoroutineDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchRecipesByCategory @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val repository: RecipeRepository,
    private val localeManager: LocaleManager
) : InvokeResultUseCase<RecipesByCategoryRequest, List<RecipeViewState>>() {
    override suspend fun doWork(params: RecipesByCategoryRequest): List<RecipeViewState> {
        return withContext(dispatchers.io) {
            repository.getRecipesByCategories(params).map { it.toRecipeViewState(localeManager.isEnglishLocale()) }
        }
    }
}