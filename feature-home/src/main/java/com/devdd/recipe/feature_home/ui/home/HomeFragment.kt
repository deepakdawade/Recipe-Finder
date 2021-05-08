package com.devdd.recipe.feature_home.ui.home

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.devdd.recipe.feature_home.databinding.FragmentHomeBinding
import com.devdd.recipe.feature_home.R
import com.devdd.recipe.ui.base.DevFragment
import com.devdd.recipe.feature_home.ui.home.adapter.HomeRecipeAdapter
import com.devdd.recipe.ui.utils.extensions.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : DevFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()

    private var recipeAdapter: HomeRecipeAdapter? = null
    override fun onViewCreated(binding: FragmentHomeBinding, savedInstanceState: Bundle?) {
        binding.homeViewModel = viewModel
        setListeners()
        setupRecyclerViewAdapter()
        setObserver()
    }

    private fun setListeners() {
        binding?.homeFragmentSwipeToRefresh?.setOnRefreshListener {
            viewModel.fetchRecipes()
        }
    }

    private fun setObserver() {
        viewModel.recipes.observe(viewLifecycleOwner) {
            recipeAdapter?.submitList(it)
        }

        viewModel.navigation.observeEvent(viewLifecycleOwner) {
            findNavController().navigate(it)
        }
    }

    private fun setupRecyclerViewAdapter() {
        recipeAdapter = HomeRecipeAdapter(viewModel)
        binding?.homeFragmentRecipes?.adapter = recipeAdapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        recipeAdapter = null
    }
}