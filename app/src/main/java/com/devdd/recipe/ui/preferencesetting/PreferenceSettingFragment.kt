package com.devdd.recipe.ui.preferencesetting

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.devdd.recipe.R
import com.devdd.recipe.base.DevFragment
import com.devdd.recipe.databinding.FragmentPreferenceSettingBinding
import com.devdd.recipe.ui.preferencesetting.adapter.PreferenceSettingAdapter
import com.devdd.recipe.utils.extensions.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreferenceSettingFragment :
    DevFragment<FragmentPreferenceSettingBinding>(R.layout.fragment_preference_setting) {
    companion object {
        private val TAG = this::class.java.simpleName
    }

    private val viewModel by viewModels<PreferenceSettingViewModel>()
    override fun onViewCreated(
        binding: FragmentPreferenceSettingBinding,
        savedInstanceState: Bundle?
    ) {
        binding.viewModel = viewModel
        setViewPagerAdapter(binding)
        setObservers()
    }

    private fun setViewPagerAdapter(binding: FragmentPreferenceSettingBinding) {
        binding.preferenceSettingFragmentViewPager.adapter = PreferenceSettingAdapter(this)
        val position = navArgs<PreferenceSettingFragmentArgs>().value.page
        viewModel.setPage(position)
    }

    private fun setObservers() {

        viewModel.page.observeEvent(viewLifecycleOwner) {
            binding?.preferenceSettingFragmentViewPager?.post {
                if (it < 2) binding?.preferenceSettingFragmentViewPager?.currentItem = it
            }

        }

        viewModel.navigation.observeEvent(viewLifecycleOwner) {
            if (it.second)
                findNavController().navigateUp()
            else findNavController().navigate(it.first)
        }
    }
}