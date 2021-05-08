package com.devdd.recipe.feature_profile.ui.profile.devoption.selectoption

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.devdd.recipe.feature_profile.R
import com.devdd.recipe.ui.base.DevFragment
import com.devdd.recipe.feature_profile.databinding.FragmentSelectOptionBinding
import com.devdd.recipe.feature_profile.ui.profile.devoption.selectoption.adapter.SelectOptionAdapter
import com.devdd.recipe.utils.extensions.navigateOnce
import com.devdd.recipe.ui.utils.extensions.observeEvent
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by @author Deepak Dawade on 4/14/2021 at 9:31 PM.
 * Copyright (c) 2021 deepak.dawade.dd1@gmail.com All rights reserved.
 *
 */
@AndroidEntryPoint
class SelectOptionFragment :
    DevFragment<FragmentSelectOptionBinding>(R.layout.fragment_select_option) {
    companion object {
        private val TAG: String = this::class.java.simpleName
    }

    private var adapter: SelectOptionAdapter? = null
    private val viewModel: SelectOptionViewModel by viewModels()

    override fun onViewCreated(binding: FragmentSelectOptionBinding, savedInstanceState: Bundle?) {
        setRecyclerViewAdapter(binding)
        setObservers()
    }

    private fun setRecyclerViewAdapter(binding: FragmentSelectOptionBinding) {

        adapter = SelectOptionAdapter { directions ->
            viewModel.navigateToOption(directions)
        }

        binding.selectOptionFragmentRecyclerView.adapter = adapter
    }

    private fun setObservers() {
        viewModel.options.observe(viewLifecycleOwner) {
            adapter?.submitList(it)
        }

        viewModel.navigation.observeEvent(viewLifecycleOwner) {
            findNavController().navigateOnce(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
    }

}