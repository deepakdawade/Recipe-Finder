package com.devdd.recipe.utils.handlemultibackstack

import com.devdd.recipe.R

data class NavAnimations(
    val enterAnimation: Int = R.anim.fragment_close_enter,
    val exitAnimation: Int = R.anim.fragment_open_exit,
    val popEnterAnimation: Int = R.anim.fragment_close_enter,
    val popExitAnimation: Int = R.anim.fragment_close_exit
)