package com.devdd.recipe.domain.observers

import com.devdd.recipe.data.remote.repository.RecipeRepository
import com.devdd.recipe.domain.result.SuspendSubjectUseCase
import com.devdd.recipe.domain.viewstate.RecipeViewState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveRecipeByPref @Inject constructor(
    private val repository: RecipeRepository
) : SuspendSubjectUseCase<Unit, List<RecipeViewState>>() {
    override suspend fun createObservable(params: Unit): Flow<List<RecipeViewState>> {
        return repository.observeRecipesByPref()
    }
}