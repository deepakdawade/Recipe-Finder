package com.devdd.recipe.ui

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devdd.recipe.api.toJsonString
import com.devdd.recipe.domain.executer.FetchRecipes
import com.devdd.recipe.domain.observer.*
import com.devdd.recipe.domain.onStarted
import com.devdd.recipe.domain.onSuccess
import com.devdd.recipe.domain.watchStatus
import com.devdd.recipe.utils.ObservableLoadingCounter
import com.devdd.recipe.utils.updateLoadingState
import com.devdd.recipe.utils.combine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
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
    private val refreshCounter = ObservableLoadingCounter()
    val viewState: StateFlow<MainViewState> = combine(
        observeSelectedLocale.flow,
        observeSelectedRecipePref.flow,
        observeRecipeByPref.flow,
        refreshCounter.observable
    ) { selectedLocale, selectedPref, recipes, loading ->
        MainViewState(
            localePref = selectedLocale,
            recipePref = selectedPref,
            recipes = recipes,
            loading = loading
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
                updateLoadingState(refreshCounter)
            }
        }
    }

    fun setRecipePref(pref: String) {
        viewModelScope.launch {
            setRecipePref.invoke(pref).watchStatus { }
        }
    }

    fun setAppLocale(locale: String) {
        val appLocale = LocaleListCompat.forLanguageTags(locale)
        AppCompatDelegate.setApplicationLocales(appLocale)
        viewModelScope.launch {
            setAppLocale.invoke(locale).watchStatus { }
        }
    }

}