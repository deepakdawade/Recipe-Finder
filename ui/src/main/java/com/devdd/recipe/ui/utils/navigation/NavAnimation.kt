package com.devdd.recipe.ui.utils.navigation

import com.devdd.recipe.ui.R

data class NavAnimations(
    val enterAnimation: Int = R.anim.fragment_close_enter,
    val exitAnimation: Int = R.anim.fragment_open_exit,
    val popEnterAnimation: Int = R.anim.fragment_close_enter,
    val popExitAnimation: Int = R.anim.fragment_close_exit
)