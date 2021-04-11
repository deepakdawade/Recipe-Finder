package com.devdd.recipe.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.devdd.recipe.domain.result.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {

    private val mNavigation: MutableLiveData<Event<Pair<NavDirections, Boolean>>> =
        MutableLiveData()
    val navigation: LiveData<Event<Pair<NavDirections, Boolean>>>
        get() = mNavigation


    fun navigateToChangeLanguage() {
        val direction = ProfileFragmentDirections.actionToPreferenceSettingFragment()
        mNavigation.value = Event(Pair(direction, false))

    }

    fun navigateToChangeRecipePref() {
        val direction = ProfileFragmentDirections.actionToPreferenceSettingFragment()
        mNavigation.value = Event(Pair(direction, false))
    }

}