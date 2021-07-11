package com.devdd.recipe.domain.executers.firebase

import com.devdd.recipe.base.utils.AppCoroutineDispatchers
import com.devdd.recipe.data.repository.FirebaseRepository
import com.devdd.recipe.domain.InvokeResultUseCase
import com.devdd.recipe.domain.InvokeUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LogoutUser @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val repository: FirebaseRepository
) : InvokeUseCase<Unit>() {
    override suspend fun doWork(params: Unit) {
        return withContext(dispatchers.io) {
            repository.logout()
        }
    }
}