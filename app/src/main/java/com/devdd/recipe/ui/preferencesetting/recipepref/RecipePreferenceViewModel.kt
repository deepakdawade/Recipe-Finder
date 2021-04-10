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

    private val mNavigation: MutableLiveData<Event<Pair<NavDirections,Boolean>>> = MutableLiveData()
    val navigation: LiveData<Event<Pair<NavDirections,Boolean>>>
        get() = mNavigation

    init {
        loadRecipeType()
    }

    private fun loadRecipeType() {
        viewModelScope.launch {
            with(recipeManager) {
                val id = when {
                    isVegetarian() -> RecipeOptionId.VEG
                    isNonVegetarian() -> RecipeOptionId.NON_VEG
                    isBothVegNonVeg() -> RecipeOptionId.BOTH_VEG_NON_VEG
                    else -> return@with
                }
                checkButtonId.postValue(id)
            }
        }
    }

    fun vegetarian() {
        checkButtonId.value = RecipeOptionId.VEG
        updateDataStore(RecipePreference.VEG)
    }

    fun nonVegetarian() {
        checkButtonId.value = RecipeOptionId.NON_VEG
        updateDataStore(RecipePreference.NON_VEG)

    }

    fun bothVegNonVeg() {
        checkButtonId.value = RecipeOptionId.BOTH_VEG_NON_VEG
        updateDataStore(RecipePreference.BOTH)
    }

    private fun updateDataStore(type: String) {
        viewModelScope.launch {
            val previouslySelected = recipeManager.isRecipeSelected()
            recipeManager.updateRecipePref(type)
            navigateToHome(previouslySelected)
        }
    }

    private fun navigateToHome(shouldPop: Boolean = false) {
        val direction = RecipePreferenceFragmentDirections.actionToHomeFragment()
        mNavigation.value = Event(Pair(direction,shouldPop))
    }

    private object RecipeOptionId {
        const val VEG = R.id.recipe_preference_fragment_option_veg
        const val NON_VEG = R.id.recipe_preference_fragment_option_non_veg
        const val BOTH_VEG_NON_VEG = R.id.recipe_preference_fragment_option_both
    }
}