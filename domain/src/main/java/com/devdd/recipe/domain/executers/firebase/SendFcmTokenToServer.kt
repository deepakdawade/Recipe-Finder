package com.devdd.recipe.domain.executers.firebase

import com.devdd.recipe.base.utils.AppCoroutineDispatchers
import com.devdd.recipe.data.repository.FirebaseRepository
import com.devdd.recipe.domain.InvokeUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SendFcmTokenToServer @Inject constructor(
    private val repository: FirebaseRepository,
    private val dispatchers: AppCoroutineDispatchers
) : InvokeUseCase<String>() {
    override suspend fun doWork(params: String) {
        withContext(dispatchers.io) {
            repository.sendTokenToServer(params)
        }
    }
}