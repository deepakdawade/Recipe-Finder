package com.devdd.recipe.domain.observers

import com.devdd.recipe.data.preference.manager.LocaleManager
import com.devdd.recipe.data.repository.RecipeRepository
import com.devdd.recipe.domain.SubjectUseCase
import com.devdd.recipe.domain.mappers.toRecipeViewState
import com.devdd.recipe.domain.viewstate.RecipeViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


/**
 * Created by @author Deepak Dawade on 4/17/2021 at 12:41 AM.
 * Copyright (c) 2021 deepak.dawade.dd1@gmail.com All rights reserved.
 *
 */
class GetRecipeById @Inject constructor(
    private val repository: RecipeRepository,
    private val localeManager: LocaleManager
) : SubjectUseCase<Int, RecipeViewState>() {
    override fun createObservable(params: Int): Flow<RecipeViewState> {
        return repository.getRecipeById(params).map {
            it.toRecipeViewState()
        }
    }
}