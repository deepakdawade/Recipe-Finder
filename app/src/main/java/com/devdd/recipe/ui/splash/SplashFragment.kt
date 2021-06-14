package com.devdd.recipe.ui.splash

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.devdd.recipe.R
import com.devdd.recipe.base_android.utils.extensions.observeEvent
import com.devdd.recipe.databinding.FragmentSplashBinding
import com.devdd.recipe.ui.base.DevFragment
import com.devdd.recipe.utils.extensions.navigateOnce
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class SplashFragment : DevFragment<FragmentSplashBinding>(R.layout.fragment_splash) {
    companion object {
        val TAG: String = this::class.java.simpleName
    }

    private val viewModel by viewModels<SplashViewModel>()

    override fun onViewCreated(binding: FragmentSplashBinding, savedInstanceState: Bundle?) {

        lifecycleScope.launchWhenResumed {
            delay(2000)
            viewModel.decideDestination()
        }

        viewModel.navigation.observeEvent(viewLifecycleOwner) {
            findNavController().navigateOnce(it)
        }
    }
}
