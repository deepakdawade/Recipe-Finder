package com.devdd.recipe.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devdd.recipe.domain.executer.FetchRecipes
import com.devdd.recipe.domain.observer.*
import com.devdd.recipe.domain.watchStatus
import com.devdd.recipe.utils.combine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val fetchRecipes: FetchRecipes,
    private val setAppLocale: SetAppLocale,
    private val setRecipePref: SetRecipePref,
    observeSelectedRecipePref: ObserveSelectedRecipePref,
    observeSelectedLocale: ObserveSelectedLocale,
    observeRecipeByPref: ObserveRecipeByPref
) : ViewModel() {
    val viewState = combine(
        observeSelectedLocale.flow,
        observeSelectedRecipePref.flow,
        observeRecipeByPref.flow
    ) { selectedLocale, selectedPref, recipes ->
        MainViewState(
            localePref = selectedLocale,
            recipePref = selectedPref,
            recipes = recipes
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        MainViewState.INITIAL
    )

    init {
        observeSelectedLocale(Unit)
        observeSelectedRecipePref(Unit)
        viewModelScope.launch {
            observeSelectedRecipePref.flow.collect {
                observeRecipeByPref(it)
            }
        }
        fetchRecipes()
    }

    fun fetchRecipes() {
        viewModelScope.launch {
            fetchRecipes.invoke("").watchStatus {

            }
        }
    }

    fun setRecipePref(pref: String) {
        viewModelScope.launch {
            setRecipePref.invoke(pref).watchStatus { }
        }
    }

    fun setAppLocale(locale: String) {
        viewModelScope.launch {
            setAppLocale.invoke(locale).watchStatus { }
        }
    }

}