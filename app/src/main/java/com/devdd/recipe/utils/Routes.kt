package com.devdd.recipe.utils

sealed class Routes(val route: String) {
    object Home : Routes("home")
    object Search : Routes("search")
    object Profile : Routes("profile")
    object Activity : Routes("activity")
}