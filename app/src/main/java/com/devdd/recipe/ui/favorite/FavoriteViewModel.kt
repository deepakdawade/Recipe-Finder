package com.devdd.recipe.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.devdd.recipe.data.prefs.manager.GuestManager
import com.devdd.recipe.data.prefs.manager.LocaleManager
import com.devdd.recipe.data.prefs.manager.RecipeManager
import com.devdd.recipe.data.remote.models.request.MarkRecipeFavoriteRequest
import com.devdd.recipe.data.remote.models.request.SavedRecipesRequest
import com.devdd.recipe.domain.executers.FetchSavedRecipes
import com.devdd.recipe.domain.executers.MarkRecipeFavorite
import com.devdd.recipe.domain.observers.ObserveRecipeByPref
import com.devdd.recipe.domain.result.Event
import com.devdd.recipe.domain.result.InvokeStarted
import com.devdd.recipe.domain.result.InvokeSuccess
import com.devdd.recipe.domain.viewstate.RecipeViewState
import com.devdd.recipe.ui.home.HomeFragmentDirections
import com.devdd.recipe.utils.extensions.toJsonString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val guestManager: GuestManager,
    private val localeManager: LocaleManager,
    private val recipeManager: RecipeManager,
    private val markRecipeFavorite: MarkRecipeFavorite,
    private val fetchSavedRecipes: FetchSavedRecipes,
    private val observeRecipeByPref: ObserveRecipeByPref
) : ViewModel() {

    private val mAllRecipes = mutableListOf<RecipeViewState>()
    private val mRecipes: MutableLiveData<List<RecipeViewState>> = MutableLiveData()
    val recipes: LiveData<List<RecipeViewState>>
        get() = mRecipes

    private val mLoading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean>
        get() = mLoading

    private val mSavingRecipe: MutableLiveData<Pair<Boolean, Int>> = MutableLiveData()
    val savingRecipe: LiveData<Pair<Boolean, Int>>
        get() = mSavingRecipe

    private val mNavigation: MutableLiveData<Event<NavDirections>> = MutableLiveData()
    val navigation: LiveData<Event<NavDirections>>
        get() = mNavigation

    init {
        fetchSavedRecipes()
        createObservers()
        observeRecipes()

    }

    private fun fetchSavedRecipes() {
        viewModelScope.launch {
            guestManager.guestToken.collect { token ->
                fetchSavedRecipes(SavedRecipesRequest(token)).collect { status ->
                    if (status is InvokeStarted)
                        mLoading.postValue(true)
                    else mLoading.postValue(false)
                }

            }
        }
    }

    fun markRecipeFavorite(recipe: RecipeViewState) {
        viewModelScope.launch {
            markRecipeFavorite.invoke(
                MarkRecipeFavoriteRequest(
                    guestManager.guestToken(),
                    recipe.id,
                    recipe.saved
                )
            )
                .collect {
                    mSavingRecipe.postValue(Pair(it is InvokeSuccess, recipe.id))
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

    private fun observeRecipes() {
        viewModelScope.launch {
            observeRecipeByPref.observe().collect {
                val recipes = it.filter { state -> state.saved }
                mAllRecipes.clear()
                mAllRecipes.addAll(recipes)
                mRecipes.postValue(recipes)

            }
        }
    }

    fun navigateToRecipeDetails(viewState: RecipeViewState) {
        val navDirection = HomeFragmentDirections.actionToRecipeDetailFragment(viewState.id)
        mNavigation.value = Event(navDirection)
    }
}
