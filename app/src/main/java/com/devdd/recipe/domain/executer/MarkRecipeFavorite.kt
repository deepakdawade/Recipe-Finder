package com.devdd.recipe.domain.executer

import com.devdd.recipe.api.model.request.MarkRecipeFavoriteRequest
import com.devdd.recipe.data.remote.repository.RecipeRepository
import com.devdd.recipe.domain.InvokeUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


/**
 * Created by @author Deepak Dawade on 4/16/2021 at 11:28 PM.
 * Copyright (c) 2021 deepak.dawade.dd1@gmail.com All rights reserved.
 *
 */
class MarkRecipeFavorite @Inject constructor(
    private val repository: RecipeRepository
) : InvokeUseCase<MarkRecipeFavoriteRequest>() {
    override suspend fun doWork(params: MarkRecipeFavoriteRequest) {
        return withContext(Dispatchers.IO) {
            repository.markRecipeFavorite(params)
        }
    }
}