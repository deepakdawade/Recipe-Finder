package com.devdd.recipe.ui.home

import android.os.Bundle
import androidx.appcompat.widget.SearchView
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

    private val viewModel by viewModels<HomeViewModel>()

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

        val searchView = binding?.homeFragmentToolbar?.menu?.getItem(0)?.actionView as? SearchView
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.searchRecipes(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchRecipes(newText)
                return true
            }
        })

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