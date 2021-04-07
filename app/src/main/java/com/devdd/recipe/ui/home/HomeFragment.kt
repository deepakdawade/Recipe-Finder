package com.devdd.recipe.ui.home

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.devdd.recipe.R
import com.devdd.recipe.base.MyFragment
import com.devdd.recipe.databinding.FragmentHomeBinding
import com.devdd.recipe.ui.home.adapter.CategoryAdapter
import com.devdd.recipe.ui.home.adapter.RecipeAdapter
import com.devdd.recipe.utils.extensions.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : MyFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val homeViewModel by viewModels<HomeViewModel>()

    private var recipeAdapter: RecipeAdapter? = null
    private var categoryAdapter: CategoryAdapter? = null
    override fun onViewCreated(binding: FragmentHomeBinding, savedInstanceState: Bundle?) {
        binding.homeViewModel = homeViewModel
        setupRecyclerViewAdapter()
        setObserver()
        setListeners()
    }

    private fun setListeners() {
        binding?.homeFragmentSwipeToRefresh?.setOnRefreshListener {
            homeViewModel.fetchRecipes()
        }
    }

    private fun setObserver() {
        homeViewModel.recipes.observe(viewLifecycleOwner) {
            recipeAdapter?.submitList(it)
        }

        homeViewModel.categories.observe(viewLifecycleOwner) {
            categoryAdapter?.submitList(it)
        }

        homeViewModel.navigation.observeEvent(viewLifecycleOwner) {
            findNavController().navigate(it)
        }
    }

    private fun setupRecyclerViewAdapter() {
        recipeAdapter = RecipeAdapter {
            homeViewModel.navigateToRecipeDetails(it)
        }
        binding?.homeFragmentRecipes?.adapter = recipeAdapter

        categoryAdapter = CategoryAdapter()
        binding?.homeFragmentCategories?.adapter = categoryAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recipeAdapter = null
        categoryAdapter = null
    }
}