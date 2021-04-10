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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
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
            recipeDataStore.vegetarianType.catch {
                Timber.e("error while reading recipeType $this")
                navigateToRecipeTypeSelection()
            }.collect {
                if (it.isBlank())
                    navigateToRecipeTypeSelection()
                else
                    navigateToHome()
            }
        }
    }

    private fun navigateToRecipeTypeSelection() {
        val direction = SplashFragmentDirections.actionToRecipeTypeSelectionFragment()
        mNavigation.value = Event(direction)
    }

    private fun navigateToHome() {
        val direction = SplashFragmentDirections.actionToHomeFragment()
        mNavigation.value = Event(direction)
    }
}