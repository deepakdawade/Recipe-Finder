package com.devdd.recipe.ui.categoryrecipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.devdd.recipe.data.remote.models.request.RecipesByCategoryRequest
import com.devdd.recipe.domain.viewstate.RecipeViewState
import com.devdd.recipe.domain.executers.FetchRecipesByCategory
import com.devdd.recipe.domain.result.Event
import com.devdd.recipe.domain.result.onError
import com.devdd.recipe.domain.result.onStarted
import com.devdd.recipe.domain.result.onSuccess
import com.devdd.recipe.utils.extensions.toJsonString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val recipesByCategory: FetchRecipesByCategory
) : ViewModel() {

    private val mRecipes: MutableLiveData<List<RecipeViewState>> = MutableLiveData()
    val recipes: LiveData<List<RecipeViewState>>
        get() = mRecipes

    private val mLoading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean>
        get() = mLoading

    private val mNavigation: MutableLiveData<Event<NavDirections>> = MutableLiveData()
    val navigation: LiveData<Event<NavDirections>>
        get() = mNavigation

    fun fetchRecipes(request: RecipesByCategoryRequest) {
        viewModelScope.launch {
            recipesByCategory.invoke(request).collect {
                it.onStarted {
                    mLoading.postValue(true)
                }.onSuccess { recipes ->
                    mLoading.postValue(false)
                    mRecipes.postValue(recipes)
                }.onError {
                    mLoading.postValue(false)
                }
            }
        }
    }

    fun navigateToRecipeDetails(viewState: RecipeViewState) {
        val recipe = viewState.toJsonString()
        val navDirection = RecipesFragmentDirections.actionToRecipeDetailFragment(recipe)
        mNavigation.value = Event(navDirection)
    }

}