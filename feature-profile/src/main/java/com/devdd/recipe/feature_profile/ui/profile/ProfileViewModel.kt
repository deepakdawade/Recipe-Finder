package com.devdd.recipe.feature_profile.ui.profile

import androidx.lifecycle.*
import androidx.navigation.NavDirections
import com.devdd.recipe.base.result.Event
import com.devdd.recipe.data.models.response.UserInfo
import com.devdd.recipe.data.preference.manager.GuestManager
import com.devdd.recipe.domain.executers.firebase.LogoutUser
import com.devdd.recipe.feature_profile.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val guestManager: GuestManager,
    val logoutUser: LogoutUser
) : ViewModel() {

    val isDebugMode: Boolean
        get() = BuildConfig.DEBUG

    val isLoggedIn: LiveData<Boolean> = guestManager.userLoggedIn.asLiveData()
    val userInfo: LiveData<UserInfo?> = guestManager.userInfo.asLiveData()

    private val mNavigation: MutableLiveData<Event<Pair<NavDirections?, Boolean>>> =
        MutableLiveData()
    val navigation: LiveData<Event<Pair<NavDirections?, Boolean>>>
        get() = mNavigation


    fun navigateToLogin() {
        val directions = ProfileFragmentDirections.actionToLoginFragment()
        navigate(directions = directions)
    }

    fun navigateToChangeLanguage() {
        val directions = ProfileFragmentDirections.actionToPreferenceSettingFragment()
        navigate(directions = directions)
    }

    fun navigateToChangeRecipePref() {
        val directions = ProfileFragmentDirections.actionToPreferenceSettingFragment(1)
        navigate(directions = directions)
    }


    fun navigateToAppInfo() {
        val directions = ProfileFragmentDirections.actionToAppInfoFragment()
        navigate(directions = directions)
    }

    fun navigateToDeveloperOption() {
        val directions = ProfileFragmentDirections.actionToDeveloperOptionFragment()
        navigate(directions = directions)
    }

    fun logout() {
        viewModelScope.launch {
            logoutUser.invoke(Unit).collect()
        }
    }

    private fun navigate(directions: NavDirections? = null, pop: Boolean = false) {
        mNavigation.value = Event(Pair(directions, pop))
    }

}