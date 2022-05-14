package com.devdd.recipe.utils

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.devdd.recipe.R

enum class DashboardTabs(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val route: String
) {
    HOME(R.string.bottom_nav_menu_home, R.drawable.ic_home, DashboardDestination.Home.route),
    SEARCH(
        R.string.bottom_nav_menu_search,
        R.drawable.ic_search,
        DashboardDestination.Search.route
    ),
    ACTIVITY(
        R.string.bottom_nav_menu_favourite,
        R.drawable.ic_favourite,
        DashboardDestination.Activity.route
    ),
    PROFILE(
        R.string.bottom_nav_menu_profile,
        R.drawable.ic_profile,
        DashboardDestination.Profile.route
    )
}