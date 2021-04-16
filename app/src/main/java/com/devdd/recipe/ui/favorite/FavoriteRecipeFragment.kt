package com.devdd.recipe.ui.favorite

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.devdd.recipe.R
import com.devdd.recipe.base.DevFragment
import com.devdd.recipe.databinding.FragmentFavoriteRecipeBinding
import com.devdd.recipe.ui.favorite.adapter.SavedRecipeAdapter
import com.devdd.recipe.utils.extensions.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteRecipeFragment :
    DevFragment<FragmentFavoriteRecipeBinding>(R.layout.fragment_favorite_recipe) {
    companion object {
        private val TAG: String = this::class.java.simpleName
    }

    private val viewModel: FavoriteViewModel by viewModels()

    private var recipeAdapter: SavedRecipeAdapter? = null
    override fun onViewCreated(
        binding: FragmentFavoriteRecipeBinding,
        savedInstanceState: Bundle?
    ) {
        binding.viewModel = viewModel
        setListeners()
        setupRecyclerViewAdapter()
        setObserver()
    }

    private fun setListeners() {

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
        recipeAdapter = SavedRecipeAdapter(viewModel)
        binding?.favoriteFragmentRecipes?.adapter = recipeAdapter

    }


    override fun onDestroyView() {
        super.onDestroyView()
        recipeAdapter = null
    }
}