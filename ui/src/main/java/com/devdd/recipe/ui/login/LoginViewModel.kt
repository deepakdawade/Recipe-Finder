package com.devdd.recipe.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
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

    private val mNavigator: MutableLiveData<Event<NavDirections>> = MutableLiveData()
    val navigator: LiveData<Event<NavDirections>> = mNavigator

    private val mLoading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean> = mLoading

    fun loggedIn(token: String) {
        viewModelScope.launch {
            googleLogin.invoke(token)
            googleLogin.observe().collect {
                    mLoading.postValue(it is InvokeStarted)
                    it.onSuccess { success ->
                        if (success) navigateToDashboard()
                    }
                }
        }
    }

    private fun navigateToDashboard() {
        val directions = LoginFragmentDirections.actionToDashboardFragment()
        navigate(directions)
    }

    fun navigate(directions: NavDirections) {
        mNavigator.value = Event(directions)
    }
}