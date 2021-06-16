package com.devdd.recipe.ui.recipedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.devdd.recipe.data.preference.manager.GuestManager
import com.devdd.recipe.data.preference.manager.LocaleManager
import com.devdd.recipe.data.preference.manager.RecipeManager
import com.devdd.recipe.data.models.request.MarkRecipeFavoriteRequest
import com.devdd.recipe.domain.executers.MarkRecipeFavorite
import com.devdd.recipe.domain.observers.GetRecipeById
import com.devdd.recipe.base.result.Event
import com.devdd.recipe.base.result.onSuccess
import com.devdd.recipe.domain.viewstate.RecipeViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by @author Deepak Dawade on 4/17/2021 at 12:40 AM.
 * Copyright (c) 2021 deepak.dawade.dd1@gmail.com All rights reserved.
 *
 */
@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val guestManager: GuestManager,
    private val localeManager: LocaleManager,
    private val recipeManager: RecipeManager,
    private val markRecipeFavorite: MarkRecipeFavorite,
    private val getRecipeById: GetRecipeById
) : ViewModel() {
    private val mRecipe: MutableLiveData<RecipeViewState> = MutableLiveData()
    val recipe: LiveData<RecipeViewState>
        get() = mRecipe

    private val mSavingRecipe: MutableLiveData<Boolean> = MutableLiveData()
    val savingRecipe: LiveData<Boolean>
        get() = mSavingRecipe

    private val mNavigation: MutableLiveData<Event<NavDirections>> = MutableLiveData()
    val navigation: LiveData<Event<NavDirections>>
        get() = mNavigation

    fun loadRecipeById(recipeId: Int) {
        getRecipeById.invoke(recipeId)
        observeRecipe()
    }

    private fun observeRecipe() {
        viewModelScope.launch {
            getRecipeById.observe().collect {
                mRecipe.postValue(it)
            }
        }
    }

    fun markRecipeFavorite() {
        mSavingRecipe.value = true
        val localRecipe = recipe.value?.entity
        viewModelScope.launch {
            markRecipeFavorite.invoke(
                MarkRecipeFavoriteRequest(
                    guestManager.guestToken(),
                    localRecipe?.id ?: return@launch,
                    localRecipe.saved.not()
                )
            )
                .collect {
                    it.onSuccess {
                        mSavingRecipe.postValue(false)
                    }
                }
        }
    }

}