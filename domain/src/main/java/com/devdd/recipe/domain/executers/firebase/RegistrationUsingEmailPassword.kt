package com.devdd.recipe.domain.executers.firebase

import com.devdd.recipe.base.result.InvokeStatus
import com.devdd.recipe.data.repository.FirebaseRepository
import com.devdd.recipe.domain.SubjectUseCase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegistrationUsingEmailPassword @Inject constructor(
    private val repository: FirebaseRepository
) : SubjectUseCase<RegistrationUsingEmailPassword.Params, InvokeStatus<Boolean>>() {
    override fun createObservable(params: Params): Flow<InvokeStatus<Boolean>> {
        GlobalScope.launch {
            repository.registerUser(emailId = params.email, password = params.password)
        }
        return repository.loginStatus
    }

    data class Params(
        val email: String,
        val password: String
    )
}