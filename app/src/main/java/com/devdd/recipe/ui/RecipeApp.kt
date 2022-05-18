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
import kotlinx.coroutines.delay
import java.util.*

@Composable
fun RecipeApp(
    viewModel: MainViewModel,
    navController: NavHostController = rememberNavController(),
    onBackHandler: () -> Unit
) {
    val viewState by rememberStateWithLifecycle(viewModel.viewState)
    val actions = remember(navController) { MainActions(navController) }
    val tabs = remember { DashboardTabs.values() }
    RecipeAppContent(
        viewModel = viewModel,
        navController = navController,
        tabs = tabs,
        onBackHandler = onBackHandler,
        viewState = viewState,
        actions = actions
    )
}

@Composable
private fun RecipeAppContent(
    viewModel: MainViewModel,
    navController: NavHostController,
    tabs: Array<DashboardTabs>,
    onBackHandler: () -> Unit,
    viewState: MainViewState,
    actions: MainActions
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
                LaunchedEffect(viewState.showOnBoarding) {
                    delay(1500)
                    actions.navigateTo(
                        if (viewState.showOnBoarding) MainDestinations.ONBOARDING_ROUTE
                        else MainDestinations.RECIPE_DASHBOARD,
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
                    selectedIndex = if (viewState.localePref.isBlank()) 0 else 1,
                    updateRecipePref = viewModel::setRecipePref,
                    updateAppLocale = viewModel::setAppLocale
                )
            }
            navigation(
                route = MainDestinations.RECIPE_DASHBOARD,
                startDestination = DashboardDestination.Home.route
            ) {
                recipeDashboard(
                    viewState = viewState,
                    modifier = Modifier.fillMaxSize(),
                    onRefresh = viewModel::fetchRecipes,
                    actions = actions
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
    updateRecipePref: (pref: String) -> Unit,
    updateAppLocale: (locale: String) -> Unit
) {
    OnBoarding(
        selectedIndex = selectedIndex,
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
    viewState: MainViewState,
    actions: MainActions,
    onRefresh: () -> Unit
) {
    composable(DashboardDestination.Home.route) { from ->
        RecipeHome(
            loading = viewState.loading,
            recipes = viewState.recipes,
            selectRecipes = {
                actions.openRecipe(it, from)
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
