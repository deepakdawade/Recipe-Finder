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

class LoginViaEmail @Inject constructor(
    private val repository: FirebaseRepository
) : SubjectUseCase<LoginViaEmail.Params, InvokeStatus<Boolean>>() {
    override fun createObservable(params: Params): Flow<InvokeStatus<Boolean>> {
        GlobalScope.launch {
            repository.login(emailId = params.email, password = params.password)
        }
        return repository.loginStatus
    }

    data class Params(
        val email: String,
        val password: String
    )
}