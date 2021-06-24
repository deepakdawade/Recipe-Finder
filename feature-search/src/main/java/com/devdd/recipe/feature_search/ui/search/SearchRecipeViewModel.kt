package com.devdd.recipe.feature_search.ui.search

import androidx.lifecycle.*
import androidx.navigation.NavDirections
import com.devdd.recipe.data.models.entity.Recipe
import com.devdd.recipe.data.preference.manager.GuestManager
import com.devdd.recipe.data.models.request.MarkRecipeFavoriteRequest
import com.devdd.recipe.domain.executers.MarkRecipeFavorite
import com.devdd.recipe.domain.observers.SearchRecipes
import com.devdd.recipe.base.result.Event
import com.devdd.recipe.base.result.InvokeStarted
import com.devdd.recipe.domain.viewstate.RecipeViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchRecipeViewModel @Inject constructor(
    private val searchRecipes: SearchRecipes,
    private val markRecipeFavorite: MarkRecipeFavorite,
    private val guestManager: GuestManager
) : ViewModel() {
    val query: MutableStateFlow<String> = MutableStateFlow("")
    val queryLiveData:LiveData<String> = query.asLiveData()

    private val mRecipes: MutableLiveData<List<RecipeViewState>> = MutableLiveData()
    val recipes: LiveData<List<RecipeViewState>>
        get() = mRecipes

    private val mNavigation: MutableLiveData<Event<NavDirections>> = MutableLiveData()
    val navigation: LiveData<Event<NavDirections>>
        get() = mNavigation

    private val mSavingRecipe: MutableLiveData<Pair<Boolean, Int>> = MutableLiveData()
    val savingRecipe: LiveData<Pair<Boolean, Int>>
        get() = mSavingRecipe

    init {
        viewModelScope.launch {
            query.debounce(300).filter { query ->
                if (query.isEmpty()) {
                    mRecipes.value = emptyList()
                    false
                } else true
            }.distinctUntilChanged().flatMapLatest {
                searchRecipes.invoke(it)
                searchRecipes.observe().catch { emit(emptyList()) }
            }.collect {
                mRecipes.postValue(it)
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

    fun navigateToRecipeDetails(recipeId: Int) {
        val navDirection = SearchRecipeFragmentDirections.actionToRecipeDetailFragment(recipeId)
        mNavigation.value = Event(navDirection)
    }
}