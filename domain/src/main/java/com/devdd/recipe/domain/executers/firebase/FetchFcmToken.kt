package com.devdd.recipe.domain.executers.firebase

import com.devdd.recipe.base.utils.AppCoroutineDispatchers
import com.devdd.recipe.data.repository.FirebaseRepository
import com.devdd.recipe.domain.InvokeUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchFcmToken @Inject constructor(
    private val repository: FirebaseRepository,
    private val dispatchers: AppCoroutineDispatchers
) : InvokeUseCase<Unit>() {
    override suspend fun doWork(params: Unit) {
        withContext(dispatchers.io) {
            repository.fetchFcmToken()
        }
    }
}