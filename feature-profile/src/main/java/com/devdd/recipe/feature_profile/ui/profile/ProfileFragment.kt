package com.devdd.recipe.feature_profile.ui.profile

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.devdd.recipe.feature_profile.R
import com.devdd.recipe.ui.base.DevFragment
import com.devdd.recipe.feature_profile.databinding.FragmentProfileBinding
import com.devdd.recipe.base_android.utils.extensions.observeEvent
import com.devdd.recipe.utils.extensions.navigateOnce
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : DevFragment<FragmentProfileBinding>(R.layout.fragment_profile) {

    companion object {
        private val TAG: String = this::class.java.simpleName
    }

    private val viewModel by viewModels<ProfileViewModel>()
    override fun onViewCreated(binding: FragmentProfileBinding, savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        setObservers()
    }

    private fun setObservers() {
        viewModel.navigation.observeEvent(viewLifecycleOwner) {
            if (it.second)
                findNavController().navigateUp()
            else findNavController().navigateOnce(it.first)
        }
    }
}