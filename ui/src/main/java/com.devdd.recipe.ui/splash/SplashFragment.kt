package com.devdd.recipe.ui.splash

import android.animation.Animator
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.devdd.recipe.ui.R
import com.devdd.recipe.ui.base.DevFragment
import com.devdd.recipe.ui.databinding.FragmentSplashBinding
import com.devdd.recipe.ui.utils.extensions.observeEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : DevFragment<FragmentSplashBinding>(R.layout.fragment_splash) {
    companion object {
        val TAG: String = this::class.java.simpleName
    }

    private val viewModel by viewModels<SplashViewModel>()

    override fun onViewCreated(binding: FragmentSplashBinding, savedInstanceState: Bundle?) {
        binding.splashFragmentLottieAnimation.addAnimatorListener(object :
            Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                lifecycleScope.launch {
                    viewModel.decideDestination()
                }
            }

            override fun onAnimationCancel(animation: Animator?) {}

            override fun onAnimationStart(animation: Animator?) {}
        })

        viewModel.navigation.observeEvent(viewLifecycleOwner) {
            findNavController().navigate(it)
        }
    }
}
