package com.devdd.recipe.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devdd.recipe.data.db.entities.Recipe
import com.devdd.recipe.domain.executers.FetchAllRecipes
import com.devdd.recipe.domain.observers.ObserveAllRecipes
import com.devdd.recipe.ui.home.viewstate.RecipeViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchAllRecipes: FetchAllRecipes,
    private val observeAllRecipes: ObserveAllRecipes
) : ViewModel() {

    private val mRecipes: MutableLiveData<List<RecipeViewState>> = MutableLiveData()
    val recipes: LiveData<List<RecipeViewState>>
        get() = mRecipes

    init {
        fetchRecipes()
        createObservers()
        observerRecipes()
    }

    private fun observerRecipes() {
        viewModelScope.launch {
            observeAllRecipes.observe().collect {

                mRecipes.postValue(it)

            }
        }
    }

    private fun createObservers() {
        observeAllRecipes(Unit)
    }

    private fun fetchRecipes() {
        viewModelScope.launch {
            fetchAllRecipes(Unit).collect {

            }
        }
    }
}