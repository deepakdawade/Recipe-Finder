package com.devdd.recipe.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devdd.recipe.base.result.Event
import com.devdd.recipe.base.result.InvokeStarted
import com.devdd.recipe.base.result.onError
import com.devdd.recipe.base.result.onSuccess
import com.devdd.recipe.base_android.utils.extensions.combine
import com.devdd.recipe.domain.executers.firebase.LoginViaEmail
import com.devdd.recipe.domain.executers.firebase.LoginViaGoogle
import com.devdd.recipe.utils.extensions.premitive.isEmailAddress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginViaGoogle: LoginViaGoogle,
    private val loginViaEmail: LoginViaEmail
) : ViewModel() {

    val emailField: MutableLiveData<String> = MutableLiveData()
    val passwordField: MutableLiveData<String> = MutableLiveData()

    val fieldValidator: LiveData<Boolean> = emailField.combine(passwordField) { email, password ->
        if (email.isNullOrBlank() || password.isNullOrBlank())
            false
        else {
            val validEmail = email.isEmailAddress()
            val validPassword = password.length >= 6
            validEmail && validPassword
        }
    }

    private val mError: MutableLiveData<String> = MutableLiveData()
    val error: LiveData<String> = mError

    private val mLoginSuccess: MutableLiveData<Event<Unit>> = MutableLiveData()
    val loginSuccess: LiveData<Event<Unit>> = mLoginSuccess

    private val mEmailLoginSuccess: MutableLiveData<Event<Unit>> = MutableLiveData()
    val emailLoginSuccess: LiveData<Event<Unit>> = mEmailLoginSuccess

    private val mLoading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean> = mLoading

    fun loggedIn(token: String) {
        viewModelScope.launch {
            loginViaGoogle.invoke(token)
            loginViaGoogle.observe().collect {
                mLoading.postValue(it is InvokeStarted)
                it.onSuccess { success ->
                    if (success) loginSuccess()
                }

                it.onError {
                    mError.postValue(it.localizedMessage)
                }
            }
        }
    }

    fun login() {
        val email = emailField.value.toString()
        val password = passwordField.value.toString()
        viewModelScope.launch {
            loginViaEmail.invoke(
                LoginViaEmail.Params(
                    email = email,
                    password = password
                )
            )
            loginViaEmail.observe().collect {
                mLoading.postValue(it is InvokeStarted)
                it.onSuccess { success ->
                    if (success) {
                        mEmailLoginSuccess.postValue(Event(Unit))
                        loginSuccess()
                    }
                }
                it.onError {
                    mError.postValue(it.localizedMessage)
                }
            }
        }
    }

    private fun loginSuccess() {
        mLoginSuccess.value = Event(Unit)
    }

    fun clearFields() {
        emailField.value = ""
        passwordField.value = ""
        mError.value = ""
    }
}