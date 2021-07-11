package com.devdd.recipe.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devdd.recipe.base.result.Event
import com.devdd.recipe.base.result.InvokeStarted
import com.devdd.recipe.base.result.onSuccess
import com.devdd.recipe.domain.executers.firebase.GoogleLogin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val googleLogin: GoogleLogin
) : ViewModel() {

    private val mLoginSuccess: MutableLiveData<Event<Unit>> = MutableLiveData()
    val loginSuccess: LiveData<Event<Unit>> = mLoginSuccess

    private val mLoading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean> = mLoading

    fun loggedIn(token: String) {
        viewModelScope.launch {
            googleLogin.invoke(token)
            googleLogin.observe().collect {
                    mLoading.postValue(it is InvokeStarted)
                    it.onSuccess { success ->
                        if (success) loginSuccess()
                    }
                }
        }
    }

    private fun loginSuccess() {
        mLoginSuccess.value = Event(Unit)
    }
}