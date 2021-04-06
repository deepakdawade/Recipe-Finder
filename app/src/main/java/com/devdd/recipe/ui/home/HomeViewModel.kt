package com.devdd.recipe.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devdd.recipe.data.db.entities.Recipe
import javax.inject.Inject


class HomeViewModel @Inject constructor() : ViewModel() {

    private val mRecipes: MutableLiveData<List<Recipe>> = MutableLiveData()
    val recipes: LiveData<List<Recipe>>
        get() = mRecipes
}