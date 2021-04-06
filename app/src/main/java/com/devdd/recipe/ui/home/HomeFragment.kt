package com.devdd.recipe.ui.home

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.devdd.recipe.R
import com.devdd.recipe.base.MyFragment
import com.devdd.recipe.databinding.FragmentHomeBinding
import com.devdd.recipe.ui.home.adapter.RecipeAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : MyFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val homeViewModel by viewModels<HomeViewModel>()

    private var recipeAdapter: RecipeAdapter? = null
    override fun onViewCreated(binding: FragmentHomeBinding, savedInstanceState: Bundle?) {
        setupRecyclerViewAdapter()
        setObserver()
    }

    private fun setObserver() {
        homeViewModel.recipes.observe(viewLifecycleOwner) {
            recipeAdapter?.submitList(it)
        }
    }

    private fun setupRecyclerViewAdapter() {
        recipeAdapter = RecipeAdapter()
        binding?.homeFragmentRecipes?.adapter = recipeAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recipeAdapter = null
    }
}