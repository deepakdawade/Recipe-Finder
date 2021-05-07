package com.devdd.recipe.ui.profile

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.devdd.recipe.ui.R
import com.devdd.recipe.ui.base.DevFragment
import com.devdd.recipe.ui.databinding.FragmentProfileBinding
import com.devdd.recipe.ui.utils.extensions.observeEvent
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
            else findNavController().navigate(it.first)
        }
    }
}