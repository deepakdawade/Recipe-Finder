package com.devdd.recipe.ui.dashboard

import android.os.Bundle
import androidx.core.view.isVisible
import com.devdd.recipe.R
import com.devdd.recipe.base.DevFragment
import com.devdd.recipe.databinding.FragmentDashboardBinding
import com.devdd.recipe.utils.extensions.hideToolTip
import com.devdd.recipe.utils.navigation.NavAnimations
import com.devdd.recipe.utils.navigation.setupMultipleBackStackBottomNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : DevFragment<FragmentDashboardBinding>(R.layout.fragment_dashboard) {
    override fun onViewCreated(binding: FragmentDashboardBinding, savedInstanceState: Bundle?) {
        binding.dashboardFragmentBottomNavigation.hideToolTip()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        // Setup the bottom navigation view with a list of navigation graphs
        setupMultipleBackStackBottomNavigation(
            navGraphIds = NAV_GRAPH_IDS,
            containerId = requireBinding().navHostFragmentDashboard.id,
            bottomNavigationView = requireBinding().dashboardFragmentBottomNavigation,
            navAnimations = NAV_ANIMATIONS,
        ) {
            // Choose when to show/hide the Bottom Navigation View
            it.addOnDestinationChangedListener { _, destination, _ ->
                if (TOP_LEVEL_DESTINATION.contains(destination.id))
                    showNavigators()
                else
                    hideNavigators()
            }
        }
    }

    @Synchronized
    private fun showNavigators() {
        binding?.dashboardFragmentBottomNavigation?.isVisible = true
    }

    @Synchronized
    private fun hideNavigators() {
        binding?.dashboardFragmentBottomNavigation?.isVisible = false
    }

    companion object {

        private val NAV_GRAPH_IDS = listOf(
            R.navigation.nav_graph_home,
            R.navigation.nav_graph_search,
            R.navigation.nav_graph_favorite,
            R.navigation.nav_graph_profile
        )

        private val TOP_LEVEL_DESTINATION: Set<Int> = setOf(
            R.id.homeFragment,
            R.id.searchRecipeFragment,
            R.id.favoriteRecipeFragment,
            R.id.profileFragment,
        )

        private val NAV_ANIMATIONS = NavAnimations(
            enterAnimation = R.anim.fragment_open_enter,
            exitAnimation = R.anim.fragment_open_exit,
            popEnterAnimation = R.anim.fragment_close_enter,
            popExitAnimation = R.anim.fragment_close_exit
        )

    }
}