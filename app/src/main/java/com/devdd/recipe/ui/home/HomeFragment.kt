package com.devdd.recipe.ui.home

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.devdd.recipe.R
import com.devdd.recipe.base.DevFragment
import com.devdd.recipe.databinding.FragmentHomeBinding
import com.devdd.recipe.ui.home.adapter.CategoryAdapter
import com.devdd.recipe.ui.home.adapter.RecipeAdapter
import com.devdd.recipe.utils.extensions.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : DevFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()

    private var recipeAdapter: RecipeAdapter? = null
    private var categoryAdapter: CategoryAdapter? = null
    override fun onViewCreated(binding: FragmentHomeBinding, savedInstanceState: Bundle?) {
        binding.homeViewModel = viewModel
        setupRecyclerViewAdapter()
        setObserver()
        setListeners()
    }

    private fun setListeners() {
        binding?.homeFragmentSwipeToRefresh?.setOnRefreshListener {
            viewModel.fetchRecipes()
        }

        binding?.homeFragmentLottieNoRecipes?.setAnimation(R.raw.not_found_animation)

    }

    private fun setObserver() {
        viewModel.recipes.observe(viewLifecycleOwner) {
            recipeAdapter?.submitList(it)
        }

        viewModel.categories.observe(viewLifecycleOwner) {
            categoryAdapter?.submitList(it)
        }

        viewModel.navigation.observeEvent(viewLifecycleOwner) {
            findNavController().navigate(it)
        }
    }

    private fun setupRecyclerViewAdapter() {
        recipeAdapter = RecipeAdapter {
            viewModel.navigateToRecipeDetails(it)
        }
        binding?.homeFragmentRecipes?.adapter = recipeAdapter

        categoryAdapter = CategoryAdapter {
            viewModel.navigateToRecipes(it.id, it.name)
        }
        binding?.homeFragmentCategories?.adapter = categoryAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recipeAdapter = null
        categoryAdapter = null
    }
}