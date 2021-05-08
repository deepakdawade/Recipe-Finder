package com.devdd.recipe.feature_home.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.devdd.recipe.base.constants.SelectedLocale.LOCALE_ENGLISH
import com.devdd.recipe.base.result.Event
import com.devdd.recipe.base.result.InvokeStarted
import com.devdd.recipe.base.result.InvokeSuccess
import com.devdd.recipe.data.preference.manager.GuestManager
import com.devdd.recipe.data.preference.manager.LocaleManager
import com.devdd.recipe.data.preference.manager.RecipeManager
import com.devdd.recipe.domain.executers.FetchGuestToken
import com.devdd.recipe.domain.executers.FetchRecipes
import com.devdd.recipe.domain.executers.MarkRecipeFavorite
import com.devdd.recipe.domain.executers.SetDeviceIdToServer
import com.devdd.recipe.domain.observers.ObserveRecipeByPref
import com.devdd.recipe.data.models.entity.Recipe
import com.devdd.recipe.data.models.request.MarkRecipeFavoriteRequest
import com.devdd.recipe.domain.viewstate.RecipeViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchRecipes: FetchRecipes,
    private val observeRecipeByPref: ObserveRecipeByPref,
    private val fetchGuestToken: FetchGuestToken,
    private val setDeviceIdToServer: SetDeviceIdToServer,
    private val markRecipeFavorite: MarkRecipeFavorite,
    private val guestManager: GuestManager,
    private val recipeManager: RecipeManager,
    private val localeManager: LocaleManager
) : ViewModel() {

    private val mAllRecipes = mutableListOf<RecipeViewState>()
    private val mRecipes: MutableLiveData<List<RecipeViewState>> = MutableLiveData()
    val recipes: LiveData<List<RecipeViewState>>
        get() = mRecipes

    private val mLoading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean>
        get() = mLoading

    private val mSavingRecipe: MutableLiveData<Pair<Boolean, Int>> = MutableLiveData()
    val savingRecipe: LiveData<Pair<Boolean, Int>>
        get() = mSavingRecipe


    private val mNavigation: MutableLiveData<Event<NavDirections>> = MutableLiveData()
    val navigation: LiveData<Event<NavDirections>>
        get() = mNavigation

    init {
        checkHasGuestTokenGenerated()
        checkHasDeviceIdGenerated()
        shouldUploadDeviceId()

        fetchRecipes()
        createObservers()
        observeRecipes()
    }

    private fun checkHasGuestTokenGenerated() {
        viewModelScope.launch {
            if (!guestManager.guestTokenGenerated())
                generateGuestToken()
        }
    }

    private fun checkHasDeviceIdGenerated() {
        viewModelScope.launch {
            if (!guestManager.deviceIdGenerated())
                guestManager.generateDeviceId()
        }
    }

    private fun generateGuestToken() {
        viewModelScope.launch {
            fetchGuestToken.invoke(Unit).collect { token ->
                guestManager.updateGuestToken(token)
            }
        }
    }

    private fun shouldUploadDeviceId() {
        viewModelScope.launch {
            guestManager.shouldUploadDeviceIdToServer.collect {
                if (it)
                    setDeviceIdToServer.invoke(guestManager.deviceId()).collect { status ->
                        if (status is InvokeSuccess)
                            guestManager.shouldUploadDeviceIdToServer(false)
                    }
            }
        }
    }

    fun fetchRecipes() {
        viewModelScope.launch {
            guestManager.guestToken.collect { token ->
                fetchRecipes(token).collect { status ->
                    if (status is InvokeStarted)
                        mLoading.postValue(true)
                    else mLoading.postValue(false)
                }

            }
        }
    }

    fun markRecipeFavorite(recipe: Recipe) {
        viewModelScope.launch {
            markRecipeFavorite.invoke(
                MarkRecipeFavoriteRequest(
                    guestManager.guestToken(),
                    recipe.id,
                    !recipe.saved
                )
            )
                .collect {
                    mSavingRecipe.postValue(Pair(it is InvokeStarted, recipe.id))
                }
        }
    }

    private fun createObservers() {
        viewModelScope.launch {
            localeManager.selectedLanguage.combineTransform(recipeManager.recipePreference) { lang: String, pref: String ->
                emit(ObserveRecipeByPref.Params(lang == LOCALE_ENGLISH, pref))
            }.collect {
                observeRecipeByPref(it)
            }
        }
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

    fun navigateToRecipeDetails(recipeId: Int) {
        val navDirection = HomeFragmentDirections.actionToRecipeDetailFragment(recipeId)
        mNavigation.value = Event(navDirection)
    }
}
