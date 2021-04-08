package com.devdd.recipe.ui.splash

import android.animation.Animator
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.devdd.recipe.R
import com.devdd.recipe.base.MyFragment
import com.devdd.recipe.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : MyFragment<FragmentSplashBinding>(R.layout.fragment_splash) {
    companion object {
        val TAG: String = this::class.java.simpleName
    }

    private val splashViewModel by viewModels<SplashViewModel>()

    override fun onViewCreated(binding: FragmentSplashBinding, savedInstanceState: Bundle?) {
        binding.splashFragmentLottieAnimation.setAnimation(R.raw.splash_animation_food)
        binding.splashFragmentLottieAnimation.playAnimation()
        binding.splashFragmentLottieAnimation.addAnimatorListener(object :
            Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                lifecycleScope.launch {
                    val nextDestination = splashViewModel.decideDestination()
                    findNavController().navigate(nextDestination)
                }
            }

            override fun onAnimationCancel(animation: Animator?) {}

            override fun onAnimationStart(animation: Animator?) {}
        })
    }
}
