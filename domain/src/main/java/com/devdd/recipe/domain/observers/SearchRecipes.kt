package com.devdd.recipe.domain.observers

import com.devdd.recipe.data.repository.RecipeRepository
import com.devdd.recipe.domain.SubjectUseCase
import com.devdd.recipe.domain.viewstate.toRecipeViewState
import com.devdd.recipe.domain.viewstate.RecipeViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchRecipes @Inject constructor(
    private val repository: RecipeRepository
) : SubjectUseCase<String, List<RecipeViewState>>() {
    override fun createObservable(params: String): Flow<List<RecipeViewState>> {
        return repository.searchRecipes(params).map { recipes ->
            recipes.map { recipe -> recipe.toRecipeViewState() }
        }
    }
}