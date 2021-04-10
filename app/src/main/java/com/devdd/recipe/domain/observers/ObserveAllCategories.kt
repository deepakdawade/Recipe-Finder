package com.devdd.recipe.domain.observers

import com.devdd.recipe.data.db.toCategoryViewState
import com.devdd.recipe.data.prefs.manager.LocaleManager
import com.devdd.recipe.data.remote.repository.RecipeRepository
import com.devdd.recipe.domain.result.SubjectUseCase
import com.devdd.recipe.domain.viewstate.CategoryViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveAllCategories @Inject constructor(
    private val repository: RecipeRepository,
    private val localeManager: LocaleManager
) : SubjectUseCase<Unit, List<CategoryViewState>>() {
    override fun createObservable(params: Unit): Flow<List<CategoryViewState>> {
        return repository.observeCategories().map { categories ->
            categories.map { category -> category.toCategoryViewState(localeManager.isEnglishLocale()) }
        }
    }
}