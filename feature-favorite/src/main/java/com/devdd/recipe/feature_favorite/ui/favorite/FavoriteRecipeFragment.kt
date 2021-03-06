package com.devdd.recipe.feature_favorite.ui.favorite

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.devdd.recipe.ui.base.DevFragment
import com.devdd.recipe.feature_favorite.R
import com.devdd.recipe.feature_favorite.databinding.FragmentFavoriteRecipeBinding
import com.devdd.recipe.feature_favorite.ui.favorite.adapter.FavoriteRecipeAdapter
import com.devdd.recipe.utils.extensions.navigateOnce
import com.devdd.recipe.base_android.utils.extensions.observeEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteRecipeFragment :
    DevFragment<FragmentFavoriteRecipeBinding>(R.layout.fragment_favorite_recipe) {
    companion object {
        private val TAG: String = this::class.java.simpleName
    }

    private val viewModel: FavoriteViewModel by viewModels()

    private var adapter: FavoriteRecipeAdapter? = null
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
            adapter?.submitList(it)
        }

        viewModel.navigation.observeEvent(viewLifecycleOwner) {
            findNavController().navigateOnce(it)
        }
    }

    private fun setupRecyclerViewAdapter() {
        adapter = FavoriteRecipeAdapter(viewModel)
        binding?.favoriteFragmentRecipes?.adapter = adapter

    }


    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
    }
}