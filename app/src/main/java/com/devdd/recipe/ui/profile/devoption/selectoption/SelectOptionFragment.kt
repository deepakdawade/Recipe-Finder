package com.devdd.recipe.ui.profile.devoption.selectoption

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.devdd.recipe.R
import com.devdd.recipe.base.DevFragment
import com.devdd.recipe.databinding.FragmentSelectOptionBinding
import com.devdd.recipe.ui.profile.devoption.selectoption.adapter.SelectOptionAdapter
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
            directions?.let { it1 -> viewModel.navigateToOption(it1) }

        }
        binding.selectOptionFragmentRecyclerView.adapter = adapter
    }

    private fun setObservers() {
        viewModel.options.observe(viewLifecycleOwner) {
            adapter?.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
    }

}