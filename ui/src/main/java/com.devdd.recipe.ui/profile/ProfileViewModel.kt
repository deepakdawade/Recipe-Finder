package com.devdd.recipe.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.devdd.recipe.ui.BuildConfig
import com.devdd.recipe.base.result.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {

    val isDebugMode:Boolean
    get() = BuildConfig.DEBUG
    private val mNavigation: MutableLiveData<Event<Pair<NavDirections, Boolean>>> =
        MutableLiveData()
    val navigation: LiveData<Event<Pair<NavDirections, Boolean>>>
        get() = mNavigation


    fun navigateToChangeLanguage() {
        val direction = ProfileFragmentDirections.actionToPreferenceSettingFragment()
        mNavigation.value = Event(Pair(direction, false))

    }

    fun navigateToChangeRecipePref() {
        val direction = ProfileFragmentDirections.actionToPreferenceSettingFragment(1)
        mNavigation.value = Event(Pair(direction, false))
    }

    fun navigateToDeveloperOption() {
        val direction = ProfileFragmentDirections.actionToDeveloperOptionFragment()
        mNavigation.value = Event(Pair(direction, false))
    }

}