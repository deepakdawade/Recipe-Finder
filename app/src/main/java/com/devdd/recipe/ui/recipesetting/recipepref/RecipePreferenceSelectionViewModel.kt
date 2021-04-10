package com.devdd.recipe.ui.recipesetting.recipepref

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.devdd.recipe.R
import com.devdd.recipe.constants.RecipePreference
import com.devdd.recipe.data.prefs.RecipeDataStore
import com.devdd.recipe.domain.result.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RecipePreferenceSelectionViewModel @Inject constructor(
    private val dataStore: RecipeDataStore
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
            dataStore.recipePreference.catch {
                Timber.e("error while reading recipe preference $this")
            }.collect {
                val id = when (it) {
                    RecipePreference.VEG -> OptionId.VEG
                    RecipePreference.NON_VEG -> OptionId.NON_VEG
                    RecipePreference.BOTH -> OptionId.BOTH_VEG_NON_VEG
                    else -> return@collect
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
            dataStore.setRecipePreference(type)
            navigateToHome()
        }
    }

    private fun navigateToHome() {
        val direction = RecipePreferenceSelectionFragmentDirections.actionToHomeFragment()
        mNavigation.value = Event(direction)
    }

    private object OptionId {
        const val VEG = R.id.recipe_preference_selection_fragment_option_veg
        const val NON_VEG = R.id.recipe_preference_selection_fragment_option_non_veg
        const val BOTH_VEG_NON_VEG = R.id.recipe_preference_selection_fragment_option_both
    }
}