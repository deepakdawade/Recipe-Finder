package com.devdd.recipe.domain.executers

import com.devdd.recipe.base.utils.AppCoroutineDispatchers
import com.devdd.recipe.data.repository.RecipeRepository
import com.devdd.recipe.domain.InvokeUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject


/**
 * Created by @author Deepak Dawade on 4/15/2021 at 10:13 PM.
 * Copyright (c) 2021 deepak.dawade.dd1@gmail.com All rights reserved.
 *
 */
class SetDeviceIdToServer @Inject constructor(
    private val appCoroutineDispatchers: AppCoroutineDispatchers,
    private val repository: RecipeRepository
) : InvokeUseCase<String>() {
    override suspend fun doWork(params: String) {
        return withContext(appCoroutineDispatchers.io) {
            repository.setDeviceId(params)
        }
    }
}