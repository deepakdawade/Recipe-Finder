package com.devdd.recipe.domain.observer

import com.devdd.recipe.data.remote.repository.RecipeRepository
import com.devdd.recipe.domain.InvokeUseCase
import com.devdd.recipe.domain.ResultUseCase
import com.devdd.recipe.domain.SubjectUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SetRecipePref @Inject constructor(
    private val repository: RecipeRepository
) : InvokeUseCase<String>() {
    override suspend fun doWork(params: String) {
        repository.setRecipePrefSelected(params)
    }
}