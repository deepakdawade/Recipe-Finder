package com.devdd.recipe.ui.preferencesetting.recipepref

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.devdd.recipe.R
import com.devdd.recipe.constants.RecipePreference
import com.devdd.recipe.data.prefs.manager.RecipeManager
import com.devdd.recipe.domain.result.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipePreferenceViewModel @Inject constructor(
    private val recipeManager: RecipeManager
) : ViewModel() {
    val checkButtonId: MutableLiveData<Int> = MutableLiveData()

    private val mNavigation: MutableLiveData<Event<NavDirections>> = MutableLiveData()
    val navigation: LiveData<Event<NavDirections>>
        get() = mNavigation

    init {
        loadRecipeType()
    }

    private fun loadRecipeType() {
        viewModelScope.launch {
            with(recipeManager) {
                val id = when {
                    isVegetarian() -> OptionId.VEG
                    isNonVegetarian() -> OptionId.NON_VEG
                    isBothVegNonVeg() -> OptionId.BOTH_VEG_NON_VEG
                    else -> return@with
                }
                checkButtonId.postValue(id)
            }
        }
    }

    fun vegetarian() {
        checkButtonId.value = OptionId.VEG
        updateDataStore(RecipePreference.VEG)
    }

    fun nonVegetarian() {
        checkButtonId.value = OptionId.NON_VEG
        updateDataStore(RecipePreference.NON_VEG)

    }

    fun bothVegNonVeg() {
        checkButtonId.value = OptionId.BOTH_VEG_NON_VEG
        updateDataStore(RecipePreference.BOTH)
    }

    private fun updateDataStore(type: String) {
        viewModelScope.launch {
            recipeManager.updateRecipePref(type)
            navigateToHome()
        }
    }

    private fun navigateToHome() {
        val direction = RecipePreferenceFragmentDirections.actionToHomeFragment()
        mNavigation.value = Event(direction)
    }

    private object OptionId {
        const val VEG = R.id.recipe_preference_fragment_option_veg
        const val NON_VEG = R.id.recipe_preference_fragment_option_non_veg
        const val BOTH_VEG_NON_VEG = R.id.recipe_preference_fragment_option_both
    }
}