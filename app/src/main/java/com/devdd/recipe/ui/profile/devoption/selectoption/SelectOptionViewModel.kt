package com.devdd.recipe.ui.profile.devoption.selectoption

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.devdd.recipe.R
import com.devdd.recipe.domain.result.Event
import com.devdd.recipe.ui.profile.ProfileFragmentDirections
import com.devdd.recipe.ui.profile.devoption.selectoption.adapter.DeveloperOption
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SelectOptionViewModel @Inject constructor() : ViewModel() {
    private val mOptions: MutableLiveData<List<DeveloperOption>> = MutableLiveData()
    val options: LiveData<List<DeveloperOption>>
        get() = mOptions

    private val mNavigation: MutableLiveData<Event<NavDirections>> =
        MutableLiveData()
    val navigation: LiveData<Event<NavDirections>>
        get() = mNavigation


    init {
        initOptions()
    }

    private fun initOptions() {
        val options = listOf(
            DeveloperOption(
                name = R.string.dev_option_preference,
                icon = R.drawable.shared_preference,
                bgColor = R.color.colorDevOption1,
                direction = null
            )
        )
        mOptions.value = options
    }

    fun navigateToOption(directions: NavDirections) {
        mNavigation.value = Event(directions)
    }
}