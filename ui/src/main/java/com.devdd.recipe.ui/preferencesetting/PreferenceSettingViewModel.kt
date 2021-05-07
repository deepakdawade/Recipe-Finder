package com.devdd.recipe.ui.preferencesetting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.devdd.recipe.base.constants.SelectedLocale.LOCALE_ENGLISH
import com.devdd.recipe.base.constants.SelectedLocale.LOCALE_HINDI
import com.devdd.recipe.base.constants.SelectedRecipePref.BOTH
import com.devdd.recipe.base.constants.SelectedRecipePref.NON_VEG
import com.devdd.recipe.base.constants.SelectedRecipePref.VEG
import com.devdd.recipe.base.result.Event
import com.devdd.recipe.data.preference.manager.LocaleManager
import com.devdd.recipe.data.preference.manager.RecipeManager
import com.devdd.recipe.ui.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferenceSettingViewModel @Inject constructor(
    private val localeManager: LocaleManager,
    private val recipeManager: RecipeManager
) : ViewModel() {
    val checkedRecipeButtonId: MutableLiveData<Int> = MutableLiveData()
    val checkedLanguageButtonId: MutableLiveData<Int> = MutableLiveData()

    private val mNavigation: MutableLiveData<Event<Triple<Boolean, Boolean, NavDirections>>> =
        MutableLiveData()
    val navigation: LiveData<Event<Triple<Boolean, Boolean, NavDirections>>>
        get() = mNavigation

    private val mPage: MutableLiveData<Event<Int>> = MutableLiveData()
    val page: LiveData<Event<Int>>
        get() = mPage

    init {

        loadLanguagePreference()
        loadRecipePreference()
    }

    fun setPage(page: Int) {
        mPage.value = Event(page)
    }

    private fun loadLanguagePreference() {
        viewModelScope.launch {
            with(localeManager) {
                val id = when {
                    isEnglishLocale() -> LanguageOptionId.ENGLISH
                    isHindiLocale() -> LanguageOptionId.HINDI
                    else -> return@with
                }
                checkedLanguageButtonId.postValue(id)
            }
        }
    }


    private fun loadRecipePreference() {
        viewModelScope.launch {
            with(recipeManager) {
                val id = when {
                    isVegetarian() -> RecipeOptionId.VEG
                    isNonVegetarian() -> RecipeOptionId.NON_VEG
                    isBothVegNonVeg() -> RecipeOptionId.BOTH_VEG_NON_VEG
                    else -> return@with
                }
                checkedRecipeButtonId.postValue(id)
            }
        }
    }

    fun english() {
        checkedLanguageButtonId.value = LanguageOptionId.ENGLISH
        updateLanguagePref(LOCALE_ENGLISH)
    }

    fun hindi() {
        checkedLanguageButtonId.value = LanguageOptionId.HINDI
        updateLanguagePref(LOCALE_HINDI)
    }

    fun vegetarian() {
        checkedRecipeButtonId.value = RecipeOptionId.VEG
        updateRecipePref(VEG)
    }

    fun nonVegetarian() {
        checkedRecipeButtonId.value = RecipeOptionId.NON_VEG
        updateRecipePref(NON_VEG)

    }

    fun bothVegNonVeg() {
        checkedRecipeButtonId.value = RecipeOptionId.BOTH_VEG_NON_VEG
        updateRecipePref(BOTH)
    }

    private fun updateLanguagePref(language: String) {
        viewModelScope.launch {
            val previouslySelected = localeManager.isLanguageSelected()
            val newSelection =
                localeManager.isEnglishLocale() && language == LOCALE_HINDI ||
                        localeManager.isHindiLocale() && language == LOCALE_ENGLISH
            val updateLanguage = !previouslySelected || newSelection
            if (updateLanguage) localeManager.updateLanguage(language)
            if (recipeManager.isRecipeSelected() && updateLanguage) {
                navigateToDashboard(shouldRestart = true)
                navigateToDashboard(shouldPop = true)
            } else {
                setPage(1)
                navigateToDashboard(shouldRestart = true)
            }
        }
    }

    private fun updateRecipePref(type: String) {
        viewModelScope.launch {
            val previouslySelected = recipeManager.isRecipeSelected()
            recipeManager.updateRecipePref(type)
            navigateToDashboard(shouldPop = previouslySelected)
        }
    }

    private fun navigateToDashboard(shouldRestart: Boolean = false, shouldPop: Boolean = false) {
        val direction = PreferenceSettingFragmentDirections.actionToDashboardFragment()
        mNavigation.value = Event(Triple(shouldPop, shouldRestart, direction))
    }

    private object RecipeOptionId {
        val VEG = R.id.recipe_preference_fragment_option_veg
        val NON_VEG = R.id.recipe_preference_fragment_option_non_veg
        val BOTH_VEG_NON_VEG = R.id.recipe_preference_fragment_option_both
    }

    private object LanguageOptionId {
        val ENGLISH = R.id.language_preference_fragment_option_english
        val HINDI = R.id.language_preference_fragment_option_hindi
    }
}
