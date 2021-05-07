package com.devdd.recipe.domain.executers

import com.devdd.recipe.base.utils.AppCoroutineDispatchers
import com.devdd.recipe.data.models.request.MarkRecipeFavoriteRequest
import com.devdd.recipe.data.repository.RecipeRepository
import com.devdd.recipe.domain.InvokeUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject


/**
 * Created by @author Deepak Dawade on 4/16/2021 at 11:28 PM.
 * Copyright (c) 2021 deepak.dawade.dd1@gmail.com All rights reserved.
 *
 */
class MarkRecipeFavorite @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val repository: RecipeRepository
) : InvokeUseCase<MarkRecipeFavoriteRequest>() {
    override suspend fun doWork(params: MarkRecipeFavoriteRequest) {
        return withContext(dispatchers.io) {
            repository.markRecipeFavorite(params)
        }
    }
}