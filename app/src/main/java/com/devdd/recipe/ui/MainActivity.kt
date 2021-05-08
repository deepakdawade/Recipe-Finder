package com.devdd.recipe.ui

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.devdd.recipe.R
import com.devdd.recipe.databinding.ActivityMainBinding
import com.devdd.recipe.ui.utils.navigation.setUpDeeplinkNavigationBehavior
import com.devdd.recipe.base_android.utils.localemanager.LocaleManagerUtils
import com.devdd.recipe.ui.utils.extensions.bindingWithLifecycleOwner
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()

    companion object {
        private val TAG: String = this::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = bindingWithLifecycleOwner<ActivityMainBinding>(R.layout.activity_main)
        // to handle recreate of activity
        setUpDeeplinkNavigationBehavior(
            navHostFragmentId = binding.mainActivityContainer.id,
            graphId = R.navigation.navigation_main_graph
        )

    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleManagerUtils.setLocale(base))
    }
}