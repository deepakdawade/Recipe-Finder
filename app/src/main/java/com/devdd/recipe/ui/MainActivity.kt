package com.devdd.recipe.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.devdd.recipe.R
import com.devdd.recipe.databinding.ActivityMainBinding
import com.devdd.recipe.utils.extensions.bindingWithLifecycleOwner
import com.devdd.recipe.utils.localemanager.LocaleManagerUtils
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG: String = this::class.java.simpleName
        private val TOP_LEVEL_DESTINATION: Set<Int> = setOf(R.id.homeFragment, R.id.profileFragment)
    }

    private var mBinding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = requireNotNull(mBinding)
    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = bindingWithLifecycleOwner(R.layout.activity_main)
        setUpNavigation()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleManagerUtils.setLocale(base))
    }

    private fun setUpNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_activity_container) as NavHostFragment
        val navController = navHostFragment.navController
        navController.let {
            NavigationUI.setupWithNavController(binding.mainActivityBottomNav, it)
        }
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            binding.mainActivityBottomNav.isVisible = TOP_LEVEL_DESTINATION.contains(destination.id)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
        navController = null
    }
}