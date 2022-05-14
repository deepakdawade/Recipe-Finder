package com.devdd.recipe.utils

sealed class DashboardDestination(val route: String) {
    object Home : DashboardDestination("home")
    object Search : DashboardDestination("search")
    object Profile : DashboardDestination("profile")
    object Activity : DashboardDestination("activity")
}