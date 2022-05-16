package com.devdd.recipe.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.compose.*
import androidx.navigation.compose.navigation
import com.devdd.recipe.ui.activity.RecipeActivity
import com.devdd.recipe.ui.detail.RecipeDetail
import com.devdd.recipe.ui.home.RecipeHome
import com.devdd.recipe.ui.onboarding.OnBoarding
import com.devdd.recipe.ui.profile.RecipeProfile
import com.devdd.recipe.ui.search.RecipeSearch
import com.devdd.recipe.utils.*
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import java.util.*

@Composable
fun RecipeApp(
    onBackHandler: () -> Unit
) {
    val viewModel: MainViewModel = hiltViewModel()
    val viewState by rememberFlowWithLifecycle(flow = viewModel.viewState).collectAsState(initial = MainViewState.INITIAL)
    val showOnboardingInitially = viewState.showOnBoarding
    val navController = rememberNavController()
    val onboardingComplete = remember(showOnboardingInitially) {
        mutableStateOf(!showOnboardingInitially)
    }
    val startDestination = remember(showOnboardingInitially) {
        if (showOnboardingInitially) MainDestinations.ONBOARDING_ROUTE
        else MainDestinations.RECIPE_DASHBOARD
    }
    val refreshState = rememberSwipeRefreshState(isRefreshing = viewState.loading)
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
            startDestination = startDestination
        ) {
            composable(MainDestinations.ONBOARDING_ROUTE) {
                BackHandler(onBack = onBackHandler)
                RecipeOnBoarding(
                    selectedLocale = viewState.localePref,
                    selectedRecipePref = viewState.recipePref,
                    selectedIndex = if (viewState.localePref.isBlank()) 0 else 1,
                    onBoardingComplete = {
                        // Set the flag so that on boarding is not shown next time.
                        onboardingComplete.value = true
                        actions.onboardingComplete()
                    },
                    updateAppLocale = viewModel::setAppLocale,
                    updateRecipePref = viewModel::setRecipePref
                )
            }
            navigation(
                route = MainDestinations.RECIPE_DASHBOARD,
                startDestination = DashboardDestination.Home.route
            ) {
                recipeDashboard(
                    recipes = viewState.recipes,
                    onRecipeSelected = actions.openRecipe,
                    modifier = Modifier.fillMaxSize(),
                    refreshState = refreshState,
                    onRefresh = viewModel::fetchRecipes
                )
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
            modifier = Modifier.windowInsetsBottomHeight(
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
                    unselectedContentColor = MaterialTheme.colors.onSurface,
                    modifier = Modifier.navigationBarsPadding()
                )
            }
        }
    }
}

@Composable
private fun RecipeOnBoarding(
    selectedIndex: Int = 0,
    onBoardingComplete: () -> Unit,
    updateRecipePref: (pref: String) -> Unit,
    updateAppLocale: (locale: String) -> Unit,
    selectedRecipePref: String,
    selectedLocale: String
) {
    OnBoarding(
        selectedLocale = selectedLocale,
        selectedRecipePref = selectedRecipePref,
        selectedIndex = selectedIndex,
        onBoardingComplete = onBoardingComplete,
        updateRecipePref = updateRecipePref,
        updateAppLocale = updateAppLocale
    )
}

private fun NavGraphBuilder.recipeDashboard(
    modifier: Modifier,
    onRecipeSelected: (Long, NavBackStackEntry) -> Unit,
    refreshState: SwipeRefreshState,
    recipes: List<RecipeViewState>,
    onRefresh: () -> Unit
) {
    composable(DashboardDestination.Home.route) { from ->
        RecipeHome(
            refreshState = refreshState,
            recipes = recipes,
            selectRecipes = {
                onRecipeSelected(it, from)
            },
            modifier = modifier,
            onRefresh = onRefresh
        )
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
