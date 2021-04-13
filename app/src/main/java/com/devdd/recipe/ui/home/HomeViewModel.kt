package com.devdd.recipe.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.devdd.recipe.data.prefs.manager.LocaleManager
import com.devdd.recipe.data.prefs.manager.RecipeManager
import com.devdd.recipe.domain.executers.FetchAllRecipes
import com.devdd.recipe.domain.executers.FetchGuestToken
import com.devdd.recipe.domain.observers.ObserveRecipeByPref
import com.devdd.recipe.domain.result.Event
import com.devdd.recipe.domain.result.InvokeStarted
import com.devdd.recipe.domain.viewstate.RecipeViewState
import com.devdd.recipe.utils.extensions.toJsonString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchGuestToken: FetchGuestToken,
    private val fetchAllRecipes: FetchAllRecipes,
    private val observeRecipeByPref: ObserveRecipeByPref,
    private val localeManager: LocaleManager,
    private val recipeManager: RecipeManager
) : ViewModel() {

    private val mAllRecipes = mutableListOf<RecipeViewState>()
    private val mRecipes: MutableLiveData<List<RecipeViewState>> = MutableLiveData()
    val recipes: LiveData<List<RecipeViewState>>
        get() = mRecipes

    private val mLoading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean>
        get() = mLoading

    private val mNavigation: MutableLiveData<Event<NavDirections>> = MutableLiveData()
    val navigation: LiveData<Event<NavDirections>>
        get() = mNavigation

    init {
        fetchGuestToken()
        fetchRecipes()
        createObservers()
        observeRecipes()
    }

    private fun fetchGuestToken() {
        viewModelScope.launch {
            fetchGuestToken.invoke(Unit).first()
        }
    }

    private fun observeRecipes() {
        viewModelScope.launch {
            observeRecipeByPref.observe().collect {
                mAllRecipes.clear()
                mAllRecipes.addAll(it)
                mRecipes.postValue(it)

            }
        }
    }

    private fun createObservers() {
        viewModelScope.launch {
            localeManager.selectedLanguage.combineTransform(recipeManager.recipePreference) { lang: String, pref: String ->
                emit(ObserveRecipeByPref.Params(lang == LocaleManager.LOCALE_ENGLISH, pref))
            }.collect {
                observeRecipeByPref(it)
            }
        }
    }

    fun fetchRecipes() {
        viewModelScope.launch {
            fetchAllRecipes(Unit).collect {
                if (it is InvokeStarted)
                    mLoading.postValue(true)
                else mLoading.postValue(false)
            }
        }
    }

    fun navigateToRecipeDetails(viewState: RecipeViewState) {
        val recipe = viewState.toJsonString()
        val navDirection = HomeFragmentDirections.actionToRecipeDetailFragment(recipe)
        mNavigation.value = Event(navDirection)
    }

    fun navigateToRecipes(categoryId: Int, categoryName: String) {
        val navDirection = HomeFragmentDirections.actionToRecipesFragment(categoryId, categoryName)
        mNavigation.value = Event(navDirection)
    }
}
