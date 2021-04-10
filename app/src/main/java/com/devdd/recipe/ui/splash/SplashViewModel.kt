package com.devdd.recipe.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.devdd.recipe.data.prefs.manager.RecipeManager
import com.devdd.recipe.domain.result.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val recipeManager: RecipeManager
) : ViewModel() {

    private val mNavigation: MutableLiveData<Event<NavDirections>> = MutableLiveData()
    val navigation: LiveData<Event<NavDirections>>
        get() = mNavigation

    fun decideDestination() {
        viewModelScope.launch {
            if (recipeManager.isRecipeSelected())
                navigateToHome()
            else
                navigateToRecipePreference()
        }
    }

    private fun navigateToRecipePreference() {
        val direction = SplashFragmentDirections.actionToRecipePreferenceFragment()
        mNavigation.value = Event(direction)
    }

    private fun navigateToHome() {
        val direction = SplashFragmentDirections.actionToHomeFragment()
        mNavigation.value = Event(direction)
    }
}