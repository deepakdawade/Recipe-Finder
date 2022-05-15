package com.devdd.recipe.domain.executer

import com.devdd.recipe.data.remote.repository.RecipeRepository
import com.devdd.recipe.domain.InvokeUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


/**
 * Created by @author Deepak Dawade on 4/15/2021 at 10:13 PM.
 * Copyright (c) 2021 deepak.dawade.dd1@gmail.com All rights reserved.
 *
 */
class SetDeviceIdToServer @Inject constructor(
    private val repository: RecipeRepository
) : InvokeUseCase<String>() {
    override suspend fun doWork(params: String) {
        return withContext(Dispatchers.IO) {
            repository.setDeviceId(params)
        }
    }
}