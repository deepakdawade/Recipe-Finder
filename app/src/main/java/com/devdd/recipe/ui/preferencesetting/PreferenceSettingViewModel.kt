package com.devdd.recipe.ui.preferencesetting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.devdd.recipe.R
import com.devdd.recipe.constants.RecipePreference
import com.devdd.recipe.data.prefs.manager.LocaleManager
import com.devdd.recipe.data.prefs.manager.RecipeManager
import com.devdd.recipe.domain.result.Event
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

    private val mNavigation: MutableLiveData<Event<Pair<NavDirections, Boolean>>> =
        MutableLiveData()
    val navigation: LiveData<Event<Pair<NavDirections, Boolean>>>
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
        updateLanguagePref(LocaleManager.LOCALE_ENGLISH)
    }

    fun hindi() {
        checkedLanguageButtonId.value = LanguageOptionId.HINDI
        updateLanguagePref(LocaleManager.LOCALE_HINDI)
    }

    fun vegetarian() {
        checkedRecipeButtonId.value = RecipeOptionId.VEG
        updateRecipePref(RecipePreference.VEG)
    }

    fun nonVegetarian() {
        checkedRecipeButtonId.value = RecipeOptionId.NON_VEG
        updateRecipePref(RecipePreference.NON_VEG)

    }

    fun bothVegNonVeg() {
        checkedRecipeButtonId.value = RecipeOptionId.BOTH_VEG_NON_VEG
        updateRecipePref(RecipePreference.BOTH)
    }

    private fun updateLanguagePref(language: String) {
        viewModelScope.launch {
            val previousSelected = localeManager.isLanguageSelected()
            localeManager.updateLanguage(language)
            if (recipeManager.isRecipeSelected())
                navigateToHome(previousSelected)
            else setPage(1)
        }
    }

    private fun updateRecipePref(type: String) {
        viewModelScope.launch {
            val previouslySelected = recipeManager.isRecipeSelected()
            recipeManager.updateRecipePref(type)
            navigateToHome(previouslySelected)
        }
    }

    private fun navigateToHome(shouldPop: Boolean = false) {
        val direction = PreferenceSettingFragmentDirections.actionToHomeFragment()
        mNavigation.value = Event(Pair(direction, shouldPop))
    }

    private object RecipeOptionId {
        const val VEG = R.id.recipe_preference_fragment_option_veg
        const val NON_VEG = R.id.recipe_preference_fragment_option_non_veg
        const val BOTH_VEG_NON_VEG = R.id.recipe_preference_fragment_option_both
    }

    private object LanguageOptionId {
        const val ENGLISH = R.id.language_preference_fragment_option_english
        const val HINDI = R.id.language_preference_fragment_option_hindi
    }
}
