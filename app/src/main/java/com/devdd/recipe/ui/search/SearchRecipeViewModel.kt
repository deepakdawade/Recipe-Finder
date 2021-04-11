package com.devdd.recipe.ui.search

import androidx.lifecycle.*
import androidx.navigation.NavDirections
import com.devdd.recipe.domain.observers.SearchRecipes
import com.devdd.recipe.domain.result.Event
import com.devdd.recipe.domain.viewstate.RecipeViewState
import com.devdd.recipe.utils.extensions.toJsonString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchRecipeViewModel @Inject constructor(
    private val searchRecipes: SearchRecipes
) : ViewModel() {
    val query: MutableLiveData<String> = MutableLiveData()
    val canClearInput = Transformations.map(query) {
        searchRecipes(it)
        it.isNotEmpty()
    }

    private val mRecipes: MutableLiveData<List<RecipeViewState>> = MutableLiveData()
    val recipes: LiveData<List<RecipeViewState>>
        get() = mRecipes

    private val mNavigation: MutableLiveData<Event<NavDirections>> = MutableLiveData()
    val navigation: LiveData<Event<NavDirections>>
        get() = mNavigation

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

    fun clearField() {
        query.value = ""
    }

    fun navigateToRecipeDetails(viewState: RecipeViewState) {
        val recipe = viewState.toJsonString()
        val navDirection = SearchRecipeFragmentDirections.actionToRecipeDetailFragment(recipe)
        mNavigation.value = Event(navDirection)
    }
}