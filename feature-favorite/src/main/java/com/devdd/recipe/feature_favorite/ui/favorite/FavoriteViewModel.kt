package com.devdd.recipe.feature_favorite.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.devdd.recipe.base.constants.SelectedLocale.LOCALE_ENGLISH
import com.devdd.recipe.base.result.Event
import com.devdd.recipe.base.result.InvokeStarted
import com.devdd.recipe.data.models.entity.Recipe
import com.devdd.recipe.data.models.request.MarkRecipeFavoriteRequest
import com.devdd.recipe.data.models.request.SavedRecipesRequest
import com.devdd.recipe.data.preference.manager.GuestManager
import com.devdd.recipe.data.preference.manager.LocaleManager
import com.devdd.recipe.data.preference.manager.RecipeManager
import com.devdd.recipe.domain.executers.FetchSavedRecipes
import com.devdd.recipe.domain.executers.MarkRecipeFavorite
import com.devdd.recipe.domain.observers.ObserveRecipeByPref
import com.devdd.recipe.domain.viewstate.HeaderDataViewState
import com.devdd.recipe.domain.viewstate.RecipeViewState
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
    private val mRecipes: MutableLiveData<List<HeaderDataViewState>> = MutableLiveData()
    val recipes: LiveData<List<HeaderDataViewState>>
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

    fun markRecipeFavorite(recipe: Recipe) {
        viewModelScope.launch {
            markRecipeFavorite.invoke(
                MarkRecipeFavoriteRequest(
                    guestManager.guestToken(),
                    recipe.id,
                    !recipe.saved
                )
            )
                .collect {
                    mSavingRecipe.postValue(Pair(it is InvokeStarted, recipe.id))
                }
        }
    }

    private fun createObservers() {
        viewModelScope.launch {
            localeManager.selectedLanguage.combineTransform(recipeManager.recipePreference) { lang: String, pref: String ->
                emit(ObserveRecipeByPref.Params(lang == LOCALE_ENGLISH, pref))
            }.collect {
                observeRecipeByPref(it)
            }
        }
    }

    private fun observeRecipes() {
        viewModelScope.launch {
            observeRecipeByPref.observe().collect {
                val original = it.filter { recipe -> recipe.entity.saved }
                val mapped: Map<String, List<RecipeViewState>> =
                    original.groupBy { state -> state.savedDate() }
                val recipes = mutableListOf<HeaderDataViewState>()
                mapped.forEach { entry ->
                    val header = HeaderDataViewState.Header(string = entry.key)
                    val data = HeaderDataViewState.Data(entry.value)
                    recipes.add(header)
                    recipes.add(data)
                }
                mAllRecipes.clear()
                mAllRecipes.addAll(original)
                mRecipes.postValue(recipes)
            }
        }
    }

    fun navigateToRecipeDetails(recipeId: Int) {
        val navDirection = FavoriteRecipeFragmentDirections.actionToRecipeDetailFragment(recipeId)
        mNavigation.value = Event(navDirection)
    }
}
