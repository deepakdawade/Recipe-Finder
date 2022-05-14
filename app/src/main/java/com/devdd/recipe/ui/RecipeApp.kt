package com.devdd.recipe.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.*
import androidx.navigation.compose.*
import androidx.navigation.compose.navigation
import com.devdd.recipe.ui.activity.RecipeActivity
import com.devdd.recipe.ui.detail.RecipeDetail
import com.devdd.recipe.ui.home.RecipeHome
import com.devdd.recipe.ui.onboarding.Onboarding
import com.devdd.recipe.ui.profile.RecipeProfile
import com.devdd.recipe.ui.search.RecipeSearch
import com.devdd.recipe.utils.MainActions
import com.devdd.recipe.utils.DashboardDestination
import com.devdd.recipe.utils.DashboardTabs
import com.devdd.recipe.utils.MainDestinations
import java.util.*

@Composable
fun RecipeApp(
    showOnboardingInitially: Boolean = true,
    onBackHandler: () -> Unit
) {
    val navController = rememberNavController()
    // Onboarding could be read from shared preferences.
    val onboardingComplete = remember(showOnboardingInitially) {
        mutableStateOf(!showOnboardingInitially)
    }
    val actions = remember(navController) { MainActions(navController) }
    val tabs = remember { DashboardTabs.values() }
    Scaffold(
        modifier = Modifier,
        bottomBar = {
            RecipeBottomBar(navController, tabs)
        }
    ) { innerContentPadding ->
        NavHost(
            navController = navController,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerContentPadding),
            startDestination = MainDestinations.RECIPE_DASHBOARD
        ) {
            composable(MainDestinations.ONBOARDING_ROUTE) {
                BackHandler(onBack = onBackHandler)
                RecipeOnboarding(
                    onboardingComplete = {
                        // Set the flag so that onboarding is not shown next time.
                        onboardingComplete.value = true
                        actions.onboardingComplete()
                    }
                )
            }
            navigation(
                route = MainDestinations.RECIPE_DASHBOARD,
                startDestination = DashboardDestination.Home.route
            ) {
                recipeDashboard()
            }
            composable(
                "${MainDestinations.RECIPE_DETAIL_ROUTE}/{${MainDestinations.RECIPE_DETAIL_ID_KEY}}",
                arguments = listOf(
                    navArgument(MainDestinations.RECIPE_DETAIL_ID_KEY) { type = NavType.LongType }
                )
            ) { backStackEntry: NavBackStackEntry ->
                val arguments = requireNotNull(backStackEntry.arguments)
                val currentCourseId = arguments.getLong(MainDestinations.RECIPE_DETAIL_ID_KEY)
                RecipeDetail(
                    recipeId = currentCourseId,
                    upPress = { actions.upPress(backStackEntry) }
                )
            }
        }
    }
}

@Composable
fun RecipeBottomBar(navController: NavHostController, tabs: Array<DashboardTabs>) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
        ?: DashboardTabs.HOME.route

    val routes = remember { DashboardTabs.values().map { it.route } }
    if (currentRoute in routes) {
        BottomNavigation(
            Modifier.windowInsetsBottomHeight(
                WindowInsets.navigationBars.add(WindowInsets(bottom = 56.dp))
            )
        ) {
            tabs.forEach { tab ->
                BottomNavigationItem(
                    icon = { Icon(painterResource(tab.icon), contentDescription = null) },
                    label = { Text(stringResource(tab.title).uppercase(Locale.getDefault())) },
                    selected = currentRoute == tab.route,
                    onClick = {
                        if (tab.route != currentRoute) {
                            navController.navigate(tab.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    alwaysShowLabel = false,
                    selectedContentColor = MaterialTheme.colors.secondary,
                    unselectedContentColor = LocalContentColor.current,
                    modifier = Modifier.navigationBarsPadding()
                )
            }
        }
    }
}

@Composable
private fun RecipeOnboarding(
    onboardingComplete: () -> Unit
) {
    Onboarding(onboardingComplete)
}

private fun NavGraphBuilder.recipeDashboard() {
    composable(DashboardDestination.Home.route) {
        RecipeHome()
    }
    composable(DashboardDestination.Search.route) {
        RecipeSearch()
    }
    composable(DashboardDestination.Activity.route) {
        RecipeActivity()
    }
    composable(DashboardDestination.Profile.route) {
        RecipeProfile()
    }
}
