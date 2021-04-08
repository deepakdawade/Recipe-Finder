package com.devdd.recipe.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.devdd.recipe.data.prefs.RecipeDataStore
import com.devdd.recipe.domain.result.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val recipeDataStore: RecipeDataStore
) : ViewModel() {

    private val mNavigation: MutableLiveData<Event<NavDirections>> = MutableLiveData()
    val navigation: LiveData<Event<NavDirections>>
        get() = mNavigation

    fun decideDestination() {
        viewModelScope.launch {
            val settingSaved = recipeDataStore.settingSaved.catch {
                navigateToSetting()
            }.first()
            if (settingSaved)
                navigateToHome()
            else
                navigateToSetting()
        }
    }

    private fun navigateToSetting() {
        //TODO:("Setting fragment needs to implement")
    }

    private fun navigateToHome() {
        val direction = SplashFragmentDirections.actionToHomeFragment()
        mNavigation.value = Event(direction)
    }
}