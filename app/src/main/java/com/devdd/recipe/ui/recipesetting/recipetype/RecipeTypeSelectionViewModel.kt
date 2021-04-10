package com.devdd.recipe.ui.recipesetting.recipetype

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.devdd.recipe.R
import com.devdd.recipe.constants.Vegetarian
import com.devdd.recipe.data.prefs.RecipeDataStore
import com.devdd.recipe.domain.result.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RecipeTypeSelectionViewModel @Inject constructor(
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
            dataStore.vegetarianType.catch {
                Timber.e("error while reading recipeType $this")
            }.collect {
                val id = when (it) {
                    Vegetarian.VEG -> OptionId.VEG
                    Vegetarian.NON_VEG -> OptionId.NON_VEG
                    Vegetarian.BOTH -> OptionId.BOTH_VEG_NON_VEG
                    else -> return@collect
                }
                checkButtonId.postValue(id)
            }
        }
    }

    fun vegetarian() {
        checkButtonId.value = OptionId.VEG
        updateDataStore(Vegetarian.VEG)
    }

    fun nonVegetarian() {
        checkButtonId.value = OptionId.NON_VEG
        updateDataStore(Vegetarian.NON_VEG)

    }

    fun bothVegNonVeg() {
        checkButtonId.value = OptionId.BOTH_VEG_NON_VEG
        updateDataStore(Vegetarian.BOTH)
    }

    private fun updateDataStore(type: String) {
        viewModelScope.launch {
            dataStore.setVegetarianType(type)
            navigateToHome()
        }
    }

    private fun navigateToHome() {
        val direction = RecipeTypeSelectionFragmentDirections.actionToHomeFragment()
        mNavigation.value = Event(direction)
    }

    private object OptionId {
        const val VEG = R.id.recipe_type_selection_fragment_option_veg
        const val NON_VEG = R.id.recipe_type_selection_fragment_option_non_veg
        const val BOTH_VEG_NON_VEG = R.id.recipe_type_selection_fragment_option_both
    }
}