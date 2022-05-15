package com.devdd.recipe.domain.observer

import com.devdd.recipe.data.remote.repository.RecipeRepository
import com.devdd.recipe.domain.SubjectUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveSelectedLocale @Inject constructor(
    private val repository: RecipeRepository
) : SubjectUseCase<Unit, String>() {
    override fun createObservable(params: Unit): Flow<String> {
        return repository.selectedLocale
    }
}