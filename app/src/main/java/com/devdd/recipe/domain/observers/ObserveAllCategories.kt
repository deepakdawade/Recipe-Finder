package com.devdd.recipe.domain.observers

import com.devdd.recipe.data.db.entities.Category
import com.devdd.recipe.data.remote.repository.RecipeRepository
import com.devdd.recipe.domain.result.SubjectUseCase
import com.devdd.recipe.ui.home.viewstate.CategoryViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveAllCategories @Inject constructor(
    private val repository: RecipeRepository
) : SubjectUseCase<Unit, List<CategoryViewState>>() {
    override fun createObservable(params: Unit): Flow<List<CategoryViewState>> {
        return repository.observeCategories().map { categories ->
            categories.map { category -> category.toCategoryViewState() }
        }
    }

    private fun Category.toCategoryViewState(): CategoryViewState {
        return CategoryViewState(
            id = id,
            description = description,
            name = name,
            imageUrl = imageUrl
        )
    }
}