package com.devdd.recipe.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.devdd.recipe.base.firebase.TopicSubscriber
import com.devdd.recipe.base.result.Event
import com.devdd.recipe.data.preference.manager.LocaleManager
import com.devdd.recipe.data.preference.manager.RecipeManager
import com.devdd.recipe.domain.executers.firebase.FetchFcmToken
import com.devdd.recipe.domain.invoke
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val fcmTopicSubscriber: TopicSubscriber,
    private val recipeManager: RecipeManager,
    private val localeManager: LocaleManager,
    private val fetchFcmToken: FetchFcmToken
) : ViewModel() {

    private val mNavigation: MutableLiveData<Event<NavDirections>> = MutableLiveData()
    val navigation: LiveData<Event<NavDirections>>
        get() = mNavigation

    fun decideDestination() {
        viewModelScope.launch {
            when {
                !localeManager.isLanguageSelected() -> navigateToPreferenceSetting()
                !recipeManager.isRecipeSelected() -> navigateToPreferenceSetting(1)
                else -> navigateToDashboard().also {
                    fcmTopicSubscriber.subscribeToLoggedInUpdates()
                    fetchFcmToken.invoke().collect()
                }
            }
        }
    }

    private fun navigateToPreferenceSetting(page:Int = 0) {
        val direction = SplashFragmentDirections.actionToPreferenceSettingFragment(page)
        mNavigation.value = Event(direction)
    }

    private fun navigateToDashboard() {
        val direction = SplashFragmentDirections.actionToDashboardFragment()
        mNavigation.value = Event(direction)
    }
}