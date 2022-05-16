package com.devdd.recipe.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.devdd.recipe.R
import com.devdd.recipe.ui.activity.RecipeActivity
import com.devdd.recipe.ui.detail.RecipeDetail
import com.devdd.recipe.ui.home.RecipeHome
import com.devdd.recipe.ui.onboarding.OnBoarding
import com.devdd.recipe.ui.profile.RecipeProfile
import com.devdd.recipe.ui.search.RecipeSearch
import com.devdd.recipe.utils.*
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay
import java.util.*

@Composable
fun RecipeApp(
    viewModel: MainViewModel,
    onBackHandler: () -> Unit
) {
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
    RecipeAppContent(
        viewModel = viewModel,
        navController = navController,
        tabs = tabs,
        refreshState = refreshState,
        onBackHandler = onBackHandler,
        viewState = viewState,
        actions = actions,
        onBoardingComplete = {
            onboardingComplete.value = true
        },
        startDestination = startDestination
    )
}

@Composable
private fun RecipeAppContent(
    viewModel: MainViewModel,
    navController: NavHostController,
    tabs: Array<DashboardTabs>,
    refreshState: SwipeRefreshState,
    onBackHandler: () -> Unit,
    viewState: MainViewState,
    actions: MainActions,
    onBoardingComplete: (complete: Boolean) -> Unit,
    startDestination: String
) {
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
            startDestination = MainDestinations.SPLASH
        ) {
            composable(MainDestinations.SPLASH) {
                LaunchedEffect(true) {
                    delay(1500)
                    actions.navigateTo(
                        startDestination,
                        popUpTo = MainDestinations.SPLASH,
                        from = it,
                        inclusive = true
                    )
                }
                RecipeSplash()
            }
            composable(MainDestinations.ONBOARDING_ROUTE) {
                BackHandler(onBack = onBackHandler)
                RecipeOnBoarding(
                    selectedLocale = viewState.localePref,
                    selectedRecipePref = viewState.recipePref,
                    selectedIndex = if (viewState.localePref.isBlank()) 0 else 1,
                    onBoardingComplete = {
                        // Set the flag so that on boarding is not shown next time.
                        onBoardingComplete(true)
                        actions.boardingToDashboard(it)
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

@Composable
private fun RecipeSplash() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "splash"
        )
    }
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
