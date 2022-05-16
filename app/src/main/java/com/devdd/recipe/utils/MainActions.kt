package com.devdd.recipe.utils

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.navOptions

/**
 * Models the navigation actions in the app.
 */
class MainActions(private val navController: NavHostController) {
    val onboardingComplete: () -> Unit = {
        navController.popBackStack()
    }

    val boardingToDashboard = { from: NavBackStackEntry ->
        navigateTo(
            MainDestinations.RECIPE_DASHBOARD,
            from,
            popUpTo = MainDestinations.ONBOARDING_ROUTE,
            inclusive = true
        )
    }

    val dashboard = { from: NavBackStackEntry ->
        if (from.lifecycleIsResumed()) {
            navController.navigate(MainDestinations.RECIPE_DASHBOARD, navOptions = navOptions {
                popUpTo(MainDestinations.SPLASH, popUpToBuilder = {
                    this.inclusive = true
                })
            })
        }
    }

    // Used from RECIPES_ROUTE
    val openRecipe = { recipeId: Long, from: NavBackStackEntry ->
        // In order to discard duplicated navigation events, we check the Lifecycle
        if (from.lifecycleIsResumed()) {
            navController.navigate("${MainDestinations.RECIPE_DETAIL_ROUTE}/$recipeId")
        }
    }

    // Used from RECIPE_DETAIL_ROUTE
    val upPress: (from: NavBackStackEntry) -> Unit = { from ->
        // In order to discard duplicated navigation events, we check the Lifecycle
        if (from.lifecycleIsResumed()) {
            navController.navigateUp()
        }
    }

    fun navigateTo(
        destination: String,
        from: NavBackStackEntry,
        popUpTo: String = "",
        inclusive: Boolean = false
    ) {
        if (from.lifecycleIsResumed()) {
            val option = if (popUpTo.isNotBlank()) navOptions {
                popUpTo(popUpTo, popUpToBuilder = {
                    this.inclusive = inclusive
                })
            } else null
            navController.navigate(destination, navOptions = option)
        }
    }

}

/**
 * If the lifecycle is not resumed it means this NavBackStackEntry already processed a nav event.
 *
 * This is used to de-duplicate navigation events.
 */
private fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED

object MainDestinations {
    const val SPLASH = "splash"
    const val ONBOARDING_ROUTE = "onboarding"
    const val RECIPE_DASHBOARD = "dashboard"
    const val RECIPE_DETAIL_ROUTE = "recipe_details"
    const val RECIPE_DETAIL_ID_KEY = "recipeId"
}
