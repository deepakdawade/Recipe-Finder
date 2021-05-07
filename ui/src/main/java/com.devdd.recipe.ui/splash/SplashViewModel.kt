package com.devdd.recipe.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.devdd.recipe.data.preference.manager.LocaleManager
import com.devdd.recipe.data.preference.manager.RecipeManager
import com.devdd.recipe.base.result.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val recipeManager: RecipeManager,
    private val localeManager: LocaleManager
) : ViewModel() {

    private val mNavigation: MutableLiveData<Event<NavDirections>> = MutableLiveData()
    val navigation: LiveData<Event<NavDirections>>
        get() = mNavigation

    fun decideDestination() {
        viewModelScope.launch {
            when {
                !localeManager.isLanguageSelected() -> navigateToPreferenceSetting()
                !recipeManager.isRecipeSelected() -> navigateToPreferenceSetting(1)
                else -> navigateToDashboard()
            }
        }
    }

    private fun navigateToPreferenceSetting(page:Int = 0) {
        val direction = SplashFragmentDirections.actionToPreferenceSettingFragment(page)
        mNavigation.value = Event(direction)
    }

    private fun navigateToDashboard() {
        val direction = SplashFragmentDirections.actionToDashboardFragment()
        mNavigation.value = Event(direction)
    }
}