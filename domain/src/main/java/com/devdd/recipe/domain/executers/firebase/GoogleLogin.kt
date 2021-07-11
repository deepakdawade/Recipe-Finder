package com.devdd.recipe.domain.executers.firebase

import com.devdd.recipe.base.result.InvokeStatus
import com.devdd.recipe.base.utils.AppCoroutineDispatchers
import com.devdd.recipe.data.repository.FirebaseRepository
import com.devdd.recipe.domain.InvokeResultUseCase
import com.devdd.recipe.domain.SubjectUseCase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GoogleLogin @Inject constructor(
    private val repository: FirebaseRepository
) : SubjectUseCase<String, InvokeStatus<Boolean>>() {
    override fun createObservable(params: String): Flow<InvokeStatus<Boolean>> {
        GlobalScope.launch {
            repository.loggedIn(params)
        }
        return repository.loginStatus
    }
}