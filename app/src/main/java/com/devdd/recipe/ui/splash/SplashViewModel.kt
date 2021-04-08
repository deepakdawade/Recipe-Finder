package com.devdd.recipe.ui.splash

import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModel() {

    fun decideDestination(): NavDirections {
        return SplashFragmentDirections.actionToHomeFragment()
    }
}