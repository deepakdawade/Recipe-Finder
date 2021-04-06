package com.devdd.recipe.domain.executers

import com.devdd.recipe.data.remote.repository.RecipeRepository
import com.devdd.recipe.domain.result.InvokeUseCase
import com.devdd.recipe.utils.AppCoroutineDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchAllRecipes @Inject constructor(
    private val appCoroutineDispatchers: AppCoroutineDispatchers,
    private val repository: RecipeRepository
) : InvokeUseCase<Unit>() {
    override suspend fun doWork(params: Unit) {
        return withContext(appCoroutineDispatchers.io) {
            repository.fetchAllRecipes()
        }
    }
}