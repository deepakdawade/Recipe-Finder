package com.devdd.recipe.domain.observers

import com.devdd.recipe.data.db.toRecipeViewState
import com.devdd.recipe.data.remote.repository.RecipeRepository
import com.devdd.recipe.domain.result.SubjectUseCase
import com.devdd.recipe.domain.viewstate.RecipeViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveRecipeByPref @Inject constructor(
    private val repository: RecipeRepository
) : SubjectUseCase<ObserveRecipeByPref.Params, List<RecipeViewState>>() {
    override fun createObservable(params: Params): Flow<List<RecipeViewState>> {
        return repository.observeRecipesByPref(params.pref).map { recipes ->
            recipes.map { recipe -> recipe.toRecipeViewState(isEnglishLocale = params.isEnglish) }
        }
    }

    data class Params(
        val isEnglish: Boolean,
        val pref: String
    )
}