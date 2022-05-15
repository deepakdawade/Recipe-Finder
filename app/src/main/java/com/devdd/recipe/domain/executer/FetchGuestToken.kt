package com.devdd.recipe.domain.executer

import com.devdd.recipe.data.remote.repository.RecipeRepository
import com.devdd.recipe.domain.ResultUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class FetchGuestToken @Inject constructor(
    private val repository: RecipeRepository
) : ResultUseCase<Unit, String>() {
    override suspend fun doWork(params: Unit): String {
        return withContext(Dispatchers.IO) {
            repository.getGuestToken()
        }
    }
}