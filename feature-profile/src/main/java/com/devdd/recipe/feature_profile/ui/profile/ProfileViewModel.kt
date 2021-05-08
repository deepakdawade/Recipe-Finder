package com.devdd.recipe.feature_profile.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.devdd.recipe.base.result.Event
import com.devdd.recipe.feature_profile.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {

    val isDebugMode: Boolean
        get() = BuildConfig.DEBUG
    private val mNavigation: MutableLiveData<Event<Pair<NavDirections?, Boolean>>> =
        MutableLiveData()
    val navigation: LiveData<Event<Pair<NavDirections?, Boolean>>>
        get() = mNavigation


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

    private fun navigate(directions: NavDirections? = null,pop:Boolean = false){

        mNavigation.value = Event(Pair(directions, pop))
    }

}