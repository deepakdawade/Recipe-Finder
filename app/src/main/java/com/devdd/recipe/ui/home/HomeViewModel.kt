package com.devdd.recipe.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devdd.recipe.domain.executers.FetchAllCategories
import com.devdd.recipe.domain.executers.FetchAllRecipes
import com.devdd.recipe.domain.observers.ObserveAllCategories
import com.devdd.recipe.domain.observers.ObserveAllRecipes
import com.devdd.recipe.domain.result.InvokeStarted
import com.devdd.recipe.ui.home.viewstate.CategoryViewState
import com.devdd.recipe.ui.home.viewstate.RecipeViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchAllRecipes: FetchAllRecipes,
    private val observeAllRecipes: ObserveAllRecipes,
    private val fetchAllCategories: FetchAllCategories,
    private val observeAllCategories: ObserveAllCategories
) : ViewModel() {

    private val mRecipes: MutableLiveData<List<RecipeViewState>> = MutableLiveData()
    val recipes: LiveData<List<RecipeViewState>>
        get() = mRecipes

    private val mCategories: MutableLiveData<List<CategoryViewState>> = MutableLiveData()
    val categories: LiveData<List<CategoryViewState>>
        get() = mCategories

    private val mLoading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean>
        get() = mLoading

    init {
        fetchRecipes()
        fetchCategories()
        createObservers()
        observeRecipes()
        observeCategories()
    }

    private fun observeRecipes() {
        viewModelScope.launch {
            observeAllRecipes.observe().collect {

                mRecipes.postValue(it)

            }
        }
    }

    private fun observeCategories() {
        viewModelScope.launch {
            observeAllCategories.observe().collect {
                mCategories.postValue(it)
            }
        }
    }

    private fun createObservers() {
        observeAllRecipes(Unit)
        observeAllCategories(Unit)
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

    private fun fetchCategories() {
        viewModelScope.launch {
            fetchAllCategories(Unit).collect {}
        }
    }
}