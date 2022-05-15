package com.devdd.recipe.domain.observer

import com.devdd.recipe.data.local.pref.manager.LocaleManager
import com.devdd.recipe.data.remote.repository.RecipeRepository
import com.devdd.recipe.domain.SubjectUseCase
import com.devdd.recipe.ui.RecipeViewState
import com.devdd.recipe.utils.AppLocale
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class ObserveRecipeByPref @Inject constructor(
    private val repository: RecipeRepository,
) : SubjectUseCase<String, List<RecipeViewState>>() {
    override fun createObservable(params: String): Flow<List<RecipeViewState>> {
        return repository.observeRecipeByPref(params)
    }
}