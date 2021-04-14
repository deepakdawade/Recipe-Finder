package com.devdd.recipe.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.devdd.recipe.domain.executers.FetchAllRecipes
import com.devdd.recipe.domain.executers.FetchGuestToken
import com.devdd.recipe.domain.observers.ObserveRecipeByPref
import com.devdd.recipe.domain.result.Event
import com.devdd.recipe.domain.result.InvokeStarted
import com.devdd.recipe.domain.viewstate.RecipeViewState
import com.devdd.recipe.utils.extensions.toJsonString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchGuestToken: FetchGuestToken,
    private val fetchAllRecipes: FetchAllRecipes,
    private val observeRecipeByPref: ObserveRecipeByPref
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
            fetchGuestToken.invoke(Unit).collect()
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

    private fun createObservers() {
        observeRecipeByPref.invoke(Unit)
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

    fun navigateToRecipeDetails(viewState: RecipeViewState) {
        val recipe = viewState.toJsonString()
        val navDirection = HomeFragmentDirections.actionToRecipeDetailFragment(recipe)
        mNavigation.value = Event(navDirection)
    }
}
