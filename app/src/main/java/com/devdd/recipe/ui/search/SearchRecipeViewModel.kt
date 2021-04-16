package com.devdd.recipe.ui.search

import androidx.lifecycle.*
import androidx.navigation.NavDirections
import com.devdd.recipe.data.prefs.manager.GuestManager
import com.devdd.recipe.data.remote.models.request.MarkRecipeFavoriteRequest
import com.devdd.recipe.domain.executers.MarkRecipeFavorite
import com.devdd.recipe.domain.observers.SearchRecipes
import com.devdd.recipe.domain.result.Event
import com.devdd.recipe.domain.result.InvokeSuccess
import com.devdd.recipe.domain.viewstate.RecipeViewState
import com.devdd.recipe.utils.extensions.toJsonString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchRecipeViewModel @Inject constructor(
    private val searchRecipes: SearchRecipes,
    private val markRecipeFavorite: MarkRecipeFavorite,
    private val guestManager: GuestManager
) : ViewModel() {
    val query: MutableLiveData<String> = MutableLiveData()

    private val mRecipes: MutableLiveData<List<RecipeViewState>> = MutableLiveData()
    val recipes: LiveData<List<RecipeViewState>>
        get() = mRecipes

    private val mNavigation: MutableLiveData<Event<NavDirections>> = MutableLiveData()
    val navigation: LiveData<Event<NavDirections>>
        get() = mNavigation

    private val mSavingRecipe: MutableLiveData<Pair<Boolean, Int>> = MutableLiveData()
    val savingRecipe: LiveData<Pair<Boolean, Int>>
        get() = mSavingRecipe

    fun searchRecipes(query: String?) {
        if (query.isNullOrBlank())
            mRecipes.value = emptyList()
        else {
            viewModelScope.launch {
                searchRecipes.invoke(query)
                searchRecipes.observe().collect { recipes ->
                    mRecipes.value = recipes
                }
            }
        }
    }

    fun markRecipeFavorite(recipe:RecipeViewState) {
        viewModelScope.launch {
            markRecipeFavorite.invoke(MarkRecipeFavoriteRequest(guestManager.deviceId(), recipe.id,recipe.saved))
                .collect {
                    mSavingRecipe.postValue(Pair(it is InvokeSuccess, recipe.id))
                }
        }
    }

    fun navigateToRecipeDetails(viewState: RecipeViewState) {
        val navDirection = SearchRecipeFragmentDirections.actionToRecipeDetailFragment(viewState.id)
        mNavigation.value = Event(navDirection)
    }
}