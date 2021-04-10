package com.devdd.recipe.domain.observers

import com.devdd.recipe.data.db.toRecipeViewState
import com.devdd.recipe.data.prefs.manager.LocaleManager
import com.devdd.recipe.data.remote.repository.RecipeRepository
import com.devdd.recipe.domain.result.SubjectUseCase
import com.devdd.recipe.domain.viewstate.RecipeViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveAllRecipes @Inject constructor(
    private val repository: RecipeRepository,
    private val localeManager: LocaleManager
) : SubjectUseCase<Unit, List<RecipeViewState>>() {
    override fun createObservable(params: Unit): Flow<List<RecipeViewState>> {
        return repository.observeRecipes().map { recipes ->
            recipes.map { recipe -> recipe.toRecipeViewState(localeManager.isEnglishLocale()) }
        }
    }
}